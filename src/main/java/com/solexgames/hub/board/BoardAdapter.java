package com.solexgames.hub.board;

import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.task.GlobalStatusUpdateTask;
import io.github.nosequel.scoreboard.element.ScoreboardElement;
import io.github.nosequel.scoreboard.element.ScoreboardElementHandler;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BoardAdapter implements ScoreboardElementHandler {

    private final HubPlugin plugin;

    @Override
    public ScoreboardElement getElement(Player player) {
        final ScoreboardElement element = new ScoreboardElement();

        element.setTitle(this.plugin.getHubHandler().getScoreboardTitle().replace("<bar>", "┃"));

        final boolean isInQueue = this.plugin.getQueueImpl().isInQueue(player);
        final List<String> finalLines = new ArrayList<>();

        if (isInQueue) {
            final List<String> scoreboardLines = this.plugin.getHubHandler().getScoreboardLinesQueued();

            for (String string : scoreboardLines) {
                finalLines.add(string.replace("<global_online>", String.valueOf(GlobalStatusUpdateTask.GLOBAL_PLAYERS))
                        .replace("<queue_name>", this.plugin.getQueueImpl().getQueueName(player))
                        .replace("<queue_position>", String.valueOf(this.plugin.getQueueImpl().getQueuePosition(player)))
                        .replace("<queue_maximum>", String.valueOf(this.plugin.getQueueImpl().getQueuePlayers(player)))
                );
            }
        } else {
            final List<String> scoreboardLines = this.plugin.getHubHandler().getScoreboardLinesNormal();

            for (String string : scoreboardLines) {
                finalLines.add(string.replace("<global_online>", String.valueOf(GlobalStatusUpdateTask.GLOBAL_PLAYERS)));
            }
        }

        element.getLines().addAll(PlaceholderAPI.setPlaceholders(player, finalLines));

        return element;
    }
}
