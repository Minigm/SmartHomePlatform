package com.gm.smartHomePlatform.SQLSeverManeger;

import com.gm.smartHomePlatform.Administrator.Table.TableCompany;
import com.gm.smartHomePlatform.Administrator.Table.TableDevice;
import com.gm.smartHomePlatform.Administrator.Table.TableManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompanyManager {
    private Connection connection;
    private String sql;
    public CompanyManager(){
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
    //用于判断是否存在公司。0，存在；1，不存在；2，未知错误
    public int isCompanyName(String companyName){
        sql = "select count(*) from companies where company_name = '"+companyName+"'";
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
    //用于判断是否存在项目。0，存在；1，不存在；2，未知错误
    public int isCompanyProject(String projectName){
        sql = "select count(*) from companies where company_project = '"+projectName+"'";
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
    //用于判断是否存在开发的设备类型。0，存在；1，不存在；2，未知错误
    public int isCompanyDevice(String deviceName){
        sql = "select count(*) from companies where company_device = '"+deviceName+"'";
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
    //用于判断企业列表是否有内容。0，有；1，没有；2，未知错误
    public int isCompanyAll(){
        sql = "select count(*) from companies";
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
    //获取企业开发的企业列表。
    public List<TableCompany> getCompanyName(String companyName){
        List<TableCompany> list = new ArrayList<TableCompany>();
        switch (this.isCompanyName(companyName)){
            case 0:
                TableCompany tableCompany_0 = null;
                String name,project,device,acts;
                sql = "select * from companies where company_name = '"+companyName+"'";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    while (resultSet.next()){
                        name = resultSet.getString("company_name");
                        project = resultSet.getString("company_project");
                        device = resultSet.getString("company_device");
                        acts = resultSet.getString("device_acts");
                        tableCompany_0 = new TableCompany(name,project,device,acts);
                        list.add(tableCompany_0);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }break;
            case 1:
                TableCompany tableCompany_1 = new TableCompany("无对应企业","","","");
                list.add(tableCompany_1);
                break;
            case 2:
            default:
                TableCompany tableCompany_2 = new TableCompany("未知错误","","","");
                list.add(tableCompany_2);
        }
        return list;
    }
    //获取相应项目的企业列表。
    public List<TableCompany> getCompanyProject(String projectName){
        List<TableCompany> list = new ArrayList<TableCompany>();
        switch (this.isCompanyProject(projectName)){
            case 0:
                TableCompany tableCompany_0 = null;
                String name,project,device,acts;
                sql = "select * from companies where company_project = '"+projectName+"'";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    while (resultSet.next()){
                        name = resultSet.getString("company_name");
                        project = resultSet.getString("company_project");
                        device = resultSet.getString("company_device");
                        acts = resultSet.getString("device_acts");
                        tableCompany_0 = new TableCompany(name,project,device,acts);
                        list.add(tableCompany_0);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }break;
            case 1:
                TableCompany tableCompany_1 = new TableCompany("无对应企业","","","");
                list.add(tableCompany_1);
                break;
            case 2:
            default:
                TableCompany tableCompany_2 = new TableCompany("未知错误","","","");
                list.add(tableCompany_2);
        }
        return list;
    }
    //获取设备类型的企业列表。
    public List<TableCompany> getCompanyDevcie(String deviceName){
        List<TableCompany> list = new ArrayList<TableCompany>();
        switch (this.isCompanyDevice(deviceName)){
            case 0:
                TableCompany tableCompany_0 = null;
                String name,project,device,acts;
                sql = "select * from companies where company_device = '"+deviceName+"'";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    while (resultSet.next()){
                        name = resultSet.getString("company_name");
                        project = resultSet.getString("company_project");
                        device = resultSet.getString("company_device");
                        acts = resultSet.getString("device_acts");
                        tableCompany_0 = new TableCompany(name,project,device,acts);
                        list.add(tableCompany_0);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }break;
            case 1:
                TableCompany tableCompany_1 = new TableCompany("无对应企业","","","");
                list.add(tableCompany_1);
                break;
            case 2:
            default:
                TableCompany tableCompany_2 = new TableCompany("未知错误","","","");
                list.add(tableCompany_2);
        }
        return list;
    }
    //获取整个企业列表
    public List<TableCompany> getAllCompany(){
        List<TableCompany> list = new ArrayList<TableCompany>();
        switch (this.isCompanyAll()){
            case 0:
                TableCompany tableCompany_0 = null;
                String name,project,device,acts;
                sql = "select * from companies";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    while (resultSet.next()){
                        name = resultSet.getString("company_name");
                        project = resultSet.getString("company_project");
                        device = resultSet.getString("company_device");
                        acts = resultSet.getString("device_acts");
                        tableCompany_0 = new TableCompany(name,project,device,acts);
                        list.add(tableCompany_0);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }break;
            case 1:
                TableCompany tableCompany_1 = new TableCompany("无对应企业","","","");
                list.add(tableCompany_1);
                break;
            case 2:
            default:
                TableCompany tableCompany_2 = new TableCompany("未知错误","","","");
                list.add(tableCompany_2);
        }
        return list;
    }
}
