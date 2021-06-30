package com.solexgames.hub.menu;

import com.solexgames.core.CorePlugin;
import com.solexgames.core.enums.NetworkServerStatusType;
import com.solexgames.core.enums.NetworkServerType;
import com.solexgames.core.menu.AbstractInventoryMenu;
import com.solexgames.core.server.NetworkServer;
import com.solexgames.core.util.BungeeUtil;
import com.solexgames.core.util.Color;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.hub.HubPlugin;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

    private Player player;
    private HubPlugin hubPlugin;

    public HubSelectorMenu(Player player, HubPlugin hubPlugin) {
        super(hubPlugin.getMenus().getString("hub-selector.title"), 27);

        this.player = player;
        this.hubPlugin = hubPlugin;

        this.update();
    }

    @Override
    public void update() {
        if (this.hubPlugin.getMenus().getBoolean("server-selector.fill-stained-glass")) {
            while (this.inventory.firstEmpty() != -1) {
                this.inventory.setItem(this.inventory.firstEmpty(), new ItemBuilder(Material.STAINED_GLASS_PANE, 7).setDisplayName(" ").create());
            }
        }

        final AtomicInteger atomicInteger = new AtomicInteger(10);

        CorePlugin.getInstance().getServerManager().getNetworkServers().stream()
                .filter(Objects::nonNull)
                .filter(networkServer -> networkServer.getServerType().equals(NetworkServerType.HUB))
                .forEach(networkServer -> {
                    if (atomicInteger.get() <= 16) {
                        List<String> list = this.hubPlugin.getMenus().getStringList("hub-selector.item.lore");
                        list = PlaceholderAPI.setPlaceholders(player, list);

                        final List<String> finalList = new ArrayList<>();
                        final boolean thisServer = CorePlugin.getInstance().getServerName().equalsIgnoreCase(networkServer.getServerName());

                        list.forEach(s -> finalList.add(s
                                .replace("<online>", String.valueOf(networkServer.getOnlinePlayers()))
                                .replace("<max>", String.valueOf(networkServer.getMaxPlayerLimit()))
                                .replace("<status>", (thisServer ? "&cAlready Connected!" : networkServer.getServerStatus().getServerStatusFancyString()))
                                .replace("<joinstatus>", (thisServer ? "&c[Currently already connected]" : this.getByStatus(networkServer.getServerStatus())))
                        ));

                        this.inventory.setItem(atomicInteger.get(), new ItemBuilder(Material.valueOf(this.hubPlugin.getMenus().getString("hub-selector.item.item")))
                                .setDurability(this.hubPlugin.getMenus().getInt("hub-selector.item.durability"))
                                .addLore(finalList)
                                .setDisplayName(this.hubPlugin.getMenus().getString("hub-selector.item.display")
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
        final Inventory clickedInventory = event.getClickedInventory();
        final Inventory topInventory = event.getView().getTopInventory();

        if (!topInventory.equals(this.inventory)) return;
        if (topInventory.equals(clickedInventory)) {
            event.setCancelled(true);

            if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()) {
                final NetworkServer networkServer = NetworkServer.getByName(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));

                if (networkServer != null) {
                    if (!CorePlugin.getInstance().getServerName().equals(networkServer.getServerName())) {
                        this.player.sendMessage(Color.SECONDARY_COLOR + "You're now being connected to " + Color.MAIN_COLOR + networkServer.getServerName() + Color.SECONDARY_COLOR + "...");

                        BungeeUtil.sendToServer(this.player, networkServer.getServerName(), this.hubPlugin);
                    } else {
                        this.player.sendMessage(Color.translate("&cYou are already connected to " + networkServer.getServerName() + "!"));
                    }

                    this.player.closeInventory();
                }
            }
        }
    }

    public String getByStatus(NetworkServerStatusType statusType) {
        switch (statusType) {
            case ONLINE:
                return "&e[Click to connect]";
            case BOOTING:
                return "&6[Currently booting]";
            default:
                return "&c[Currently cannot join]";
        }
    }
}
