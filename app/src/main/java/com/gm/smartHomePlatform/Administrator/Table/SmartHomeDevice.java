package com.gm.smartHomePlatform.Administrator.Table;
//设计基本设备类
// 厂商通过继承其开发各自的设备
// 尤其是添加相应的方法
public class SmartHomeDevice {
    //设备名称数据
    private String device_name;
    //设备类型数据
    private String device_type;
    //设备所属公司数据
    private String device_company;
    //设备所属工程数据
    private String device_project;
    //构造函数用于实例化对象
    public SmartHomeDevice(String name,String type,String company,String project){
        this.device_name = name;
        this.device_type = type;
        this.device_company = company;
        this.device_project = project;
    }
    //取得设备名方法
    public String getName(){return this.device_name;}
    //取得设备类型方法
    public String getType(){return this.device_type;}
    //取得所属公司方法
    public String getCompany(){return this.device_company;}
    //取得所属工程方法
    public String getProject(){return this.device_project;}
    //更换设备名方法
    public void rename(String name){this.device_name = name;}
}
