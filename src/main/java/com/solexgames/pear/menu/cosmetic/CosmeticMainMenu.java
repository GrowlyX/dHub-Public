package com.solexgames.pear.menu.cosmetic;

import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.core.util.external.Button;
import com.solexgames.core.util.external.Menu;
import com.solexgames.pear.PearSpigotPlugin;
import com.solexgames.pear.menu.cosmetic.selection.CosmeticArmorSelectionMenu;
import com.solexgames.pear.menu.cosmetic.selection.CosmeticParticleSelectionMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CosmeticMainMenu extends Menu {

    private final PearSpigotPlugin plugin;

    @Override
    public String getTitle(Player player) {
        return "Cosmetics » Main";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttonMap = new HashMap<>();

        buttonMap.put(3, new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .setDisplayName(ChatColor.AQUA + ChatColor.BOLD.toString() + "Armor")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .setColor(Color.RED)
                .addLore(
                        "&7Click to view all available",
                        "&7armor cosmetics!",
                        "",
                        "&e[Click to view armor]"
                )
                .toButton((player1, clickType) -> new CosmeticArmorSelectionMenu(this.plugin).openMenu(player))
        );

        buttonMap.put(5, new ItemBuilder(Material.BLAZE_POWDER)
                .setDisplayName(ChatColor.GREEN + ChatColor.BOLD.toString() + "Trails")
                .addLore(
                        "&7Click to view all available",
                        "&7trail cosmetics!",
                        "",
                        "&e[Click to view trails]"
                )
                .toButton((player1, clickType) -> new CosmeticParticleSelectionMenu(this.plugin).openMenu(player))
        );

        return buttonMap;
    }
}
