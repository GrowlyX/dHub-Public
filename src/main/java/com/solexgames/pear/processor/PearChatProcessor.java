package com.solexgames.pear.processor;

import com.solexgames.core.chat.IChatCheck;
import com.solexgames.core.player.PotPlayer;
import com.solexgames.pear.PearSpigotPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author GrowlyX
 * @since 6/30/2021
 */

@RequiredArgsConstructor
public class PearChatProcessor implements IChatCheck {

    private final PearSpigotPlugin plugin;

    @Override
    public void check(AsyncPlayerChatEvent asyncPlayerChatEvent, PotPlayer potPlayer) {
        if (!this.plugin.getSettingsProcessor().isChatEnabled()) {
            if (!potPlayer.getPlayer().hasPermission("scandium.staff")) {
                potPlayer.getPlayer().sendMessage(ChatColor.RED + "You're not allowed to chat in the hub.");
                asyncPlayerChatEvent.setCancelled(true);
            }
        }
    }
}
