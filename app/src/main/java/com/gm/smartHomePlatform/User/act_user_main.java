package com.gm.smartHomePlatform.User;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.gm.qrscaner_zxingbased.CaptureActivity;
import com.gm.smartHomePlatform.Device.BaseDevice;
import com.gm.smartHomePlatform.Device.DeviceListAdapter;
import com.gm.smartHomePlatform.R;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class act_user_main extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private final int REQUEST_SCAN_QRCODE = 0;
    Button button_add_device;
    TextView text_welcome,text_temperature,text_humidity,text_co,text_gas;
    GridView gridView;
    String USER;
    private ArrayList<BaseDevice> device_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_user_main);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        initView();
    }
    void initView (){
        SharedPreferences sharedPreferences = getSharedPreferences("signed_information",MODE_PRIVATE);
        USER = sharedPreferences.getString("signed_user","");
        button_add_device = (Button) findViewById(R.id.buttonAddDevice_user_main);
        button_add_device.setOnClickListener(act_user_main.this);
        text_welcome = (TextView) findViewById(R.id.textWelcome_user_main);
        text_welcome.setText("欢迎"+USER);
        text_temperature = (TextView) findViewById(R.id.textTemperature_user_main);
        text_humidity = (TextView) findViewById(R.id.textHumidity_user_main);
        text_co = (TextView) findViewById(R.id.textCO_user_main);
        text_gas = (TextView) findViewById(R.id.textGas_user_main);
        device_list = BaseDevice.getTestList();
        gridView = (GridView) findViewById(R.id.gridView_user_main);
        DeviceListAdapter deviceListAdapter = new DeviceListAdapter(act_user_main.this,device_list);
        gridView.setAdapter(deviceListAdapter);
        gridView.setOnItemClickListener(act_user_main.this);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonAddDevice_user_main){
            System.out.println("点击了！");
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SCAN_QRCODE && resultCode == CaptureActivity.RESULT_OK) {
            Toast.makeText(act_user_main.this,""+data.getExtras().getString("ResultQRCode"),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("已点击");
        Toast.makeText(act_user_main.this,device_list.get(position).getName(),Toast.LENGTH_LONG).show();
    }
}
