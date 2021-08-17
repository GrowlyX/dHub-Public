package com.solexgames.pear.task;

import com.solexgames.lib.commons.redis.JedisManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

/**
 * @author GrowlyX
 * @since 5/18/2021
 */

@RequiredArgsConstructor
public class GlobalStatusUpdateTask extends BukkitRunnable {

    public static final String JEDIS_KEY_NETWORK_PLAYERS = "xenon_network_dataupdates";
    public static final String JEDIS_KEY_NETWORK_PROXY_HEARTBEATS = "heartbeats";

    public static int GLOBAL_PLAYERS = -1;
    public static int GLOBAL_SERVER_COUNT = -1;
    public static int GLOBAL_PROXY_COUNT = -1;

    private final JedisManager jedisManager;

    @Override
    public void run() {
        this.jedisManager.runCommand(jedis -> {
            final Map<String, String> allKeysMap = jedis.hgetAll(JEDIS_KEY_NETWORK_PLAYERS);
            final Map<String, String> proxyHeartbeatMap = jedis.hgetAll(JEDIS_KEY_NETWORK_PROXY_HEARTBEATS);

            final String stringVersion = allKeysMap.get("global");

            GLOBAL_PLAYERS = Integer.parseInt(stringVersion);
            GLOBAL_SERVER_COUNT = allKeysMap.size();
            GLOBAL_PROXY_COUNT = proxyHeartbeatMap.size();
        });
    }
}
