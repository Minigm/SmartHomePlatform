package com.gm.smartHomePlatform.Administrator.Table;

public class SmartHomeDevice {
    private String device_name;
    private String device_type;
    private String device_company;
    private String device_project;
    public SmartHomeDevice(String name,String type,String company,String project){
        this.device_name = name;
        this.device_type = type;
        this.device_company = company;
        this.device_project = project;
    }
    public String getName(){return this.device_name;}
    public String getType(){return this.device_type;}
    public String getCompany(){return this.device_company;}
    public String getProject(){return this.device_project;}
    public void rename(String name){this.device_name = name;}
}
