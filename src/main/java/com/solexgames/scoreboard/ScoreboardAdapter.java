package com.solexgames.scoreboard;

import com.solexgames.HubPlugin;
import com.solexgames.core.board.ScoreBoard;
import com.solexgames.core.util.Color;
import com.solexgames.manager.HubManager;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ScoreboardAdapter extends ScoreBoard {

    private final Player player;
    private final HubManager manager;

    public ScoreboardAdapter(Player player) {
        super(player);

        this.player = player;
        this.manager = HubPlugin.getInstance().getHubManager();
    }

    @Override
    public List<String> getLines() {
        List<String> lines = manager.getScoreboardLines();

        lines = Color.translate(lines);
        lines = PlaceholderAPI.setPlaceholders(this.player, lines);

        return lines;
    }

    @Override
    public String getTitle() {
        return manager.getScoreboardTitle();
    }
}
