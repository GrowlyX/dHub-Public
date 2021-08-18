package com.solexgames.pear.processor;

import com.solexgames.core.server.NetworkServer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * @author GrowlyX
 * @since 8/18/2021
 */

public class PearServerPlaceholderProcessor extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "pear";
    }

    @Override
    public @NotNull String getAuthor() {
        return "solexgames";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.0.1";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        final String[] arguments = params.split("-");
        final NetworkServer networkServer = NetworkServer.getByName(arguments[1]);

        switch (arguments[0].toLowerCase()) {
            case "status":
                if (networkServer == null) {
                    return ChatColor.RED + "Server Offline";
                } else {
                    switch (networkServer.getServerStatus()) {
                        case ONLINE:
                            return ChatColor.GREEN + "Server online";
                        case WHITELISTED:
                            return ChatColor.YELLOW + "Server whitelisted";
                        default:
                            return ChatColor.RED + "Server offline";
                    }
                }
            case "queueable":
                if (networkServer == null) {
                    return ChatColor.RED + "[Currently cannot join]";
                } else {
                    switch (networkServer.getServerStatus()) {
                        case ONLINE:
                            return ChatColor.YELLOW + "[Click to join queue]";
                        case WHITELISTED:
                            return ChatColor.YELLOW + "[Server whitelisted]";
                        default:
                            return ChatColor.RED + "[Currently cannot join]";
                    }
                }
        }

        return "";
    }
}
