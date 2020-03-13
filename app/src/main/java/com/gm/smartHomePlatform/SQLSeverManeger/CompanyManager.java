package com.gm.smartHomePlatform.SQLSeverManeger;

import java.sql.Connection;

public class CompanyManager {
    private Connection connection;
    private String sql;
    public CompanyManager(){
        ConnectManager connectManager = new ConnectManager();
        connection = connectManager.getConnection();
        sql = "";
    }
}
