package com.dbsoftwares.wheeloffortune;

import com.dbsoftwares.commandapi.CommandManager;
import com.dbsoftwares.configuration.api.IConfiguration;
import com.dbsoftwares.wheeloffortune.commands.FortuneWheelCommand;
import com.dbsoftwares.wheeloffortune.commands.subcommands.TestSubCommand;
import com.dbsoftwares.wheeloffortune.listeners.PlayerListener;
import com.dbsoftwares.wheeloffortune.script.Script;
import com.dbsoftwares.wheeloffortune.script.ScriptData;
import com.dbsoftwares.wheeloffortune.storage.AbstractStorageManager;
import com.dbsoftwares.wheeloffortune.utils.objects.CircleColor;
import com.dbsoftwares.wheeloffortune.wheel.WheelManager;
import com.dbsoftwares.wheeloffortune.wheel.block.BlockManager;
import com.dbsoftwares.wheeloffortune.wheel.hologram.HologramManager;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.dbsoftwares.wheeloffortune.storage.AbstractStorageManager.StorageType;

public class WheelOfFortune extends JavaPlugin {

    @Getter
    private static Logger log = LoggerFactory.getLogger("WheelOfFortune");

    @Getter
    private static WheelOfFortune instance;

    @Getter
    private IConfiguration configuration;

    @Getter
    private IConfiguration dataConfig;

    @Getter
    private List<ScriptData> scripts = Lists.newArrayList();

    @Getter
    private Map<CircleColor, ScriptData> colorEvents = Maps.newHashMap();

    @Getter
    private WheelManager wheelManager;

    @Getter
    private AbstractStorageManager storage;

    @Override
    public void onEnable() {
        instance = this;

        this.loadConfiguration();
        this.loadColorEvents();

        CommandManager.getInstance().registerCommand(new FortuneWheelCommand());
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        StorageType type;
        final String typeString = configuration.getString("storage.type").toUpperCase();
        try {

            if (typeString.contains(":")) {
                type = StorageType.valueOf(typeString.split(":")[0]);
            } else {
                type = StorageType.valueOf(typeString);
            }
        } catch (IllegalArgumentException e) {
            type = StorageType.MYSQL;
        }
        try {
            storage = typeString.contains(":")
                    ? type.getManager().getConstructor(String.class).newInstance(typeString.split(":")[1])
                    : type.getManager().getConstructor().newInstance();
            storage.initializeStorage();
        } catch (Exception e) {
            log.error("An error occured: ", e);
        }
    }

    @Override
    public void onDisable() {
        unload();
    }

    public void reload() {
        unload();

        this.loadConfiguration();
    }

    private void unload() {
        wheelManager.despawn();

        TestSubCommand.getManagers().forEach(WheelManager::despawn);
        TestSubCommand.getManagers().clear();

        scripts.forEach(ScriptData::unload);
        scripts.clear();
    }

    private void loadConfiguration() {
        final File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            IConfiguration.createDefaultFile(getResource("config.yml"), configFile);
        }
        configuration = IConfiguration.loadYamlConfiguration(configFile);

        final File dataFile = new File(getDataFolder(), "data.yml");
        if (!configFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dataConfig = IConfiguration.loadYamlConfiguration(dataFile);

        this.loadScripts();
        this.loadWheel();
    }

    private void loadWheel() {
        if (!dataConfig.exists("locations.wheel")) {
            return;
        }

        final boolean hologram = configuration.getString("wheel.type").equalsIgnoreCase("hologram");
        final Location location = dataConfig.spigot().getLocation("locations.wheel");

        if (hologram) {
            this.wheelManager = new HologramManager(location);
        } else {
            this.wheelManager = new BlockManager(location);
        }

        this.wheelManager.spawnStandard();
    }

    private void loadScripts() {
        final File scriptsFolder = new File(getDataFolder(), "events");

        if (!scriptsFolder.exists()) {
            scriptsFolder.mkdir();

            IConfiguration.createDefaultFile(
                    getResource("events/changediff.js"), new File(scriptsFolder, "changediff.js")
            );
        }

        configuration.getSectionList("events").forEach(section -> {
            final String name = section.getString("name");
            final String scriptName = section.getString("script");
            final CircleColor color = CircleColor.valueOf(section.getString("color"));
            final File file = new File(scriptsFolder, scriptName);

            try {
                final String code = new String(Files.readAllBytes(file.toPath()));
                final Script script = new Script(file.getName(), code);

                this.scripts.add(new ScriptData(
                        name,
                        script,
                        color
                ));
            } catch (IOException | ScriptException e) {
                log.error("Could not load script " + file.getName(), e);
            }
        });
    }

    private void loadColorEvents() {
        if (scripts.isEmpty()) {
            return;
        }
        final Random random = new Random();
        scripts.forEach(script -> colorEvents.put(script.getColor(), script));

        for (CircleColor color : CircleColor.values()) {
            if (!colorEvents.containsKey(color)) {
                colorEvents.put(color, scripts.get(random.nextInt(scripts.size())));
            }
        }
    }
}
