package com.solexgames.hub.listener;

import com.solexgames.core.CorePlugin;
import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.handler.HubHandler;
import com.solexgames.hub.menu.HubSelectorMenu;
import com.solexgames.hub.menu.ServerSelectorMenu;
import com.solexgames.hub.menu.captcha.CaptchaMenu;
import com.solexgames.hub.scoreboard.ScoreboardAdapter;
import com.solexgames.hub.util.ItemUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.Arrays;

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final HubPlugin plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final HubHandler hubHandler = this.plugin.getHubHandler();

        event.setJoinMessage(null);

        player.getInventory().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.ADVENTURE);

        if (hubHandler.isScoreboardEnabled()) {
            new ScoreboardAdapter(player, this.plugin);
        }

        if (hubHandler.isHubSpeedEnabled()) {
            player.setWalkSpeed(0.5F);
        }

        player.setAllowFlight(hubHandler.isDoubleJumpEnabled());

        if (CorePlugin.getInstance().getServerManager().getSpawnLocation() != null) {
            player.teleport(CorePlugin.getInstance().getServerManager().getSpawnLocation());
        }

        if (hubHandler.isEnderButtEnabled()) {
            player.getInventory().setItem(ItemUtil.getInventoryItemFromConfig("items.enderbutt", this.plugin).getKey(), ItemUtil.getInventoryItemFromConfig("items.enderbutt", this.plugin).getValue());
        }

        player.getInventory().setItem(ItemUtil.getInventoryItemFromConfig("items.server-selector", this.plugin).getKey(), ItemUtil.getInventoryItemFromConfig("items.server-selector", this.plugin).getValue());
        player.getInventory().setItem(ItemUtil.getInventoryItemFromConfig("items.hub-selector", this.plugin).getKey(), ItemUtil.getInventoryItemFromConfig("items.hub-selector", this.plugin).getValue());

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> new CaptchaMenu(player, Material.BLAZE_POWDER, this.plugin).open(player), 10L);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        final Player player = event.getPlayer();
        final HubHandler hubHandler = this.plugin.getHubHandler();

        if (!this.plugin.getPermittedBuilders().contains(player)) {
            if (player.getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);

                player.setVelocity(player.getLocation().getDirection().multiply(hubHandler.getDoubleJumpMultiply()).setY(1.1D));

                if (hubHandler.isDoubleJumpSoundEnabled()) {
                    player.playSound(player.getLocation(), hubHandler.getDoubleJumpSound(), 1F, 1F);
                }
                if (hubHandler.isDoubleJumpEffectEnabled()) {
                    player.spigot().playEffect(player.getLocation(), hubHandler.getDoubleJumpEffect(), 1, 1, 1.0f, 1.0f, 1.0f, 1.0f, 1, 1);
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
