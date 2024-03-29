package com.solexgames.pear.menu.cosmetic;

import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.core.util.external.Button;
import com.solexgames.core.util.external.Menu;
import com.solexgames.pear.PearSpigotPlugin;
import com.solexgames.pear.menu.cosmetic.selection.CosmeticArmorSelectionMenu;
import com.solexgames.pear.menu.cosmetic.selection.CosmeticParticleSelectionMenu;
import com.solexgames.pear.player.impl.PersistentPearPlayer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

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
        final PersistentPearPlayer pearPlayer = this.plugin.getPersistentPlayerCache().getByPlayer(player);

        buttonMap.put(2, new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .setDisplayName(ChatColor.GREEN + "Armor")
                .addLore(
                        "&7Select one of our multiple",
                        "&7rank-specific armor types!",
                        "",
                        "&7Selected: " + ChatColor.WHITE + (pearPlayer.getArmor() == null ? "None" : pearPlayer.getArmor()),
                        "",
                        "&e[Click to view armor]"
                )
                .toButton((player1, clickType) -> {
                    new CosmeticArmorSelectionMenu(this.plugin).openMenu(player);
                })
        );

        buttonMap.put(4, new ItemBuilder(Material.EMERALD)
                .setDisplayName(ChatColor.GREEN + "Trails")
                .addLore(
                        "&7Select one of our multiple",
                        "&7trail effects!",
                        "",
                        "&7Selected: " + ChatColor.WHITE + (pearPlayer.getTrail() == null ? "None" : StringUtils.capitalize(pearPlayer.getTrail().toLowerCase())),
                        "",
                        "&e[Click to view trails]"
                )
                .toButton((player1, clickType) -> {
                    new CosmeticParticleSelectionMenu(this.plugin).openMenu(player);
                })
        );

        buttonMap.put(6, new ItemBuilder(Material.FIREWORK)
                .setDisplayName(ChatColor.GREEN + "Gadgets")
                .addLore(
                        "&7Select one of our multiple",
                        "&7gadgets!",
                        "",
                        "&7Selected: " + ChatColor.WHITE + (pearPlayer.getTrail() == null ? "None" : StringUtils.capitalize(pearPlayer.getTrail().toLowerCase())),
                        "",
                        "&e[Click to view trails]"
                )
                .toButton((player1, clickType) -> {
                    player1.sendMessage(ChatColor.RED + "Gadgets are temporarily disabled.");
                })
        );

        for (int i = 0; i <= 8; i++) {
            buttonMap.putIfAbsent(i, PearSpigotPlugin.GLASS);
        }

        return buttonMap;
    }
}
