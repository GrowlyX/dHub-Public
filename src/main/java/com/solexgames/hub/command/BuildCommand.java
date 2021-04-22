package com.solexgames.hub.command;

import com.solexgames.hub.HubPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class BuildCommand implements CommandExecutor {

    private final HubPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("die");
            return false;
        }

        final Player player = (Player) sender;

        if (!player.hasPermission("neon.command.build")) {
            player.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command.");
            return false;
        }

        final boolean toggleType = !this.plugin.getPermittedBuilders().contains(player);
        final String toggleMessage = toggleType ? ChatColor.GREEN + "You've been added to the Build Mode." : ChatColor.RED + "You've been removed from the Build Mode.";

        if (toggleType) {
            this.plugin.getPermittedBuilders().add(player);
        } else {
            this.plugin.getPermittedBuilders().remove(player);
        }

        player.sendMessage(toggleMessage);

        return false;
    }
}
