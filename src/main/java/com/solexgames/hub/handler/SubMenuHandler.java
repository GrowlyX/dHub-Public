package com.solexgames.hub.handler;

import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.menu.submenu.SubMenu;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GrowlyX
 * @since 4/22/2021
 */

@Getter
public class SubMenuHandler {

    private final List<String> menuPathList = new ArrayList<>();

    private final HubPlugin plugin;

    public SubMenuHandler(HubPlugin plugin) {
        this.plugin = plugin;

        try {
            this.plugin.getMenus().getConfiguration().getConfigurationSection("sub-menus").getKeys(false).forEach(this::registerSubMenu);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public void registerSubMenu(String name) {
        this.menuPathList.add(name);

        System.out.println("[Menu] Registered a new menu with the name \"" + name + "\"!");
    }

    public void openSubMenu(String menuName, Player player) {
        final boolean pathExists = this.menuPathList.contains(menuName.toLowerCase());

        if (pathExists) {
            final SubMenu subMenu = new SubMenu(player, menuName.toLowerCase());

            player.openInventory(subMenu.getInventory());
        } else {
            player.sendMessage(ChatColor.RED + "Error: No menu with the name '" + ChatColor.YELLOW + menuName + ChatColor.RED + "' exists.");
        }
    }
}
