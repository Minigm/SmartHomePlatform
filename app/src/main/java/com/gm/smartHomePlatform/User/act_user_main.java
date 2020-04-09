package com.gm.smartHomePlatform.User;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.gmcompany.MQTT.GMCompanyService;
import com.example.gmcompany.devices.GMCompanyDeviceHelper;
import com.gm.basedevice.BaseDevice;
import com.gm.qrscaner_zxingbased.CaptureActivity;
import com.gm.smartHomePlatform.Device.DeviceListAdapter;
import com.gm.smartHomePlatform.R;
import com.gm.smartHomePlatform.SQLSeverManeger.DeviceManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class act_user_main extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private final int REQUEST_SCAN_QRCODE = 0;
    Button button_add_device,button_confirm_add;
    TextView text_welcome,text_temperature,text_humidity,text_co,text_gas;
    RelativeLayout lay_add,lay_top,lay_value;
    EditText edit_device_name;
    GridView gridView;
    String USER;
    boolean INTERNET_FLAG = false;
    int THREAD_STATE = 0;
    int ADD_DEVICE = 0,ADD_SUCCESS = 1,REFRESH = 2,UPDATE = 3;
    private ArrayList<BaseDevice> device_list = new ArrayList<BaseDevice>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_user_main);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        initDevice();
        initView();
        initBroadcast();
    }
    void initBroadcast(){
        mReceiver receiver = new mReceiver();
        IntentFilter filter = new IntentFilter(GMCompanyDeviceHelper.getBroadcastName);
        act_user_main.this.registerReceiver(receiver,filter);
    }
    void initDevice(){
        new InternetThread().start();
        INTERNET_FLAG = true;
        THREAD_STATE = 1;
    }
    void initView (){
        SharedPreferences sharedPreferences = getSharedPreferences("signed_information",MODE_PRIVATE);
        USER = sharedPreferences.getString("signed_user","");
        button_add_device = (Button) findViewById(R.id.buttonAddDevice_user_main);
        button_add_device.setOnClickListener(act_user_main.this);
        button_confirm_add = (Button) findViewById(R.id.buttonAdd_user_main);
        button_confirm_add.setOnClickListener(act_user_main.this);
        text_welcome = (TextView) findViewById(R.id.textWelcome_user_main);
        text_welcome.setText("欢迎"+USER);
        text_temperature = (TextView) findViewById(R.id.textTemperature_user_main);
        text_humidity = (TextView) findViewById(R.id.textHumidity_user_main);
        text_co = (TextView) findViewById(R.id.textCO_user_main);
        text_gas = (TextView) findViewById(R.id.textGas_user_main);
        gridView = (GridView) findViewById(R.id.gridView_user_main);
        lay_add = (RelativeLayout) findViewById(R.id.relativeLayout_add_user_main);
        lay_add.setVisibility(View.INVISIBLE);
        lay_top = (RelativeLayout) findViewById(R.id.relativeLayout_top_user_main);
        lay_top.setVisibility(View.VISIBLE);
        lay_value = (RelativeLayout) findViewById(R.id.relativeLayout_content_user_main);
        lay_value.setVisibility(View.VISIBLE);
        edit_device_name = (EditText) findViewById(R.id.editDevice_user_main);
    }
    void initService(){
        for (int i = 0;i < device_list.size();i++){
            switch (device_list.get(i).getCompany()){
                case "GMCompany":
                    if (!GMCompanyDeviceHelper.onServer(act_user_main.this)){
                        Intent intent = new Intent(act_user_main.this, GMCompanyService.class);
                        intent.putExtra("user",USER);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startService(intent);
                    }
            }
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonAddDevice_user_main){
            if (Build.VERSION.SDK_INT>22){
                if (ContextCompat.checkSelfPermission(act_user_main.this,
                        android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    //先判断有没有权限 ，没有就在这里进行权限的申请
                    ActivityCompat.requestPermissions(act_user_main.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    //说明已经获取到摄像头权限了
                    Log.i("MainActivity","已经获取了权限");
                }
            }else {
                //这个说明系统版本在6.0之下，不需要动态获取权限。
                Log.i("MainActivity","这个说明系统版本在6.0之下，不需要动态获取权限。");
            }
            Intent intent = new Intent(act_user_main.this, CaptureActivity.class);
            startActivityForResult(intent,REQUEST_SCAN_QRCODE);
        }else if (v.getId() == R.id.buttonAdd_user_main){
            SharedPreferences sharedPreferences = getSharedPreferences("QRCode",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String name = edit_device_name.getText().toString();
            editor.putString("name",name);
            editor.apply();
            THREAD_STATE = 2;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SCAN_QRCODE && resultCode == CaptureActivity.RESULT_OK) {
            String code = data.getExtras().getString("ResultQRCode");
            String[] pass = code.split(";");
            if (pass.length == 4){
                SharedPreferences sharedPreferences = getSharedPreferences("QRCode",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("id",pass[0]);
                editor.putString("company",pass[1]);
                editor.putString("project",pass[2]);
                editor.putString("type",pass[3]);
                editor.apply();
                mHandler.sendEmptyMessage(ADD_DEVICE);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(act_user_main.this,device_list.get(position).getName(),Toast.LENGTH_LONG).show();
    }

    private class InternetThread extends Thread{
        @Override
        public void run() {
            DeviceManager deviceManager = new DeviceManager();
            while (INTERNET_FLAG){
                switch (THREAD_STATE){
                    case 0:
                        break;
                    case 1:
                        THREAD_STATE = 0;
                        if (deviceManager.isDeviceUser(USER) == 0){
                            device_list.clear();
                            device_list.addAll(deviceManager.getUserDevice(USER));
                        }
                        mHandler.sendEmptyMessage(REFRESH);
                        break;
                    case 2:
                        THREAD_STATE = 0;
                        SharedPreferences sharedPreferences_2 = getSharedPreferences("QRCode",MODE_PRIVATE);
                        String name,company,project,type,id;
                        id = sharedPreferences_2.getString("id","");
                        company = sharedPreferences_2.getString("company","");
                        project = sharedPreferences_2.getString("project","");
                        type = sharedPreferences_2.getString("type","");
                        name = sharedPreferences_2.getString("name","");
                        switch (deviceManager.addDevice(id,USER,name,type,company,project)){
                            case 0:
                                break;//id设备已经添加过了，请解绑。
                            case 1:
                                mHandler.sendEmptyMessage(ADD_SUCCESS);break;
                        }
                        break;
                    case 3:
                        THREAD_STATE = 0;
                        SharedPreferences sharedPreferences_3 = getSharedPreferences("message_transport",MODE_PRIVATE);
                        String message = sharedPreferences_3.getString("message","");
                        deviceManager.updateDevice(message,device_list);
                        mHandler.sendEmptyMessage(UPDATE);
                        break;
                    default:
                        THREAD_STATE = 0;
                        break;
                }
            }
        }
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            if (msg.what == ADD_DEVICE){
                lay_add.setVisibility(View.VISIBLE);
                lay_top.setVisibility(View.INVISIBLE);
                lay_value.setVisibility(View.INVISIBLE);
                gridView.setVisibility(View.INVISIBLE);
            }else if (msg.what == ADD_SUCCESS){
                Toast.makeText(act_user_main.this,"添加成功",Toast.LENGTH_LONG);
                lay_add.setVisibility(View.INVISIBLE);
                lay_top.setVisibility(View.VISIBLE);
                lay_value.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.VISIBLE);
                THREAD_STATE = 1;
            }else if (msg.what == REFRESH){
                DeviceListAdapter deviceListAdapter = new DeviceListAdapter(act_user_main.this,device_list);
                gridView.setAdapter(deviceListAdapter);
                gridView.setOnItemClickListener(act_user_main.this);
                initService();
            }else if (msg.what == UPDATE){
                DeviceListAdapter deviceListAdapter = new DeviceListAdapter(act_user_main.this,device_list);
                gridView.setAdapter(deviceListAdapter);
            }
        }
    };

    private class mReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"试试",Toast.LENGTH_LONG).show();
            if (intent != null){
                System.out.println("收到更新！");
                String message = intent.getStringExtra("message");
                SharedPreferences sharedPreferences = getSharedPreferences("message_transport",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.putString("message",message);
                editor.apply();
                THREAD_STATE = 3;
            }
        }
    }
}
