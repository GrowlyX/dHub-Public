package com.solexgames.hub.processor;

import com.solexgames.core.chat.IChatCheck;
import com.solexgames.core.player.PotPlayer;
import com.solexgames.hub.HubPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author GrowlyX
 * @since 6/30/2021
 */

@RequiredArgsConstructor
public class NeonChatProcessor implements IChatCheck {

    private final HubPlugin plugin;

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
