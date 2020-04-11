package com.example.gmcompany.devices;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gmcompany.MQTT.GMCompanyService;
import com.example.gmcompany.MQTT.MQTTManager;
import com.example.gmcompany.R;
import com.gm.basedevice.BaseDevice;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

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
                System.out.println("获得了acts");
                return new String[]{"density","0.0","state","off"};
            }else if (device.getType().equals("COSensor")){
                return new String[]{"density","0.0","state","off"};
            }else if (device.getType().equals("TemperatureSensor")){
                return new String[]{"temperature","0.0","state","off"};
            }else if (device.getType().equals("HumiditySensor")){
                return new String[]{"humidity","0.0","state","off"};
            }else if (device.getType().equals("GasSensor")){
                return new String[]{"density","0.0","state","off"};
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
                TextView text_state = view.findViewById(R.id.text_state_co2);
                text_name.setText(device.getName());
                String density = device.getAct("density");
                if (density.length()>6){
                    density = density.substring(0,6);
                }
                if (Float.parseFloat(density) > 10){
                    ((RelativeLayout)view.findViewById(R.id.relative_co2)).setBackgroundColor(Color.RED);
                }else{
                    ((RelativeLayout)view.findViewById(R.id.relative_co2)).setBackgroundColor(Color.GREEN);
                }
                text_value.setText( density+"mg/m³");
                text_state.setText("状态："+device.getAct("state"));
                return view;
            }else if (device.getType().equals("COSensor")){
                View view = LayoutInflater.from(context).inflate(R.layout.cosensor_adapter,null);
                TextView text_name = view.findViewById(R.id.text_name_co);
                TextView text_value = view.findViewById(R.id.text_value_co);
                TextView text_state = view.findViewById(R.id.text_state_co);
                text_name.setText(device.getName());
                String density = device.getAct("density");
                if (density.length()>6){
                    density = density.substring(0,6);
                }
                if (Float.parseFloat(density) > 10){
                    ((RelativeLayout)view.findViewById(R.id.relative_co)).setBackgroundColor(Color.RED);
                }else{
                    ((RelativeLayout)view.findViewById(R.id.relative_co)).setBackgroundColor(Color.GREEN);
                }
                text_value.setText( density+"mg/m³");
                text_state.setText("状态："+device.getAct("state"));
                return view;
            }else if (device.getType().equals("TemperatureSensor")){
                View view = LayoutInflater.from(context).inflate(R.layout.temperaturesensor_adapter,null);
                TextView text_name = view.findViewById(R.id.text_name_temperature);
                TextView text_value = view.findViewById(R.id.text_value_temperature);
                TextView text_state = view.findViewById(R.id.text_state_temperature);
                text_name.setText(device.getName());
                String temperature = device.getAct("temperature");
                if (temperature.length()>6){
                    temperature = temperature.substring(0,6);
                }
                if (Float.parseFloat(temperature) > 30){
                    ((RelativeLayout)view.findViewById(R.id.relative_temperature)).setBackgroundColor(Color.RED);
                }else{
                    ((RelativeLayout)view.findViewById(R.id.relative_temperature)).setBackgroundColor(Color.GREEN);
                }
                text_value.setText( temperature+"℃");
                text_state.setText("状态："+device.getAct("state"));
                return view;
            }else if (device.getType().equals("HumiditySensor")){
                View view = LayoutInflater.from(context).inflate(R.layout.humiditysensor_adapter,null);
                TextView text_name = view.findViewById(R.id.text_name_humidity);
                TextView text_value = view.findViewById(R.id.text_value_humidity);
                TextView text_state = view.findViewById(R.id.text_state_humidity);
                text_name.setText(device.getName());
                String humidity = device.getAct("humidity");
                if (humidity.length()>6){
                    humidity = humidity.substring(0,6);
                }
                if (Float.parseFloat(humidity) > 70){
                    ((RelativeLayout)view.findViewById(R.id.relative_humidity)).setBackgroundColor(Color.RED);
                }else{
                    ((RelativeLayout)view.findViewById(R.id.relative_humidity)).setBackgroundColor(Color.GREEN);
                }
                text_value.setText( humidity+"%RH");
                text_state.setText("状态："+device.getAct("state"));
                return view;
            }else if (device.getType().equals("GasSensor")){
                View view = LayoutInflater.from(context).inflate(R.layout.gassensor_adapter,null);
                TextView text_name = view.findViewById(R.id.text_name_gas);
                TextView text_value = view.findViewById(R.id.text_value_gas);
                TextView text_state = view.findViewById(R.id.text_state_gas);
                text_name.setText(device.getName());
                String density = device.getAct("density");
                if (density.length()>6){
                    density = density.substring(0,6);
                }
                if (Float.parseFloat(density) > 10){
                    ((RelativeLayout)view.findViewById(R.id.relative_gas)).setBackgroundColor(Color.RED);
                }else{
                    ((RelativeLayout)view.findViewById(R.id.relative_gas)).setBackgroundColor(Color.GREEN);
                }
                text_value.setText( density+"mg/m³");
                text_state.setText("状态："+device.getAct("state"));
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
            }else if (type.equals("COSensor")){
                String state = message.split(";")[3];
                String density = message.split(";")[4];
                device.setAct("state",state);
                device.setAct("density",density);
            }else if (type.equals("TemperatureSensor")){
                String state = message.split(";")[3];
                String temperature = message.split(";")[4];
                device.setAct("state",state);
                device.setAct("temperature",temperature);
            }else if (type.equals("HumiditySensor")){
                String state = message.split(";")[3];
                String humidity = message.split(";")[4];
                device.setAct("state",state);
                device.setAct("humidity",humidity);
            }else if (type.equals("GasSensor")){
                String state = message.split(";")[3];
                String density = message.split(";")[4];
                device.setAct("state",state);
                device.setAct("density",density);
            }
        }
    }
    public void callLayout(BaseDevice device, Context context){
        if (device.getProject().equals("smarthome01")){
            if (device.getType().equals("CO2Sensor")){
                Intent intent = new Intent(context,act_CO2Sensor.class);
                intent.putExtra("id",device.getId());
                context.startActivity(intent);
            }else if (device.getType().equals("COSensor")){
                Intent intent = new Intent(context,act_COSensor.class);
                intent.putExtra("id",device.getId());
                context.startActivity(intent);
            }else if (device.getType().equals("TemperatureSensor")){
                Intent intent = new Intent(context,act_TemperatureSensor.class);
                intent.putExtra("id",device.getId());
                context.startActivity(intent);
            }else if (device.getType().equals("HumiditySensor")){
                Intent intent = new Intent(context,act_HumiditySensor.class);
                intent.putExtra("id",device.getId());
                context.startActivity(intent);
            }else if (device.getType().equals("GasSensor")){
                Intent intent = new Intent(context,act_GasSensor.class);
                intent.putExtra("id",device.getId());
                context.startActivity(intent);
            }
        }
    }
}
