package com.solexgames.hub.menu;

import com.cryptomorin.xseries.XMaterial;
import com.solexgames.hub.HubPlugin;
import com.solexgames.core.menu.AbstractInventoryMenu;
import com.solexgames.core.util.Color;
import com.solexgames.core.util.LocationUtil;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.hub.manager.HubManager;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Getter
public class SetupHubMenu extends AbstractInventoryMenu {

    private final Player player;
    private final HubManager hubManager;
    private final HubPlugin plugin;

    public SetupHubMenu(Player player, HubPlugin plugin) {
        super("Setup dHub", 9);

        this.player = player;
        this.plugin = plugin;
        this.hubManager = this.plugin.getHubManager();

        this.update();
    }

    @Override
    public void update() {
        this.inventory.setItem(4, new ItemBuilder(XMaterial.LIME_DYE.parseMaterial(), 6)
                .setDisplayName("&aReload Neon")
                .addLore(
                        "&7Click this item to reload",
                        "&7configuration files!"
                ).create());
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        final Inventory clickedInventory = event.getClickedInventory();
        final Inventory topInventory = event.getView().getTopInventory();

        if (!topInventory.equals(this.inventory)) return;
        if (topInventory.equals(clickedInventory)) {
            event.setCancelled(true);

            final ItemStack item = event.getCurrentItem();

            if (!(item == null || item.getType() == Material.AIR) && event.getRawSlot() == 4) {
                this.plugin.reloadAllConfigs();

                this.player.sendMessage(Color.SECONDARY_COLOR + "You've reloaded all configuration files related to Neon!");
                this.player.closeInventory();
            }
        }
    }
}
