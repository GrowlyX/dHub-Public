package com.solexgames.pear.menu.action;

import com.solexgames.core.CorePlugin;
import com.solexgames.core.player.PotPlayer;
import com.solexgames.core.util.BungeeUtil;
import com.solexgames.core.util.Color;
import com.solexgames.core.util.LockedState;
import com.solexgames.pear.PearSpigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MenuAction {

    public static void completeAction(Type type, String value, Player player, PearSpigotPlugin plugin) {
        final PotPlayer potPlayer = CorePlugin.getInstance().getPlayerManager().getPlayer(player);
        player.closeInventory();

        switch (type) {
            case MESSAGE:
                player.sendMessage(Color.translate(value));
                break;
            case MENU:
                plugin.getSubMenuHandler().openSubMenu(value, player);
                break;
            case COMMAND:
                Bukkit.dispatchCommand(player, value);
                break;
            case SERVER_SEND:
                if (LockedState.isLocked(player)) {
                    player.sendMessage(ChatColor.RED + "You cannot join servers as you need to authenticate.");
                    return;
                }

                if (potPlayer.isCurrentlyRestricted()) {
                    player.sendMessage(ChatColor.RED + "You cannot join servers as you are banned.");
                    return;
                }

                if (potPlayer.isCurrentlyBlacklisted()) {
                    player.sendMessage(ChatColor.RED + "You cannot join servers as you are blacklisted.");
                    return;
                }

                BungeeUtil.sendToServer(player, value, plugin);
                break;
            case JOIN_QUEUE:
                if (LockedState.isLocked(player)) {
                    player.sendMessage(ChatColor.RED + "You cannot join servers as you need to authenticate.");
                    return;
                }

                if (potPlayer.isCurrentlyRestricted()) {
                    player.sendMessage(ChatColor.RED + "You cannot join servers as you are banned.");
                    return;
                }

                if (potPlayer.isCurrentlyBlacklisted()) {
                    player.sendMessage(ChatColor.RED + "You cannot join servers as you are blacklisted.");
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
        COMMAND,
        MENU

    }
}
