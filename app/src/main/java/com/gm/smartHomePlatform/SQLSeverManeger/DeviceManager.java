package com.gm.smartHomePlatform.SQLSeverManeger;

import com.gm.smartHomePlatform.Administrator.Table.SmartHomeDevice;
import com.gm.smartHomePlatform.Administrator.Table.TableDevice;
import com.gm.smartHomePlatform.Administrator.Table.TableManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DeviceManager {
    private Connection connection;
    private String sql;
    //用于实例化DM对象
    public DeviceManager(){
        ConnectManager connectManager = new ConnectManager();
        connection = connectManager.getConnection();
        sql = "";
    }
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
    //用于判断是否存在对应的唯一设备。0，有;1，没有；2，未知错误
    public int isOnlyDevice(String deviceName,String userName){
        sql = "select count(*) from devices where device_owner = '"+userName+"' and device_name = '"+deviceName+"'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            if (resultSet.getInt(1) == 0){
                this.close(statement,resultSet);return 1;
            }else {
                this.close(statement,resultSet);return 0;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 2;
    }
    //用于判断是否存在对应设备名设备。0，有；1，没有；2，未知错误
    public int isDeviceName(String deviceName){
        sql = "select count(*) from devices where device_name = '"+deviceName+"'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            if (resultSet.getInt(1) == 0){
                this.close(statement,resultSet);return 1;
            }else {
                this.close(statement,resultSet);return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 2;
    }
    //用于判断用户是否添加设备。0，添加了；1，没有；2，未知错误
    public int isDeviceUser(String userName){
        sql = "select count(*) from devices where device_owner = '"+userName+"'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            if (resultSet.getInt(1) == 0){
                this.close(statement,resultSet);return 1;
            }else {
                this.close(statement,resultSet);return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 2;
    }
    //获取用户拥有的设备列表。
    public List<TableDevice> getDeviceUser(String userName){
        List<TableDevice> list = new ArrayList<TableDevice>();
        switch (this.isDeviceUser(userName)){
            case 0:
                TableDevice tableDevice_0 =null;
                String name,type,company,project;
                sql = "select * from devices where device_owner = '"+userName+"'";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    while (resultSet.next()){
                        name = resultSet.getString("device_name");
                        type = resultSet.getString("device_type");
                        company = resultSet.getString("device_company");
                        project = resultSet.getString("device_project");
                        tableDevice_0 = new TableDevice(userName,name,type,company,project);
                        list.add(tableDevice_0);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }break;
            case 1:
                TableDevice tableDevice_1 = new TableDevice("无对应设备","","","","");
                list.add(tableDevice_1);
                break;
            case 2:
            default:
                TableDevice tableDevice_2 = new TableDevice("未知错误","","","","");
                list.add(tableDevice_2);
        }
        return list;
    }
    //获取相同设备名的设备
    public List<TableDevice> getDeviceName(String deviceName){
        List<TableDevice> list = new ArrayList<TableDevice>();
        switch (this.isDeviceName(deviceName)){
            case 0:
                TableDevice tableDevice_0 =null;
                String name,type,company,project;
                sql = "select * from devices where device_name = '"+deviceName+"'";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    while (resultSet.next()){
                        name = resultSet.getString("device_owner");
                        type = resultSet.getString("device_type");
                        company = resultSet.getString("device_company");
                        project = resultSet.getString("device_project");
                        tableDevice_0 = new TableDevice(name,deviceName,type,company,project);
                        list.add(tableDevice_0);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }break;
            case 1:
                TableDevice tableDevice_1 = new TableDevice("无对应设备","","","","");
                list.add(tableDevice_1);
                break;
            case 2:
            default:
                TableDevice tableDevice_2 = new TableDevice("未知错误","","","","");
                list.add(tableDevice_2);
        }
        return list;
    }
    //获取整个设备列表
    public List<TableDevice> getAllDevice(){
        List<TableDevice> list = new ArrayList<TableDevice>();
        TableDevice tableDevice_0 =null;
        String name,device,type,company,project;
        sql = "select * from devices";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                name = resultSet.getString("device_owner");
                device = resultSet.getString("device_name");
                type = resultSet.getString("device_type");
                company = resultSet.getString("device_company");
                project = resultSet.getString("device_project");
                tableDevice_0 = new TableDevice(name,device,type,company,project);
                list.add(tableDevice_0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

