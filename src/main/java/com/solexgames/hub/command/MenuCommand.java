package com.solexgames.hub.command;

import com.solexgames.core.util.Color;
import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.menu.SetupHubMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            player.sendMessage(Color.SECONDARY_COLOR + "Usage: " + Color.MAIN_COLOR + "/menu " + ChatColor.WHITE + "<name>.");
        }
        if (args.length == 1) {
            this.plugin.getSubMenuHandler().openSubMenu(args[0], player);
        }

        return false;
    }
}
