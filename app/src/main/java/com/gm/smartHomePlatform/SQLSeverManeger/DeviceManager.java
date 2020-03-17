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

}
