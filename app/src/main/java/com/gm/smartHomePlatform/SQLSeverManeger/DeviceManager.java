package com.gm.smartHomePlatform.SQLSeverManeger;

import java.sql.Connection;

public class DeviceManager {
    private Connection connection;
    private String sql;
    public DeviceManager(){
        ConnectManager connectManager = new ConnectManager();
        connection = connectManager.getConnection();
        sql = "";
    }
<<<<<<< HEAD
    //用于关闭connection关闭
    public void disconnected(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //用于内部资源的关闭。
    private void close(Statement statement, ResultSet resultSet){
        try {
            //如果statement有实例，执行close清除。
            if (statement != null)statement.close();
            //如果resultSet有实例，执行close清除。
            if (resultSet != null)resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int isDevice(String deviceName,String userName){
        try {
            sql = "select count(*) where device_owner = '"+userName+"' and device_name = '"+deviceName+"'";
        }
    }
=======

>>>>>>> parent of fb41088... 多电脑操作test
}
