package com.solexgames.hub.listener;

import com.cryptomorin.xseries.XSound;
import com.solexgames.hub.HubPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

@RequiredArgsConstructor
public class EnderbuttListener implements Listener {

    private final HubPlugin plugin;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final String display = this.plugin.getItemCache().get("enderbutt").getValue().getItemMeta().getDisplayName();

        if ((event.hasItem() && event.getAction().name().contains("RIGHT") && event.getItem().hasItemMeta())) {
            final String displayName = event.getItem().getItemMeta().getDisplayName();

            if (displayName.equalsIgnoreCase(display)) {
                player.setVelocity(player.getLocation().getDirection().multiply(2.5F));
                player.playSound(player.getLocation(), XSound.ENTITY_EXPERIENCE_ORB_PICKUP.parseSound(), 1.0F, 1.0F);

                event.setCancelled(true);
                event.setUseItemInHand(Event.Result.DENY);

                player.updateInventory();
            }
        }
    }
}
