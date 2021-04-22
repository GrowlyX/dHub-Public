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
        final ConfigurationSection section = this.plugin.getSettings().getConfiguration().getConfigurationSection("tablist.slots");

        section.getKeys(false).forEach(s -> {
            int slot;

            try {
                slot = Integer.parseInt(s);
            } catch (Exception ignored) {
                return;
            }

            final String value = PlaceholderAPI.setPlaceholders(player, Color.translate(section.getString(s)));

            element.add(slot, value);
        });

        return element;
    }
}
