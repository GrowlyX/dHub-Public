package com.solexgames.hub.listener;

import com.solexgames.core.menu.impl.player.PlayerInfoMenu;
import com.solexgames.core.util.Color;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.cosmetic.impl.ArmorCosmetic;
import com.solexgames.hub.menu.HubSelectorMenu;
import com.solexgames.hub.menu.ServerSelectorMenu;
import com.solexgames.hub.menu.captcha.CaptchaMenu;
import com.solexgames.hub.menu.cosmetic.CosmeticMainMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final HubPlugin plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        event.setJoinMessage(null);

        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);

        player.setHealth(20);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.ADVENTURE);

        player.setWalkSpeed(0.4F);
        player.setAllowFlight(this.plugin.getSettingsProcessor().isDoubleJumpEnabled());

        if (this.plugin.getSettingsProcessor().getSpawnLocation() != null) {
            player.teleport(this.plugin.getSettingsProcessor().getSpawnLocation().toLocation());
        } else {
            if (player.isOp()) {
                player.sendMessage(Color.MAIN_COLOR + "[Neon] " + Color.SECONDARY_COLOR + "Hey! You should set the spawn location via /neon setup.");
            }
        }

        if (this.plugin.getItemCache().get("enderbutt") != null) {
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

        if (this.plugin.getSettingsProcessor().isCaptchaEnabled()) {
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> new CaptchaMenu(player, Material.BLAZE_POWDER, this.plugin).open(player), 5L);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        final BukkitRunnable bukkitRunnable = this.plugin.getCosmeticHandler().getRunnableHashMap().get(event.getPlayer());

        if (bukkitRunnable != null) {
            bukkitRunnable.cancel();

            this.plugin.getCosmeticHandler().getRunnableHashMap().remove(event.getPlayer());
        }

        final ArmorCosmetic.ArmorUpdaterRunnable updaterRunnable = ArmorCosmetic.ARMOR_UPDATER_RUNNABLE_MAP.get(event.getPlayer().getUniqueId());

        if (updaterRunnable != null) {
            event.getPlayer().getInventory().clear();

            updaterRunnable.cancel();
            ArmorCosmetic.ARMOR_UPDATER_RUNNABLE_MAP.remove(event.getPlayer().getUniqueId());
        }

        event.setQuitMessage(null);
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        final Player player = event.getPlayer();

        if (!this.plugin.getPermittedBuilders().contains(player)) {
            if (player.getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);

                final Vector finalLoc = player.getLocation().getDirection().multiply(2.5D).setY(1.1D);

                player.setVelocity(finalLoc);

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
