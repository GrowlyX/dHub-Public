package com.solexgames.menu;

import com.solexgames.HubPlugin;
import com.solexgames.core.menu.AbstractInventoryMenu;
import com.solexgames.core.util.Color;
import com.solexgames.core.util.LocationUtil;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.manager.HubManager;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Getter
public class SetupHubMenu extends AbstractInventoryMenu {

    private final Player player;
    private final HubManager hubManager;

    public SetupHubMenu(Player player) {
        super("Setup dHub", 9);

        this.player = player;
        this.hubManager = HubPlugin.getInstance().getHubManager();

        this.update();
    }

    @Override
    public void update() {
        this.inventory.setItem(3, new ItemBuilder(Material.INK_SACK, 2).setDisplayName("&6Set Spawn").addLore("", "&7Click this item to set", "&7the hub spawn.").create());
        this.inventory.setItem(5, new ItemBuilder(Material.INK_SACK, 6).setDisplayName("&6Reload dHub").addLore("", "&7Click this item to reload", "&7configuration files!").create());
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        Inventory topInventory = event.getView().getTopInventory();
        if (!topInventory.equals(this.inventory)) return;
        if (topInventory.equals(clickedInventory)) {
            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();
            if (item == null || item.getType() == Material.AIR) return;
            if (event.getRawSlot() == 3) {
                HubPlugin.getInstance().getHubManager().setHubLocation(player.getLocation());
                HubPlugin.getInstance().getConfig().set("locations.spawn-location", LocationUtil.getStringFromLocation(player.getLocation()).orElse("none"));
                HubPlugin.getInstance().saveConfig();

                player.sendMessage(Color.translate("&aSuccessfully set the hub spawn!"));
                player.closeInventory();

                return;
            }
            if (event.getRawSlot() == 5) {
                HubPlugin.getInstance().reloadAllConfigs();

                player.sendMessage(Color.translate("&aSuccessfully reloaded dHub!"));
                player.closeInventory();
            }
        }
    }
}
