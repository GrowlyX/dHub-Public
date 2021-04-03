package com.solexgames.command;

import com.solexgames.HubPlugin;
import com.solexgames.core.command.BaseCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand extends BaseCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("die");
            return false;
        }

        final Player player = (Player) sender;
        if (!player.hasPermission("dhub.permissions.build")) {
            player.sendMessage(ChatColor.RED + "No permission!");
        }

        if (HubPlugin.getInstance().getPermittedBuilders().contains(player.getName())) {
            HubPlugin.getInstance().getPermittedBuilders().remove(player.getName());
            player.sendMessage(ChatColor.RED + "You've been removed from the Build Mode.");
        } else {
            HubPlugin.getInstance().getPermittedBuilders().add(player.getName());
            player.sendMessage(ChatColor.GREEN + "You've been added to the Build Mode.");
        }

        return false;
    }
}
