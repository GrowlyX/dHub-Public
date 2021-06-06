package com.solexgames.hub.menu.submenu;

import com.solexgames.core.menu.AbstractInventoryMenu;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.menu.action.MenuAction;
import com.solexgames.hub.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class SubMenu extends AbstractInventoryMenu {

    private final Player player;
    private final String path;
    private final HubPlugin plugin;

    public SubMenu(Player player, String path, HubPlugin plugin) {
        super(plugin.getMenus().getString("sub-menus." + path + ".title"), plugin.getMenus().getInt("sub-menus." + path + ".size"));

        this.player = player;
        this.path = path;
        this.plugin = plugin;

        this.update();
    }

    @Override
    public void update() {
        if (this.plugin.getMenus().getBoolean("sub-menus." + this.path + ".fill-stained-glass")) {
            while(this.inventory.firstEmpty() != -1) {
                this.inventory.setItem(this.inventory.firstEmpty(), new ItemBuilder(Material.STAINED_GLASS_PANE, 7).setDisplayName(" ").create());
            }
        }

        final ConfigurationSection configurationSection = this.plugin.getMenus().getConfiguration().getConfigurationSection("sub-menus." + this.path + ".items");
        configurationSection.getKeys(false).forEach(itemSection -> {
            final int slot = Integer.parseInt(itemSection);
            this.inventory.setItem(slot, ItemUtil.getMenuItem("sub-menus." + this.path + ".items." + itemSection, this.player, this.plugin));
        });
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        final Inventory clickedInventory = event.getClickedInventory();
        final Inventory topInventory = event.getView().getTopInventory();

        if (!topInventory.equals(this.inventory)) return;
        if (topInventory.equals(clickedInventory)) {
            event.setCancelled(true);

            if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() &&  !event.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) {
                final MenuAction.Type action = ItemUtil.getActionFromConfig("sub-menus." + this.path + ".items." + event.getRawSlot(), this.plugin);
                final String value = ItemUtil.getValueFromConfig("sub-menus." + this.path + ".items." + event.getRawSlot(), this.plugin);

                if (action != null && value != null && ItemUtil.isEnabledAction("sub-menus." + this.path + ".items." + event.getRawSlot(), this.plugin)) {
                    MenuAction.completeAction(action, value, this.player, this.plugin);
                }
            }
        }
    }
}
