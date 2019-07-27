package com.dbsoftwares.dangerwheel.storage.managers;

import com.dbsoftwares.configuration.api.FileStorageType;
import com.dbsoftwares.configuration.api.IConfiguration;
import com.dbsoftwares.dangerwheel.DangerWheel;
import com.dbsoftwares.dangerwheel.storage.AbstractStorageManager;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.UUID;

public class FileStorageManager extends AbstractStorageManager {

    private final IConfiguration storage;

    public FileStorageManager(final String type) {
        super(StorageType.FILE);

        // load configuration file
        FileStorageType storageType;
        try {
            storageType = FileStorageType.valueOf(type);
        } catch (IllegalArgumentException e) {
            storageType = FileStorageType.JSON;
        }
        final File storageFile = new File(
                DangerWheel.getInstance().getDataFolder(),
                "file-storage." + (storageType.equals(FileStorageType.JSON) ? "json" : "yml")
        );
        if (!storageFile.exists()) {
            try {
                storageFile.createNewFile();
            } catch (IOException e) {
                DangerWheel.getLog().error("An error occured: ", e);
            }
        }
        this.storage = IConfiguration.loadConfiguration(storageType, storageFile);
        if (!storage.exists("optedout")) {
            storage.set("optedout", Lists.newArrayList());
            save();
        }
    }

    @Override
    public boolean isOptedIn(UUID uuid) {
        return !storage.getStringList("optedout").contains(uuid.toString());
    }

    @Override
    public void optIn(UUID uuid) {
        final List<String> optedout = storage.getStringList("optedout");

        optedout.remove(uuid.toString());

        storage.set("optedout", optedout);
        if (DangerWheel.getInstance().getConfiguration().getBoolean("storage.save-per-change")) {
            save();
        }
    }

    @Override
    public void optOut(UUID uuid) {
        final List<String> optedout = storage.getStringList("optedout");

        if (!optedout.contains(uuid.toString())) {
            optedout.add(uuid.toString());
        }

        storage.set("optedout", optedout);
        if (DangerWheel.getInstance().getConfiguration().getBoolean("storage.save-per-change")) {
            save();
        }
    }

    @Override
    public Connection getConnection() {
        return null;
    }

    @Override
    public void close() {
        save();
    }

    private void save() {
        try {
            storage.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
