package com.example.gmcompany.MQTT;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTManager {
    private String host_local = "tcp://192.168.1.2:801";
    private String host_net = "tcp://111.175.77.137:801";
    private String id;
    private String username;
    private String password;
    MqttCallback callback;
    private MqttClient mqttClient;
    private MqttConnectOptions options;
    private MqttTopic mqttTopic;
    private MqttMessage mqttMessage;
    public MQTTManager(String id,String username,String password){
        this.id = id;
        this.username = username;
        this.password = password;
        initMqtt();
        connect();
    }
    public MQTTManager(String id,String username,String password,MqttCallback callback){
        this.id = id;
        this.username = username;
        this.password = password;
        this.callback = callback;
        initMqtt_call();
        connect();
    }
    private void initMqtt(){
        try {
            mqttClient = new MqttClient(host_net,id,new MemoryPersistence());
            options = new MqttConnectOptions();
            options.setCleanSession(true);
            //options.setUserName(username);
            //options.setPassword(password.toCharArray());
            options.setConnectionTimeout(30);
            options.setKeepAliveInterval(15);
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    System.out.println("连接丢失");
                    connect();
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    System.out.println("消息到达"+s+":"+mqttMessage.toString());
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    System.out.println("传输成功");
                }
            });
            System.out.println("初始化成功");
        } catch (IllegalArgumentException | MqttException e) {
            e.printStackTrace();
        }
    }
    private void initMqtt_call(){
        try {
            mqttClient = new MqttClient(host_net,id,new MemoryPersistence());
            options = new MqttConnectOptions();
            options.setCleanSession(true);
            //options.setUserName(username);
            //options.setPassword(password.toCharArray());
            options.setConnectionTimeout(30);
            options.setKeepAliveInterval(15);
            mqttClient.setCallback(callback);
            System.out.println("初始化成功");
        } catch (IllegalArgumentException | MqttException e) {
            e.printStackTrace();
        }
    }
    private void connect(){
        try {
            mqttClient.connect(options);
            System.out.println("连接成功");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void subscribe(String topic,int qos){
        try {
            mqttClient.subscribe(topic,qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void publish(String topic,String message){
        try {
            mqttMessage = new MqttMessage();
            mqttMessage.setQos(1);
            mqttMessage.setRetained(true);
            mqttMessage.setPayload(message.getBytes());
            mqttTopic = mqttClient.getTopic(topic);
            mqttTopic.publish(mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void disconnect(){
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
