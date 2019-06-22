package com.dbsoftwares.dangerwheel.utils.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WheelRunData {

    private int maxRuns;
    private int defaultDuration;
    private List<WheelRun> runs;

}
