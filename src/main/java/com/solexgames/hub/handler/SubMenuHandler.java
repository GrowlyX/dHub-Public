package com.solexgames.hub.handler;

import com.solexgames.core.util.external.Menu;
import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.menu.ConfigurableMenu;
import com.solexgames.hub.menu.HubSelectorMenu;
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

    private final Map<String, Menu> menuMap = new HashMap<>();

    private final HubPlugin plugin;

    public void registerSubMenusFromConfig() {
        this.menuMap.put("server-selector", new ConfigurableMenu("server-selector", this.plugin));
        this.menuMap.put("hub-selector", new HubSelectorMenu(this.plugin));

        this.plugin.getMenus().getConfiguration().getConfigurationSection("sub-menus")
                .getKeys(false).forEach(s -> this.menuMap.put(s, new ConfigurableMenu("sub-menus." + s.toLowerCase(), this.plugin)));
    }

    public void openSubMenu(String menuName, Player player) {
        final Menu menu = this.menuMap.get(menuName.toLowerCase());

        if (menu != null) {
            menu.openMenu(player);
        } else {
            player.sendMessage(ChatColor.RED + "Error: No menu by the name " + ChatColor.YELLOW + menuName + ChatColor.RED + " exists.");
        }
    }
}
