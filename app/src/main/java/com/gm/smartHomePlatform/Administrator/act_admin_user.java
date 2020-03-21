package com.gm.smartHomePlatform.Administrator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.gm.smartHomePlatform.Administrator.Table.TableManager;
import com.gm.smartHomePlatform.SQLSeverManeger.UserManager;
import com.gm.smartHomePlatform.Administrator.Table.TableUser;
import com.gm.smartHomePlatform.R;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class act_admin_user extends AppCompatActivity {
    //用于存储用于表显示的列表
    List<TableUser> list = new ArrayList<TableUser>();
    //子线程进度标志
    // 0 常态化子线程；1 启动搜索；2 添加企业
    private int CONNECTION_STATE = 0;
    //子线程处理标志
    private boolean CONNECTION_FLAG = false;
    //子线程处理消息
    private final int UNKNOWN_SITUATION = 0,CHANGE_TABLE = 1,WRONG_INPUT = 2,ADD_USER = 3,
            ADD_BACK_MAIN = 4,ADD_SUCCESS = 5,EXIST_NAME = 6,EMPTY_USER_NAME = 7,EMPTY_USER_PASSWORD = 8,
            DELETE_USER = 9,NO_USER = 10,DELETE_SUCCESS = 11,DELETE_BACK_MAIN = 12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_admin_user);
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
        SearchView searchView = (SearchView)findViewById(R.id.searchView_admin_user);
        searchView.setOnQueryTextListener(new act_admin_user.myQueryTextListener());
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_0_admin_user);
        relativeLayout.setVisibility(View.VISIBLE);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_1_admin_user);
        relativeLayout.setVisibility(View.INVISIBLE);
        //relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_2_admin_user);
       // relativeLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //启动子线程
        new act_admin_user.InternetThread().start();
        CONNECTION_FLAG = true;
        //初始化显示
        TextView textView = (TextView)findViewById(R.id.textCount_admin_user);
        textView.setText("0");
    }

    public void buttonExit_admin_userOnClick(View view) {
        //清空设备列表
        if (list != null)list.clear();
        //停止子线程
        CONNECTION_FLAG = false;
        finish();
    }

    public void buttonConfirmDelete_admin_userOnClick(View view) {
        EditText editText = (EditText)findViewById(R.id.editUserName_1_admin_user);
        String name = editText.getText().toString();
        list.add(new TableUser(name,"",""));
        CONNECTION_STATE = 3;
    }

    public void buttonExitDelete_admin_userOnClick(View view) {
        mHandler.sendEmptyMessage(DELETE_BACK_MAIN);
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

    public void buttonAddUser_admin_userOnClick(View view) {
        mHandler.sendEmptyMessage(ADD_USER);
    }

    public void buttonDeleteUser_admin_userOnClick(View view) {
        mHandler.sendEmptyMessage(DELETE_USER);
    }

    public void buttonConfirmAdd_admin_userOnClick(View view) {
        EditText editText = (EditText)findViewById(R.id.editUserName_admin_user);
        String name = editText.getText().toString();
        editText = (EditText)findViewById(R.id.editUserPassword_admin_user);
        String password = editText.getText().toString();
        editText = (EditText)findViewById(R.id.editUserPower_admin_user);
        String power = editText.getText().toString();
        list.add(new TableUser(name,password,power));
        CONNECTION_STATE = 2;
    }

    public void buttonExitAdd_admin_userOnClick(View view) {
        mHandler.sendEmptyMessage(ADD_BACK_MAIN);
    }

    //网络处理子线程
    private class InternetThread extends Thread{
        @Override
        public void run(){
            UserManager userManager = new UserManager();
            SharedPreferences sharedPreferences = getSharedPreferences("search_information",MODE_PRIVATE);
            while(CONNECTION_FLAG){
                switch (CONNECTION_STATE){
                    case 0:
                        //0状态挂起，避免退出子线程。
                        break;
                    case 1:
                        //1状态处理指令，完成后返回0状态
                        CONNECTION_STATE = 0;
                        //获得共享数据
                        String method = sharedPreferences.getString("method","");
                        String main = sharedPreferences.getString("main","");
                        switch (method){
                            case "all":
                                list.clear();
                                list.addAll(userManager.getAllUser());
                                mHandler.sendEmptyMessage(CHANGE_TABLE);break;
                            case "用户名":
                                list.clear();
                                list.addAll(userManager.getUserName(main));
                                mHandler.sendEmptyMessage(CHANGE_TABLE);break;
                            case "用户类型":
                                list.clear();
                                list.addAll(userManager.getUserPower(main));
                                mHandler.sendEmptyMessage(CHANGE_TABLE);break;
                            default:
                                //如果没有对应处理方法，发送WRONG_INPUT消息
                                mHandler.sendEmptyMessage(WRONG_INPUT);break;
                        }break;
                    case 2:
                        CONNECTION_STATE = 0;
                        switch (userManager.addUser(list.get(list.size()-1).getUser_name(),
                                list.get(list.size()-1).getUser_password(),
                                list.get(list.size()-1).getUser_power())){
                            case 0:
                                mHandler.sendEmptyMessage(ADD_SUCCESS);break;
                            case 1:
                                mHandler.sendEmptyMessage(EXIST_NAME);break;
                            case 2:
                                mHandler.sendEmptyMessage(EMPTY_USER_NAME);break;
                            case 3:
                                mHandler.sendEmptyMessage(EMPTY_USER_PASSWORD);break;
                            default:
                                mHandler.sendEmptyMessage(UNKNOWN_SITUATION);break;
                        }
                        break;
                    case 3:
                        CONNECTION_STATE = 0;
                        switch (userManager.deleteUser(list.get(list.size()-1).getUser_name())){
                            case 0:
                                mHandler.sendEmptyMessage(DELETE_SUCCESS);break;
                            case 1:
                                mHandler.sendEmptyMessage(NO_USER);break;
                            default:
                                mHandler.sendEmptyMessage(UNKNOWN_SITUATION);break;
                        }
                    default:
                        CONNECTION_STATE = 0;break;
                }
            }
        }
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            if (msg.what == CHANGE_TABLE){
                //刷新表格
                TextView textView = (TextView)findViewById(R.id.textCount_admin_user);
                textView.setText(Integer.toString(list.size()));
                TableLayout tableLayout = (TableLayout)findViewById(R.id.tableLayout_in_admin_user);
                tableLayout.removeAllViews();
                for (int i = 0;i <list.size();i++){
                    TableManager tableManager = new TableManager(act_admin_user.this);
                    tableManager.getTableRow(list.get(i).getStrings());
                    tableLayout.addView(tableManager);
                    tableManager.clearAnimation();
                }
            }
            else if (msg.what == WRONG_INPUT){
                //提示指令错误
                Toast.makeText(getBaseContext(),"不正确的语句",Toast.LENGTH_LONG).show();
            }
            else if (msg.what == ADD_USER){
                RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_0_admin_user);
                relativeLayout.setVisibility(View.INVISIBLE);
                relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_1_admin_user);
                relativeLayout.setVisibility(View.VISIBLE);
            }
            else if (msg.what == ADD_BACK_MAIN){
                RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_0_admin_user);
                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_1_admin_user);
                relativeLayout.setVisibility(View.INVISIBLE);
                EditText editText = (EditText)findViewById(R.id.editUserName_admin_user);
                editText.setText("");
                editText = (EditText)findViewById(R.id.editUserPassword_admin_user);
                editText.setText("");
                editText = (EditText)findViewById(R.id.editUserPower_admin_user);
                editText.setText("");

            }
            else if (msg.what == ADD_SUCCESS){
                Toast.makeText(getBaseContext(),"添加成功！",Toast.LENGTH_LONG).show();
            }
            else if (msg.what == EMPTY_USER_NAME){
                Toast.makeText(getBaseContext(),"用户名不能为空！",Toast.LENGTH_LONG).show();
            }
            else if (msg.what == EMPTY_USER_PASSWORD){
                Toast.makeText(getBaseContext(),"密码不能为空！",Toast.LENGTH_LONG).show();
            }
            else if (msg.what == EXIST_NAME){
                Toast.makeText(getBaseContext(),"用户已存在！",Toast.LENGTH_LONG).show();
            }
            else if (msg.what == DELETE_USER){
                RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_0_admin_user);
                relativeLayout.setVisibility(View.INVISIBLE);
                relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_2_admin_user);
                relativeLayout.setVisibility(View.VISIBLE);
            }
            else if (msg.what == NO_USER){
                Toast.makeText(getBaseContext(),"没有该用户！",Toast.LENGTH_LONG).show();
            }
            else if (msg.what == DELETE_SUCCESS){
                Toast.makeText(getBaseContext(),"删除成功！",Toast.LENGTH_LONG).show();
            }
            else if (msg.what == DELETE_BACK_MAIN){
                RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_0_admin_user);
                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_2_admin_user);
                relativeLayout.setVisibility(View.INVISIBLE);
            }
        }
    };
}
