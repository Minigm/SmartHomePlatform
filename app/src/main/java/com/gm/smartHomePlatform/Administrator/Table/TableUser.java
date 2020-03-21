package com.gm.smartHomePlatform.Administrator.Table;

public class TableUser {
    private String user_name;
    private String user_password;
    private String user_power;
    public TableUser (String name, String password, String power){
        this.user_name = name;
        this.user_password = password;
        this.user_power = power;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public String getUser_power() {
        return user_power;
    }
    public String[] getStrings(){
        String[] strings = new String[3];
        strings[0]=user_name;
        strings[1]=user_password;
        strings[2]=user_power;
        return strings;
    }
}
