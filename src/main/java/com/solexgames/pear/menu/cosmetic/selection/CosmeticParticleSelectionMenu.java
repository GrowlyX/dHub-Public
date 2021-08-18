package com.solexgames.pear.menu.cosmetic.selection;

import com.solexgames.core.util.Color;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.core.util.external.Button;
import com.solexgames.core.util.external.pagination.PaginatedMenu;
import com.solexgames.pear.PearSpigotPlugin;
import com.solexgames.pear.menu.cosmetic.CosmeticMainMenu;
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

        buttonMap.put(3, new ItemBuilder(Material.NETHER_STAR)
                .setDisplayName(ChatColor.GREEN + "Reset Trail")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .addLore(
                        "&7Clear & reset your trail.",
                        "",
                        "&e[Click to reset trail]"
                )
                .toButton((player1, clickType) -> {
                    final BukkitRunnable bukkitRunnable = this.plugin.getCosmeticHandler().getRunnableHashMap().get(player);

                    if (bukkitRunnable == null) {
                        player.sendMessage(ChatColor.RED + "You do not have a trail selected.");
                        return;
                    }

                    bukkitRunnable.cancel();

                    this.plugin.getCosmeticHandler().getRunnableHashMap().remove(player);

                    final PersistentPearPlayer pearPlayer = plugin.getPersistentPlayerCache().getByPlayer(player);

                    if (pearPlayer != null) {
                        pearPlayer.setTrail(null);
                        pearPlayer.save();
                    }

                    player.sendMessage(ChatColor.RED + "You've unequipped your trail.");
                })
        );

        buttonMap.put(5, new ItemBuilder(Material.BED)
                .setDisplayName(ChatColor.RED + "Return to Main")
                .addLore(
                        "&7Return back to the",
                        "&7main cosmetic menu.",
                        " ",
                        "&e[Click to open main]"
                )
                .toButton((player1, clickType) -> {
                    new CosmeticMainMenu(this.plugin).openMenu(player1);
                })
        );

        return buttonMap;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        final Map<Integer, Button> buttonMap = new HashMap<>();
        final AtomicInteger atomicInteger = new AtomicInteger();

        this.plugin.getCosmeticHandler().getTrailCosmeticMap().values().forEach(trail -> buttonMap.put(atomicInteger.getAndIncrement(), trail.getMenuItemBuilder()
                .addLore(
                        "&e[Click to apply]"
                )
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
