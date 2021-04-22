package com.solexgames.hub.command;

import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.menu.SetupHubMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class NeonCommand implements CommandExecutor {

    private final HubPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("die");
            return false;
        }

        final Player player = (Player) sender;

        if (!player.hasPermission(this.plugin.getHubManager().getSetupPermission())) {
            player.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command.");
            return false;
        }

        new SetupHubMenu(player, this.plugin).open(player);

        return false;
    }
}
