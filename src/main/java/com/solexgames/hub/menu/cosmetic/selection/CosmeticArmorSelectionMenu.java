package com.solexgames.hub.menu.cosmetic.selection;

import com.solexgames.core.CorePlugin;
import com.solexgames.core.player.PotPlayer;
import com.solexgames.core.util.Color;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.core.util.external.Button;
import com.solexgames.core.util.external.pagination.PaginatedMenu;
import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.cosmetic.Cosmetic;
import com.solexgames.hub.cosmetic.CosmeticType;
import com.solexgames.hub.cosmetic.impl.ArmorCosmetic;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CosmeticArmorSelectionMenu extends PaginatedMenu {

    private final HubPlugin plugin;

    public CosmeticArmorSelectionMenu(HubPlugin plugin) {
        super(18);

        this.plugin = plugin;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        final Map<Integer, Button> buttonMap = new HashMap<>();

        buttonMap.put(4, new ItemBuilder(Material.BED)
                .setDisplayName(ChatColor.GREEN + ChatColor.BOLD.toString() + "Reset Cosmetic")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .addLore(
                        "&7Reset your currently applied",
                        "&7cosmetic!",
                        "",
                        "&e[Click to reset cosmetic]"
                )
                .toButton((player1, clickType) -> {
                    player1.getInventory().setArmorContents(null);
                    player1.updateInventory();
                    player1.closeInventory();

                    player1.sendMessage(Color.SECONDARY_COLOR + "You're currently applied cosmetic has been reset.");
                })
        );

        return buttonMap;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        final Map<Integer, Button> buttonMap = new HashMap<>();
        final AtomicInteger atomicInteger = new AtomicInteger();
        final PotPlayer potPlayer = CorePlugin.getInstance().getPlayerManager().getPlayer(player);

        this.plugin.getCosmeticHandler().getCosmeticList().stream()
                .filter(cosmetic -> cosmetic.getCosmeticType().equals(CosmeticType.ARMOR))
                .map(cosmetic -> (ArmorCosmetic) cosmetic)
                .forEach(armorCosmetic -> buttonMap.put(atomicInteger.getAndIncrement(), armorCosmetic.getMenuItemBuilder()
                        .addLore(
                                "&7Required Rank: " + armorCosmetic.getRank().getColor() + armorCosmetic.getRank().getName(),
                                "",
                                "&e[Click to apply this cosmetic]"
                        )
                        .toButton((player1, clickType) -> {
                            if (!player.hasPermission(armorCosmetic.getPermission()) || !potPlayer.getActiveGrant().getRank().equals(armorCosmetic.getRank())) {
                                player.sendMessage(ChatColor.RED + "You don't have permission to apply this cosmetic!");
                                return;
                            }

                            armorCosmetic.applyTo(player, armorCosmetic.getRank());

                            player.closeInventory();
                            player.sendMessage(Color.SECONDARY_COLOR + "You've applied the " + ChatColor.BLUE + armorCosmetic.getName() + Color.SECONDARY_COLOR + " cosmetic!");
                        })));

        return buttonMap;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Armor Cosmetics";
    }
}
