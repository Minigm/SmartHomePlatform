package com.gm.smartHomePlatform.Login;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.gm.smartHomePlatform.Administrator.act_admin_main;
import com.gm.smartHomePlatform.Main.act_user_main;
import com.gm.smartHomePlatform.R;
import com.gm.smartHomePlatform.SQLSeverManeger.UserManager;

public class act_login extends AppCompatActivity {
    //子线程进度标志
    private int CONNECTION_STATE = 0;
    //子线程处理标志
    private boolean CONNECTION_FLAG = false;
    //子线程处理消息
    private final int USER_SIGNED = 0, COMPANY_SIGNED = 1, ADMIN_SIGNED = 2,
                WRONG_PASSWORD = 3, NO_USER = 4, UNKNOWN_SITUATION = 5, FORGET_PASSWORD = 6,
                ADD_USER = 7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_login);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        //建立SharedPreference记录各活动中登录用户的变化
        SharedPreferences sharedPreferences = getSharedPreferences("signed_information",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("signed_user","");
        editor.putString("signed_password","");
        editor.apply();
    }
    @Override
    protected void onStart() {
        super.onStart();
        //每次切换回act_login时重载一次登录用户信息
        SharedPreferences sharedPreferences = getSharedPreferences("signed_information",MODE_PRIVATE);
        ((EditText)findViewById(R.id.editUsername_login)).setText(sharedPreferences.getString("signed_user",""));
        //开启子线程
        new InternetThread().start();
        CONNECTION_FLAG = true;
    }
    @Override
    protected void onStop() {
        //每次切换出act_login退出子线程
        CONNECTION_FLAG = false;
        super.onStop();
    }
    //忘记密码链接操作
    public void textForgetPassword_loginOnClick(View v){
        //发送忘记密码消息
        mHandler.sendEmptyMessage(FORGET_PASSWORD);
    }
    //注册连接操作
    public void textSignUp_loginOcClick(View v){
        //发送添加用户消息
        mHandler.sendEmptyMessage(ADD_USER);
    }
    //登录按钮操作
    public void buttonSignIn_loginOnClick(View v){
        //读取当前输入用户信息，并压入SharedPreference
        SharedPreferences sharedPreferences = getSharedPreferences("signed_information",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String username = ((EditText)findViewById(R.id.editUsername_login)).getText().toString();
        String password = ((EditText)findViewById(R.id.editPassword_login)).getText().toString();
        editor.putString("signed_user",username);
        editor.putString("signed_password",password);
        editor.apply();
        //子进程进入1状态
        CONNECTION_STATE = 1;
    }
    //退出按钮操作
    public void buttonExit_loginOnClick(View v){
        //退出前先退出子线程
        CONNECTION_FLAG = false;
        onDestroy();
        //完成彻底的退出
        System.exit(0);
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
                        String username = sharedPreferences.getString("signed_user","");
                        String password = sharedPreferences.getString("signed_password","");
                        switch (userManager.whatAccount(username,password)){
                            case 0:
                                //0状态表示一般用户登录，发送一般用户登录消息
                                mHandler.sendEmptyMessage(USER_SIGNED);break;
                            case 1:
                                //1状态表示企业用户登录，发送企业用户登陆消息
                                mHandler.sendEmptyMessage(COMPANY_SIGNED);break;
                            case 2:
                                //2状态表示平台管理员登录，发送管理员登录消息
                                mHandler.sendEmptyMessage(ADMIN_SIGNED);break;
                            case 3:
                                //3状态表示用户存在密码错误，发送密码错误消息
                                mHandler.sendEmptyMessage(WRONG_PASSWORD);break;
                            case 4:
                                //4状态表示用户不存在，发送没有用户消息
                                mHandler.sendEmptyMessage(NO_USER);break;
                            default:
                                //其他状态是目前无法理解，也基本不可能出现的问题，发送未知错误消息
                                mHandler.sendEmptyMessage(UNKNOWN_SITUATION);break;
                        }break;
                    default:
                        //意外运行至此，返回0状态
                        CONNECTION_STATE = 0;break;
                }
            }
        }
    }
    //消息处理部分
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            if (msg.what == USER_SIGNED){
                //一般用户登录消息处理
                Toast to = Toast.makeText(getBaseContext(),"欢迎使用慧家平台",Toast.LENGTH_LONG);
                to.show();
                Intent intent = new Intent(act_login.this, act_user_main.class);
                startActivity(intent);
            }else if (msg.what == COMPANY_SIGNED){
                Toast to = Toast.makeText(getBaseContext(),"欢迎企业用户",Toast.LENGTH_LONG);
                to.show();
            }else if (msg.what == ADMIN_SIGNED){
                Toast to = Toast.makeText(getBaseContext(),"管理员登录快去维护",Toast.LENGTH_LONG);
                to.show();
                Intent intent = new Intent(act_login.this, act_admin_main.class);
                startActivity(intent);
            }else if (msg.what == WRONG_PASSWORD){
                //密码错误消息处理
                AlertDialog.Builder ad = new AlertDialog.Builder(act_login.this);
                ad.setTitle("密码错误，忘记密码？");
                ad.setNegativeButton("再试试",null);
                ad.setPositiveButton("忘记了",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mHandler.sendEmptyMessage(FORGET_PASSWORD);
                    }
                });
                ad.show();
            }else if (msg.what == NO_USER){
                //无用户消息处理
                AlertDialog.Builder ad = new AlertDialog.Builder(act_login.this);
                ad.setTitle("该用户未注册，是否前往注册？");
                ad.setNegativeButton("取消",null);
                ad.setPositiveButton("注册",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mHandler.sendEmptyMessage(ADD_USER);
                    }
                });
                ad.show();
            }else if (msg.what == UNKNOWN_SITUATION){
                //未知状态消息处理
                Toast to = Toast.makeText(getBaseContext(),"未知错误，请尝试重新操作",Toast.LENGTH_LONG);
                to.show();
            }else if (msg.what == FORGET_PASSWORD){
                //忘记密码消息处理,跳转至忘记密码活动
                Intent intent = new Intent(act_login.this,act_forget_password.class);
                startActivity(intent);
            }else if (msg.what == ADD_USER){
                //添加用户消息处理，跳转至注册用户活动
                Intent intent = new Intent(act_login.this,act_add_user.class);
                startActivity(intent);
            }
        }
    };
}
