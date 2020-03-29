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
    static final String url_local = "jdbc:mysql://192.168.1.200:3306/smarthome";
    static final String url_net = "jdbc:mysql://111.175.77.137:3306/smarthome";
    static final String username_admin = "admin";
    static final String password_admin = "###0O0O00o0";
    //用于实例化的构造函数。
    public ConnectManager(){
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection_temp = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection_temp = (Connection) DriverManager.getConnection(url_net,username_admin,password_admin);
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
        this.connection = connection_temp;
    }
    //用于获得该Connection Manager的connection实例
    public Connection getConnection(){
        return connection;
    }
}
