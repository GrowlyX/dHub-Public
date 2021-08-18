package com.solexgames.pear.menu;

import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.core.util.external.Button;
import com.solexgames.core.util.external.Menu;
import com.solexgames.pear.PearSpigotConstants;
import com.solexgames.pear.PearSpigotPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class SetupHubMenu extends Menu {

    private final PearSpigotPlugin plugin;

    @Override
    public String getTitle(Player player) {
        return "Pear » Main";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttonMap = new HashMap<>();

        buttonMap.put(0, new ItemBuilder(Material.INK_SACK, 8)
                .setDisplayName(ChatColor.GREEN + "Set Spawn")
                .addLore(
                        ChatColor.GRAY + "Modify the spawn location.",
                        "",
                        ChatColor.YELLOW + "[Click to set the spawn]"
                ).toButton((player1, clickType) -> {
                    this.plugin.getSettingsProcessor().setSpawnLocation(player1.getLocation());

                    player1.closeInventory();
                    player1.sendMessage(PearSpigotConstants.CHAT_PREFIX + ChatColor.GREEN + "You've set the hub spawn location.");
                })
        );

        buttonMap.put(8, new ItemBuilder(Material.INK_SACK, 1)
                .setDisplayName(ChatColor.GREEN + "Menu Reload")
                .addLore(
                        ChatColor.GRAY + "Reload all menus.",
                        "",
                        ChatColor.YELLOW + "[Click to reload menus]"
                ).toButton((player1, clickType) -> {
                    this.plugin.reloadAllConfigs();

                    player1.closeInventory();
                    player1.sendMessage(PearSpigotConstants.CHAT_PREFIX + ChatColor.GREEN + "You've reloaded all config files.");
                })
        );

        return buttonMap;
    }
}
