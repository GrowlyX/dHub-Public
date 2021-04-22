package com.solexgames.hub.scoreboard;

import com.solexgames.hub.HubPlugin;
import com.solexgames.core.board.ScoreBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardAdapter extends ScoreBoard {

    private final Player player;
    private final HubPlugin plugin;

    public ScoreboardAdapter(Player player, HubPlugin plugin) {
        super(player);

        this.player = player;
        this.plugin = plugin;
    }

    @Override
    public List<String> getLines() {
        final boolean isInQueue = this.plugin.getQueueImpl().isInQueue(this.player);

        if (isInQueue) {
            final List<String> scoreboardLines = this.plugin.getHubHandler().getScoreboardLinesQueued();
            final List<String> finalLines = new ArrayList<>();

            for (String string : scoreboardLines) {
                finalLines.add(string
                        .replace("<queue_name>", this.plugin.getQueueImpl().getQueueName(this.player))
                        .replace("<queue_position>", String.valueOf(this.plugin.getQueueImpl().getQueuePosition(this.player)))
                        .replace("<queue_maximum>", String.valueOf(this.plugin.getQueueImpl().getQueuePlayers(this.player)))
                );
            }

            return PlaceholderAPI.setPlaceholders(this.player, finalLines);
        } else {
            return PlaceholderAPI.setPlaceholders(this.player, this.plugin.getHubHandler().getScoreboardLinesNormal());
        }
    }

    @Override
    public String getTitle() {
        return this.plugin.getHubHandler().getScoreboardTitle();
    }
}
