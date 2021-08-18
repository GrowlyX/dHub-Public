package com.solexgames.pear.menu.cosmetic;

import com.solexgames.core.CorePlugin;
import com.solexgames.core.player.PotPlayer;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.core.util.external.Button;
import com.solexgames.core.util.external.Menu;
import com.solexgames.pear.PearSpigotPlugin;
import com.solexgames.pear.menu.cosmetic.selection.CosmeticArmorSelectionMenu;
import com.solexgames.pear.menu.cosmetic.selection.CosmeticParticleSelectionMenu;
import com.solexgames.pear.player.impl.PersistentPearPlayer;
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
        final PersistentPearPlayer pearPlayer = this.plugin.getPersistentPlayerCache().getByPlayer(player);

        buttonMap.put(1, new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .setDisplayName(ChatColor.GREEN + "Armor")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .setColor(Color.YELLOW)
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

        buttonMap.put(3, new ItemBuilder(Material.NETHER_STAR)
                .setDisplayName(ChatColor.GREEN + "Trails")
                .addLore(
                        "&7Click to view all available",
                        "&7trails!",
                        "",
                        "&7Selected: " + ChatColor.WHITE + (pearPlayer.getTrail() == null ? "None" : pearPlayer.getTrail()),
                        "",
                        "&e[Click to view trails]"
                )
                .toButton((player1, clickType) -> {
                    new CosmeticParticleSelectionMenu(this.plugin).openMenu(player);
                })
        );

        return buttonMap;
    }
}
