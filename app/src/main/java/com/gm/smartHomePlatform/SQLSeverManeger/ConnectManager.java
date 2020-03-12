package com.gm.smartHomePlatform.SQLSeverManeger;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//用于获得数据库连接
public class ConnectManager {
    //用于实例化的私有Connection变量
    private Connection connection;
    //用于登录口令的类静态变量们
    static final String url_local = "jdbc:mysql://10.0.2.2:3306/smart_home";
    static final String url_net = "jdbc:mysql://111.175.77.137:3306/smart_home";
    static final String username_userAdmin = "user_admin";
    static final String password_userAdmin = "0O0O00O0";
    static final String usernmae_deviceAdmin = "device_admin";
    static final String password_deviceAdmin = "0O0O00O0";
    static final String username_companyAdmin = "company_admin";
    static final String password_companyAdmin = "0O0O00O0";
    //用于实例化的构造函数，0，以用户管理员登录；1，以设备管理员登录；2，以企业管理员登录
    public ConnectManager(int connectAs){
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        switch (connectAs){
            case 0:
                //以用户管理员身份获得Connection实例
                Connection connection_user = null;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    connection_user = (Connection) DriverManager.getConnection(url_local,username_userAdmin,password_userAdmin);
                    System.out.println("连接成功");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("驱动加载错误");
                }catch (SQLException e){
                    e.getErrorCode();
                    e.getNextException();
                    e.getSQLState();
                    System.out.println("获得连接出错");
                }
                this.connection = connection_user;break;
            case 1:
                //以设备管理员身份获得Connection实例
                Connection connection_device = null;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    connection_device = (Connection) DriverManager.getConnection(url_local,usernmae_deviceAdmin,password_deviceAdmin);
                    System.out.println("连接成功");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("驱动加载错误");
                }catch (SQLException e){
                    e.getErrorCode();
                    e.getNextException();
                    e.getSQLState();
                    System.out.println("获得连接出错");
                }
                this.connection = connection_device;break;
            case 2:
                //以设备管理员身份获得Connection实例
                Connection connection_company = null;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    connection_company = (Connection) DriverManager.getConnection(url_local,usernmae_deviceAdmin,password_deviceAdmin);
                    System.out.println("连接成功");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("驱动加载错误");
                }catch (SQLException e){
                    e.getErrorCode();
                    e.getNextException();
                    e.getSQLState();
                    System.out.println("获得连接出错");
                }
                this.connection = connection_company;break;
            default:
                break;
        }
    }
    //用于获得该Connection Manager的connection实例
    public Connection getConnection(){
        return connection;
    }
}
