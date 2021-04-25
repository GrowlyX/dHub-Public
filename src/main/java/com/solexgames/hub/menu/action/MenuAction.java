package com.solexgames.hub.menu.action;

import com.solexgames.core.util.BungeeUtil;
import com.solexgames.hub.HubPlugin;
import org.bukkit.entity.Player;

public class MenuAction {

    public static void completeAction(Type type, String value, Player player) {
        player.closeInventory();

        switch (type) {
            case MENU:
                HubPlugin.getPlugin(HubPlugin.class).getSubMenuHandler().openSubMenu(value, player);
                break;
            case SERVER_SEND:
                BungeeUtil.sendToServer(player, value, HubPlugin.getPlugin(HubPlugin.class));
                break;
            case JOIN_QUEUE:
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
