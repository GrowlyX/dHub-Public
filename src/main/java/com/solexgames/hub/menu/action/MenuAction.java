package com.solexgames.hub.menu.action;

import com.solexgames.core.CorePlugin;
import com.solexgames.core.player.PotPlayer;
import com.solexgames.core.util.BungeeUtil;
import com.solexgames.hub.HubPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MenuAction {

    public static void completeAction(Type type, String value, Player player) {
        player.closeInventory();

        final PotPlayer potPlayer = CorePlugin.getInstance().getPlayerManager().getPlayer(player);

        switch (type) {
            case MENU:
                HubPlugin.getPlugin(HubPlugin.class).getSubMenuHandler().openSubMenu(value, player);
                break;
            case SERVER_SEND:
                if (potPlayer.isCurrentlyRestricted()) {
                    player.sendMessage(ChatColor.RED + "You cannot join servers right now since you are restricted.");
                    return;
                }

                BungeeUtil.sendToServer(player, value, HubPlugin.getPlugin(HubPlugin.class));
                break;
            case JOIN_QUEUE:
                if (potPlayer.isCurrentlyRestricted()) {
                    player.sendMessage(ChatColor.RED + "You cannot join queues right now since you are restricted.");
                    return;
                }

                HubPlugin.getPlugin(HubPlugin.class).getQueueImpl().joinQueue(player, value);
                break;
        }
    }

    public enum Type {
        SERVER_SEND,
        JOIN_QUEUE,
        MENU
    }
}
