package com.solexgames.hub.tablist;

import com.solexgames.core.util.Color;
import com.solexgames.hub.HubPlugin;
import io.github.nosequel.tab.shared.entry.TabElement;
import io.github.nosequel.tab.shared.entry.TabElementHandler;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class TablistAdapter implements TabElementHandler {

    @Override
    public TabElement getElement(Player player) {
        final TabElement element = new TabElement();
        final ConfigurationSection section = HubPlugin.getInstance().getConfig().getConfigurationSection("tablist.slots");

        section.getKeys(false).forEach(s -> {
            int slot;
            String value = PlaceholderAPI.setPlaceholders(player, Color.translate(section.getString(s)));

            try {
                slot = Integer.parseInt(s);
            } catch (Exception ignored) {
                return;
            }

            element.add(slot, value);
        });

        return element;
    }
}
