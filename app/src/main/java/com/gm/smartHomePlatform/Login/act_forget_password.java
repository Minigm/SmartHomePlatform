package com.gm.smartHomePlatform.Login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.gm.smartHomePlatform.R;
import com.gm.smartHomePlatform.SQLSeverManeger.UserManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class act_forget_password extends AppCompatActivity {
    //子线程进度标志
    private int CONNECTION_STATE = 0;
    //子线程处理标志
    private boolean CONNECTION_FLAG = false;
    //子线程处理消息
    private final int RIGHT_CODE = 0,WRONG_CODE = 1,NO_USER = 2,
                    UNKNOWN_SITUATION = 3,CHANGE_PASSWORD = 4,DIFFRENT_PASSWORD = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_forget_password);
        SharedPreferences sharedPreferences = getSharedPreferences("forget_information",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("forget_user","");
        editor.putString("forget_code","");
        editor.putString("forget_password","");
        editor.putString("forget_password_again","");
        editor.apply();
        new InternetThread().start();
        CONNECTION_FLAG = true;
    }
    //找回按钮操作
    public void buttonFindBack_forget_passwordOnClick(View v){
        SharedPreferences sharedPreferences = getSharedPreferences("forget_information",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String username = ((EditText)findViewById(R.id.editUsername_forget_password)).getText().toString();
        String code = ((EditText)findViewById(R.id.editCode_forget_password)).getText().toString();
        editor.putString("forget_user",username);
        editor.putString("forget_code",code);
        editor.apply();
        //发送找回消息
        CONNECTION_STATE = 1;
    }
    //返回按钮操作
    public void buttonExit_forget_passwordOnClick(View v){
        CONNECTION_FLAG = false;
        finish();
    }
    //核对按钮操作
    public void buttonCheck_forget_passwordOnClick(View v){
        //写入活动数据
        SharedPreferences sharedPreferences = getSharedPreferences("forget_information",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String password = ((EditText)findViewById(R.id.editPassword_forget_password)).getText().toString();
        String passwordAgain = ((EditText)findViewById(R.id.editPasswordAgain_forget_password)).getText().toString();
        editor.putString("forget_password",password);
        editor.putString("forget_password_again",passwordAgain);
        editor.apply();
        //进入子线程2状态
        CONNECTION_STATE = 2;
    }
    //转换视图函数
    private void change(){
        findViewById(R.id.editCode_forget_password).setVisibility(View.INVISIBLE);
        findViewById(R.id.editUsername_forget_password).setVisibility(View.INVISIBLE);
        findViewById(R.id.buttonFindBack_forget_password).setVisibility(View.INVISIBLE);
        findViewById(R.id.buttonExit_forget_password).setVisibility(View.INVISIBLE);
        findViewById(R.id.editPassword_forget_password).setVisibility(View.VISIBLE);
        findViewById(R.id.editPasswordAgain_forget_password).setVisibility(View.VISIBLE);
        findViewById(R.id.buttonCheck_forget_password).setVisibility(View.VISIBLE);
    }
    //网络子线程
    private class InternetThread extends Thread{
        @Override
        public void run(){
            //获得活动中的数据信息
            UserManager userManager = new UserManager();
            SharedPreferences sharedPreferences;
            while (CONNECTION_FLAG){
                switch (CONNECTION_STATE){
                    //0状态为挂起态，不进行操作
                    case 0:break;
                    //1状态为登录处理状态，单次触发仅进入一次后回归0状态
                    case 1:
                        CONNECTION_STATE = 0;
                        sharedPreferences = getSharedPreferences("forget_information",MODE_PRIVATE);
                        String username = sharedPreferences.getString("forget_user","");
                        String code = sharedPreferences.getString("forget_code","");
                        switch (userManager.isUser(username)){
                            case 0:
                                if (userManager.isCode(code)){
                                    mHandler.sendEmptyMessage(RIGHT_CODE);
                                } else {
                                    mHandler.sendEmptyMessage(WRONG_CODE);
                                }break;
                            case 1:
                                mHandler.sendEmptyMessage(NO_USER);break;
                            default:
                                mHandler.sendEmptyMessage(UNKNOWN_SITUATION);break;
                        }break;
                    case 2:
                        //更改密码状态
                        CONNECTION_STATE = 0;
                        sharedPreferences = getSharedPreferences("forget_information",MODE_PRIVATE);
                        String password = sharedPreferences.getString("forget_password","");
                        String passwordAgain = sharedPreferences.getString("forget_password_again","");
                        if (password.equals(passwordAgain)){
                            String username_2 = sharedPreferences.getString("forget_user","");
                            String password_2 = sharedPreferences.getString("forget_password","");
                            userManager.changePassword(username_2,password_2);
                            mHandler.sendEmptyMessage(CHANGE_PASSWORD);
                        }else {
                            mHandler.sendEmptyMessage(DIFFRENT_PASSWORD);
                        }break;
                    default:
                        //意外运行至此，返回0状态
                        CONNECTION_STATE = 0;break;
                }
            }
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == RIGHT_CODE){
                change();
            }else if (msg.what == WRONG_CODE){
                Toast to = Toast.makeText(getBaseContext(),"管理码错误，联系管理员",Toast.LENGTH_LONG);
                to.show();
            }else if (msg.what == NO_USER){
                AlertDialog.Builder ad = new AlertDialog.Builder(act_forget_password.this);
                ad.setTitle("用户不存在是否尝试注册？");
                ad.setNegativeButton("取消",null);
                ad.setPositiveButton("注册",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(act_forget_password.this,act_add_user.class);
                        SharedPreferences sharedPreferences = getSharedPreferences("signed_information",MODE_PRIVATE);
                        String username = ((EditText)findViewById(R.id.editUsername_forget_password)).getText().toString();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("signed_user",username);
                        editor.apply();
                        startActivity(intent);
                        finish();
                    }
                });
                ad.show();
            }else if (msg.what == CHANGE_PASSWORD){
                Toast to = Toast.makeText(getBaseContext(),"密码修改完毕",Toast.LENGTH_LONG);
                to.show();
                finish();
            }else if (msg.what == DIFFRENT_PASSWORD){
                Toast to = Toast.makeText(getBaseContext(),"两次输入不一致",Toast.LENGTH_LONG);
                to.show();
                ((EditText)findViewById(R.id.editPassword_forget_password)).setText("");
                ((EditText)findViewById(R.id.editPasswordAgain_forget_password)).setText("");
            }else if (msg.what == UNKNOWN_SITUATION){
                Toast to = Toast.makeText(getBaseContext(),"未知错误，重新尝试",Toast.LENGTH_LONG);
                to.show();
            }
        }
    };
}
