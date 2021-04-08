package com.solexgames.listener;

import com.solexgames.HubPlugin;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class EnderbuttListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if ((event.hasItem() && event.getAction().name().contains("RIGHT") && event.getItem().hasItemMeta())) {
            if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(HubPlugin.getInstance().getEnderButt())) {
                player.setVelocity(event.getPlayer().getLocation().getDirection().multiply(2.5F));
                player.playSound(event.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);

                event.setCancelled(true);
                event.setUseItemInHand(Event.Result.DENY);

                player.updateInventory();
            }
        }
    }
}
