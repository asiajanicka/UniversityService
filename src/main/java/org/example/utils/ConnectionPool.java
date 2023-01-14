package org.example.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.propertiesReaders.DataSourcePropertiesReader;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;
    private static ConnectionPool instance;

    private ConnectionPool() {
        DataSourcePropertiesReader props = new DataSourcePropertiesReader();
        config.setJdbcUrl(props.getUrl());
        config.setUsername(props.getUsername());
        config.setPassword(props.getPassword());
        config.setDriverClassName(props.getDriver());
        ds = new HikariDataSource(config);
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

}
