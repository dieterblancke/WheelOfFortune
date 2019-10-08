package com.dbsoftwares.wheeloffortune.storage.managers;

import com.dbsoftwares.wheeloffortune.WheelOfFortune;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class H2StorageManager extends HikariStorageManager {

    public H2StorageManager() {
        super(StorageType.H2, WheelOfFortune.getInstance().getConfiguration().getSection("storage"));
        final File database = new File(WheelOfFortune.getInstance().getDataFolder(), "h2-storage.db");

        try {
            if (!database.exists() && !database.createNewFile()) {
                return;
            }
        } catch (IOException e) {
            WheelOfFortune.getLog().error("An error occured: ", e);
        }

        config.addDataSourceProperty("url", "jdbc:h2:./" + database.getPath());
        setupDataSource();
    }

    @Override
    protected String getDataSourceClass() {
        return "org.h2.jdbcx.JdbcDataSource";
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}