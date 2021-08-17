package com.solexgames.pear.handler;

import com.solexgames.pear.PearSpigotPlugin;
import com.solexgames.pear.menu.ConfigurableMenu;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author GrowlyX
 * @since 4/22/2021
 */

@Getter
@RequiredArgsConstructor
public class SubMenuHandler {

    private final Map<String, String> menuMap = new HashMap<>();

    private final PearSpigotPlugin plugin;

    public void registerSubMenusFromConfig() {
        this.menuMap.put("server-selector", "server-selector");

        this.plugin.getMenus().getConfiguration().getConfigurationSection("sub-menus")
                .getKeys(false).forEach(s -> this.menuMap.put(s, "sub-menus." + s.toLowerCase()));
    }

    public void openSubMenu(String menuName, Player player) {
        final String menu = this.menuMap.get(menuName.toLowerCase());

        if (menu != null) {
            new ConfigurableMenu(menu, this.plugin).openMenu(player);
        } else {
            player.sendMessage(ChatColor.RED + "Error: No menu by the name " + ChatColor.YELLOW + menuName + ChatColor.RED + " exists.");
        }
    }
}
