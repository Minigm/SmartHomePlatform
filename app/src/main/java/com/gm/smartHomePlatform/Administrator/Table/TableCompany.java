package com.gm.smartHomePlatform.Administrator.Table;

public class TableCompany {
    private String company_name;
    private String company_project;
    private String company_device;
    private String device_acts;
    public TableCompany(String name,String project,String device,String acts){
        this.company_name = name;
        this.company_project = project;
        this.company_device = device;
        this.device_acts = acts;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getCompany_project() {
        return company_project;
    }

    public String getCompany_device() {
        return company_device;
    }

    public String getDevice_acts() {
        return device_acts;
    }
    //信息打包方法，用于在表中显示
    public String[] getStrings(){
        String[] strings = new String[4];
        strings[0]=company_name;
        strings[1]=company_project;
        strings[2]=company_device;
        strings[3]=device_acts;
        return strings;
    }
}
