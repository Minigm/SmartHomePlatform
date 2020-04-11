package com.example.gmcompany.MQTT;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.gmcompany.R;
import com.example.gmcompany.devices.act_CO2Sensor;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class GMCompanyService extends Service {
    private MQTTManager mqttManager;
    private String user;
    private String id;
    private int count;
    private MqttCallback callback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable throwable) {
            System.out.println("哈哈我掉了");
        }

        @Override
        public void messageArrived(String s, MqttMessage mqttMessage) {
            if (s.equals("Update_"+user)){
                Intent intent = new Intent("com.example.gmcompany.mqtt.update");
                intent.putExtra("message","GMCompany;"+mqttMessage.toString());
                sendBroadcast(intent);
            }else if (s.equals("Warning_"+user)){
                id = mqttMessage.toString().split(";")[0];
                String type = mqttMessage.toString().split(";")[1];
                String time = mqttMessage.toString().split((";"))[2];
                if (type.equals("CO2Sensor")){
                    sendSimpleNotify("CO2超标","二氧化碳超标于："+time);
                }else if (type.equals("COSensor")){
                    sendSimpleNotify("CO超标","一氧化碳超标于："+time);
                }else if (type.equals("TemperatureSensor")){
                    sendSimpleNotify("温度超标","温度超标于："+time);
                }else if (type.equals("HumiditySensor")){
                    sendSimpleNotify("湿度超标","湿度超标于："+time);
                }else if (type.equals("GasSensor")){
                    sendSimpleNotify("可燃气体超标","可燃气体超标于："+time);
                }
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            System.out.println("哈哈我穿好了");
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        count = 0;
        System.out.println("服务启动");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(count == 0){
            user = intent.getStringExtra("user");
            mqttManager = new MQTTManager("Phone Service "+user,"","",this.callback);
            mqttManager.subscribe("Update_"+user,1);
            mqttManager.subscribe("Warning_"+user,1);
            count++;
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void sendSimpleNotify(String title,String message){
        Notification.Builder builder = new Notification.Builder(GMCompanyService.this);
        builder.setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setTicker("警告！")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setDefaults(Notification.DEFAULT_ALL);
        Intent intent = new Intent(this, act_CO2Sensor.class);
        intent.putExtra("id",id);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1,notification);
        //startForeground(332011,notification);
    }
}
