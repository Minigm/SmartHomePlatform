package com.gm.smartHomePlatform.Administrator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.gm.smartHomePlatform.R;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class act_admin_main extends AppCompatActivity {
    //子线程处理消息
    private final int UNKNOWN_SITUATION = 0,EXIT = 1,DEVICE_ADMIN = 2;
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
        mHandler.sendEmptyMessage(DEVICE_ADMIN);
    }
    //退出按钮响应
    public void buttonExit_admin_mainOnClick(View v){
        mHandler.sendEmptyMessage(EXIT);
    }
    //网络处理子线程
    //消息处理
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            if (msg.what == EXIT){
                finish();
            }else if (msg.what == DEVICE_ADMIN){
                startActivity(new Intent(act_admin_main.this,act_admin_device.class));
            }else if (msg.what == UNKNOWN_SITUATION){
                Toast.makeText(getBaseContext(),"未知错误，请尝试重新操作",Toast.LENGTH_LONG).show();
            }
        }
    };
}
