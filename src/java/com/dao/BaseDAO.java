package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 咕噜科
 * ClassName: BaseDao
 * date: 2021/8/26 22:11
 * Description:
 * version 1.0
 */

public class BaseDAO {

    public Connection conn = null;
    public PreparedStatement state = null;
    public ResultSet rs = null;


    /**
     * 获取连接对象
     * @return
     */
    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pet_store", "root", "123");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    public int update(String sql, Object...obs) throws SQLException {
        int result = 0;
        conn = getConnection();
        state = conn.prepareStatement(sql);

        for (int i = 0; i < obs.length; i++) {
            state.setObject(i + 1, obs[i]);
        }
        result = state.executeUpdate();
        return result;
    }

    public ResultSet search(String sql, Object...obs) {
        try {
            conn = getConnection();
            state = conn.prepareStatement(sql);
            for (int i = 0; i < obs.length; i++) {
                state.setObject(i + 1, obs[i]);
            }
            rs = state.executeQuery();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rs;
    }
    public void closeObject1() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (state != null) {
                state.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void closeObject2(AutoCloseable... obs) {
        try {
            for (int i = 0; i < obs.length; i++) {
                if (obs[i] != null) {
                    obs[i].close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


