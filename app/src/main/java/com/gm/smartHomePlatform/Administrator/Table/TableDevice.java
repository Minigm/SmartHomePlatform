package com.gm.smartHomePlatform.Administrator.Table;
//用于在表中显示的设备类
public class TableDevice extends SmartHomeDevice {
    //表中设备需包含拥有者数据
    private String owner;
    //构造函数用于实例化对象
    public TableDevice(String user,String name, String type, String company, String project) {
        super(name, type, company, project);
        this.owner = user;
    }
    //获得拥有者方法
    public String getOwner(){return this.owner;}
    //信息打包方法，用于在表中显示
    public String[] getStrings(){
        String[] strings = new String[5];
        strings[0]=owner;
        strings[1]=this.getName();
        strings[2]=this.getType();
        strings[3]=this.getCompany();
        strings[4]=this.getProject();
        return strings;
    }
}
