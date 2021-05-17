package com.solexgames.hub.tablist;

import com.solexgames.core.CorePlugin;
import com.solexgames.core.player.PotPlayer;
import io.github.nosequel.tab.shared.entry.TabElement;
import io.github.nosequel.tab.shared.entry.TabElementHandler;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TablistAdapter implements TabElementHandler {

    @Override
    public TabElement getElement(Player player) {
        final TabElement element = new TabElement();
        final PotPlayer potPlayer = CorePlugin.getInstance().getPlayerManager().getPlayer(player);

        // dont use unless you want lag x50k

        // Social Media
        element.add(0, 3, ChatColor.GOLD + ChatColor.BOLD.toString() + "Store");
        element.add(0, 4, ChatColor.GRAY + "store.pvp.bar");

        element.add(1, 0, ChatColor.GOLD + ChatColor.BOLD.toString() + "PvPBar Network");

        element.add(1, 3, ChatColor.GOLD + ChatColor.BOLD.toString() + "Player Info");
        element.add(1, 4, ChatColor.GRAY + "Rank: " + potPlayer.getActiveGrant().getRank().getColor() + potPlayer.getActiveGrant().getRank().getName());

        element.add(2, 3, ChatColor.GOLD + ChatColor.BOLD.toString() + "Discord");
        element.add(2, 4, ChatColor.GRAY + "pvp.bar/discord");

        // Server Statuses - temporary online & offline stuff
        element.add(0, 10, ChatColor.GOLD + ChatColor.BOLD.toString() + "NA Practice");
        element.add(0, 11, ChatColor.GREEN + "Online");
        element.add(0, 12, ChatColor.GRAY + PlaceholderAPI.setPlaceholders(player, "(%bungee_na-practice%/350)"));

        element.add(0, 14, ChatColor.GOLD + ChatColor.BOLD.toString() + "EU Practice");
        element.add(0, 15, ChatColor.RED + "Offline");
        element.add(0, 16, ChatColor.GRAY + PlaceholderAPI.setPlaceholders(player, "(%bungee_eu-practice%/350)"));

        element.add(2, 10, ChatColor.GOLD + ChatColor.BOLD.toString() + "UHC Meetup");
        element.add(2, 11, ChatColor.YELLOW + "Whitelisted");
        element.add(2, 12, ChatColor.GRAY + PlaceholderAPI.setPlaceholders(player, "(%bungee_uhcm-1%/125)"));

        element.add(2, 14, ChatColor.GOLD + ChatColor.BOLD.toString() + "UHC");
        element.add(2, 15, ChatColor.RED + "Offline");
        element.add(2, 16, ChatColor.GRAY + PlaceholderAPI.setPlaceholders(player, "(%bungee_uhc-1%/300)"));

        return element;
    }
}
