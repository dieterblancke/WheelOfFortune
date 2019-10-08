package com.dbsoftwares.wheeloffortune.storage;

import com.dbsoftwares.wheeloffortune.WheelOfFortune;
import com.dbsoftwares.wheeloffortune.storage.managers.FileStorageManager;
import com.dbsoftwares.wheeloffortune.storage.managers.H2StorageManager;
import com.dbsoftwares.wheeloffortune.storage.managers.MySQLStorageManager;
import com.dbsoftwares.wheeloffortune.storage.managers.SQLiteStorageManager;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public abstract class AbstractStorageManager {

    @Getter
    private static AbstractStorageManager manager;

    @Getter
    private StorageType type;

    public AbstractStorageManager(final StorageType type) {
        manager = this;

        this.type = type;
    }

    public String getName() {
        return type.getName();
    }

    public void initializeStorage() throws Exception {
        if (!type.hasSchema()) {
            return;
        }
        try (InputStream is = WheelOfFortune.getInstance().getResource(type.getSchema())) {
            if (is == null) {
                throw new Exception("Could not find schema for " + type.toString() + ": " + type.getSchema() + "!");
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                 Connection connection = getConnection(); Statement st = connection.createStatement()) {

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);

                    if (line.endsWith(";")) {
                        builder.deleteCharAt(builder.length() - 1);

                        final String statement = builder.toString().trim();
                        if (!statement.isEmpty()) {
                            st.executeUpdate(statement);
                        }

                        builder = new StringBuilder();
                    }
                }
            }
        }
    }

    public abstract boolean isOptedIn(final UUID uuid);

    public abstract void optIn(final UUID uuid);

    public abstract void optOut(final UUID uuid);

    public abstract Connection getConnection() throws SQLException;

    public abstract void close() throws SQLException;

    @Getter
    public enum StorageType {

        MYSQL(MySQLStorageManager.class, "MySQL", "schemas/mysql.sql"),
        SQLITE(SQLiteStorageManager.class, "SQLite", "schemas/sqlite.sql"),
        H2(H2StorageManager.class, "H2", "schemas/h2.sql"),
        FILE(FileStorageManager.class, "PLAIN", null);

        private Class<? extends AbstractStorageManager> manager;
        private String name;
        private String schema;

        StorageType(final Class<? extends AbstractStorageManager> manager, final String name, final String schema) {
            this.manager = manager;
            this.name = name;
            this.schema = schema;
        }

        public boolean hasSchema() {
            return schema != null;
        }
    }
}