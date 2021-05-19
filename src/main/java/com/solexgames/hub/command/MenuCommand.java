package com.solexgames.hub.command;

import com.solexgames.core.command.EBaseCommand;
import com.solexgames.core.util.Color;
import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.menu.submenu.SubMenu;
import lombok.RequiredArgsConstructor;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Name;
import me.vaperion.blade.command.annotation.Sender;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author GrowlyX
 * @since 5/1/2021
 */

public class MenuCommand {

    @Command(value = "menu")
    public void onMenu(@Sender Player player, @Name("menu") SubMenu subMenu) {
        subMenu.open(player);
    }
}
