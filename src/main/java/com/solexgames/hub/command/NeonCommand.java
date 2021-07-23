package com.solexgames.hub.command;

import com.mongodb.Block;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.solexgames.core.CorePlugin;
import com.solexgames.core.util.PlayerUtil;
import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.menu.SetupHubMenu;
import com.solexgames.lib.acf.BaseCommand;
import com.solexgames.lib.acf.CommandHelp;
import com.solexgames.lib.acf.annotation.*;
import com.solexgames.lib.commons.command.annotation.Service;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

/**
 * @author GrowlyX
 * @since 6/29/2021
 */

@Service
@CommandAlias("neon")
@RequiredArgsConstructor
@CommandPermission("neon.command.neon")
public class NeonCommand extends BaseCommand {

    private final HubPlugin plugin;

    @Default
    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("setup")
    @CommandPermission("neon.command.neon.subcommand.setup")
    public void onSetup(Player sender) {
        new SetupHubMenu(sender, this.plugin).open(sender);
    }

    @Subcommand("build")
    @CommandPermission("neon.command.neon.subcommand.build")
    public void onBuild(Player player) {
        final boolean toggleType = !this.plugin.getPermittedBuilders().contains(player);
        final String toggleMessage = toggleType ? ChatColor.GREEN + "You've been added to the Build Mode." : ChatColor.RED + "You've been removed from the Build Mode.";
        final String staffMessage = toggleType ? "enabled build mode" : "disabled build mode";
        final GameMode toggleGameMode = toggleType ? GameMode.CREATIVE : GameMode.ADVENTURE;

        if (toggleType) {
            this.plugin.getPermittedBuilders().add(player);
        } else {
            this.plugin.getPermittedBuilders().remove(player);
        }

        player.setGameMode(toggleGameMode);
        player.sendMessage(toggleMessage);

        PlayerUtil.sendAlert(player, staffMessage);
    }
}
