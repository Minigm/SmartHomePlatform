package com.example.gmcompany.devices;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.gmcompany.MQTT.GMCompanyService;
import com.example.gmcompany.MQTT.MQTTManager;
import com.example.gmcompany.R;
import com.gm.basedevice.BaseDevice;

import java.util.ArrayList;

public class GMCompanyDeviceHelper {
    public static void onService(Context context,String user){
        Intent intent = new Intent(context, GMCompanyService.class);
        intent.putExtra("user",user);
        context.startService(intent);
    }
    private String getAddMessage(BaseDevice device){
        return (device.getId()+";"+device.getType()+";"+device.getOwner()+";");
    }
    public void addDevice(BaseDevice device){
        if (device.getProject().equals("smarthome01")){
            MQTTManager mqttManager = new MQTTManager("Device_add","","");
            mqttManager.publish("Add",this.getAddMessage(device));
            mqttManager.disconnect();
        }
    }
    //获得设备的acts属性
    public String[] getActs(BaseDevice device){
        if (device.getProject().equals("smarthome01")){
            if (device.getType().equals("CO2Sensor")){
                return new String[]{"density","0.0","state","off"};
            }else if (device.getType().equals("TemperatureSensor")){
                return new String[]{"temperature","0.0","state","off"};
            }
        }
        return new String[]{""};
    }
    public View getConvertView(BaseDevice device, Context context){
        if (device.getProject().equals("smarthome01")){
            if (device.getType().equals("CO2Sensor")){
                View view = LayoutInflater.from(context).inflate(R.layout.co2sensor_adapter,null);
                TextView text_name = view.findViewById(R.id.text_name_co2);
                TextView text_value = view.findViewById(R.id.text_value_co2);
                text_name.setText(device.getName());
                text_value.setText("CO2浓度"+device.getAct("density"));
                return view;
            }
        }
        return null;
    }
    public boolean isSetAdapter(){
        return true;
    }
    static public String getBroadcastName = "com.example.gmcompany.mqtt.update";
    public void updateDevice(BaseDevice device,String message){
        String project = device.getProject();
        String type = device.getType();
        if (project.equals("smarthome01")){
            if (type.equals("CO2Sensor")){
                String state = message.split(";")[3];
                String density = message.split(";")[4];
                device.setAct("state",state);
                device.setAct("density",density);
            }
        }
    }
}
