package com.dbsoftwares.dangerwheel.storage.managers;

import com.dbsoftwares.dangerwheel.DangerWheel;
import java.sql.Connection;
import java.sql.SQLException;

public class MySQLStorageManager extends HikariStorageManager {

    public MySQLStorageManager() {
        super(StorageType.MYSQL, DangerWheel.getInstance().getConfiguration().getSection("storage"));

        setupDataSource();
    }

    @Override
    protected String getDataSourceClass() {
        return "com.mysql.jdbc.jdbc2.optional.MysqlDataSource";
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}