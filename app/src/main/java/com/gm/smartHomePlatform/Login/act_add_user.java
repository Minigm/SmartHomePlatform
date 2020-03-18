package com.gm.smartHomePlatform.Login;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.gm.smartHomePlatform.R;
import com.gm.smartHomePlatform.SQLSeverManeger.UserManager;

public class act_add_user extends AppCompatActivity {
    //子线程进度标志
    private int CONNECTION_STATE = 0;
    //子线程处理标志
    private boolean CONNECTION_FLAG = false;
    //子线程处理消息
    private final int SUCCESSFUL_ADD = 0,USER_EXISTED = 1,UNKNOWN_SITUATION = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_add_user);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
    }
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("signed_information",MODE_PRIVATE);
        String name = sharedPreferences.getString("signed_user","");
        ((EditText)findViewById(R.id.editUsername_add_user)).setText(name);
        new InternetThread().start();
        CONNECTION_FLAG = true;
    }
    public void buttonSignUp_add_userOnClick(View v){
        SharedPreferences sharedPreferences = getSharedPreferences("signed_information",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String name = ((EditText)findViewById(R.id.editUsername_add_user)).getText().toString();
        String password = ((EditText)findViewById(R.id.editPassword_add_user)).getText().toString();
        editor.putString("signed_user",name);
        editor.putString("signed_password",password);
        editor.apply();
        CONNECTION_STATE = 1;
    }
    public void buttonExit_add_userOnClick(View v){
        CONNECTION_FLAG = false;
        finish();
    }
    //子线程部分
    private class InternetThread extends Thread {
        @Override
        public void run(){
            //获得活动中的数据
            UserManager userManager = new UserManager();
            SharedPreferences sharedPreferences = getSharedPreferences("signed_information",MODE_PRIVATE);
            while (CONNECTION_FLAG) {
                switch (CONNECTION_STATE){
                    case 0:
                        //0状态为待机状态
                        break;
                    case 1:
                        //1状态为添加用户进程，执行完毕后返回0状态。
                        CONNECTION_STATE = 0;
                        String name = sharedPreferences.getString("signed_user","");
                        String password = sharedPreferences.getString("signed_password","");
                        switch (userManager.addUser(name,password)){
                            case 0:
                                //0状态表示成功添加状态，发送成功添加消息
                                mHandler.sendEmptyMessage(SUCCESSFUL_ADD);break;
                            case 1:
                                //1状态表示用户已存在状态，发送用户已存在消息
                                mHandler.sendEmptyMessage(USER_EXISTED);break;
                            default:
                                //若有其余情况发送未知状态消息
                                mHandler.sendEmptyMessage(UNKNOWN_SITUATION);break;
                        }
                    default:
                        //如出现意外运行至此，返回0状态
                        CONNECTION_STATE = 0;break;
                }
            }
        }
    }
    //消息处理部分
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            if (msg.what == SUCCESSFUL_ADD){
                //注册成功消息处理
                Toast.makeText(getBaseContext(),"注册成功，返回登录",Toast.LENGTH_LONG).show();
                finish();
            }else if (msg.what == USER_EXISTED){
                //用户已存在消息处理
                Toast.makeText(getBaseContext(),"该用户已存在，请尝试找回密码",Toast.LENGTH_LONG).show();
            }else if (msg.what == UNKNOWN_SITUATION){
                //未知错误消息处理
                Toast.makeText(getBaseContext(),"未知错误，尝试重新注册",Toast.LENGTH_LONG).show();
            }
        }
    };
}
