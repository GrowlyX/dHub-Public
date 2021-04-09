package com.solexgames.hub.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.solexgames.hub.HubPlugin;
import org.bukkit.entity.Player;

public final class BungeeUtil {

    public static void sendToServer(Player player, String server) {
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();

            out.writeUTF("Connect");
            out.writeUTF(server);

            player.sendPluginMessage(HubPlugin.getInstance(), "BungeeCord", out.toByteArray());
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }
}
