package com.dbsoftwares.dangerwheel;

import com.dbsoftwares.commandapi.CommandManager;
import com.dbsoftwares.configuration.api.IConfiguration;
import com.dbsoftwares.dangerwheel.commands.DangerWheelCommand;
import com.dbsoftwares.dangerwheel.script.Script;
import com.dbsoftwares.dangerwheel.script.ScriptData;
import com.dbsoftwares.dangerwheel.utils.objects.CircleColor;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.bukkit.Bukkit;
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

public class DangerWheel extends JavaPlugin {

    @Getter
    private static Logger log = LoggerFactory.getLogger("DangerWheel");

    @Getter
    private static DangerWheel instance;

    @Getter
    private IConfiguration configuration;

    @Getter
    private List<ScriptData> scripts = Lists.newArrayList();

    @Getter
    private Map<CircleColor, ScriptData> colorEvents = Maps.newHashMap();

    @Override
    public void onEnable() {
        instance = this;

        this.loadConfiguration();
        this.loadColorEvents();

        CommandManager.getInstance().registerCommand(new DangerWheelCommand());
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
        Bukkit.getScheduler().cancelTasks(this);
        // First adding all holograms in new list to assure no ConcurrentModificationExceptions get raised.
        Lists.newArrayList(HologramsAPI.getHolograms(this)).forEach(Hologram::delete);

        scripts.forEach(ScriptData::unload);
        scripts.clear();
    }

    private void loadConfiguration() {
        final File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            IConfiguration.createDefaultFile(getResource("config.yml"), configFile);
        }
        configuration = IConfiguration.loadYamlConfiguration(configFile);

        this.loadScripts();
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
