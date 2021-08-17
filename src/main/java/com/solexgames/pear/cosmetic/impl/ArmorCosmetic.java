package com.solexgames.pear.cosmetic.impl;

import com.solexgames.core.player.ranks.Rank;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.pear.PearSpigotPlugin;
import com.solexgames.pear.cosmetic.Cosmetic;
import com.solexgames.pear.cosmetic.CosmeticType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author GrowlyX
 * @since 5/1/2021
 */

@Getter
@RequiredArgsConstructor
public class ArmorCosmetic extends Cosmetic<Rank> {

    public static final Map<UUID, ArmorUpdaterRunnable> ARMOR_UPDATER_RUNNABLE_MAP = new HashMap<>();

    private final Rank rank;
    private final String name;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getPermission() {
        return "pear.cosmetic.armor." + (this.rank != null ? this.rank.getName().toLowerCase() : this.name.toLowerCase());
    }

    @Override
    public CosmeticType getCosmeticType() {
        return CosmeticType.ARMOR;
    }

    @Override
    public void applyTo(Player player, Rank rank) {
        final ChatColor chatColor = ChatColor.getByChar(com.solexgames.core.util.Color.translate(rank != null ? rank.getColor() : "&r").replace("&", "").replace("§", ""));
        final Color color = this.getByChatColor(chatColor);
        final String rankFancy = chatColor + ChatColor.BOLD.toString() + (rank != null ? rank.getName() : this.name);

        final ItemStack[] itemStacks = new ItemStack[4];

        itemStacks[3] = new ItemBuilder(Material.LEATHER_HELMET).setDisplayName(rankFancy + " Helmet").setColor(color).create();
        itemStacks[2] = new ItemBuilder(Material.LEATHER_CHESTPLATE).setDisplayName(rankFancy + " Chestplate").setColor(color).create();
        itemStacks[1] = new ItemBuilder(Material.LEATHER_LEGGINGS).setDisplayName(rankFancy + " Pants").setColor(color).create();
        itemStacks[0] = new ItemBuilder(Material.LEATHER_BOOTS).setDisplayName(rankFancy + " Boots").setColor(color).create();

        player.getInventory().setArmorContents(itemStacks);

        player.updateInventory();
    }

    @Override
    public void removeFrom(Player player) {
        player.getInventory().setArmorContents(null);
        player.updateInventory();
    }

    public ItemBuilder getMenuItemBuilder() {
        final ChatColor chatColor = ChatColor.getByChar(com.solexgames.core.util.Color.translate(this.rank != null ? this.rank.getColor() : "&r").replace("&", "").replace("§", ""));
        final Color color = this.getByChatColor(chatColor);
        final String rankFancy = chatColor + ChatColor.BOLD.toString() + (this.rank != null ? this.rank.getName() : this.name);

        return new ItemBuilder(Material.LEATHER_HELMET)
                .setDisplayName(rankFancy + " Armor")
                .setColor(color);
    }

    public Color getByChatColor(ChatColor color) {
        switch (color) {
            case BLUE: case DARK_BLUE: return Color.BLUE;
            case DARK_GREEN:
                return Color.GREEN;
            case GREEN:
                return Color.fromRGB(0, 255, 0);
            case DARK_AQUA: case AQUA: return Color.AQUA;
            case DARK_RED: case RED: return Color.RED;
            case DARK_PURPLE: case LIGHT_PURPLE: return Color.FUCHSIA;
            case GOLD: return Color.ORANGE;
            case GRAY: case DARK_GRAY: return Color.GRAY;
            case YELLOW: return Color.YELLOW;
            case WHITE: return Color.WHITE;
            default: return Color.BLACK;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class ArmorUpdaterRunnable extends BukkitRunnable {

        private final Player player;
        private final ArmorCosmetic armorCosmetic;
        private final PearSpigotPlugin plugin;

        private int tick = -1;
        private int glassTick = -1;

        @Override
        public void run() {
            if (this.tick == 256) {
                this.tick = -1;
            }
            if (this.glassTick == 16) {
                this.glassTick = -1;
            }

            final int newTick = this.tick++;
            final int newGlassTick = this.glassTick++;

            final Color color = this.plugin.getCosmeticHandler()
                    .getColorHashMap().get(newTick);
            final String rankFancy = ChatColor.GREEN.toString() + ChatColor.BOLD + "Chroma";

            final ItemStack[] itemStacks = new ItemStack[4];

            itemStacks[3] = new ItemBuilder(Material.STAINED_GLASS).setDisplayName(rankFancy + " Helmet").setDurability(newGlassTick).create();
            itemStacks[2] = new ItemBuilder(Material.LEATHER_CHESTPLATE).setDisplayName(rankFancy + " Chestplate").setColor(color).create();
            itemStacks[1] = new ItemBuilder(Material.LEATHER_LEGGINGS).setDisplayName(rankFancy + " Pants").setColor(color).create();
            itemStacks[0] = new ItemBuilder(Material.LEATHER_BOOTS).setDisplayName(rankFancy + " Boots").setColor(color).create();

            this.player.getInventory().setArmorContents(itemStacks);
            this.player.updateInventory();
        }
    }
}
