package com.solexgames.hub.listener;

import com.solexgames.core.CorePlugin;
import com.solexgames.core.player.PotPlayer;
import com.solexgames.core.player.ranks.Rank;
import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.handler.HubHandler;
import com.solexgames.hub.menu.HubSelectorMenu;
import com.solexgames.hub.menu.ServerSelectorMenu;
import com.solexgames.hub.menu.captcha.CaptchaMenu;
import com.solexgames.hub.menu.cosmetic.CosmeticMainMenu;
import com.solexgames.hub.scoreboard.ScoreboardAdapter;
import com.solexgames.hub.util.ItemUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;

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

        player.setWalkSpeed(0.3F);
        player.setAllowFlight(hubHandler.isDoubleJumpEnabled());
        player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());

        if (hubHandler.isEnderButtEnabled()) {
            player.getInventory().setItem(this.plugin.getItemCache().get("enderbutt").getKey(), this.plugin.getItemCache().get("enderbutt").getValue());
        }

        player.getInventory().setItem(this.plugin.getItemCache().get("server-selector").getKey(), this.plugin.getItemCache().get("server-selector").getValue());
        player.getInventory().setItem(this.plugin.getItemCache().get("hub-selector").getKey(), this.plugin.getItemCache().get("hub-selector").getValue());
        player.getInventory().setItem(this.plugin.getItemCache().get("cosmetics").getKey(), this.plugin.getItemCache().get("cosmetics").getValue());

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
        final Player player = event.getPlayer();

        if (event.getAction().name().contains("RIGHT") && event.getItem() != null && event.getItem().hasItemMeta()) {
            if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(this.plugin.getItemCache().get("server-selector").getValue().getItemMeta().getDisplayName())) {
                new ServerSelectorMenu(player).open(player);
            }
            if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(this.plugin.getItemCache().get("hub-selector").getValue().getItemMeta().getDisplayName())) {
                new HubSelectorMenu(player, this.plugin).open(player);
            }
            if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(this.plugin.getItemCache().get("cosmetics").getValue().getItemMeta().getDisplayName())) {
                new CosmeticMainMenu(this.plugin).openMenu(player);
            }
        }
    }
}
