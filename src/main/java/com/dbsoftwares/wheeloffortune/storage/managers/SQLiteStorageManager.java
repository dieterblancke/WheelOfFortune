package com.dbsoftwares.wheeloffortune.storage.managers;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.dbsoftwares.wheeloffortune.WheelOfFortune;

public class SQLiteStorageManager extends HikariStorageManager {

    public SQLiteStorageManager() {
        super(StorageType.SQLITE, WheelOfFortune.getInstance().getConfiguration().getSection("storage"));
        final File database = new File(WheelOfFortune.getInstance().getDataFolder(), "sqlite-storage.db");

        try {
            if (!database.exists() && !database.createNewFile()) {
                return;
            }
        } catch (IOException e) {
            WheelOfFortune.getLog().error("An error occured: ", e);
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