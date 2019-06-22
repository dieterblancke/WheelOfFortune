package com.dbsoftwares.dangerwheel.utils.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WheelRun {

    private int first;
    private int last;
    private int tickDuration;

}