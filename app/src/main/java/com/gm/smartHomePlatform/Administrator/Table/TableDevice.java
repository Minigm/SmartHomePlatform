package com.gm.smartHomePlatform.Administrator.Table;

public class TableDevice extends SmartHomeDevice {
    private String owner;
    public TableDevice(String user,String name, String type, String company, String project) {
        super(name, type, company, project);
        this.owner = user;
    }
    public String getOwner(){return this.owner;}
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
