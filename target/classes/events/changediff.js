var oldWorlds = {};

function execute() {
    updateDifficulties();

    var Runnable = Java.type('java.lang.Runnable');
    var runnable = Java.extend(Runnable, {
        run: function () {
            var worldNames = Object.keys(oldWorlds);
            var worldNamesSize = worldNames.length;

            for (var i = 0; i < worldNamesSize; i++) {
                var worldName = worldNames[i];
                var world = server.getWorld(worldName);

                world.setDifficulty(oldWorlds[worldName]);
                server.broadcastMessage("Updating difficulty of " + world.getName() + " to " + oldWorlds[worldName] + "!");
            }
        }
    });

    // 36000 = 30 * 60 * 20 (to get ticks in 30 min)
    server.getScheduler().runTaskLater(plugin, runnable, 100);
}

function updateDifficulties() {
    var worldsSize = server.getWorlds().size();

    for (var i = 0; i < worldsSize; i++) {
        var world = server.getWorlds().get(i);
        oldWorlds[world.getName()] = world.getDifficulty();

        var difficulty = Java.type("org.bukkit.Difficulty");
        world.setDifficulty(difficulty.HARD);

        server.broadcastMessage("Updating difficulty of " + world.getName() + " to " + difficulty + "!");
    }
}

execute();