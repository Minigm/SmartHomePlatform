package com.gm.smartHomePlatform.Administrator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.gm.smartHomePlatform.Administrator.Table.TableManager;
import com.gm.smartHomePlatform.Login.act_add_user;
import com.gm.smartHomePlatform.Login.act_forget_password;
import com.gm.smartHomePlatform.Login.act_login;
import com.gm.smartHomePlatform.Main.act_user_main;
import com.gm.smartHomePlatform.R;
import com.gm.smartHomePlatform.SQLSeverManeger.DeviceManager;
import com.gm.smartHomePlatform.SQLSeverManeger.UserManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class act_admin_device extends AppCompatActivity {
    //子线程进度标志
    private int CONNECTION_STATE = 0;
    //子线程处理标志
    private boolean CONNECTION_FLAG = false;
    //子线程处理消息
    private final int UNKNOWN_SITUATION = 0,CHANGE_TABLE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_admin_device);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new InternetThread().start();
        CONNECTION_STATE = 1;
}

    //网络处理子线程
    private class InternetThread extends Thread{
        @Override
        public void run(){
            DeviceManager deviceManager = new DeviceManager();
            while(CONNECTION_FLAG){
                switch (CONNECTION_STATE){
                    case 0:break;
                    case 1:

                }
            }
        }
    }
    //消息处理部分
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            if (msg.what == CHANGE_TABLE){
                TableManager tableManager = new TableManager(act_admin_device.this);
                
            }
        }
    };
}
