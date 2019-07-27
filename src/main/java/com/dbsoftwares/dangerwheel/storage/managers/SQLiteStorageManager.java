package com.dbsoftwares.dangerwheel.storage.managers;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.dbsoftwares.dangerwheel.DangerWheel;

public class SQLiteStorageManager extends HikariStorageManager {

    public SQLiteStorageManager() {
        super(StorageType.SQLITE, DangerWheel.getInstance().getConfiguration().getSection("storage"));
        final File database = new File(DangerWheel.getInstance().getDataFolder(), "sqlite-storage.db");

        try {
            if (!database.exists() && !database.createNewFile()) {
                return;
            }
        } catch (IOException e) {
            DangerWheel.getLog().error("An error occured: ", e);
        }

        config.addDataSourceProperty("url", "jdbc:sqlite:" + database.getPath());
        setupDataSource();
    }

    @Override
    protected String getDataSourceClass() {
        return "org.sqlite.SQLiteDataSource";
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}