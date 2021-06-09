package com.solexgames.hub.menu.cosmetic.selection;

import com.solexgames.core.CorePlugin;
import com.solexgames.core.player.PotPlayer;
import com.solexgames.core.player.ranks.Rank;
import com.solexgames.core.util.Color;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.core.util.external.Button;
import com.solexgames.core.util.external.pagination.PaginatedMenu;
import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.cosmetic.CosmeticType;
import com.solexgames.hub.cosmetic.impl.ArmorCosmetic;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;
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

        buttonMap.put(3, new ArmorCosmeticButton(null, this.plugin.getCosmeticHandler().getChromaCosmetic()));
        buttonMap.put(5, new ItemBuilder(Material.BED)
                .setDisplayName(ChatColor.GREEN + ChatColor.BOLD.toString() + "Reset Cosmetic")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .addLore(
                        "&7Reset your currently applied",
                        "&7cosmetic!",
                        "",
                        "&e[Click to reset cosmetic]"
                ).toButton((player1, clickType) -> {
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

        this.plugin.getCosmeticHandler().getArmorCosmeticMap().values().stream()
                .filter(cosmetic -> cosmetic.getCosmeticType().equals(CosmeticType.ARMOR) && cosmetic.getRank() != null && !cosmetic.getRank().isHidden())
                .sorted(Comparator.comparingInt(cosmetic -> -cosmetic.getRank().getWeight()))
                .forEach(armorCosmetic -> buttonMap.put(atomicInteger.getAndIncrement(), new ArmorCosmeticButton(potPlayer, armorCosmetic)));

        return buttonMap;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Cosmetics » Armor";
    }

    @AllArgsConstructor
    public static class ArmorCosmeticButton extends Button {

        private final PotPlayer potPlayer;
        private final ArmorCosmetic cosmetic;

        @Override
        public ItemStack getButtonItem(Player player) {
            final Rank rank = this.potPlayer.getActiveGrant().getRank();
            final List<String> lore = new ArrayList<>();
            final boolean canUse = rank != null ? rank.equals(this.cosmetic.getRank()) : player.hasPermission(this.cosmetic.getPermission());

            lore.add(" ");
            lore.add(canUse ? ChatColor.GRAY + "You have access to this armor." : Color.SECONDARY_COLOR + "You can buy this armor at");

            if (!canUse) {
                lore.add(Color.MAIN_COLOR + CorePlugin.getInstance().getServerManager().getNetwork().getStoreLink() + Color.SECONDARY_COLOR + ".");
            }

            lore.add(" ");
            lore.add(canUse ? "&e[Click to apply this armor]" : "&c[You cannot apply this armor]");

            return this.cosmetic.getMenuItemBuilder()
                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .addLore(lore).create();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (this.cosmetic.getRank() != null && !this.potPlayer.getActiveGrant().getRank().equals(this.cosmetic.getRank())) {
                return;
            } else if (this.cosmetic.getRank() == null && !player.hasPermission(this.cosmetic.getPermission())) {
                return;
            }

            this.cosmetic.applyTo(player, this.cosmetic.getRank());

            player.closeInventory();
            player.sendMessage(Color.SECONDARY_COLOR + "You've applied the " + ChatColor.BLUE + this.cosmetic.getRank().getColor() + this.cosmetic.getRank().getName() + Color.SECONDARY_COLOR + " cosmetic!");
        }
    }
}
