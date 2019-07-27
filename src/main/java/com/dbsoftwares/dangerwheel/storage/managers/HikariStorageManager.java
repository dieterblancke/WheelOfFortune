package com.dbsoftwares.dangerwheel.storage.managers;

import com.dbsoftwares.configuration.api.ISection;
import com.dbsoftwares.dangerwheel.DangerWheel;
import com.dbsoftwares.dangerwheel.storage.AbstractStorageManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class HikariStorageManager extends AbstractStorageManager {

    protected HikariConfig config;
    protected HikariDataSource dataSource;

    public HikariStorageManager(final StorageType type, final ISection section) {
        super(type);
        config = new HikariConfig();
        config.setDataSourceClassName(getDataSourceClass());

        // Mysql-only properties
        if (type == StorageType.MYSQL) {
            config.addDataSourceProperty("serverName", section.getString("hostname"));
            config.addDataSourceProperty("port", section.getInteger("port"));
            config.addDataSourceProperty("databaseName", section.getString("database"));
            config.addDataSourceProperty("user", section.getString("username"));
            config.addDataSourceProperty("password", section.getString("password"));
            config.addDataSourceProperty("useSSL", section.getBoolean("useSSL"));

            config.addDataSourceProperty("cacheServerConfiguration", "true");
            config.addDataSourceProperty("elideSetAutoCommits", "true");
            config.addDataSourceProperty("useServerPrepStmts", "true");
            config.addDataSourceProperty("cacheCallableStmts", "true");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("alwaysSendSetIsolation", "false");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("useLocalSessionState", "true");
        }

        config.setMaximumPoolSize(section.getInteger("pool.max-pool-size"));
        config.setMinimumIdle(section.getInteger("pool.min-idle"));
        config.setMaxLifetime(section.getLong("pool.max-lifetime") * 1000);
        config.setConnectionTimeout(section.getLong("pool.connection-timeout") * 1000);

        config.setPoolName("DangerWheel");
        config.setLeakDetectionThreshold(10000);
        config.setConnectionTestQuery("/* DangerWheel ping */ SELECT 1;");
        config.setInitializationFailTimeout(-1);
    }

    protected void setupDataSource() {
        if (dataSource == null) {
            dataSource = new HikariDataSource(config);
        }
    }

    protected abstract String getDataSourceClass();

    @Override
    public void close() {
        dataSource.close();
    }

    @Override
    public boolean isOptedIn(UUID uuid) {
        boolean optedin = true;

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM dw_data WHERE uuid = ? LIMIT 1;")) {
            pstmt.setString(1, uuid.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                optedin = !rs.next();
            }

        } catch (SQLException e) {
            DangerWheel.getLog().error("An error occured: ", e);
        }
        return optedin;
    }

    @Override
    public void optIn(UUID uuid) {
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement("DELETE FROM dw_data WHERE uuid = ?;")) {
            pstmt.setString(1, uuid.toString());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            DangerWheel.getLog().error("An error occured: ", e);
        }
    }

    @Override
    public void optOut(UUID uuid) {
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement("INSERT INTO dw_data VALUES (?);")) {
            pstmt.setString(1, uuid.toString());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            DangerWheel.getLog().error("An error occured: ", e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
