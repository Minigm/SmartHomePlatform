package com.gm.smartHomePlatform.SQLSeverManeger;

import com.gm.smartHomePlatform.Administrator.Table.TableCompany;
import com.gm.smartHomePlatform.Administrator.Table.TableDevice;
import com.gm.smartHomePlatform.Administrator.Table.TableManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
    //用于获取对应设备的属性列表。
    public String getActs(String companyName,String projectName,String deviceName){
        String acts = null;
        if (isCompanyName(companyName)+isCompanyProject(projectName)+isCompanyDevice(deviceName) == 0){
            sql = "select device_acts from companies where company_name = '"+companyName+"' and company_project = '"+projectName+"' and company_device = '"+deviceName+"'";
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                resultSet.next();
                acts = resultSet.getString("device_acts");
                this.close(statement,resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return acts;
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
    //添加企业。0,未知错误；1，企业名为空；2，项目名为空；3，设备类型为空；4，已存在的记录；5，更新成功；6，添加成功。
    public int addCompany(TableCompany tableCompany){
        String name = tableCompany.getCompany_name();
        String project = tableCompany.getCompany_project();
        String device = tableCompany.getCompany_device();
        String acts = tableCompany.getDevice_acts();
        if (name.equals("")){
            return 1;
        }else {
            if (project.equals("")){
                return 2;
            }else {
                if (device.equals("")){
                    return 3;
                }else {
                    if (this.isCompanyName(name) == 0){
                        if (this.isCompanyProject(project) == 0){
                            if (this.isCompanyDevice(device) == 0){
                                if (acts.equals(this.getActs(name,project,device))){
                                    return 4;
                                }else {
                                    sql = "update companies set device_acts = ? where company_name = ? and company_project = ? and company_device = ?";
                                    try {
                                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                                        preparedStatement.setString(1,acts);
                                        preparedStatement.setString(2,name);
                                        preparedStatement.setString(3,project);
                                        preparedStatement.setString(4,device);
                                        preparedStatement.executeUpdate();
                                        return 5;
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                    sql = "insert into companies (company_name,company_project,company_device,device_acts) values(?,?,?,?)";
                    try {
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1,name);
                        preparedStatement.setString(2,project);
                        preparedStatement.setString(3,device);
                        preparedStatement.setString(4,acts);
                        preparedStatement.executeUpdate();
                        return 6;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return 0;
    }
    //删除企业。0未知错误；1，企业不存在；2，项目不存在；3，设备类型不存在；4，设备属性不存在；5，删除成功。
    public int deleteCompany(TableCompany tableCompany){
        String name = tableCompany.getCompany_name();
        String project = tableCompany.getCompany_project();
        String device = tableCompany.getCompany_device();
        String acts = tableCompany.getDevice_acts();
        if (this.isCompanyName(name) == 0){
            if (project.equals("")){
                sql = "delete from companies where company_name = '"+name+"'";
                try {
                    Statement statement = connection.createStatement();
                    statement.execute(sql);
                    return 5;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else {
                if (this.isCompanyProject(project) == 0){
                    if (device.equals("")){
                        sql = "delete from companies where company_name = '"+name+"' and company_project = '"+project+"'";
                        try {
                            Statement statement = connection.createStatement();
                            statement.execute(sql);
                            return 5;
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else {
                        if (this.isCompanyDevice(device) == 0){
                            String[] has_acts = this.getActs(name,project,device).split(";");
                            String[] now_acts = acts.split(";");
                            List<String> list = new ArrayList<String>();
                            for (int i = 0;i < has_acts.length;i++){
                                list.add(now_acts[i]);
                            }
                            for (int j = 0;j < now_acts.length;j++){
                                if (list.get(j).equals(now_acts[j])){
                                    list.remove(j);
                                }
                            }
                            for (int i = 0;i < list.size();i++){
                                acts += list.get(i);
                            }
                            TableCompany tableCompany1 = new TableCompany(name,project,device,acts);
                            return this.addCompany(tableCompany1);
                        }else {
                            return 3;
                        }
                    }
                }else {
                    return 2;
                }
            }
        }else {
            return 1;
        }
        return 0;
    }
}
