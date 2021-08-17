package com.solexgames.pear.menu;

import com.solexgames.core.CorePlugin;
import com.solexgames.core.enums.NetworkServerStatusType;
import com.solexgames.core.enums.NetworkServerType;
import com.solexgames.core.util.BungeeUtil;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.core.util.external.Button;
import com.solexgames.core.util.external.Menu;
import com.solexgames.pear.PearSpigotPlugin;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class HubSelectorMenu extends Menu {

    private final PearSpigotPlugin plugin;

    @Override
    public String getTitle(Player player) {
        String root = "hub-selector";
        return this.plugin.getMenus().getString(root + ".title");
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    @Override
    public boolean isFillBorders() {
        return true;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttonMap = new HashMap<>();
        final AtomicInteger atomicInteger = new AtomicInteger(10);

        CorePlugin.getInstance().getServerManager().getNetworkServers().stream()
                .filter(Objects::nonNull)
                .filter(networkServer -> networkServer.getServerType().equals(NetworkServerType.HUB) && !networkServer.getServerName().contains("dev"))
                .forEach(networkServer -> {
                    if (atomicInteger.get() <= 16) {
                        List<String> list = this.plugin.getMenus().getStringList("hub-selector.item.lore");
                        list = PlaceholderAPI.setPlaceholders(player, list);

                        final List<String> finalList = new ArrayList<>();
                        final boolean thisServer = CorePlugin.getInstance().getServerName().equalsIgnoreCase(networkServer.getServerName());

                        list.forEach(s -> finalList.add(s
                                .replace("<online>", String.valueOf(networkServer.getOnlinePlayers()))
                                .replace("<max>", String.valueOf(networkServer.getMaxPlayerLimit()))
                                .replace("<status>", networkServer.getServerStatus().getServerStatusFancyString())
                                .replace("<joinstatus>", (thisServer ? "&c[Already connected]" : this.getByStatus(networkServer.getServerStatus())))
                        ));

                        buttonMap.put(atomicInteger.getAndIncrement(), new ItemBuilder(Material.valueOf(this.plugin.getMenus().getString("hub-selector.item.item")))
                                .setDurability(this.plugin.getMenus().getInt("hub-selector.item.durability"))
                                .addLore(finalList)
                                .setDisplayName(this.plugin.getMenus().getString("hub-selector.item.display")
                                        .replace("<server-name>", networkServer.getServerName())
                                )
                                .toButton((player1, clickType) -> {
                                    if (!CorePlugin.getInstance().getServerName().equals(networkServer.getServerName())) {
                                        BungeeUtil.sendToServer(player, networkServer.getServerName(), this.plugin);
                                    } else {
                                        player.sendMessage(ChatColor.RED + "You're already connected to " + ChatColor.YELLOW + networkServer.getServerName() + ChatColor.RED + ".");
                                    }
                                })
                        );
                    }
                });

        return buttonMap;
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
