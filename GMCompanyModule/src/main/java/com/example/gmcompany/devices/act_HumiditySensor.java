package com.example.gmcompany.devices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gmcompany.MQTT.MQTTManager;
import com.example.gmcompany.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class act_HumiditySensor extends AppCompatActivity {
    //记录状态变量
    private String id;
    //表格绘制所需变量
    private LineChart chart;
    private DataFlow dataFlow = new DataFlow();
    //消息值
    private int UPDATE = 0;
    mReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_humiditysensor);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        chart = (LineChart) findViewById(R.id.chart_humiditysensor);
        receiver = new mReceiver();
        IntentFilter filter = new IntentFilter(GMCompanyDeviceHelper.getBroadcastName);
        act_HumiditySensor.this.registerReceiver(receiver,filter);
        LineDataSet dataSet = new LineDataSet(dataFlow.getEntries(),"近10次测量数据");
        dataSet.setColor(Color.BLACK);
        Description description = chart.getDescription();
        description.setText("湿度值%RH");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
        ((TextView)findViewById(R.id.text_state_humiditysensor)).setText("状态：off");
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    public void buttonBack_humiditysensorOnClick(View view) {
        finish();
    }

    public void buttonOn_humiditysensorOnClick(View view) {
        MQTTManager mqttManager = new MQTTManager("HumiditySensor"+id,"","");
        mqttManager.publish("Commond",id+";state;on;");
        mqttManager.disconnect();
    }

    public void buttonOff_humiditysensorOnClick(View view) {
        MQTTManager mqttManager = new MQTTManager("HumiditySensor"+id,"","");
        mqttManager.publish("Commond",id+";state;off;");
        mqttManager.disconnect();
    }

    private class mReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null){
                String message = intent.getStringExtra("message");
                String id_in = message.split(";")[1];
                if (id_in.equals(id)) {
                    String humidity = message.split(";")[4];
                    String state = message.split(";")[3];
                    //System.out.println(state);
                    ((TextView)findViewById(R.id.text_state_humiditysensor)).setText("状态："+state);
                    dataFlow.addData(Float.parseFloat(humidity));
                    mHandler.sendEmptyMessage(UPDATE);
                }
            }
        }
    }
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE){
                LineDataSet dataSet = new LineDataSet(dataFlow.getEntries(),"近10次测量数据");
                dataSet.setColor(Color.BLACK);
                Description description = chart.getDescription();
                description.setText("湿度值%RH");
                LineData lineData = new LineData(dataSet);
                chart.setData(lineData);
                chart.invalidate();
            }
        }
    };
    private class DataFlow{
        private float[] data;
        private List<Entry> entries = new ArrayList<Entry>();
        public DataFlow(){
            data = new float[10];
        }
        public void addData(float data){
            for (int i = 0;i < 10;i++){
                if (i == 9){
                    this.data[i] = data;
                }else {
                    this.data[i] = this.data[i+1];
                }
            }
        }
        public float getItem(int index){
            if (index > 9){
                return 0;
            }else{
                return data[index];
            }
        }
        public List<Entry> getEntries(){
            entries.clear();
            for (int i = 0;i < 10;i++){
                entries.add(new Entry(i+1,this.getItem(i)));
            }
            return entries;
        }
    }
}
