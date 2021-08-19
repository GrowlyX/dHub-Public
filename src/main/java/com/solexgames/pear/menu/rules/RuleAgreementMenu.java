package com.solexgames.pear.menu.rules;

import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.core.util.external.Button;
import com.solexgames.core.util.external.Menu;
import com.solexgames.pear.PearSpigotConstants;
import com.solexgames.pear.PearSpigotPlugin;
import com.solexgames.pear.player.impl.PersistentPearPlayer;
import lombok.RequiredArgsConstructor;
import me.lucko.helper.Schedulers;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author GrowlyX
 * @since 8/19/2021
 */

@RequiredArgsConstructor
public class RuleAgreementMenu extends Menu {

    private final PearSpigotPlugin plugin;

    @Override
    public String getTitle(Player player) {
        return "Welcome to " + PearSpigotConstants.SERVER_NAME;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttonMap = new HashMap<>();
        final PersistentPearPlayer pearPlayer = this.plugin.getPersistentPlayerCache().getByPlayer(player);

        buttonMap.put(2, new ItemBuilder(Material.CARPET)
                .setDisplayName(ChatColor.GREEN + "Yes")
                .setDurability(5)
                .toButton((player1, clickType) -> {
                    pearPlayer.setRuleAgreed(true);
                    pearPlayer.save();

                    player1.closeInventory();
                    player1.sendMessage(PearSpigotConstants.RULE_AGREED);
                })
        );

        buttonMap.put(4, new ItemBuilder(Material.BOOK)
                .setDisplayName(ChatColor.GOLD + PearSpigotConstants.SERVER_NAME + " Rules")
                .addLore(
                        ChatColor.GRAY + "Before you join one of",
                        ChatColor.GRAY + "our game servers, do you",
                        ChatColor.GRAY + "agree with our rules?",
                        "",
                        ChatColor.GRAY + "Network Guidelines:",
                        ChatColor.WHITE + PearSpigotConstants.RULES_PAGE,
                        "",
                        ChatColor.YELLOW + "[Click one of the buttons]"
                )
                .toButton()
        );

        buttonMap.put(6, new ItemBuilder(Material.CARPET)
                .setDisplayName(ChatColor.RED + "No")
                .setDurability(14)
                .toButton((player1, clickType) -> {
                    player1.kickPlayer(PearSpigotConstants.DID_NOT_RULE_AGREE);
                })
        );

        for (int i = 0; i <= 8; i++) {
            buttonMap.putIfAbsent(i, PearSpigotPlugin.GLASS);
        }

        return buttonMap;
    }

    @Override
    public void onClose(Player player) {
        final PersistentPearPlayer pearPlayer = this.plugin.getPersistentPlayerCache().getByPlayer(player);

        if (!pearPlayer.isRuleAgreed()) {
            Schedulers.sync().runLater(() -> {
                new RuleAgreementMenu(this.plugin).openMenu(player);
            }, 1L);
        }
    }
}
