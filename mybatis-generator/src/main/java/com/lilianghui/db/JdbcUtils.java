package com.lilianghui.db;

import com.lilianghui.entity.Config;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class JdbcUtils {
    private static Map<String, Connection> stringConnectionMap = new ConcurrentHashMap<>();

    public static synchronized Connection getConnection(Config config)
            throws Exception {
        Connection connection = null;
        String key = String.format("%s-%s-%s", config.getJdbcUrl(), config.getJdbcUser(), config.getJdbcPassword());
        if (stringConnectionMap.containsKey(key)) {
            connection = stringConnectionMap.get(key);
            if (connection == null || connection.isClosed()) {
                stringConnectionMap.remove(key);
                return getConnection(config);
            }
            return connection;
        }
        Class<?> jdbcDriverClass = Class.forName(config.getJdbcDriver());
        Driver driver = (Driver) jdbcDriverClass.newInstance();
        DriverManager.deregisterDriver(driver);
        DriverManager.registerDriver(driver);
        connection = DriverManager.getConnection(config.getJdbcUrl(),
                config.getJdbcUser(), config.getJdbcPassword());
        stringConnectionMap.put(key, connection);
        return connection;
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
//            if (conn != null) {
//                conn.close();
//            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public static void closeAll() {
        stringConnectionMap.forEach((s, connection) -> {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
