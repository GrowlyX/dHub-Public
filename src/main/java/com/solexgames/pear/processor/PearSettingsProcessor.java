package com.solexgames.pear.processor;

import com.solexgames.lib.processor.config.Coloured;
import com.solexgames.lib.processor.config.RequiredField;
import com.solexgames.lib.processor.config.comment.Comment;
import lombok.Data;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;

/**
 * @author GrowlyX
 * @since 6/29/2021
 */

@Data
public class PearSettingsProcessor {

    @Comment("Should we let players chat in the hub?")
    private boolean chatEnabled = true;

    @Comment("Should we automatically hide all players?")
    private boolean hidePlayers = false;

    @Comment("Should we block players from griefing the server?")
    private boolean antiGrief = true;

    @Comment("Should we enable the scoreboard?")
    private boolean scoreboardEnabled = true;

    @Comment("Should we enable the tablist?")
    private boolean tablistEnabled = true;

    @Comment("Should we enable a simple captcha when a player logs on?")
    private boolean captchaEnabled = true;

    @Comment("Should we enable double jump?")
    private boolean doubleJumpEnabled = true;

    @RequiredField
    @Comment("What queue system should we use?")
    private String queueSystem = "PORTAL";

    @Comment("Should we let players chat in the hub?")
    private Location spawnLocation = null;

    @Coloured
    @Comment("What should the scoreboard title be?")
    private String scoreboardTitle = ChatColor.GOLD + "SolexGames";

    private List<String> normalScoreboardLines = Arrays.asList(
            "&7&m-----------------",
            "&fOnline: &6<global_online>",
            "&fRank: &6%scandium_rankcolor%%scandium_rankname%",
            "",
            "&6solexgames.com",
            "&7&m-----------------"
    );

    @Comment("The queued scoreboard lines will only show if you use a queue plugin.")
    private List<String> queuedScoreboardLines = Arrays.asList(
            "&7&m-----------------",
            "&fOnline: &6<global_online>",
            "&fRank: &6%scandium_rankcolor%%scandium_rankname%",
            "",
            "Queue:",
            "&6<queue_name> &7(#<queue_position>/<queue_maximum>)",
            "",
            "&6solexgames.com",
            "&7&m-----------------"
    );
}
