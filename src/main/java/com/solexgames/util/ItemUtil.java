package com.solexgames.util;

import com.solexgames.HubPlugin;
import com.solexgames.core.util.Color;
import com.solexgames.core.util.builder.ItemBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.AbstractMap;
import java.util.List;

public final class ItemUtil {

    public static AbstractMap.SimpleEntry<Integer, ItemStack> getInventoryItemFromConfig(String section) {
        ConfigurationSection configurationSection = HubPlugin.getInstance().getSettings().getConfiguration().getConfigurationSection(section);
        ItemBuilder itemBuilder = new ItemBuilder(Material.valueOf(configurationSection.getString("type")));

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

    public static ItemStack getItemFromConfig(String section) {
        ConfigurationSection configurationSection = HubPlugin.getInstance().getSettings().getConfiguration().getConfigurationSection(section);
        ItemBuilder itemBuilder = new ItemBuilder(Material.valueOf(configurationSection.getString("type")));

        if (configurationSection.getString("display") != null) {
            itemBuilder.setDisplayName(configurationSection.getString("display"));
        }
        itemBuilder.setDurability(configurationSection.getInt("durability"));
        itemBuilder.setUnbreakable(configurationSection.getBoolean("unbreakable"));

        if (configurationSection.getBoolean("lore.enabled")) {
            itemBuilder.addLore(configurationSection.getStringList("lore.strings"));
        }

        return itemBuilder.create();
    }

    public static ItemStack getMenuItem(String section, Player player) {
        ConfigurationSection configurationSection = HubPlugin.getInstance().getMenus().getConfiguration().getConfigurationSection(section);
        ItemBuilder itemBuilder = new ItemBuilder(Material.valueOf(configurationSection.getString("type")));

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

        return itemBuilder.create();
    }

    public static String getActionFromConfig(String section) {
        ConfigurationSection configurationSection = HubPlugin.getInstance().getMenus().getConfiguration().getConfigurationSection(section);
        return configurationSection.getString("action.string");
    }
}
