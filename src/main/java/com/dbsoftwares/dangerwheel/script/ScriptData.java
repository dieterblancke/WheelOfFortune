package com.dbsoftwares.dangerwheel.script;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

@Data
@AllArgsConstructor
public class ScriptData {

    private String name;
    private ItemStack item;
    private Script script;

    public void unload() {
        script.unload();
    }
}
