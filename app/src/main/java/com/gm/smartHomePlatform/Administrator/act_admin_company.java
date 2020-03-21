package com.gm.smartHomePlatform.Administrator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.gm.smartHomePlatform.Administrator.Table.TableCompany;
import com.gm.smartHomePlatform.Administrator.Table.TableManager;
import com.gm.smartHomePlatform.R;
import com.gm.smartHomePlatform.SQLSeverManeger.CompanyManager;

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

public class act_admin_company extends AppCompatActivity {
    //用于存储用于表显示的列表
    List<TableCompany> list = new ArrayList<TableCompany>();
    //子线程进度标志
    // 0 常态化子线程；1 启动搜索；2 添加企业
    private int CONNECTION_STATE = 0;
    //子线程处理标志
    private boolean CONNECTION_FLAG = false;
    //子线程处理消息
    private final int UNKNOWN_SITUATION = 0,CHANGE_TABLE = 1,WRONG_INPUT = 2,ADD_COMPANY = 3,
            ADD_BACK_MAIN = 4,EMPTY_COMPANY_NAME = 5,EMPTY_COMPANY_PROJECT = 6,EMPTY_COMPANY_DEVICE = 7,
            EXIST_ACTS = 8,UPDATE_SUCCESS = 9,ADD_SUCCESS = 10,DELETE_COMPANY = 11,DELETE_BACK_MAIN = 12,
            NO_COMPANY_NAME = 13,NO_COMPANY_PROJECT = 14,NO_COMPANY_DEVICE = 15,NO_DEVICE_ACT = 16,
            DELETE_SUSCCESS = 17;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_admin_company);
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
        SearchView searchView = (SearchView)findViewById(R.id.searchView_admin_company);
        searchView.setOnQueryTextListener(new act_admin_company.myQueryTextListener());
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_0_admin_company);
        relativeLayout.setVisibility(View.VISIBLE);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_1_admin_company);
        relativeLayout.setVisibility(View.INVISIBLE);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_2_admin_company);
        relativeLayout.setVisibility(View.INVISIBLE);
    }
    @Override
    protected void onStart() {
        super.onStart();
        //启动子线程
        new act_admin_company.InternetThread().start();
        CONNECTION_FLAG = true;
        //初始化显示
        TextView textView = (TextView)findViewById(R.id.textCount_admin_company);
        textView.setText("0");
    }
    //添加企业按钮响应
    public void buttonAddCompany_admin_companyOnClick(View view) {
        mHandler.sendEmptyMessage(ADD_COMPANY);
    }
    //确认添加按钮响应
    public void buttonConfirmAdd_admin_companyOnClick(View view) {
        EditText editText = (EditText)findViewById(R.id.editCompanyName_admin_company);
        String name = editText.getText().toString();
        editText = (EditText)findViewById(R.id.editCompanyProject_admin_company);
        String project = editText.getText().toString();
        editText = (EditText)findViewById(R.id.editCompanyDevice_admin_company);
        String device = editText.getText().toString();
        editText = (EditText)findViewById(R.id.editCompanyActs_admin_company);
        String acts = editText.getText().toString();
        list.add(new TableCompany(name,project,device,acts));
        CONNECTION_STATE =2;
    }
    //添加企业退出按钮响应
    public void buttonExitAdd_admin_companyOnClick(View view) {
        mHandler.sendEmptyMessage(ADD_BACK_MAIN);
    }
    //删除企业按钮响应
    public void buttonDeleteCompany_admin_companyOnClick(View view) {
        mHandler.sendEmptyMessage(DELETE_COMPANY);
    }
    //确认删除按钮响应
    public void buttonConfirmDelete_admin_companyOnClick(View view) {
        EditText editText = (EditText)findViewById(R.id.editCompanyName_1_admin_company);
        String name = editText.getText().toString();
        editText = (EditText)findViewById(R.id.editCompanyProject_1_admin_company);
        String project = editText.getText().toString();
        editText = (EditText)findViewById(R.id.editCompanyDevice_1_admin_company);
        String device = editText.getText().toString();
        editText = (EditText)findViewById(R.id.editCompanyActs_1_admin_company);
        String acts = editText.getText().toString();
        list.add(new TableCompany(name,project,device,acts));
        CONNECTION_STATE = 3;
    }
    //删除企业退出按钮响应
    public void buttonExitDelete_admin_companyOnClick(View view) {
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
    //返回按钮响应
    public void buttonExit_admin_companyOnClick(View v){
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
            CompanyManager companyManager = new CompanyManager();
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
                                list.addAll(companyManager.getAllCompany());
                                mHandler.sendEmptyMessage(CHANGE_TABLE);break;
                            case "公司名":
                                list.clear();
                                list.addAll(companyManager.getCompanyName(main));
                                mHandler.sendEmptyMessage(CHANGE_TABLE);break;
                            case "项目名":
                                list.clear();
                                list.addAll(companyManager.getCompanyProject(main));
                                mHandler.sendEmptyMessage(CHANGE_TABLE);break;
                            case "设备类型":
                                list.clear();
                                list.addAll(companyManager.getCompanyDevcie(main));
                                mHandler.sendEmptyMessage(CHANGE_TABLE);break;
                            default:
                                //如果没有对应处理方法，发送WRONG_INPUT消息
                                mHandler.sendEmptyMessage(WRONG_INPUT);break;
                        }break;
                    case 2:
                        CONNECTION_STATE = 0;
                        switch (companyManager.addCompany(list.get(list.size()-1))){
                            case 1:
                                mHandler.sendEmptyMessage(EMPTY_COMPANY_NAME);break;
                            case 2:
                                mHandler.sendEmptyMessage(EMPTY_COMPANY_PROJECT);break;
                            case 3:
                                mHandler.sendEmptyMessage(EMPTY_COMPANY_DEVICE);break;
                            case 4:
                                mHandler.sendEmptyMessage(EXIST_ACTS);break;
                            case 5:
                                mHandler.sendEmptyMessage(UPDATE_SUCCESS);break;
                            case 6:
                                mHandler.sendEmptyMessage(ADD_SUCCESS);break;
                            default:
                                mHandler.sendEmptyMessage(UNKNOWN_SITUATION);break;
                        }
                        break;
                    case 3:
                        CONNECTION_STATE = 0;
                        switch (companyManager.deleteCompany(list.get(list.size()-1))){
                            case 1:
                                mHandler.sendEmptyMessage(NO_COMPANY_NAME);break;
                            case 2:
                                mHandler.sendEmptyMessage(NO_COMPANY_PROJECT);break;
                            case 3:
                                mHandler.sendEmptyMessage(NO_COMPANY_DEVICE);break;
                            case 4:
                                mHandler.sendEmptyMessage(NO_DEVICE_ACT);break;
                            case 5:
                                mHandler.sendEmptyMessage(DELETE_SUSCCESS);break;
                            default:
                                mHandler.sendEmptyMessage(UNKNOWN_SITUATION);break;
                        }
                    default:
                        CONNECTION_STATE = 0;break;
                }
            }
        }
    }
    //消息处理部分
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            if (msg.what == CHANGE_TABLE){
                //刷新表格
                TextView textView = (TextView)findViewById(R.id.textCount_admin_company);
                textView.setText(Integer.toString(list.size()));
                TableLayout tableLayout = (TableLayout)findViewById(R.id.tableLayout_in_admin_company);
                tableLayout.removeAllViews();
                for (int i = 0;i <list.size();i++){
                    TableManager tableManager = new TableManager(act_admin_company.this);
                    tableManager.getTableRow(list.get(i).getStrings());
                    tableLayout.addView(tableManager);
                    tableManager.clearAnimation();
                }
            }
            else if (msg.what == WRONG_INPUT){
                //提示指令错误
                Toast.makeText(getBaseContext(),"不正确的语句",Toast.LENGTH_LONG).show();
            }
            else if (msg.what == ADD_COMPANY){
                RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_0_admin_company);
                relativeLayout.setVisibility(View.INVISIBLE);
                relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_1_admin_company);
                relativeLayout.setVisibility(View.VISIBLE);
            }
            else if (msg.what == ADD_BACK_MAIN){
                RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_0_admin_company);
                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_1_admin_company);
                relativeLayout.setVisibility(View.INVISIBLE);
                EditText editText = (EditText)findViewById(R.id.editCompanyName_admin_company);
                editText.setText("");
                editText = (EditText)findViewById(R.id.editCompanyProject_admin_company);
                editText.setText("");
                editText = (EditText)findViewById(R.id.editCompanyDevice_admin_company);
                editText.setText("");
                editText = (EditText)findViewById(R.id.editCompanyActs_admin_company);
                editText.setText("");

            }
            else if (msg.what == DELETE_BACK_MAIN){
                RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_0_admin_company);
                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_2_admin_company);
                relativeLayout.setVisibility(View.INVISIBLE);
                EditText editText = (EditText)findViewById(R.id.editCompanyName_1_admin_company);
                editText.setText("");
                editText = (EditText)findViewById(R.id.editCompanyProject_1_admin_company);
                editText.setText("");
                editText = (EditText)findViewById(R.id.editCompanyDevice_1_admin_company);
                editText.setText("");
                editText = (EditText)findViewById(R.id.editCompanyActs_1_admin_company);
                editText.setText("");
            }
            else if (msg.what == EMPTY_COMPANY_NAME){
                Toast.makeText(getBaseContext(),"企业名不能为空！",Toast.LENGTH_LONG).show();
            }
            else if (msg.what == EMPTY_COMPANY_PROJECT){
                Toast.makeText(getBaseContext(),"工程名不能为空！",Toast.LENGTH_LONG).show();
            }
            else if (msg.what == EMPTY_COMPANY_DEVICE){
                Toast.makeText(getBaseContext(),"设备类型不能为空！",Toast.LENGTH_LONG).show();
            }
            else if (msg.what == EXIST_ACTS){
                Toast.makeText(getBaseContext(),"已经存在相同数据，未添加",Toast.LENGTH_LONG).show();
            }
            else if (msg.what == UPDATE_SUCCESS){
                Toast.makeText(getBaseContext(),"已更新属性列表",Toast.LENGTH_LONG).show();
            }
            else if (msg.what == ADD_SUCCESS){
                Toast.makeText(getBaseContext(),"添加成功！",Toast.LENGTH_LONG).show();
            }
            else if (msg.what == UNKNOWN_SITUATION){
                Toast.makeText(getBaseContext(),"未知错误！",Toast.LENGTH_LONG).show();
            }
            else if (msg.what == DELETE_COMPANY){
                RelativeLayout relativeLayout =(RelativeLayout)findViewById(R.id.relativeLayout_2_admin_company);
                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_0_admin_company);
                relativeLayout.setVisibility(View.INVISIBLE);
            }
            else if (msg.what == NO_COMPANY_NAME){
                Toast.makeText(getBaseContext(),"没有该企业！",Toast.LENGTH_LONG).show();
            }
            else if (msg.what == NO_COMPANY_PROJECT){
                Toast.makeText(getBaseContext(),"没有该工程！",Toast.LENGTH_LONG).show();
            }
            else if (msg.what == NO_COMPANY_DEVICE){
                Toast.makeText(getBaseContext(),"没有该设备类型！",Toast.LENGTH_LONG).show();
            }
            else if (msg.what == NO_DEVICE_ACT){
                Toast.makeText(getBaseContext(),"没有该设备属性！",Toast.LENGTH_LONG).show();
            }
            else if (msg.what == DELETE_SUSCCESS){
                Toast.makeText(getBaseContext(),"删除成功！",Toast.LENGTH_LONG).show();
            }
        }
    };
}
