package com.gm.smartHomePlatform.SQLSeverManeger;

import android.content.Context;

import com.example.gmcompany.devices.GMCompanyDeviceHelper;
import com.gm.basedevice.BaseDevice;

import java.util.Arrays;

public class CompanyHelper {
    private BaseDevice device;
    public CompanyHelper(BaseDevice device){
        this.device = device;
    }
    public void addDevice(){
        switch (device.getCompany()){
            case "GMCompany":
                GMCompanyDeviceHelper gmCompanyDeviceHelper = new GMCompanyDeviceHelper();
                gmCompanyDeviceHelper.addDevice(device);break;
        }
    }
    public String[] getActs(){
        switch (device.getCompany()){
            case "GMCompany":
                GMCompanyDeviceHelper gmCompanyDeviceHelper = new GMCompanyDeviceHelper();
                return gmCompanyDeviceHelper.getActs(this.device);
        }
        return new String[]{""};
    }
    public String getActString(){
        String trans = "";
        String[] tans_loo = this.getActs();
        for (int i = 0;i < tans_loo.length;i++){
            trans = trans +tans_loo[i]+";";
            i++;
        }
        return trans;
    }
    public BaseDevice getDevice(){
        return new BaseDevice(device.getOwner(),device.getName(),
                device.getCompany(),device.getProject(),device.getType(),
                device.getId(),this.getActs());
    }
    public void callLayout(Context context){
        switch (device.getCompany()){
            case "GMCompany":
                GMCompanyDeviceHelper gmCompanyDeviceHelper = new GMCompanyDeviceHelper();
                gmCompanyDeviceHelper.callLayout(device,context);
        }
    }
}
