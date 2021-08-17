package com.solexgames.pear.board;

import com.solexgames.core.CorePlugin;
import com.solexgames.core.player.PotPlayer;
import com.solexgames.core.player.ranks.Rank;
import com.solexgames.pear.PearSpigotPlugin;
import com.solexgames.pear.module.HubModule;
import com.solexgames.pear.module.impl.HubModuleScoreboardAdapter;
import com.solexgames.pear.task.GlobalStatusUpdateTask;
import io.github.nosequel.scoreboard.element.ScoreboardElement;
import io.github.nosequel.scoreboard.element.ScoreboardElementHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BoardAdapter implements ScoreboardElementHandler {

    private final PearSpigotPlugin plugin;

    @Override
    public ScoreboardElement getElement(Player player) {
        final ScoreboardElement element = new ScoreboardElement();
        final HubModule hubModule = this.plugin.getHubModule();
        final PotPlayer potPlayer = CorePlugin.getInstance().getPlayerManager().getPlayer(player);

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
            final Rank rank = potPlayer.getActiveGrant().getRank();

            if (isInQueue) {
                final List<String> scoreboardLines = this.plugin.getSettingsProcessor().getQueuedScoreboardLines();

                for (final String string : scoreboardLines) {
                    finalLines.add(string.replace("<global_online>", GlobalStatusUpdateTask.GLOBAL_PLAYERS + "")
                            .replace("<queue_name>", this.plugin.getQueueImpl().getQueueName(player))
                            .replace("<queue_lane>", this.plugin.getQueueImpl().getQueueLane(player))
                            .replace("<queue_position>", String.valueOf(this.plugin.getQueueImpl().getQueuePosition(player)))
                            .replace("<queue_maximum>", String.valueOf(this.plugin.getQueueImpl().getQueuePlayers(player)))
                            .replace("<formatted_rank>", rank.getColor().replace("&", "§") + rank.getName())
                    );
                }
            } else {
                final List<String> scoreboardLines = this.plugin.getSettingsProcessor().getNormalScoreboardLines();

                for (final String string : scoreboardLines) {
                    finalLines.add(string
                            .replace("<global_online>", GlobalStatusUpdateTask.GLOBAL_PLAYERS + "")
                            .replace("<formatted_rank>", rank.getColor().replace("&", "§") + rank.getName())
                    );
                }
            }

            element.getLines().addAll(finalLines);
        }

        return element;
    }
}
