package com.solexgames.hub.tablist;

import com.solexgames.core.CorePlugin;
import com.solexgames.core.util.Color;
import com.solexgames.hub.HubPlugin;
import io.github.nosequel.tab.shared.entry.TabElement;
import io.github.nosequel.tab.shared.entry.TabElementHandler;
import lombok.AllArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class TablistAdapter implements TabElementHandler {

    private final HubPlugin plugin;

    public TablistAdapter() {
        this.plugin = HubPlugin.getPlugin(HubPlugin.class);
    }

    @Override
    public TabElement getElement(Player player) {
        final TabElement element = new TabElement();

        final List<Player> finalList = Bukkit.getOnlinePlayers().stream()
                .sorted(Comparator.comparingInt(potPlayer -> +(CorePlugin.getInstance().getPlayerManager().getPlayer(potPlayer.getName()).getDisguiseRank() != null ? CorePlugin.getInstance().getPlayerManager().getPlayer(potPlayer.getName()).getDisguiseRank().getWeight() : CorePlugin.getInstance().getPlayerManager().getPlayer(potPlayer.getName()).getActiveGrant().getRank().getWeight())))
                .collect(Collectors.toList());

        return element;
    }
}
