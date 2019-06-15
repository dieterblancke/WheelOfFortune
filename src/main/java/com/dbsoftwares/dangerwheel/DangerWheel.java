package com.dbsoftwares.dangerwheel;

import com.dbsoftwares.commandapi.CommandManager;
import com.dbsoftwares.configuration.api.IConfiguration;
import com.dbsoftwares.dangerwheel.commands.DangerWheelCommand;
import com.dbsoftwares.dangerwheel.script.Script;
import com.dbsoftwares.dangerwheel.script.ScriptData;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class DangerWheel extends JavaPlugin {

    @Getter
    private static Logger log = LoggerFactory.getLogger("DangerWheel");

    @Getter
    private static DangerWheel instance;

    @Getter
    private IConfiguration configuration;

    @Getter
    private List<ScriptData> scripts = Lists.newArrayList();

    @Override
    public void onEnable() {
        instance = this;

        final File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            IConfiguration.createDefaultFile(getResource("config.yml"), configFile);
        }
        configuration = IConfiguration.loadYamlConfiguration(configFile);

        loadScripts();

        CommandManager.getInstance().registerCommand(new DangerWheelCommand());
    }

    @Override
    public void onDisable() {

    }

    private void loadScripts() {
        scripts.forEach(ScriptData::unload);
        scripts.clear();
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
            final File file = new File(scriptsFolder, scriptName);

            try {
                final String code = new String(Files.readAllBytes(file.toPath()));
                final Script script = new Script(file.getName(), code);

                this.scripts.add(new ScriptData(
                        name,
                        null,
                        script
                ));
            } catch (IOException | ScriptException e) {
                log.error("Could not load script " + file.getName(), e);
            }
        });
    }
}
