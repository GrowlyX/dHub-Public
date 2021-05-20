package com.solexgames.hub.listener;

import com.solexgames.core.menu.impl.player.PlayerInfoMenu;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.handler.HubHandler;
import com.solexgames.hub.menu.HubSelectorMenu;
import com.solexgames.hub.menu.ServerSelectorMenu;
import com.solexgames.hub.menu.captcha.CaptchaMenu;
import com.solexgames.hub.menu.cosmetic.CosmeticMainMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;

import java.time.LocalDate;

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

        player.setWalkSpeed(0.4F);
        player.setAllowFlight(hubHandler.isDoubleJumpEnabled());
        player.teleport(hubHandler.getSpawn() == null ? Bukkit.getWorlds().get(0).getSpawnLocation().clone() : hubHandler.getSpawn());

        if (hubHandler.isEnderButtEnabled()) {
            player.getInventory().setItem(this.plugin.getItemCache().get("enderbutt").getKey(), this.plugin.getItemCache().get("enderbutt").getValue());
        }

        final int serverSelectorSlot = this.plugin.getItemCache().get("server-selector").getKey();
        player.getInventory().setItem(serverSelectorSlot, this.plugin.getItemCache().get("server-selector").getValue());

        player.getInventory().setItem(this.plugin.getItemCache().get("hub-selector").getKey(), this.plugin.getItemCache().get("hub-selector").getValue());
        player.getInventory().setItem(this.plugin.getItemCache().get("cosmetics").getKey(), this.plugin.getItemCache().get("cosmetics").getValue());
        player.getInventory().setItem(this.plugin.getItemCache().get("profile").getKey(), new ItemBuilder(this.plugin.getItemCache().get("profile").getValue())
                .setOwner(player.getName())
                .create());

        player.getInventory().setHeldItemSlot(serverSelectorSlot > 8 ? 0 : serverSelectorSlot);

        player.updateInventory();

        if (hubHandler.isJoinCaptchaEnabled()) {
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> new CaptchaMenu(player, Material.BLAZE_POWDER, this.plugin).open(player), 10L);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        final BukkitRunnable bukkitRunnable = this.plugin.getCosmeticHandler().getRunnableHashMap().get(event.getPlayer());

        if (bukkitRunnable != null) {
            bukkitRunnable.cancel();

            this.plugin.getCosmeticHandler().getRunnableHashMap().remove(event.getPlayer());
        }

        event.setQuitMessage(null);
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        final Player player = event.getPlayer();
        final HubHandler hubHandler = this.plugin.getHubHandler();

        if (!this.plugin.getPermittedBuilders().contains(player)) {
            if (player.getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);

                final Vector finalLoc = player.getLocation().getDirection().multiply(hubHandler.getDoubleJumpMultiply()).setY(1.1D);

                player.setVelocity(finalLoc);

                if (hubHandler.isDoubleJumpSoundEnabled()) {
                    player.playSound(player.getLocation(), hubHandler.getDoubleJumpSound(), 1F, 1F);
                }

                ParticleEffect.EXPLOSION_LARGE.display(player.getLocation());
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
        final Entity entity = event.getRightClicked();

        if (entity instanceof Player && !entity.hasMetadata("NPC")) {
            final Player player = (Player) entity;

            new PlayerInfoMenu(player).open(event.getPlayer());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        if (event.getAction().name().contains("RIGHT") && event.getItem() != null && event.getItem().hasItemMeta()) {
            if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(this.plugin.getItemCache().get("server-selector").getValue().getItemMeta().getDisplayName())) {
                new ServerSelectorMenu(player, this.plugin).open(player);
            } else if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(this.plugin.getItemCache().get("hub-selector").getValue().getItemMeta().getDisplayName())) {
                new HubSelectorMenu(player, this.plugin).open(player);
            } else if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(this.plugin.getItemCache().get("profile").getValue().getItemMeta().getDisplayName())) {
                new PlayerInfoMenu(player).open(player);
            } else if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(this.plugin.getItemCache().get("cosmetics").getValue().getItemMeta().getDisplayName())) {
                new CosmeticMainMenu(this.plugin).openMenu(player);
            }
        }
    }
}
