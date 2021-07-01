package com.solexgames.hub.tab;

import com.solexgames.core.CorePlugin;
import com.solexgames.core.enums.NetworkServerType;
import com.solexgames.core.player.PotPlayer;
import com.solexgames.core.server.NetworkServer;
import io.github.nosequel.tab.shared.entry.TabElement;
import io.github.nosequel.tab.shared.entry.TabElementHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author GrowlyX
 * @since 6/30/2021
 */

public class NeonTabProcessor implements TabElementHandler {

    private final Map<String, NetworkServer> serverMap = new HashMap<>();

    @Override
    public TabElement getElement(Player player) {
        final TabElement element = new TabElement();
        final PotPlayer potPlayer = CorePlugin.getInstance().getPlayerManager().getPlayer(player);

        element.add(0, 3, ChatColor.GOLD + ChatColor.BOLD.toString() + "Store");
        element.add(0, 4, ChatColor.GRAY + "store.pvp.bar");
        element.add(1, 0, ChatColor.GOLD + ChatColor.BOLD.toString() + "PvPBar Network");

        element.add(1, 3, ChatColor.GOLD + ChatColor.BOLD.toString() + "Player Info");
        element.add(1, 4, ChatColor.GRAY + "Rank: " + potPlayer.getActiveGrant().getRank().getColor() + potPlayer.getActiveGrant().getRank().getName());
        element.add(1, 5, ChatColor.GRAY + "Experience: " + ChatColor.WHITE + potPlayer.getExperience());

        element.add(2, 3, ChatColor.GOLD + ChatColor.BOLD.toString() + "Discord");
        element.add(2, 4, ChatColor.GRAY + "discord.pvp.bar");

        element.add(0, 10, ChatColor.GOLD + ChatColor.BOLD.toString() + "NA Practice");
        element.add(0, 11, ChatColor.GREEN + this.getStatusOf("na-practice"));
        element.add(0, 12, ChatColor.GRAY + "(" + this.getPlayerCountOf("na-practice") + "/350)");

        element.add(0, 14, ChatColor.GOLD + ChatColor.BOLD.toString() + "EU Practice");
        element.add(0, 15, ChatColor.RED + this.getStatusOf("eu-practice"));
        element.add(0, 16, ChatColor.GRAY + "(" + this.getPlayerCountOf("eu-practice") + "/350)");

        element.add(1, 14, ChatColor.GOLD + ChatColor.BOLD.toString() + "Hub");
        element.add(1, 15, ChatColor.RED + this.getStatusOfServersWith(NetworkServerType.HUB));
        element.add(1, 16, ChatColor.GRAY + "(" + this.getAllOnlineOn(NetworkServerType.HUB) + "/" + this.getMaxPlayerCountOf(NetworkServerType.HUB) + ")");

        element.add(1, 10, ChatColor.GOLD + ChatColor.BOLD.toString() + "HCF");
        element.add(1, 11, ChatColor.RED + this.getStatusOf("na-hcf"));
        element.add(1, 12, ChatColor.GRAY + "(" + this.getPlayerCountOf("na-hcf") + "/350)");

        element.add(2, 10, ChatColor.GOLD + ChatColor.BOLD.toString() + "UHC Meetup");
        element.add(2, 11, ChatColor.GREEN + this.getStatusOfServersWith(NetworkServerType.MEETUP));
        element.add(2, 12, ChatColor.GRAY + "(" + this.getAllOnlineOn(NetworkServerType.MEETUP) + "/" + this.getMaxPlayerCountOf(NetworkServerType.MEETUP) + ")");

        element.add(2, 14, ChatColor.GOLD + ChatColor.BOLD.toString() + "SkyWars");
        element.add(2, 15, ChatColor.GREEN + this.getStatusOfServersWith(NetworkServerType.SKYWARS));
        element.add(2, 16, ChatColor.GRAY + "(" + this.getAllOnlineOn(NetworkServerType.SKYWARS) + "/" + this.getMaxPlayerCountOf(NetworkServerType.SKYWARS) + ")");

        return element;
    }

    public String getStatusOfServersWith(NetworkServerType serverType) {
        final NetworkServer server = CorePlugin.getInstance().getServerManager().getNetworkServers().stream()
                .filter(networkServer -> networkServer.getServerType().equals(serverType))
                .findFirst().orElse(null);

        return this.getShortStatus(server);
    }

    public String getStatusOf(String serverName) {
        final NetworkServer server = this.serverMap.get(serverName) == null ? CorePlugin.getInstance().getServerManager().getNetworkServers().stream()
                .filter(networkServer -> networkServer.getServerName().equalsIgnoreCase(serverName))
                .findFirst().orElse(null) : this.serverMap.get(serverName);

        this.serverMap.putIfAbsent(serverName, server);

        return this.getShortStatus(server);
    }

    public String getShortStatus(NetworkServer server) {
        if (server == null) {
            return ChatColor.RED + "Offline";
        }

        switch (server.getServerStatus()) {
            case BOOTING:
                return ChatColor.GOLD + "Booting";
            case WHITELISTED:
                return ChatColor.YELLOW + "Whitelisted";
            case ONLINE:
                return ChatColor.GREEN + "Online";
            default:
                return ChatColor.RED + "Offline";
        }
    }

    public int getPlayerCountOf(String serverName) {
        final NetworkServer server = this.serverMap.get(serverName) == null ? CorePlugin.getInstance().getServerManager().getNetworkServers().stream()
                .filter(networkServer -> networkServer.getServerName().equalsIgnoreCase(serverName))
                .findFirst().orElse(null) : this.serverMap.get(serverName);

        this.serverMap.putIfAbsent(serverName, server);

        return server != null ? server.getOnlinePlayers() : 0;
    }

    public int getAllOnlineOn(NetworkServerType serverType) {
        return CorePlugin.getInstance().getServerManager().getNetworkServers().stream()
                .filter(networkServer -> networkServer.getServerType().equals(serverType))
                .mapToInt(NetworkServer::getOnlinePlayers).sum();
    }

    public int getMaxPlayerCountOf(NetworkServerType serverType) {
        return CorePlugin.getInstance().getServerManager().getNetworkServers().stream()
                .filter(networkServer -> networkServer.getServerType().equals(serverType))
                .mapToInt(NetworkServer::getMaxPlayerLimit).sum();
    }
}
