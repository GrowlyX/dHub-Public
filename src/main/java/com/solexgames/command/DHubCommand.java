package com.solexgames.command;

import com.solexgames.core.command.BaseCommand;
import com.solexgames.core.util.Color;
import com.solexgames.menu.SetupHubMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DHubCommand extends BaseCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("die");
            return false;
        }

        Player player = (Player) sender;
        if (player.hasPermission("dhub.permissions.setup")) {
            new SetupHubMenu(player).open(player);
        } else {
            player.sendMessage(Color.translate("&cNo permission!"));
        }
        return false;
    }
}
