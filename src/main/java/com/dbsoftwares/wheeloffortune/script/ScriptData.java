package com.dbsoftwares.wheeloffortune.script;

import com.dbsoftwares.wheeloffortune.utils.objects.CircleColor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScriptData {

    private String name;
    private Script script;
    private CircleColor color;

    public void unload() {
        script.unload();
    }
}
