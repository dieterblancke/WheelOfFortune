/*
 * Copyright (C) 2018 DBSoftwares - Dieter Blancke
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.dbsoftwares.dangerwheel.script;

import com.dbsoftwares.configuration.api.IConfiguration;
import com.dbsoftwares.dangerwheel.DangerWheel;
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
        cacheFolder = new File(DangerWheel.getInstance().getDataFolder(), "events" + File.separator + "cache");

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
        engine.put("plugin", DangerWheel.getInstance());
        engine.put("server", Bukkit.getServer());

        engine.eval("function isConsole() { return user === null || user.getClass().getSimpleName() !== 'BUser'; }");

        return engine;
    }

    public String execute() {
        final String script = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")
                ? me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(null, this.script)
                : this.script;

        try {
            return String.valueOf(engine.eval(script));
        } catch (ScriptException e) {
            DangerWheel.getLog().error("An error occured: ", e);
            return "SCRIPT ERROR";
        }
    }

    public void unload() {
        final File storage = new File(cacheFolder, hash(file));

        if (storage.length() == 0) {
            try {
                Files.delete(storage.toPath());
            } catch (IOException e) {
                DangerWheel.getLog().error("Could not remove empty script storage.", e);
            }
        }
    }
}
