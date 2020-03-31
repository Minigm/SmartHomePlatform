package com.gm.smartHomePlatform.Device;


import java.util.ArrayList;

public class BaseDevice {
    private String name;
    private String company;
    private String project;
    private String type;
    private String id;
    public BaseDevice(String name, String company, String project, String type, String id){
        this.name = name;this.company = company;this.project = project;
        this.type = type;this.id = id;
    }
    public String getName() {
        return name;
    }
    public String getCompany() {
        return company;
    }
    public String getProject() {
        return project;
    }
    public String getType() {
        return type;
    }
    public String getId() {
        return id;
    }
    public static ArrayList<BaseDevice> getTestList(){
        ArrayList<BaseDevice> list = new ArrayList<BaseDevice>();
        BaseDevice baseDevice;
        String nert;
        for (int i = 0;i < 100;i++){
            nert = "品名测试"+i;
            baseDevice = new BaseDevice(nert,"测试","测试","测试","001");
            list.add(baseDevice);
        }
        return list;
    }
}
