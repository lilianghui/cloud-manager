package com.lilianghui.db;

import com.lilianghui.entity.Config;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class JdbcUtils {
    public static synchronized Connection getConnection(Config config)
            throws Exception {
        Class<?> jdbcDriverClass = Class.forName(config.getJdbcDriver());
        Driver driver = (Driver) jdbcDriverClass.newInstance();
//        DriverManager.deregisterDriver(driver);
        DriverManager.registerDriver(driver);

        return DriverManager.getConnection(config.getJdbcUrl(),
                config.getJdbcUser(), config.getJdbcPassword());
    }

    public static void close(ResultSet rs, PreparedStatement stmt,
                             Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
