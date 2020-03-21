package com.gm.smartHomePlatform.SQLSeverManeger;

import com.gm.smartHomePlatform.Administrator.Table.TableDevice;


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
    //用于判断是否有该类型的设备。0，添加了；1，没有；2，未知错误
    public int isDeviceType(String typeName){
        sql = "select count(*) from devices where device_type = '"+typeName+"'";
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
    //用于判断企业的设备是否被添加。0，添加了；1，没有；2，未知错误
    public int isDeviceCompany(String companyName){
        sql = "select count(*) from devices where device_company = '"+companyName+"'";
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
    //用于判断企业项目的设备是否被添加。0，添加了；1，没有；2，未知错误
    public int isDeviceProject(String projectName){
        sql = "select count(*) from devices where device_project = '"+projectName+"'";
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
    //用于判断设备列表是否有设备。0，有；1，没有；2，未知错误
    public int isDeviceAll(){
        sql = "select count(*) from devices";
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
    //获取相同类型的设备
    public List<TableDevice> getDeviceType(String typeName){
        List<TableDevice> list = new ArrayList<TableDevice>();
        switch (this.isDeviceType(typeName)){
            case 0:
                TableDevice tableDevice_0 =null;
                String name,device,company,project;
                sql = "select * from devices where device_type = '"+typeName+"'";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    while (resultSet.next()){
                        name = resultSet.getString("device_owner");
                        device = resultSet.getString("device_name");
                        company = resultSet.getString("device_company");
                        project = resultSet.getString("device_project");
                        tableDevice_0 = new TableDevice(name,device,typeName,company,project);
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
    //获取相同公司的设备
    public List<TableDevice> getDeviceCompany(String companyName){
        List<TableDevice> list = new ArrayList<TableDevice>();
        switch (this.isDeviceCompany(companyName)){
            case 0:
                TableDevice tableDevice_0 =null;
                String name,device,type,project;
                sql = "select * from devices where device_company = '"+companyName+"'";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    while (resultSet.next()){
                        name = resultSet.getString("device_owner");
                        device = resultSet.getString("device_name");
                        type = resultSet.getString("device_type");
                        project = resultSet.getString("device_project");
                        tableDevice_0 = new TableDevice(name,device,type,companyName,project);
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
    //获取相同项目的设备
    public List<TableDevice> getDeviceProject(String projectName){
        List<TableDevice> list = new ArrayList<TableDevice>();
        switch (this.isDeviceCompany(projectName)){
            case 0:
                TableDevice tableDevice_0 =null;
                String name,device,type,company;
                sql = "select * from devices where device_project = '"+projectName+"'";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    while (resultSet.next()){
                        name = resultSet.getString("device_owner");
                        device = resultSet.getString("device_name");
                        type = resultSet.getString("device_type");
                        company = resultSet.getString("device_company");
                        tableDevice_0 = new TableDevice(name,device,type,company,projectName);
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
        switch (this.isDeviceAll()){
            case 0:
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
                }break;
            case 1:
                TableDevice tableDevice_1 = new TableDevice("当前无设备","","","","");
                list.add(tableDevice_1);
                break;
            case 2:
            default:
                TableDevice tableDevice_2 = new TableDevice("未知错误","","","","");
                list.add(tableDevice_2);
        }
        return list;
    }
    //删除设备。0用户名为空，1不存在设备，2用户设备删除成功，3设备删除成功，4未知错误
    public int deleteDevice(String owner,String deviceName){
        if (owner.equals("")){
            return 0;
        }else {
            if (deviceName.equals("")){
                sql = "delete from devices where device_owner = '"+owner+"'";
                try {
                    connection.createStatement().execute(sql);
                    return 2;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else{
                if (this.isDeviceName(deviceName) == 0){
                    sql = "delete from devices where device_owner = '"+owner+"' and where device_name = '"+deviceName+"'";
                    try {
                        connection.createStatement().execute(sql);
                        return 3;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else {
                    return 1;
                }
            }
        }
        return 4;
    }
}

