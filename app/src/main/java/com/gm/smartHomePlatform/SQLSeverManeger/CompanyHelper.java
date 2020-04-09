package com.gm.smartHomePlatform.SQLSeverManeger;

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
                System.out.println("进入1");
                GMCompanyDeviceHelper gmCompanyDeviceHelper = new GMCompanyDeviceHelper();
                gmCompanyDeviceHelper.addDevice(device);break;
        }
    }
    public String[] setDevice(){
        switch (device.getCompany()){
            case "GMCompany":
                System.out.println("进入2");
                GMCompanyDeviceHelper gmCompanyDeviceHelper = new GMCompanyDeviceHelper();
                return gmCompanyDeviceHelper.setActs(this.device);
        }
        return new String[]{""};
    }
}
