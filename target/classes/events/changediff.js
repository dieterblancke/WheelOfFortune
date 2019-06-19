var oldWorlds = {};

function execute() {
    updateDifficulties();

    var BukkitRunnable = Java.type('org.bukkit.scheduler.BukkitRunnable');
    var runnable = new BukkitRunnable(function () {
        var worldNames = Object.keys(oldWorlds);
        var worldNamesSize = worldNames.length;

        for (var i = 0; i < worldNamesSize; i++) {
            var worldName = worldNames[i];
            var world = server.getWorld(worldName);

            world.setDifficulty(oldWorlds[worldName]);
            server.broadcastMessage("Updating difficulty of " + world.getName() + " to " + oldWorlds[worldName] + "!");
        }
    });

    // 36000 = 30 * 60 * 20 (to get ticks in 30 min)
    runnable.runTaskLater(plugin, 100);
}

function updateDifficulties() {
    var worldsSize = server.getWorlds().size();

    for (var i = 0; i < worldsSize; i++) {
        var world = server.getWorlds().get(i);
        oldWorlds[world.getName()] = world.getDifficulty();

        var difficulty = Java.type("org.bukkit.Difficulty");
        world.setDifficulty(difficulty.HARD);

        server.broadcastMessage("Updating difficulty of " + world.getName() + " to " + difficulty.HARD + "!");
    }
}

execute();