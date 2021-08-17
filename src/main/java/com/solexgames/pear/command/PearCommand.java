package com.solexgames.pear.command;

import com.solexgames.core.util.PlayerUtil;
import com.solexgames.pear.PearSpigotPlugin;
import com.solexgames.pear.menu.SetupHubMenu;
import com.solexgames.lib.acf.BaseCommand;
import com.solexgames.lib.acf.CommandHelp;
import com.solexgames.lib.acf.annotation.*;
import com.solexgames.lib.commons.command.annotation.Service;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 6/29/2021
 */

@Service
@CommandAlias("pear")
@RequiredArgsConstructor
@CommandPermission("pear.command.pear")
public class PearCommand extends BaseCommand {

    private final PearSpigotPlugin plugin;

    @Default
    @HelpCommand
    @Syntax("[page]")
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("setup")
    @Description("Launch the hub setup panel.")
    @CommandPermission("pear.command.pear.subcommand.setup")
    public void onSetup(Player sender) {
        new SetupHubMenu(sender, this.plugin).open(sender);
    }

    @Subcommand("build")
    @Description("Enter/exit hub build mode.")
    @CommandPermission("pear.command.pear.subcommand.build")
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
