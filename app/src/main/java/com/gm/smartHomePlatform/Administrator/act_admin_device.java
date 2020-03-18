package com.gm.smartHomePlatform.Administrator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.gm.smartHomePlatform.Administrator.Table.TableDevice;
import com.gm.smartHomePlatform.Administrator.Table.TableManager;
import com.gm.smartHomePlatform.R;
import com.gm.smartHomePlatform.SQLSeverManeger.DeviceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class act_admin_device extends AppCompatActivity {
    List<TableDevice> list = new ArrayList<TableDevice>();
    //子线程进度标志
    // 0 常态化子线程；1 启动搜索；
    private int CONNECTION_STATE = 0;
    //子线程处理标志
    private boolean CONNECTION_FLAG = false;
    //子线程处理消息
    private final int UNKNOWN_SITUATION = 0,CHANGE_TABLE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_admin_device);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        SharedPreferences sharedPreferences = getSharedPreferences("search_information",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("method","");
        editor.putString("main","");
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new InternetThread().start();
        CONNECTION_FLAG = true;
        SearchView searchView = (SearchView)findViewById(R.id.searchView_admin_device);
        searchView.setOnQueryTextListener(new myQueryTextListener());
    }

    private class myQueryTextListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            query = query+" ";
            String method = "",main = "";
            method = query.split(" ",2)[0];
            main = query.split(" ",2)[1];
            SharedPreferences sharedPreferences = getSharedPreferences("search_information",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("method",method);
            editor.putString("main",main);
            editor.apply();
            CONNECTION_STATE = 1;
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    }




    //网络处理子线程
    private class InternetThread extends Thread{
        @Override
        public void run(){
            DeviceManager deviceManager = new DeviceManager();
            SharedPreferences sharedPreferences = getSharedPreferences("search_information",MODE_PRIVATE);
            while(CONNECTION_FLAG){
                switch (CONNECTION_STATE){
                    case 0:break;
                    case 1:
                    {
                        CONNECTION_STATE = 0;
                        String method = sharedPreferences.getString("method","");
                        String main = sharedPreferences.getString("main","");
                        switch (method){
                            case "all":
                                list.clear();
                                list.addAll(deviceManager.getAllDevice());
                                System.out.println("klklkllk");
                                break;
                            case "拥有者":
                                list.clear();
                                list.addAll(deviceManager.getDeviceUser(main));
                                break;
                            case "设备名":
                                list.clear();
                                list.addAll(deviceManager.getDeviceName(main));
                                break;
                            case "设备类型":break;
                            case "所属公司":break;
                            case "所属项目":break;
                            default:break;
                        }
                        mHandler.sendEmptyMessage(CHANGE_TABLE);
                    }
                }
            }
        }
    }
    //消息处理部分
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            if (msg.what == CHANGE_TABLE){
                TableLayout tableLayout = (TableLayout)findViewById(R.id.tableLayout_in_admin_device);
                tableLayout.removeAllViews();
                for (int i = 0;i <list.size();i++){
                    TableManager tableManager = new TableManager(act_admin_device.this);
                    tableManager.getTableRow(list.get(i).getStrings());
                    tableLayout.addView(tableManager);
                    tableManager.clearAnimation();
                }
            }
        }
    };
}
