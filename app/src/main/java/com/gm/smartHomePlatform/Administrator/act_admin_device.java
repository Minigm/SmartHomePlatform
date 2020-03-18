package com.gm.smartHomePlatform.Administrator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.gm.smartHomePlatform.Administrator.Table.TableDevice;
import com.gm.smartHomePlatform.Administrator.Table.TableManager;
import com.gm.smartHomePlatform.R;
import com.gm.smartHomePlatform.SQLSeverManeger.DeviceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class act_admin_device extends AppCompatActivity {
    //用于存储用于表显示的列表
    List<TableDevice> list = new ArrayList<TableDevice>();
    //子线程进度标志
    // 0 常态化子线程；1 启动搜索；
    private int CONNECTION_STATE = 0;
    //子线程处理标志
    private boolean CONNECTION_FLAG = false;
    //子线程处理消息
    private final int UNKNOWN_SITUATION = 0,CHANGE_TABLE = 1,WRONG_INPUT = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_admin_device);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        //将活动共享数据压入SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("search_information",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("method","");
        editor.putString("main","");
        editor.apply();
        //为搜索控件设置监听
        SearchView searchView = (SearchView)findViewById(R.id.searchView_admin_device);
        searchView.setOnQueryTextListener(new myQueryTextListener());
    }
    @Override
    protected void onStart() {
        super.onStart();
        //启动子线程
        new InternetThread().start();
        CONNECTION_FLAG = true;
        //初始化显示
        TextView textView = (TextView)findViewById(R.id.textCount_admin_device);
        textView.setText("0");
    }
    //搜索框响应
    private class myQueryTextListener implements SearchView.OnQueryTextListener {
        @Override
        public boolean onQueryTextSubmit(String query) {
            //获得输入指令，并压入SharedPreferences
            query = query+" ";
            String method = "",main = "";
            method = query.split(" ",2)[0];
            main = query.split(" ",2)[1];
            SharedPreferences sharedPreferences = getSharedPreferences("search_information",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("method",method);
            editor.putString("main",main);
            editor.apply();
            //子线程进入1状态
            CONNECTION_STATE = 1;
            return true;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    }
    //返回按钮响应
    public void buttonExit_admin_deviceOcClick(View v) {
        //清空设备列表
        if (list != null)list.clear();
        //停止子线程
        CONNECTION_FLAG = false;
        finish();
    }
    //网络处理子线程
    private class InternetThread extends Thread{
        @Override
        public void run(){
            DeviceManager deviceManager = new DeviceManager();
            SharedPreferences sharedPreferences = getSharedPreferences("search_information",MODE_PRIVATE);
            while(CONNECTION_FLAG){
                switch (CONNECTION_STATE){
                    case 0:
                        //0状态挂起，避免退出子线程。
                        break;
                    case 1:
                    {
                        //1状态处理指令，完成后返回0状态
                        CONNECTION_STATE = 0;
                        //获得共享数据
                        String method = sharedPreferences.getString("method","");
                        String main = sharedPreferences.getString("main","");
                        switch (method){
                            case "all":
                                list.clear();
                                list.addAll(deviceManager.getAllDevice());
                                mHandler.sendEmptyMessage(CHANGE_TABLE);break;
                            case "拥有者":
                                list.clear();
                                list.addAll(deviceManager.getDeviceUser(main));
                                mHandler.sendEmptyMessage(CHANGE_TABLE);break;
                            case "设备名":
                                list.clear();
                                list.addAll(deviceManager.getDeviceName(main));
                                mHandler.sendEmptyMessage(CHANGE_TABLE);break;
                            case "设备类型":
                                list.clear();
                                list.addAll(deviceManager.getDeviceType(main));
                                mHandler.sendEmptyMessage(CHANGE_TABLE);break;
                            case "所属公司":
                                list.clear();
                                list.addAll(deviceManager.getDeviceCompany(main));
                                mHandler.sendEmptyMessage(CHANGE_TABLE);break;
                            case "所属项目":
                                list.clear();
                                list.addAll(deviceManager.getDeviceProject(main));
                                mHandler.sendEmptyMessage(CHANGE_TABLE);break;
                            default:
                                //如果没有对应处理方法，发送WRONG_INPUT消息
                                mHandler.sendEmptyMessage(WRONG_INPUT);break;
                        }
                    }break;
                }
            }
        }
    }
    //消息处理部分
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            if (msg.what == CHANGE_TABLE){
                //刷新表格
                TextView textView = (TextView)findViewById(R.id.textCount_admin_device);
                textView.setText(Integer.toString(list.size()));
                TableLayout tableLayout = (TableLayout)findViewById(R.id.tableLayout_in_admin_device);
                tableLayout.removeAllViews();
                for (int i = 0;i <list.size();i++){
                    TableManager tableManager = new TableManager(act_admin_device.this);
                    tableManager.getTableRow(list.get(i).getStrings());
                    tableLayout.addView(tableManager);
                    tableManager.clearAnimation();
                }
            }else if (msg.what == WRONG_INPUT){
                //提示指令错误
                Toast.makeText(getBaseContext(),"不正确的语句",Toast.LENGTH_LONG).show();
            }
        }
    };
}
