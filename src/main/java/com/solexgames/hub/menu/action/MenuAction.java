package com.solexgames.hub.menu.action;

import com.solexgames.core.CorePlugin;
import com.solexgames.core.player.PotPlayer;
import com.solexgames.core.util.BungeeUtil;
import com.solexgames.core.util.Color;
import com.solexgames.hub.HubPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MenuAction {

    public static void completeAction(Type type, String value, Player player, HubPlugin plugin) {
        final PotPlayer potPlayer = CorePlugin.getInstance().getPlayerManager().getPlayer(player);

        player.closeInventory();

        switch (type) {
            case MESSAGE:
                player.sendMessage(Color.translate(value));
                break;
            case MENU:
                plugin.getSubMenuHandler().openSubMenu(value, player);
                break;
            case SERVER_SEND:
                if (potPlayer.isCurrentlyRestricted()) {
                    player.sendMessage(ChatColor.RED + "You cannot join servers right now since you are restricted.");
                    return;
                }

                BungeeUtil.sendToServer(player, value, plugin);
                break;
            case JOIN_QUEUE:
                if (potPlayer.isCurrentlyRestricted()) {
                    player.sendMessage(ChatColor.RED + "You cannot join queues right now since you are restricted.");
                    return;
                }

                plugin.getQueueImpl().joinQueue(player, value);
                break;
        }
    }

    public enum Type {

        SERVER_SEND,
        JOIN_QUEUE,
        MESSAGE,
        MENU

    }
}
