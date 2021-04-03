package com.solexgames.menu;

import com.solexgames.HubPlugin;
import com.solexgames.core.CorePlugin;
import com.solexgames.core.enums.NetworkServerType;
import com.solexgames.core.menu.AbstractInventoryMenu;
import com.solexgames.core.server.NetworkServer;
import com.solexgames.core.util.Color;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.external.ExternalConfig;
import com.solexgames.util.BungeeUtil;
import com.solexgames.util.ItemUtil;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class HubSelectorMenu extends AbstractInventoryMenu {

    private final HubPlugin plugin = HubPlugin.getInstance();
    private final ExternalConfig menus = plugin.getMenus();

    private Player player;

    public HubSelectorMenu(Player player) {
        super(HubPlugin.getInstance().getMenus().getString("hub-selector.title"), 27);

        this.player = player;

        this.update();
    }

    @Override
    public void update() {
        if (menus.getBoolean("server-selector.fill-stained-glass")) {
            while (this.inventory.firstEmpty() != -1) {
                this.inventory.setItem(this.inventory.firstEmpty(), new ItemBuilder(Material.STAINED_GLASS_PANE, 7).setDisplayName(" ").create());
            }
        }

        AtomicInteger atomicInteger = new AtomicInteger(10);
        CorePlugin.getInstance().getServerManager().getNetworkServers().stream()
                .filter(Objects::nonNull)
                .filter(networkServer -> networkServer.getServerType().equals(NetworkServerType.HUB))
                .forEach(networkServer -> {
                    if (atomicInteger.get() < 16) {
                        List<String> list = menus.getStringList("hub-selector.item.lore");
                        list = PlaceholderAPI.setPlaceholders(player, list);

                        List<String> finalList = new ArrayList<>();
                        list.forEach(s -> finalList.add(s
                                .replace("<online>", String.valueOf(networkServer.getOnlinePlayers()))
                                .replace("<max>", String.valueOf(networkServer.getMaxPlayerLimit()))
                                .replace("<status>", (CorePlugin.getInstance().getServerName().equalsIgnoreCase(networkServer.getServerName()) ? "&cAlready Connected!" : networkServer.getServerStatus().getServerStatusFancyString()))
                        ));

                        this.inventory.setItem(atomicInteger.get(), new ItemBuilder(Material.valueOf(menus.getString("hub-selector.item.item")))
                                .setDurability(menus.getInt("hub-selector.item.durability"))
                                .addLore(finalList)
                                .setDisplayName(menus.getString("hub-selector.item.display")
                                        .replace("<server-name>", networkServer.getServerName())
                                )
                                .create()
                        );

                        atomicInteger.getAndIncrement();
                    }
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
                    if (NetworkServer.getByName(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName())) != null) {
                        NetworkServer networkServer = NetworkServer.getByName(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
                        if (!CorePlugin.getInstance().getServerName().equals(networkServer.getServerName())) {
                            player.sendMessage(Color.translate("&aSending you to " + networkServer.getServerName() + "..."));
                            BungeeUtil.sendToServer(player, networkServer.getServerName());
                        } else {
                            player.sendMessage(Color.translate("&cYou are already on " + networkServer.getServerName() + "!"));
                        }
                        player.closeInventory();
                    }
                }
            }
        }
    }
}
