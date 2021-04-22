package com.solexgames.hub.listener;

import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.manager.HubManager;
import com.solexgames.hub.menu.HubSelectorMenu;
import com.solexgames.hub.menu.ServerSelectorMenu;
import com.solexgames.hub.scoreboard.ScoreboardAdapter;
import com.solexgames.hub.util.ItemUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final HubPlugin plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final HubManager hubManager = this.plugin.getHubManager();

        event.setJoinMessage(null);

        player.getInventory().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.ADVENTURE);

        if (hubManager.isScoreboardEnabled()) {
            new ScoreboardAdapter(player, this.plugin);
        }

        if (hubManager.isHubSpeedEnabled()) {
            player.setWalkSpeed(0.5F);
        }

        player.setAllowFlight(hubManager.isDoubleJumpEnabled());

        final Location location = Bukkit.getWorlds().get(0).getSpawnLocation();

        location.setPitch(0);
        location.setYaw(0);

        player.teleport(location);

        if (hubManager.isEnderButtEnabled()) {
            player.getInventory().setItem(ItemUtil.getInventoryItemFromConfig("items.enderbutt", this.plugin).getKey(), ItemUtil.getInventoryItemFromConfig("items.enderbutt", this.plugin).getValue());
        }

        player.getInventory().setItem(ItemUtil.getInventoryItemFromConfig("items.server-selector", this.plugin).getKey(), ItemUtil.getInventoryItemFromConfig("items.server-selector", this.plugin).getValue());
        player.getInventory().setItem(ItemUtil.getInventoryItemFromConfig("items.hub-selector", this.plugin).getKey(), ItemUtil.getInventoryItemFromConfig("items.hub-selector", this.plugin).getValue());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        final Player player = event.getPlayer();
        final HubManager hubManager = this.plugin.getHubManager();

        if (!this.plugin.getPermittedBuilders().contains(player)) {
            if (player.getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);

                player.setVelocity(player.getLocation().getDirection().multiply(hubManager.getDoubleJumpMultiply()).setY(1.1D));

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
                if ((event.getAction().name().contains("RIGHT"))) {
                    if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemUtil.getInventoryItemFromConfig("items.server-selector", this.plugin).getValue().getItemMeta().getDisplayName())) {
                        new ServerSelectorMenu(event.getPlayer()).open(event.getPlayer());
                    }
                    if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ItemUtil.getInventoryItemFromConfig("items.hub-selector", this.plugin).getValue().getItemMeta().getDisplayName())) {
                        new HubSelectorMenu(event.getPlayer()).open(event.getPlayer());
                    }
                }
            }
        }
    }
}
