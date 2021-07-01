package com.solexgames.hub.processor;

import com.solexgames.core.listener.custom.ServerDeleteEvent;
import com.solexgames.core.listener.custom.ServerRetrieveEvent;
import com.solexgames.core.server.NetworkServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author GrowlyX
 * @since 6/30/2021
 */

public class PlaceholderProcessor implements Listener {

    private final Map<String, String> placeholderMap = new HashMap<>();

    @EventHandler
    public void onServerRetrieve(ServerRetrieveEvent event) {
        final NetworkServer server = event.getServer();


    }

    @EventHandler
    public void onServerRetrieve(ServerDeleteEvent event) {
        final NetworkServer server = event.getServer();


    }
}
