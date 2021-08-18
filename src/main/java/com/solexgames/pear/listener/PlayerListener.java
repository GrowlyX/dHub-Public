package com.solexgames.pear.listener;

import com.solexgames.core.menu.impl.player.PlayerInfoMenu;
import com.solexgames.core.util.Color;
import com.solexgames.pear.PearSpigotPlugin;
import com.solexgames.pear.cosmetic.impl.ArmorCosmetic;
import com.solexgames.pear.menu.HubSelectorMenu;
import com.solexgames.pear.menu.cosmetic.CosmeticMainMenu;
import com.solexgames.pear.module.HubModule;
import io.papermc.lib.PaperLib;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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

    private final PearSpigotPlugin plugin;

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
            PaperLib.teleportAsync(player, this.plugin.getSettingsProcessor().getSpawnLocation());
        } else {
            if (player.isOp()) {
                player.sendMessage(Color.MAIN_COLOR + "[Neon] " + Color.SECONDARY_COLOR + "Hey! You should set the spawn location via /neon setup.");
            }
        }

//        if (this.plugin.getItemCache().get("enderbutt") != null) {
//            player.getInventory().setItem(this.plugin.getItemCache().get("enderbutt").getKey(), this.plugin.getItemCache().get("enderbutt").getValue());
//        }

        final int serverSelectorSlot = this.plugin.getItemCache().get("server-selector").getKey();
        player.getInventory().setItem(serverSelectorSlot, this.plugin.getItemCache().get("server-selector").getValue());

        player.getInventory().setItem(this.plugin.getItemCache().get("hub-selector").getKey(), this.plugin.getItemCache().get("hub-selector").getValue());
        player.getInventory().setItem(this.plugin.getItemCache().get("cosmetics").getKey(), this.plugin.getItemCache().get("cosmetics").getValue());

//        if (this.plugin.getItemCache().get("profile") != null) {
//            player.getInventory().setItem(this.plugin.getItemCache().get("profile").getKey(), new ItemBuilder(this.plugin.getItemCache().get("profile").getValue())
//                    .setOwner(player.getName())
//                    .create());
//        }

        final HubModule hubModule = this.plugin.getHubModule();

        if (hubModule != null && hubModule.getItemAdapter() != null) {
            hubModule.getItemAdapter().handle(player);
        }

        player.getInventory().setHeldItemSlot(serverSelectorSlot > 8 ? 0 : serverSelectorSlot);

        player.updateInventory();

        if (this.plugin.getSettingsProcessor().isHidePlayers()) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer != player && !(onlinePlayer.hasPermission("pear.hub.show") || onlinePlayer.hasPermission("scandium.staff"))) {
                    player.hidePlayer(onlinePlayer);
                }
            }
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
                this.plugin.getSubMenuHandler().openSubMenu("server-selector", player);
            } else if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(this.plugin.getItemCache().get("hub-selector").getValue().getItemMeta().getDisplayName())) {
                new HubSelectorMenu(this.plugin).openMenu(player);
            } else if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(this.plugin.getItemCache().get("profile").getValue().getItemMeta().getDisplayName())) {
                new PlayerInfoMenu(player).open(player);
            } else if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(this.plugin.getItemCache().get("cosmetics").getValue().getItemMeta().getDisplayName())) {
                new CosmeticMainMenu(this.plugin).openMenu(player);
            }
        }
    }
}
