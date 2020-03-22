package com.gm.smartHomePlatform.Company;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.gm.smartHomePlatform.Administrator.Table.TableCompany;
import com.gm.smartHomePlatform.Administrator.Table.TableManager;
import com.gm.smartHomePlatform.Administrator.act_admin_company;
import com.gm.smartHomePlatform.R;
import com.gm.smartHomePlatform.SQLSeverManeger.CompanyManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class act_company_main extends AppCompatActivity {
    //活动内传递的表数据
    List<TableCompany> list = new ArrayList<TableCompany>();
    boolean CONNECT_FLAG = false;
    int CONNECT_STATE = 0;
    int UNKNOWN_SITUATION = 0,CHECK = 1,CHANGE_TABLE = 2,CHECK_BACK = 3,ADD = 4,
            EMPTY_COMPANY_PROJECT = 5,EMPTY_COMPANY_DEVICE = 6,EXIST_ACTS = 7,
            UPDATE_SUCCESS = 8,ADD_SUCCESS = 9,ADD_BACK = 10,DELETE = 11,NO_COMPANY_PROJECT = 12,
            NO_COMPANY_DEVICE = 13,NO_DEVICE_ACT = 14,DELETE_SUCCESS = 15,DELETE_BACK = 16;
    String company;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_company_main);
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
        company = sharedPreferences.getString("signed_user","");
        TextView textView = (TextView) findViewById(R.id.textWelcome_company_main);
        textView.setText("欢迎"+company);
        initTable();
        CONNECT_FLAG = true;
        CONNECT_STATE = 0;
        new act_company_main.InternetThread().start();
    }

    void initTable(){
        TableRow tableRow = new TableRow(getBaseContext());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(getBaseContext());
        textView.setText("项目");
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(layoutParams);
        tableRow.addView(textView);
        textView = new TextView(getBaseContext());
        textView.setText("设备类型");
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(layoutParams);
        tableRow.addView(textView);
        textView = new TextView(getBaseContext());
        textView.setText("属性");
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(layoutParams);
        tableRow.addView(textView);
        TableLayout tableLayout = (TableLayout)findViewById(R.id.tableLayout_company_main);
        tableLayout.addView(tableRow);
    }

    public void buttonCheck_company_mainOnClick(View view) {
        mHandler.sendEmptyMessage(CHECK);
    }

    public void buttonCheckBack_company_mainOnClick(View view) {
        mHandler.sendEmptyMessage(CHECK_BACK);
    }

    public void buttonAdd_company_mainOnClick(View view) {
        mHandler.sendEmptyMessage(ADD);
    }

    public void buttonExitAdd_company_mainOnClick(View view) {
        mHandler.sendEmptyMessage(ADD_BACK);
    }

    public void buttonConfirmAdd_company_mainOnClick(View view) {
        EditText editText = (EditText)findViewById(R.id.editProject_company_main);
        String project = editText.getText().toString();
        editText = (EditText)findViewById(R.id.editDevice_company_main);
        String device = editText.getText().toString();
        editText = (EditText)findViewById(R.id.editActs_company_main);
        String acts = editText.getText().toString();
        list.add(new TableCompany(company,project,device,acts));
        CONNECT_STATE = 2;
    }

    public void buttonDelete_company_mainOnClick(View view) {
        mHandler.sendEmptyMessage(DELETE);
    }

    public void buttonConfirmDelete_company_mainOnClick(View view) {
        EditText editText = (EditText)findViewById(R.id.editProject_1_company_main);
        String project = editText.getText().toString();
        editText = (EditText)findViewById(R.id.editDevice_1_company_main);
        String device = editText.getText().toString();
        editText = (EditText)findViewById(R.id.editActs_1_company_main);
        String acts = editText.getText().toString();
        list.add(new TableCompany(company,project,device,acts));
        CONNECT_STATE = 3;
    }

    public void buttonExitDelete_company_mainOnClick(View view) {
        mHandler.sendEmptyMessage(DELETE_BACK);
    }

    public void buttonExit_company_mainOnClick(View view) {
        //清空设备列表
        if (list != null)list.clear();
        //停止子线程
        CONNECT_FLAG = false;
        finish();
    }


    private class InternetThread extends Thread{
        @Override
        public void run() {
            CompanyManager companyManager = new CompanyManager();
            while (CONNECT_FLAG){
                switch (CONNECT_STATE){
                    case 0:
                        break;
                    case 1:
                        CONNECT_STATE = 0;
                        list.clear();
                        list.addAll(companyManager.getCompanyName(company));
                        mHandler.sendEmptyMessage(CHANGE_TABLE);break;
                    case 2:
                        CONNECT_STATE = 0;
                        switch (companyManager.addCompany(list.get(list.size()-1))){
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
                        }break;
                    case 3:
                        CONNECT_STATE = 0;
                        switch (companyManager.deleteCompany(list.get(list.size()-1))){
                            case 2:
                                mHandler.sendEmptyMessage(NO_COMPANY_PROJECT);break;
                            case 3:
                                mHandler.sendEmptyMessage(NO_COMPANY_DEVICE);break;
                            case 4:
                                mHandler.sendEmptyMessage(NO_DEVICE_ACT);break;
                            case 5:
                                mHandler.sendEmptyMessage(DELETE_SUCCESS);break;
                            default:
                                mHandler.sendEmptyMessage(UNKNOWN_SITUATION);break;
                        }break;
                    default:
                        CONNECT_STATE = 0;break;
                }
            }
        }
    }

    private Handler mHandler =  new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == CHECK){
                RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_0_company_main);
                relativeLayout.setVisibility(View.INVISIBLE);
                relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_1_company_main);
                relativeLayout.setVisibility(View.VISIBLE);
                CONNECT_STATE = 1;
            }
            else if (msg.what == CHANGE_TABLE){
                TableLayout tableLayout = (TableLayout)findViewById(R.id.tableLayout_company_main);
                tableLayout.removeAllViews();
                initTable();
                for (int i = 0;i <list.size();i++){
                    TableManager tableManager = new TableManager(act_company_main.this);
                    tableManager.getTableRow(list.get(i).getStrings(1));
                    tableLayout.addView(tableManager);
                    tableManager.clearAnimation();
                }
            }
            else if (msg.what == CHECK_BACK){
                RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_0_company_main);
                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_1_company_main);
                relativeLayout.setVisibility(View.INVISIBLE);
            }
            else if (msg.what == ADD){
                RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_0_company_main);
                relativeLayout.setVisibility(View.INVISIBLE);
                relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_2_company_main);
                relativeLayout.setVisibility(View.VISIBLE);
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
            else if (msg.what == ADD_BACK){
                RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_0_company_main);
                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_2_company_main);
                relativeLayout.setVisibility(View.INVISIBLE);
            }
            else if (msg.what == DELETE){
                RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_0_company_main);
                relativeLayout.setVisibility(View.INVISIBLE);
                relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_3_company_main);
                relativeLayout.setVisibility(View.VISIBLE);
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
            else if (msg.what == DELETE_SUCCESS){
                Toast.makeText(getBaseContext(),"删除成功！",Toast.LENGTH_LONG).show();
            }
            else if (msg.what == DELETE_BACK){
                RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_0_company_main);
                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_3_company_main);
                relativeLayout.setVisibility(View.INVISIBLE);
            }
        }
    };
}
