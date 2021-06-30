package com.solexgames.hub.task;

import me.lucko.helper.Services;
import me.lucko.helper.messaging.bungee.BungeeCord;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author GrowlyX
 * @since 5/18/2021
 */

public class GlobalStatusUpdateTask extends BukkitRunnable {

    public static int GLOBAL_PLAYERS = -1;

    @Override
    public void run() {
        Services.get(BungeeCord.class).ifPresent(bungeeCord -> {
            try {
                GLOBAL_PLAYERS = bungeeCord.playerCount(BungeeCord.ALL_SERVERS).get();
            } catch (Exception ignored) { }
        });
    }
}
