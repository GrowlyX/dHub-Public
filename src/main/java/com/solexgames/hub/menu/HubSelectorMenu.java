package com.solexgames.hub.menu;

import com.solexgames.core.CorePlugin;
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
        if (HubPlugin.getPlugin(HubPlugin.class).getMenus().getBoolean("server-selector.fill-stained-glass")) {
            while (this.inventory.firstEmpty() != -1) {
                this.inventory.setItem(this.inventory.firstEmpty(), new ItemBuilder(Material.STAINED_GLASS_PANE, 7).setDisplayName(" ").create());
            }
        }

        final AtomicInteger atomicInteger = new AtomicInteger(10);
        CorePlugin.getInstance().getServerManager().getNetworkServers().stream()
                .filter(Objects::nonNull)
                .filter(networkServer -> networkServer.getServerType().equals(NetworkServerType.HUB))
                .forEach(networkServer -> {
                    if (atomicInteger.get() < 16) {
                        List<String> list = this.hubPlugin.getMenus().getStringList("hub-selector.item.lore");
                        list = PlaceholderAPI.setPlaceholders(player, list);

                        List<String> finalList = new ArrayList<>();
                        list.forEach(s -> finalList.add(s
                                .replace("<online>", String.valueOf(networkServer.getOnlinePlayers()))
                                .replace("<max>", String.valueOf(networkServer.getMaxPlayerLimit()))
                                .replace("<status>", (CorePlugin.getInstance().getServerName().equalsIgnoreCase(networkServer.getServerName()) ? "&cAlready Connected!" : networkServer.getServerStatus().getServerStatusFancyString()))
                                .replace("<joinstatus>", (CorePlugin.getInstance().getServerName().equalsIgnoreCase(networkServer.getServerName()) ? "&c[Currently connected]" : networkServer.getServerStatus().getServerStatusFancyString()))
                        ));

                        this.inventory.setItem(atomicInteger.get(), new ItemBuilder(Material.valueOf(HubPlugin.getPlugin(HubPlugin.class).getMenus().getString("hub-selector.item.item")))
                                .setDurability(HubPlugin.getPlugin(HubPlugin.class).getMenus().getInt("hub-selector.item.durability"))
                                .addLore(finalList)
                                .setDisplayName(HubPlugin.getPlugin(HubPlugin.class).getMenus().getString("hub-selector.item.display")
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
                        this.player.sendMessage(Color.translate("&aSending you to " + networkServer.getServerName() + "..."));

                        BungeeUtil.sendToServer(this.player, networkServer.getServerName(), HubPlugin.getPlugin(HubPlugin.class));
                    } else {
                        this.player.sendMessage(Color.translate("&cYou are already on " + networkServer.getServerName() + "!"));
                    }
                    this.player.closeInventory();
                }
            }
        }
    }

    private String getStatusJoin(NetworkServer networkServer) {
        switch (networkServer.getServerStatus()) {
            case WHITELISTED:
                return ChatColor.YELLOW + "[Currently whitelisted]";
            case ONLINE:
                return ChatColor.YELLOW + "[Click to connect to " + networkServer.getServerName() + "]";
            case BOOTING:
                return ChatColor.GOLD + "[Currently booting]";
            default:
                return ChatColor.RED + "[Currently offline]";
        }
    }
}
