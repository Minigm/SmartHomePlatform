package com.gm.smartHomePlatform.Administrator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.gm.smartHomePlatform.R;
import com.gm.smartHomePlatform.SQLSeverManeger.UserManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;

public class act_admin_main extends AppCompatActivity {
    //子线程进度标志
    private int CONNECTION_STATE = 0;
    //子线程处理标志
    private boolean CONNECTION_FLAG = false;
    //子线程处理消息
    private final int UNKNOWN_SITUATION = 0,EXIT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_admin_main);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
    }
    //用户管理按钮响应
    public void buttonUserAdmin_admin_mainOnClick(View v){
        ;
    }
    //企业管理按钮响应
    public void buttonCompanyAdmin_admin_mainOnClick(View v){
        ;
    }
    //设备管理按钮响应
    public void buttonDeviceAdmin_admin_mainOnClick(View v){
        ;
    }
    //退出按钮响应
    public void buttonExit_admin_mainOnClick(View v){
        mHandler.sendEmptyMessage(EXIT);
    }
    //网络处理子线程
    private class InternetThread extends Thread{
        @Override
        public void run(){
            //获得活动中的数据信息
            UserManager userManager = new UserManager();
            SharedPreferences sharedPreferences = getSharedPreferences("signed_information",MODE_PRIVATE);
            while (CONNECTION_FLAG){
                switch (CONNECTION_STATE){
                    //0状态为挂起态，不进行操作
                    case 0:break;
                    //1状态为登录处理状态，单次触发仅进入一次后回归0状态
                    case 1:
                        CONNECTION_STATE = 0;
                        break;
                    default:
                        //意外运行至此，返回0状态
                        CONNECTION_STATE = 0;break;
                }
            }
        }
    }
    //消息处理
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            if (msg.what == EXIT){
                finish();
            }
        }
    };
}
