package com.solexgames.hub.tablist;

import com.solexgames.core.util.Color;
import com.solexgames.hub.HubPlugin;
import io.github.nosequel.tab.shared.entry.TabElement;
import io.github.nosequel.tab.shared.entry.TabElementHandler;
import lombok.AllArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class TablistAdapter implements TabElementHandler {

    private final HubPlugin plugin;

    @Override
    public TabElement getElement(Player player) {
        final TabElement element = new TabElement();
        final ConfigurationSection section = this.plugin.getSettings().getConfiguration().getConfigurationSection("tablist.left");

        section.getKeys(false).forEach(s -> {
            final int slot = Integer.parseInt(s);
            final String value = PlaceholderAPI.setPlaceholders(player, Color.translate(section.getString(s)));

            element.add(0, slot, value);
        });

        final ConfigurationSection center = this.plugin.getSettings().getConfiguration().getConfigurationSection("tablist.center");

        center.getKeys(false).forEach(s -> {
            final int slot = Integer.parseInt(s);
            final String value = PlaceholderAPI.setPlaceholders(player, Color.translate(center.getString(s)));

            element.add(1, slot, value);
        });

        final ConfigurationSection right = this.plugin.getSettings().getConfiguration().getConfigurationSection("tablist.right");

        right.getKeys(false).forEach(s -> {
            final int slot = Integer.parseInt(s);
            final String value = PlaceholderAPI.setPlaceholders(player, Color.translate(right.getString(s)));

            element.add(2, slot, value);
        });

        return element;
    }
}
