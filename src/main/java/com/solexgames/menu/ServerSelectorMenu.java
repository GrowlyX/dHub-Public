package com.solexgames.menu;

import com.solexgames.HubPlugin;
import com.solexgames.core.menu.AbstractInventoryMenu;
import com.solexgames.core.util.Color;
import com.solexgames.core.util.LocationUtil;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.external.ExternalConfig;
import com.solexgames.util.ItemUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class ServerSelectorMenu extends AbstractInventoryMenu {

    private final HubPlugin plugin = HubPlugin.getInstance();
    private final ExternalConfig menus = plugin.getMenus();

    private Player player;

    public ServerSelectorMenu(Player player) {
        super(HubPlugin.getInstance().getMenus().getString("server-selector.title"), HubPlugin.getInstance().getMenus().getInt("server-selector.size"));

        this.player = player;

        this.update();
    }

    @Override
    public void update() {
        if (menus.getBoolean("server-selector.fill-stained-glass")) {
            while(this.inventory.firstEmpty() != -1) {
                this.inventory.setItem(this.inventory.firstEmpty(), new ItemBuilder(Material.STAINED_GLASS_PANE, 7).setDisplayName(" ").create());
            }
        }

        ConfigurationSection configurationSection = menus.getConfiguration().getConfigurationSection("server-selector.items");
        configurationSection.getKeys(false).forEach(itemSection -> {
            int slot = Integer.parseInt(itemSection);
            this.inventory.setItem(slot, ItemUtil.getMenuItem("server-selector.items." + itemSection, player));
        });
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        Inventory topInventory = event.getView().getTopInventory();
        if (!topInventory.equals(this.inventory)) return;
        if (topInventory.equals(clickedInventory)) {
            event.setCancelled(true);

            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().hasItemMeta()) {
                    if (ItemUtil.getActionFromConfig("server-selector.items." + event.getRawSlot()) != null) {
                        player.chat("/" + ItemUtil.getActionFromConfig("server-selector.items." + event.getRawSlot()));
                        player.closeInventory();
                    }
                }
            }
        }
    }
}
