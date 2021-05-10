package com.solexgames.hub.menu.cosmetic;

import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.core.util.external.Button;
import com.solexgames.core.util.external.Menu;
import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.menu.cosmetic.selection.CosmeticArmorSelectionMenu;
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

    private final HubPlugin plugin;

    @Override
    public String getTitle(Player player) {
        return "Cosmetics";
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
                        "&e[Click to view cosmetics]"
                )
                .toButton((player1, clickType) -> {
                    player1.closeInventory();

                    new CosmeticArmorSelectionMenu(this.plugin).openMenu(player);
                })
        );

        buttonMap.put(5, new ItemBuilder(Material.BLAZE_POWDER)
                .setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Particles")
                .addLore(
                        "&7Click to view all available",
                        "&7particle cosmetics!",
                        "",
                        "&c[Currently unavailable]"
                )
                .toButton((player1, clickType) -> {
                    player1.closeInventory();
                    player1.sendMessage(ChatColor.RED + "I'm sorry, but you cannot open this menu right now.");
                })
        );

        return buttonMap;
    }
}
