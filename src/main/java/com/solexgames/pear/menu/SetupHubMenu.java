package com.solexgames.pear.menu;

import com.solexgames.pear.PearSpigotPlugin;
import com.solexgames.core.menu.AbstractInventoryMenu;
import com.solexgames.core.util.Color;
import com.solexgames.core.util.builder.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Getter
public class SetupHubMenu extends AbstractInventoryMenu {

    private final Player player;
    private final PearSpigotPlugin plugin;

    public SetupHubMenu(Player player, PearSpigotPlugin plugin) {
        super("Pear Spigot » Control", 9);

        this.player = player;
        this.plugin = plugin;

        this.update();
    }

    @Override
    public void update() {
        this.inventory.setItem(0, new ItemBuilder(Material.INK_SACK, 8)
                .setDisplayName("&aSet Spawn")
                .addLore(
                        "&7Click this button to set",
                        "&7the hub spawn!"
                ).create());
        this.inventory.setItem(8, new ItemBuilder(Material.INK_SACK, 1)
                .setDisplayName("&aReload menus")
                .addLore(
                        "&7Click this button to reload",
                        "&7menus!"
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

            if (!(item == null || item.getType() == Material.AIR)) {
                if (event.getRawSlot() == 1) {
                    this.plugin.reloadAllConfigs();

                    this.player.sendMessage(Color.SECONDARY_COLOR + "You've reloaded all configuration files related to Neon!");
                    this.player.closeInventory();
                } else if (event.getRawSlot() == 0) {
                    this.getPlugin().getSettingsProcessor().setSpawnLocation(this.player.getLocation());

                    this.player.closeInventory();
                    this.player.sendMessage("You've set the spawn of this hub!");
                }
            }
        }
    }
}
