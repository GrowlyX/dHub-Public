package com.solexgames.pear.menu.cosmetic.selection;

import com.solexgames.core.util.Color;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.core.util.external.Button;
import com.solexgames.core.util.external.pagination.PaginatedMenu;
import com.solexgames.pear.PearSpigotPlugin;
import com.solexgames.pear.player.impl.PersistentPearPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CosmeticParticleSelectionMenu extends PaginatedMenu {

    private final PearSpigotPlugin plugin;

    public CosmeticParticleSelectionMenu(PearSpigotPlugin plugin) {
        super(18);

        this.plugin = plugin;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        final Map<Integer, Button> buttonMap = new HashMap<>();

        buttonMap.put(4, new ItemBuilder(Material.BED)
                .setDisplayName(ChatColor.GREEN + ChatColor.BOLD.toString() + "Reset Cosmetic")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .addLore(
                        "&7Reset your currently applied",
                        "&7cosmetic!",
                        "",
                        "&e[Click to reset cosmetic]"
                )
                .toButton((player1, clickType) -> {
                    final BukkitRunnable bukkitRunnable = this.plugin.getCosmeticHandler().getRunnableHashMap().get(player);

                    if (bukkitRunnable == null) {
                        player.sendMessage(ChatColor.RED + "You don't have a particle cosmetic equipped right now.");
                        return;
                    }

                    bukkitRunnable.cancel();

                    this.plugin.getCosmeticHandler().getRunnableHashMap().remove(player);

                    final PersistentPearPlayer pearPlayer = plugin.getPersistentPlayerCache().getByPlayer(player);

                    if (pearPlayer != null) {
                        pearPlayer.setTrail(null);
                        pearPlayer.save();
                    }

                    player.sendMessage(ChatColor.RED + "You've unequipped your particle cosmetic.");
                })
        );

        return buttonMap;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        final Map<Integer, Button> buttonMap = new HashMap<>();
        final AtomicInteger atomicInteger = new AtomicInteger();

        this.plugin.getCosmeticHandler().getTrailCosmeticMap().values().forEach(trail -> buttonMap.put(atomicInteger.getAndIncrement(), trail.getMenuItemBuilder()
                .addLore("&e[Click to apply this cosmetic]")
                .toButton((player1, clickType) -> {
                    if (!player.hasPermission(trail.getPermission())) {
                        player.sendMessage(ChatColor.RED + "You don't have permission to apply this cosmetic!");
                        return;
                    }

                    final BukkitRunnable bukkitRunnable = this.plugin.getCosmeticHandler().getRunnableHashMap().get(player);

                    if (bukkitRunnable != null) {
                        player.sendMessage(ChatColor.RED + "You already have a cosmetic equipped.");
                        return;
                    }

                    trail.applyTo(player, trail.getParticleEffect());

                    final PersistentPearPlayer pearPlayer = plugin.getPersistentPlayerCache().getByPlayer(player);

                    if (pearPlayer != null) {
                        pearPlayer.setTrail(trail.getParticleEffect().name());
                        pearPlayer.save();
                    }

                    player.closeInventory();
                    player.sendMessage(Color.SECONDARY_COLOR + "You've applied the " + ChatColor.BLUE + trail.getName() + Color.SECONDARY_COLOR + " cosmetic!");
                })));

        return buttonMap;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Cosmetics » Trails";
    }
}
