package com.solexgames.hub.board;

import com.solexgames.hub.HubPlugin;
import com.solexgames.hub.module.HubModule;
import com.solexgames.hub.module.impl.HubModuleScoreboardAdapter;
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
        final HubModule hubModule = this.plugin.getHubModule();

        boolean shouldHandleDefault = true;

        if (hubModule != null) {
            final HubModuleScoreboardAdapter adapter = hubModule.getScoreboardAdapter();

            if (adapter != null) {
                element.setTitle(adapter.getNewTitle());

                for (final String newLine : adapter.getNewLines(player)) {
                    element.add(newLine);
                }

                shouldHandleDefault = false;
            }
        }

        if (shouldHandleDefault) {
            element.setTitle(this.plugin.getSettingsProcessor().getScoreboardTitle().replace("<bar>", Character.toString('┃')));

            final boolean isInQueue = this.plugin.getQueueImpl().isInQueue(player);
            final List<String> finalLines = new ArrayList<>();

            if (isInQueue) {
                final List<String> scoreboardLines = this.plugin.getSettingsProcessor().getQueuedScoreboardLines();

                for (String string : scoreboardLines) {
                    finalLines.add(string.replace("<global_online>", GlobalStatusUpdateTask.GLOBAL_PLAYERS + "")
                            .replace("<queue_name>", this.plugin.getQueueImpl().getQueueName(player))
                            .replace("<queue_lane>", this.plugin.getQueueImpl().getQueueLane(player))
                            .replace("<queue_position>", String.valueOf(this.plugin.getQueueImpl().getQueuePosition(player)))
                            .replace("<queue_maximum>", String.valueOf(this.plugin.getQueueImpl().getQueuePlayers(player)))
                    );
                }
            } else {
                final List<String> scoreboardLines = this.plugin.getSettingsProcessor().getNormalScoreboardLines();

                for (String string : scoreboardLines) {
                    finalLines.add(string.replace("<global_online>", GlobalStatusUpdateTask.GLOBAL_PLAYERS + ""));
                }
            }

            element.getLines().addAll(PlaceholderAPI.setPlaceholders(player, finalLines));
        }

        return element;
    }
}
