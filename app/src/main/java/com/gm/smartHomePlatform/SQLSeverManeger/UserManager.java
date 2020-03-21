package com.gm.smartHomePlatform.SQLSeverManeger;

import com.gm.smartHomePlatform.Administrator.Table.TableUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    //通过构造函数创建UserManager实例，获得connect值
    private Connection connection;
    private String sql;
    public UserManager(){
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
    private void close(Statement statement,ResultSet resultSet){
        try {
            //如果statement有实例，执行close清除。
            if (statement != null)statement.close();
            //如果resultSet有实例，执行close清除。
            if (resultSet != null)resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //判断users表中是否有对应用户。0，有；1，没有；2，未知错误。
    public int isUser(String username){
        sql = "select count(*) from users where user_name = '"+username+"'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            //将结果移动到第一个结果
            resultSet.next();
            if (resultSet.getInt(1) == 0){
                //如果表中没有该用户，以1状态返回
                this.close(statement,resultSet);return 1;
            }else {
                //如果存在则以0状态返回
                this.close(statement,resultSet);return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //如果发生意外运行至此，以2状态返回
        return 2;
    }
    //判断users表中是否有对应权力的用户。0，有；1，没有；2，未知错误。
    public int isPower(String power){
        sql = "select count(*) from users where user_power = '"+power+"'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            //将结果移动到第一个结果
            resultSet.next();
            if (resultSet.getInt(1) == 0){
                //如果表中没有该用户，以1状态返回
                this.close(statement,resultSet);return 1;
            }else {
                //如果存在则以0状态返回
                this.close(statement,resultSet);return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //如果发生意外运行至此，以2状态返回
        return 2;
    }
    //通过用户名找到密码。"no user"没有该用户,"密码"成功找到,"unknown mistake"未知错误。
    private String getPassword(String username){
        if (this.isUser(username) == 1){
            //如果表中没有用户，以“no user"状态返回
            return "no user";
        }else if (this.isUser(username) == 0) {
            //如果表中有该用户，以获得的密码状态返回
            sql = "select user_password from users where user_name = '"+username+"'";
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                //由于user_name是键值，故若能找到密码，该密码唯一。
                resultSet.next();
                String temp = resultSet.getString("user_password");
                this.close(statement,resultSet);
                return temp;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //如果意外运行至此，以“unknown mistake”返回
        return "unknown mistake";
    }
    //判断是否为正确账户。0,是正确的用户;1,密码错误;2,没有用户;3,未知错误。
    private int isAccount(String username, String password){
        String temp = this.getPassword(username);
        if (temp.equals("no user")){
            //若get到密码为"no user"，即无该用户，以2状态返回
            return 2;
        }else if (temp.equals("unknown mistake")){
            //若get到密码为“unknown mistake”,即出现未知错误，以3状态返回
            return 3;
        }else{
            if (temp.equals(password)){
                //如果get到密码与输入相同，以0状态返回
                return 0;
            }else {
                //如果与输入的不同，以1状态返回
                return 1;
            }
        }
    }
    //判断登录用户类型。0,普通用户;1,企业用户;2,平台管理用户;3,密码错误;4,没有该用户;5,未知错误。
    public int whatAccount(String username,String password){
        switch (this.isAccount(username,password)){
            case 0:
                //0状态是正确的用户与密码，则执行判断身份功能
                sql = "select user_power from users where user_name = '"+username+"'";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    resultSet.next();
                    String temp = resultSet.getString("user_power");
                    switch (temp) {
                        case "0":
                            //0状态表示是普通用户，以0状态返回
                            this.close(statement, resultSet);return 0;
                        case "1":
                            //1状态表示是企业用户，以1状态返回
                            this.close(statement, resultSet);return 1;
                        case "2":
                            //2状态表示是管理员用户，以2状态返回
                            this.close(statement, resultSet);
                            return 2;
                        default:
                            //避免有人尝试出管理员口令，错误登录，以4状态返回
                            return 4;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }break;
            case 1:
                //1状态表示输入用户名与密码错误，以3状态返回
                return 3;
            case 2:
                //2状态表示没有该用户，以4状态返回
                return 4;
            default:
                break;
        }
        //若出现意外运行至此，以5状态返回
        return 5;
    }
    //判断用户列表中是否有元素。0，有；1，没有；2，未知错误。
    public int isUserAll(){
        sql = "select count(*) from users";
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
    //用户注册。0,注册成功；1，用户已存在；2，用户名为空；3，密码为空；4，未知错误
    public int addUser(String username, String password){
        return addUser(username, password,"0");
    }
    public int addUser(String username, String password,String power){
        if (username.equals("")){
            return 2;
        }else {
            if (password.equals("")){
                return 3;
            }else {
                switch (this.isUser(username)){
                    case 0:
                        //0状态表示用户已经存在，以1状态返回
                        return 1;
                    case 1:
                        //1状态表示不存在用户，执行注册流程，以0状态返回
                        sql = "insert into users (user_name,user_password,user_power) values(?,?,?)";
                        try {
                            if (power.equals(""))power = "0" ;
                            PreparedStatement preparedStatement = connection.prepareStatement(sql);
                            preparedStatement.setString(1,username);
                            preparedStatement.setString(2,password);
                            preparedStatement.setString(3,power);
                            preparedStatement.executeUpdate();
                            DeviceManager deviceManager = null;
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    default:
                        break;
                }
            }
        }
        //若发生意外运行至此，以2状态返回
        return 4;
    }
    //管理员口令获取。正确口令返回true，错误返会false。
    public boolean isCode(String code){
        sql = "select user_name from users where user_power = '3'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                if (resultSet.getString("user_name").equals(code)) {
                    this.close(statement,resultSet);return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    //用户密码修改。
    public void changePassword(String username,String password){
        sql = "update users set user_password = ? where user_name = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,password);
            ps.setString(2,username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //删除用户。0，删除成功；1，没有该用户。
    public int deleteUser(String username){
        if (this.isUser(username)==0){
            sql = "delete from users where user_name = '"+username+"'";
            try {
                connection.createStatement().execute(sql);
                DeviceManager deviceManager = new DeviceManager();
                deviceManager.deleteDevice(username,"");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }else {
            return 1;
        }
    }
    //获取某权力的用户
    public List<TableUser> getUserPower(String userPower){
        List<TableUser> tableUser = new ArrayList<TableUser>();
        switch (this.isPower(userPower)){
            case 0:
                TableUser tableUser_0 = null;
                String name,password,power;
                sql = "select * from users where user_power = '"+userPower+"'";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    while (resultSet.next()){
                        name = resultSet.getString("user_name");
                        password = resultSet.getString("user_password");
                        power = resultSet.getString("user_power");
                        tableUser_0 = new TableUser(name,password,power);
                        tableUser.add(tableUser_0);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }break;
            case 1:
                TableUser tableUser_1 = new TableUser("无对应用户","","");
                tableUser.add(tableUser_1);
                break;
            case 2:
            default:
                TableUser tableUser_2 = new TableUser("未知错误","","");
                tableUser.add(tableUser_2);
        }
        return tableUser;
    }
    //获取某用户
    public List<TableUser> getUserName(String userName){
        List<TableUser> tableUser = new ArrayList<TableUser>();
        switch (this.isUser(userName)){
            case 0:
                TableUser tableUser_0 = null;
                String name,password,power;
                sql = "select * from users where user_name = '"+userName+"'";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    while (resultSet.next()){
                        name = resultSet.getString("user_name");
                        password = resultSet.getString("user_password");
                        power = resultSet.getString("user_power");
                        tableUser_0 = new TableUser(name,password,power);
                        tableUser.add(tableUser_0);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }break;
            case 1:
                TableUser tableUser_1 = new TableUser("无对应用户","","");
                tableUser.add(tableUser_1);
                break;
            case 2:
            default:
                TableUser tableUser_2 = new TableUser("未知错误","","");
                tableUser.add(tableUser_2);
        }
        return tableUser;
    }
    //获取所用用户
    public List<TableUser> getAllUser(){
        List<TableUser> list = new ArrayList<TableUser>();
        switch (this.isUserAll()){
            case 0:
                TableUser tableUser_0 = null;
                String name,password,power;
                sql = "select * from users";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    while (resultSet.next()){
                        name = resultSet.getString("user_name");
                        password = resultSet.getString("user_password");
                        power = resultSet.getString("user_power");
                        tableUser_0 = new TableUser(name,password,power);
                        list.add(tableUser_0);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }break;
            case 1:
                TableUser tableUser_1 = new TableUser("无对应用户","","");
                list.add(tableUser_1);
                break;
            case 2:
            default:
                TableUser tableUser_2 = new TableUser("未知错误","","");
                list.add(tableUser_2);
        }
        return list;
    }
}
