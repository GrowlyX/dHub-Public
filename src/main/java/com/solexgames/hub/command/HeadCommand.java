package com.solexgames.hub.command;

import com.solexgames.core.command.EBaseCommand;
import com.solexgames.core.util.Color;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.hub.HubPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GrowlyX
 * @since 5/1/2021
 */

@RequiredArgsConstructor
public class HeadCommand extends EBaseCommand {

    private final HubPlugin plugin;

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("die");
            return false;
        }

        final Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(Color.SECONDARY_COLOR + "Usage: " + Color.MAIN_COLOR + "/head " + ChatColor.WHITE + "<name>.");
        }
        if (args.length == 1) {
            player.setItemInHand(new ItemBuilder(Material.SKULL_ITEM)
                    .setDurability(3)
                    .setDisplayName(Color.MAIN_COLOR + args[0] + "'s " + Color.SECONDARY_COLOR + "Head")
                    .setOwner(args[0])
                    .create());
        }

        return false;
    }

    @Override
    public List<String> getAliases() {
        return new ArrayList<>();
    }
}
