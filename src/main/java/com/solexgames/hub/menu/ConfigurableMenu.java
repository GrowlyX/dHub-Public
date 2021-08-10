package com.solexgames.hub.menu;

import com.solexgames.core.util.external.Button;
import com.solexgames.core.util.external.Menu;
import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.menu.action.MenuAction;
import com.solexgames.hub.util.ItemUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ConfigurableMenu extends Menu {

    private final String root;
    private final HubPlugin plugin;

    @Override
    public String getTitle(Player player) {
        return this.plugin.getMenus().getString(this.root + ".title");
    }

    @Override
    public int getSize() {
        return this.plugin.getMenus().getInt(this.root + ".size");
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttonMap = new HashMap<>();

        final ConfigurationSection configurationSection = this.plugin.getMenus().getConfiguration().getConfigurationSection(this.root + ".items");
        configurationSection.getKeys(false).forEach(itemSection -> {
            try {
                final int slot = Integer.parseInt(itemSection);

                buttonMap.put(slot,
                        ItemUtil.getMenuItem(
                                this.root + ".items." + itemSection,
                                player, this.plugin,
                                (player1, clickType) -> {
                                    final MenuAction.Type action = ItemUtil.getActionFromConfig(this.root + ".items." + slot, this.plugin);
                                    final String value = ItemUtil.getValueFromConfig(this.root + ".items." + slot, this.plugin);

                                    if (action != null && value != null && ItemUtil.isEnabledAction(this.root + ".items." + slot, this.plugin)) {
                                        MenuAction.completeAction(action, value, player1, this.plugin);
                                    }
                                }
                        )
                );
            } catch (Exception ignored) {
            }
        });

        if (this.plugin.getMenus().getBoolean(this.root + ".fill-stained-glass")) {
            for (int i = 0; i <= this.getSize(); i++) {
                buttonMap.putIfAbsent(i, HubPlugin.GLASS);
            }
        }

        return buttonMap;
    }
}
