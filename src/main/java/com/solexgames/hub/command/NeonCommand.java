package com.solexgames.hub.command;

import com.solexgames.core.command.EBaseCommand;
import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.menu.SetupHubMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class NeonCommand extends EBaseCommand {

    private final HubPlugin plugin;

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("die");
            return false;
        }

        final Player player = (Player) sender;

        if (!player.hasPermission("neon.command.setup")) {
            player.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command.");
            return false;
        }

        if (args.length == 0) {
            this.getHelpMessage(1, player,
                    "/neon setup",
                    "/neon build <boolean>",
                    "/neon captcha <boolean>"
            );
        }
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "setup":
                    new SetupHubMenu(player, this.plugin).open(player);
                    break;
                case "build":
                    final boolean toggleType = !this.plugin.getPermittedBuilders().contains(player);
                    final String toggleMessage = toggleType ? ChatColor.GREEN + "You've been added to the Build Mode." : ChatColor.RED + "You've been removed from the Build Mode.";
                    final GameMode toggleGameMode = toggleType ? GameMode.CREATIVE : GameMode.ADVENTURE;

                    if (toggleType) {
                        this.plugin.getPermittedBuilders().add(player);
                    } else {
                        this.plugin.getPermittedBuilders().remove(player);
                    }

                    player.setGameMode(toggleGameMode);
                    player.sendMessage(toggleMessage);
                    break;
                default:
                    this.getHelpMessage(1, player,
                            "/neon setup",
                            "/neon build <boolean>",
                            "/neon captcha <boolean>"
                    );
            }
        }

        return false;
    }

    @Override
    public List<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public boolean isHidden() {
        return false;
    }
}
