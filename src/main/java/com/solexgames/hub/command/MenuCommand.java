package com.solexgames.hub.command;

import com.solexgames.core.util.Color;
import com.solexgames.hub.HubPlugin;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MenuCommand implements CommandExecutor {

    private final HubPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("die");
            return false;
        }

        final Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(Color.SECONDARY_COLOR + "Usage: " + Color.MAIN_COLOR + "/menu " + ChatColor.WHITE + "<name|list>.");
        }
        if (args.length == 1) {
            final String targetMenu = args[0].toLowerCase();

            if (targetMenu.equals("list")) {
                final String listString = this.plugin.getSubMenuHandler().getMenuPathList().stream()
                        .map(StringUtils::capitalize)
                        .collect(Collectors.joining(ChatColor.GRAY + ", " + Color.MAIN_COLOR))
                        + ChatColor.GRAY + ".";

                player.sendMessage(Color.SECONDARY_COLOR + "Available menus: " + Color.MAIN_COLOR + listString);
            } else {
                this.plugin.getSubMenuHandler().openSubMenu(args[0], player);
            }
        }

        return false;
    }
}
