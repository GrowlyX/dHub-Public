package com.solexgames.hub.processor;

import com.solexgames.lib.processor.config.comment.Comment;
import lombok.Data;
import me.lucko.helper.serialize.Position;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;

/**
 * @author GrowlyX
 * @since 6/29/2021
 */

@Data
public class NeonSettingsProcessor {

    @Comment("Should we let players chat in the hub?")
    private boolean chatEnabled = true;

    @Comment("Should we automatically hide all players?")
    private boolean hidePlayers = false;

    @Comment("Should we block players from griefing the server?")
    private boolean antiGrief = true;

    @Comment("Should we enable the scoreboard?")
    private boolean scoreboardEnabled = true;

    @Comment("Should we enable a simple captcha when a player logs on?")
    private boolean captchaEnabled = true;

    @Comment("Should we enable double jump?")
    private boolean doubleJumpEnabled = true;

    @Comment("What queue system should we use?")
    private String queueSystem = "PORTAL";

    @Comment("Should we let players chat in the hub?")
    private Location spawnLocation = null;

    @Comment("What should the scoreboard title be?")
    private String scoreboardTitle = ChatColor.GOLD + "SolexGames";

    private List<String> normalScoreboardLines = Arrays.asList(
            "&7&m------------",
            "&fOnline: &6<online>",
            "&7&m------------"
    );

    @Comment("The queued scoreboard lines will only show if you use a queue plugin.")
    private List<String> queuedScoreboardLines = Arrays.asList(
            "&7&m------------",
            "&fOnline: &6<online>",
            "&fQueued: &6<queue>",
            "&7&m------------"
    );
}
