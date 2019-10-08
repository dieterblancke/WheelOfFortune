package com.dbsoftwares.wheeloffortune.script;

import com.dbsoftwares.configuration.api.IConfiguration;
import com.dbsoftwares.wheeloffortune.WheelOfFortune;
import com.dbsoftwares.wheeloffortune.api.DWApi;
import com.google.common.hash.Hashing;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Data
public class Script {

    private static final File cacheFolder;

    static {
        cacheFolder = new File(WheelOfFortune.getInstance().getDataFolder(), "events" + File.separator + "cache");

        if (!cacheFolder.exists()) {
            cacheFolder.mkdirs();
        }
    }

    private final String file;
    private final String script;
    private IConfiguration storage;
    private ScriptEngine engine;

    public Script(String file, String script) throws ScriptException, IOException {
        this.file = file;
        this.script = script;

        final File storage = new File(cacheFolder, hash(file));

        if (!storage.exists() && !storage.createNewFile()) {
            return;
        }

        this.storage = IConfiguration.loadYamlConfiguration(storage);
        this.engine = loadEngine();
    }

    private static String hash(String str) {
        return Hashing.sha256().hashString(str, StandardCharsets.UTF_8).toString();
    }

    private ScriptEngine loadEngine() throws ScriptException {
        final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

        engine.put("storage", storage);
        engine.put("plugin", WheelOfFortune.getInstance());
        engine.put("server", Bukkit.getServer());
        engine.put("api", DWApi.getInstance());

        engine.eval("function isConsole() { return user === null || user.getClass().getSimpleName() !== 'BUser'; }");

        return engine;
    }

    public String execute(final Player executor) {
        final String script = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")
                ? me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(null, this.script)
                : this.script;

        engine.put("executor", executor);

        try {
            return String.valueOf(engine.eval(script));
        } catch (ScriptException e) {
            WheelOfFortune.getLog().error("An error occured: ", e);
            return "SCRIPT ERROR";
        }
    }

    public void unload() {
        final File storage = new File(cacheFolder, hash(file));

        if (storage.length() == 0) {
            try {
                Files.delete(storage.toPath());
            } catch (IOException e) {
                WheelOfFortune.getLog().error("Could not remove empty script storage.", e);
            }
        }
    }
}
