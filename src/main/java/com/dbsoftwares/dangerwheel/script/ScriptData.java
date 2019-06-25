package com.dbsoftwares.dangerwheel.script;

import com.dbsoftwares.dangerwheel.utils.objects.CircleColor;
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
