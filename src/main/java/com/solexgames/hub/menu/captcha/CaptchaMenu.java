package com.solexgames.hub.menu.captcha;

import com.solexgames.core.CorePlugin;
import com.solexgames.core.menu.AbstractInventoryMenu;
import com.solexgames.core.util.Color;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.hub.HubPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.concurrent.ThreadLocalRandom;

public class CaptchaMenu extends AbstractInventoryMenu {

    private final Player player;
    private final Material targetMaterial;

    private final HubPlugin plugin;

    public CaptchaMenu(Player player, Material material, HubPlugin plugin) {
        super("Please click the " + Color.MAIN_COLOR + ChatColor.BOLD.toString() + "Blaze Powder", 27);

        this.player = player;
        this.targetMaterial = material;
        this.plugin = plugin;

        this.update();
    }

    @Override
    public void update() {
        while (inventory.firstEmpty() != -1) {
            final Material material = Material.values()[ThreadLocalRandom.current().nextInt(Material.values().length)];

            if ((material.isSolid() && material.isBlock()) && !material.equals(this.targetMaterial)) {
                this.inventory.setItem(inventory.firstEmpty(), new ItemBuilder(material)
                        .setDisplayName("  ")
                        .create()
                );
            }
        }

        final int randomSlot = ThreadLocalRandom.current().nextInt(27);

        this.inventory.setItem(randomSlot, new ItemBuilder(this.targetMaterial)
                .setDisplayName(ChatColor.GREEN + ChatColor.BOLD.toString() + "Click me!")
                .addLore(ChatColor.GRAY + "Click to pass the captcha!")
                .create());
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        final Inventory clickedInventory = event.getClickedInventory();
        final Inventory topInventory = event.getView().getTopInventory();

        if (!topInventory.equals(this.inventory)) return;
        if (topInventory.equals(clickedInventory)) {
            event.setCancelled(true);

            if (event.getCurrentItem().getType().equals(this.targetMaterial)) {
                this.player.sendMessage(ChatColor.GREEN + "You've passed the captcha, have fun!");

                this.player.setMetadata("captcha", new FixedMetadataValue(this.plugin, true));
                this.player.closeInventory();
            }
        }
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!this.player.hasMetadata("captcha")) {
            this.player.kickPlayer(ChatColor.RED + ChatColor.BOLD.toString() + "You've failed the captcha!" + "\n" + ChatColor.RED + "Please login and try again!");
        }
    }
}
