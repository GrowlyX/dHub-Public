package com.solexgames.pear.tab;

import com.solexgames.core.CorePlugin;
import com.solexgames.core.player.PotPlayer;
import io.github.nosequel.tab.shared.entry.TabElement;
import io.github.nosequel.tab.shared.entry.TabElementHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 6/30/2021
 */

public class PearTabProcessor implements TabElementHandler {

    @Override
    public TabElement getElement(Player player) {
        final TabElement element = new TabElement();
        final PotPlayer potPlayer = CorePlugin.getInstance().getPlayerManager().getPlayer(player);

        element.add(0, 3, ChatColor.GOLD + ChatColor.BOLD.toString() + "Store");
        element.add(0, 4, ChatColor.GRAY + "store.pvp.bar");
        element.add(1, 0, ChatColor.GOLD + ChatColor.BOLD.toString() + "PvPBar Network");

        element.add(1, 3, ChatColor.GOLD + ChatColor.BOLD.toString() + "Player Info");
        element.add(1, 4, ChatColor.GRAY + "Rank: " + potPlayer.getActiveGrant().getRank().getColor() + potPlayer.getActiveGrant().getRank().getName());
        element.add(1, 5, ChatColor.GRAY + "Experience: " + ChatColor.WHITE + potPlayer.getExperience());

        element.add(2, 3, ChatColor.GOLD + ChatColor.BOLD.toString() + "Discord");
        element.add(2, 4, ChatColor.GRAY + "discord.pvp.bar");

        return element;
    }
}
