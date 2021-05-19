package com.solexgames.hub.task;

import com.solexgames.core.CorePlugin;
import com.solexgames.core.server.NetworkServer;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author GrowlyX
 * @since 5/18/2021
 */

public class GlobalStatusUpdateTask extends BukkitRunnable {

    public static int GLOBAL_PLAYERS = -1;

    @Override
    public void run() {
        GlobalStatusUpdateTask.GLOBAL_PLAYERS = CorePlugin.getInstance().getServerManager().getNetworkServers().stream()
                .mapToInt(NetworkServer::getOnlinePlayers).sum();
    }
}
