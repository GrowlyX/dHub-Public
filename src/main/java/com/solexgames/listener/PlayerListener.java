package com.solexgames.listener;

import com.solexgames.HubPlugin;
import com.solexgames.manager.HubManager;
import com.solexgames.menu.HubSelectorMenu;
import com.solexgames.menu.ServerSelectorMenu;
import com.solexgames.scoreboard.ScoreboardAdapter;
import com.solexgames.util.ItemUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        HubManager hubManager = HubPlugin.getInstance().getHubManager();

        player.getInventory().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.ADVENTURE);

        if (hubManager.isHubSpeedEnabled()) {
            player.setWalkSpeed(0.5F);
        }

        player.setAllowFlight(hubManager.isDoubleJumpEnabled());

        if (hubManager.isHubLocationSet()) {
            player.teleport(hubManager.getHubLocation());
        }

        if (hubManager.isEnderButtEnabled()) {
            player.getInventory().setItem(ItemUtil.getInventoryItemFromConfig("items.enderbutt").getKey(), ItemUtil.getInventoryItemFromConfig("items.enderbutt").getValue());
        }

        player.getInventory().setItem(ItemUtil.getInventoryItemFromConfig("items.server-selector").getKey(), ItemUtil.getInventoryItemFromConfig("items.server-selector").getValue());
        player.getInventory().setItem(ItemUtil.getInventoryItemFromConfig("items.hub-selector").getKey(), ItemUtil.getInventoryItemFromConfig("items.hub-selector").getValue());

        new ScoreboardAdapter(player);
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        HubManager hubManager = HubPlugin.getInstance().getHubManager();

        if (!HubPlugin.getInstance().getPermittedBuilders().contains(player.getName())) {
            if (player.getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);

                player.setVelocity(player.getLocation().getDirection().multiply(hubManager.getDoubleJumpMultiply()).setY(0.8D));

                if (hubManager.isDoubleJumpSoundEnabled()) {
                    player.playSound(player.getLocation(), hubManager.getDoubleJumpSound(), 1F, 1F);
                }
                if (hubManager.isDoubleJumpEffectEnabled()) {
                    player.spigot().playEffect(player.getLocation(), hubManager.getDoubleJumpEffect(), 1, 1, 1.0f, 1.0f, 1.0f, 1.0f, 1, 1);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() != null) {
            if (event.getItem().hasItemMeta()) {
                if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                    if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemUtil.getInventoryItemFromConfig("items.server-selector").getValue().getItemMeta().getDisplayName())) {
                        new ServerSelectorMenu(event.getPlayer()).open(event.getPlayer());
                    }
                    if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemUtil.getInventoryItemFromConfig("items.hub-selector").getValue().getItemMeta().getDisplayName())) {
                        new HubSelectorMenu(event.getPlayer()).open(event.getPlayer());
                    }
                }
            }
        }
    }
}
