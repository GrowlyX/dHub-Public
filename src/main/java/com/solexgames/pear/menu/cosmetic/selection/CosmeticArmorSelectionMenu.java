package com.solexgames.pear.menu.cosmetic.selection;

import com.solexgames.core.CorePlugin;
import com.solexgames.core.player.PotPlayer;
import com.solexgames.core.util.Color;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.core.util.external.Button;
import com.solexgames.core.util.external.pagination.PaginatedMenu;
import com.solexgames.pear.PearSpigotPlugin;
import com.solexgames.pear.cosmetic.type.CosmeticType;
import com.solexgames.pear.cosmetic.impl.ArmorCosmetic;
import com.solexgames.pear.menu.cosmetic.CosmeticMainMenu;
import com.solexgames.pear.player.impl.PersistentPearPlayer;
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

    private final PearSpigotPlugin plugin;

    public CosmeticArmorSelectionMenu(PearSpigotPlugin plugin) {
        super(18);

        this.plugin = plugin;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        final Map<Integer, Button> buttonMap = new HashMap<>();
        final PotPlayer potPlayer = CorePlugin.getInstance().getPlayerManager().getPlayer(player);

        buttonMap.put(2, new ArmorCosmeticButton(potPlayer, this.plugin.getCosmeticHandler().getChromaCosmetic()));
        buttonMap.put(4, new ItemBuilder(Material.NETHER_STAR)
                .setDisplayName(ChatColor.GREEN + "Reset Armor")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .addLore(
                        "&7Clear & reset your armor.",
                        "",
                        "&e[Click to reset armor]"
                ).toButton((player1, clickType) -> {
                    final ArmorCosmetic.ArmorUpdaterRunnable updaterRunnable = ArmorCosmetic.ARMOR_UPDATER_RUNNABLE_MAP.get(player1.getUniqueId());

                    if (updaterRunnable != null) {
                        updaterRunnable.cancel();
                        ArmorCosmetic.ARMOR_UPDATER_RUNNABLE_MAP.remove(player1.getUniqueId());
                    }

                    player1.getInventory().setArmorContents(null);
                    player1.updateInventory();
                    player1.closeInventory();

                    final PersistentPearPlayer pearPlayer = plugin.getPersistentPlayerCache().getByPlayer(player);

                    if (pearPlayer != null) {
                        pearPlayer.setArmor(null);
                        pearPlayer.save();
                    }

                    player1.sendMessage(ChatColor.RED + "You've unequipped your armor.");
                })
        );

        buttonMap.put(6, new ItemBuilder(Material.BED)
                .setDisplayName(ChatColor.RED + "Return to Main")
                .addLore(
                        "&7Return back to the",
                        "&7main cosmetic menu.",
                        " ",
                        "&e[Click to open main]"
                )
                .toButton((player1, clickType) -> {
                    new CosmeticMainMenu(this.plugin).openMenu(player1);
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
    public class ArmorCosmeticButton extends Button {

        private final PotPlayer potPlayer;
        private final ArmorCosmetic cosmetic;

        @Override
        public ItemStack getButtonItem(Player player) {
            return this.cosmetic.getMenuItemBuilder()
                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .addLore("&e[Click to apply]").create();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (this.cosmetic.getRank() != null && !this.potPlayer.getActiveGrant().getRank().equals(this.cosmetic.getRank())) {
                return;
            } else if (this.cosmetic.getRank() == null && !player.hasPermission(this.cosmetic.getPermission())) {
                return;
            }

            this.cosmetic.applyTo(player, this.cosmetic.getRank());

            final PersistentPearPlayer pearPlayer = plugin.getPersistentPlayerCache().getByPlayer(player);

            if (pearPlayer != null) {
                pearPlayer.setArmor(this.cosmetic.getRank() == null ? "Chroma" : this.cosmetic.getRank().getName());
                pearPlayer.save();
            }

            if (this.cosmetic.getRank() == null) {
                if (ArmorCosmetic.ARMOR_UPDATER_RUNNABLE_MAP.containsKey(player.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + "You already have chroma armor equipped.");
                    return;
                }

                final ArmorCosmetic.ArmorUpdaterRunnable updaterRunnable = new ArmorCosmetic.ArmorUpdaterRunnable(player, this.cosmetic, plugin);
                updaterRunnable.runTaskTimer(plugin, 0L, 2L);

                ArmorCosmetic.ARMOR_UPDATER_RUNNABLE_MAP.put(player.getUniqueId(), updaterRunnable);
            }

            player.closeInventory();
            player.sendMessage(Color.SECONDARY_COLOR + "You've applied the " + ChatColor.BLUE + (this.cosmetic.getRank() != null ? this.cosmetic.getRank().getColor() + this.cosmetic.getRank().getName() : ChatColor.GREEN + "Chroma") + Color.SECONDARY_COLOR + " cosmetic!");
        }
    }
}
