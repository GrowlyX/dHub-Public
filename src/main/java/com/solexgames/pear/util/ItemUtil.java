package com.solexgames.pear.util;

import com.solexgames.core.util.external.Button;
import com.solexgames.pear.PearSpigotPlugin;
import com.solexgames.core.util.Color;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.pear.menu.action.MenuAction;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.AbstractMap;
import java.util.List;
import java.util.function.BiConsumer;

public final class ItemUtil {

    public static AbstractMap.SimpleEntry<Integer, ItemStack> getInventoryItemFromConfig(String section, PearSpigotPlugin plugin) {
        final ConfigurationSection configurationSection = plugin.getConfig().getConfigurationSection(section);

        if (configurationSection == null) {
            return null;
        }

        final ItemBuilder itemBuilder = new ItemBuilder(Material.valueOf(configurationSection.getString("type")));

        if (configurationSection.getString("display") != null) {
            itemBuilder.setDisplayName(configurationSection.getString("display"));
        }
        itemBuilder.setDurability(configurationSection.getInt("durability"));
        itemBuilder.setUnbreakable(configurationSection.getBoolean("unbreakable"));

        if (configurationSection.getBoolean("lore.enabled")) {
            itemBuilder.addLore(configurationSection.getStringList("lore.strings"));
        }

        return new AbstractMap.SimpleEntry<>(configurationSection.getInt("slot"), itemBuilder.create());
    }

    public static Button getMenuItem(String section, Player player, PearSpigotPlugin plugin, BiConsumer<Player, ClickType> consumer) {
        final ConfigurationSection configurationSection = plugin.getMenus().getConfiguration().getConfigurationSection(section);
        final ItemBuilder itemBuilder = new ItemBuilder(Material.valueOf(configurationSection.getString("type")));

        if (configurationSection.getString("display") != null) {
            itemBuilder.setDisplayName(configurationSection.getString("display"));
        }
        itemBuilder.setDurability(configurationSection.getInt("durability"));
        itemBuilder.setUnbreakable(configurationSection.getBoolean("unbreakable"));

        List<String> lore = configurationSection.getStringList("lore.strings");
        lore = PlaceholderAPI.setPlaceholders(player, lore);

        if (configurationSection.getBoolean("lore.enabled")) {
            itemBuilder.addLore(Color.translate(lore));
        }

        return itemBuilder.toButton(consumer);
    }

    public static String getValueFromConfig(String section, PearSpigotPlugin plugin) {
        final ConfigurationSection configurationSection = plugin.getMenus().getConfiguration().getConfigurationSection(section);
        return configurationSection.getString("action.value");
    }

    public static MenuAction.Type getActionFromConfig(String section, PearSpigotPlugin plugin) {
        final ConfigurationSection configurationSection = plugin.getMenus().getConfiguration().getConfigurationSection(section);
        return MenuAction.Type.valueOf(configurationSection.getString("action.type"));
    }

    public static boolean isEnabledAction(String section, PearSpigotPlugin plugin) {
        final ConfigurationSection configurationSection = plugin.getMenus().getConfiguration().getConfigurationSection(section);
        return configurationSection.getBoolean("action.enabled");
    }
}
