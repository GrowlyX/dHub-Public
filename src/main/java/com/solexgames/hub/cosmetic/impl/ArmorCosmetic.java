package com.solexgames.hub.cosmetic.impl;

import com.solexgames.core.player.ranks.Rank;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.hub.cosmetic.Cosmetic;
import com.solexgames.hub.cosmetic.CosmeticType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@RequiredArgsConstructor
public class ArmorCosmetic extends Cosmetic<Rank> {

    private final Rank rank;
    private final String name;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getPermission() {
        return "neon.cosmetic.armor." + this.rank.getName().toLowerCase();
    }

    @Override
    public CosmeticType getCosmeticType() {
        return CosmeticType.ARMOR;
    }

    @Override
    public void applyTo(Player player, Rank rank) {
        final ChatColor chatColor = ChatColor.getByChar(com.solexgames.core.util.Color.translate(rank.getColor()).replace("&", "").replace("§", ""));
        final Color color = this.getByChatColor(chatColor);
        final String rankFancy = chatColor + ChatColor.BOLD.toString() + rank.getName();

        player.getInventory().setArmorContents(new ItemStack[]{
                new ItemBuilder(Material.LEATHER_HELMET).setDisplayName(rankFancy + " Helmet").setColor(color).create(),
                new ItemBuilder(Material.LEATHER_CHESTPLATE).setDisplayName(rankFancy + " Chestplate").setColor(color).create(),
                new ItemBuilder(Material.LEATHER_LEGGINGS).setDisplayName(rankFancy + " Pants").setColor(color).create(),
                new ItemBuilder(Material.LEATHER_BOOTS).setDisplayName(rankFancy + " Boots").setColor(color).create(),
        });

        player.updateInventory();
    }

    @Override
    public void removeFrom(Player player) {
        player.getInventory().setArmorContents(null);
        player.updateInventory();
    }

    public ItemBuilder getMenuItemBuilder() {
        final ChatColor chatColor = ChatColor.getByChar(com.solexgames.core.util.Color.translate(rank.getColor()).replace("&", "").replace("§", ""));
        final Color color = this.getByChatColor(chatColor);
        final String rankFancy = chatColor + ChatColor.BOLD.toString() + rank.getName();

        return new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .setDisplayName(rankFancy + " Cosmetic")
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
}
