package com.solexgames.hub.menu;

import com.solexgames.core.menu.AbstractInventoryMenu;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.menu.action.MenuAction;
import com.solexgames.hub.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ServerSelectorMenu extends AbstractInventoryMenu {

    private final Player player;
    private final HubPlugin plugin;

    public ServerSelectorMenu(Player player, HubPlugin plugin) {
        super(plugin.getMenus().getString("server-selector.title"), plugin.getMenus().getInt("server-selector.size"));

        this.player = player;
        this.plugin = plugin;

        this.update();
    }

    @Override
    public void update() {
        if (this.plugin.getMenus().getBoolean("server-selector.fill-stained-glass")) {
            while(this.inventory.firstEmpty() != -1) {
                this.inventory.setItem(this.inventory.firstEmpty(), new ItemBuilder(Material.STAINED_GLASS_PANE, 7).setDisplayName(" ").create());
            }
        }

        final ConfigurationSection configurationSection = this.plugin.getMenus().getConfiguration().getConfigurationSection("server-selector.items");
        configurationSection.getKeys(false).forEach(itemSection -> {
            final int slot = Integer.parseInt(itemSection);
            this.inventory.setItem(slot, ItemUtil.getMenuItem("server-selector.items." + itemSection, this.player, this.plugin));
        });
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        final Inventory clickedInventory = event.getClickedInventory();
        final Inventory topInventory = event.getView().getTopInventory();

        if (!topInventory.equals(this.inventory)) return;
        if (topInventory.equals(clickedInventory)) {
            event.setCancelled(true);

            if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()) {
                final MenuAction.Type action = ItemUtil.getActionFromConfig("server-selector.items." + event.getRawSlot(), this.plugin);
                final String value = ItemUtil.getValueFromConfig("server-selector.items." + event.getRawSlot(), this.plugin);

                if (action != null && value != null && ItemUtil.isEnabledAction("server-selector.items." + event.getRawSlot(), this.plugin)) {
                    MenuAction.completeAction(action, value, this.player, this.plugin);
                }
            }
        }
    }
}
