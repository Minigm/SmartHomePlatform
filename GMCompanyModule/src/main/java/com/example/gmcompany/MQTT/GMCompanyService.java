package com.example.gmcompany.MQTT;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class GMCompanyService extends Service {
    private MQTTManager mqttManager;
    private String user;
    private MqttCallback callback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable throwable) {
            System.out.println("哈哈我掉了");
        }

        @Override
        public void messageArrived(String s, MqttMessage mqttMessage) {
            if (s.equals("Update_"+user)){
                System.out.println("更新中！");
                Intent intent = new Intent("com.example.gmcompany.mqtt.update");
                intent.putExtra("message","GMCompany;"+mqttMessage.toString());
                sendBroadcast(intent);
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
        System.out.println("服务启动");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            user = intent.getStringExtra("user");
            mqttManager = new MQTTManager("Phone Service"+user,"","",callback);
            mqttManager.subscribe("Update_"+user,1);
            mqttManager.subscribe("Warning_"+user,1);
            System.out.println("进入！");
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
}
