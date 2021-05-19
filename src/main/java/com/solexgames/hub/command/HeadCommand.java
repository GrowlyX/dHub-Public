package com.solexgames.hub.command;

import com.solexgames.core.util.Color;
import com.solexgames.core.util.builder.ItemBuilder;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Name;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 5/1/2021
 */

public class HeadCommand {

    @Command(value = "head")
    @Permission(value = "neon.command.head")
    public void onHead(@Sender Player player, @Name("target") String target) {
        player.setItemInHand(new ItemBuilder(Material.SKULL_ITEM)
                .setDurability(3)
                .setDisplayName(Color.MAIN_COLOR + target + "'s " + Color.SECONDARY_COLOR + "Head")
                .setOwner(target)
                .create());

        player.sendMessage(Color.SECONDARY_COLOR + "You've received " + Color.MAIN_COLOR + target + Color.SECONDARY_COLOR + "'s head.");
    }
}
