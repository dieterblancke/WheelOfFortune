package com.dbsoftwares.dangerwheel.listeners;

import com.dbsoftwares.dangerwheel.DangerWheel;
import com.dbsoftwares.dangerwheel.utils.Utils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class PlayerListener implements Listener {

    private final LoadingCache<UUID, CompletableFuture<Boolean>> loadingCache = CacheBuilder.newBuilder()
            .expireAfterWrite(15, TimeUnit.SECONDS)
            .build(new CacheLoader<UUID, CompletableFuture<Boolean>>() {
                @ParametersAreNonnullByDefault
                public CompletableFuture<Boolean> load(final UUID uuid) {
                    return CompletableFuture.supplyAsync(() -> DangerWheel.getInstance().getStorage().isOptedIn(uuid));
                }
            });

    @EventHandler
    public void onLoad(final PlayerLoginEvent event) {
        final Player player = event.getPlayer();

        final CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> DangerWheel.getInstance().getStorage().isOptedIn(player.getUniqueId()));
        loadingCache.put(player.getUniqueId(), future);
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player p = event.getPlayer();

        final boolean optedIn = getOptedStatus(p.getUniqueId());
        Utils.setMetaData(p, Utils.OPTED_IN_KEY, optedIn);
    }

    private boolean getOptedStatus(final UUID uuid) {
        try {
            final CompletableFuture<Boolean> future = loadingCache.get(uuid);

            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            return DangerWheel.getInstance().getStorage().isOptedIn(uuid);
        }
    }
}
