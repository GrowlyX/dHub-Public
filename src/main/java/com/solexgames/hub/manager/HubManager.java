package com.solexgames.hub.manager;

import com.solexgames.hub.HubPlugin;
import com.solexgames.core.util.LocationUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class HubManager {

    private List<String> scoreboardLines;
    private Location hubLocation;

    private String scoreboardTitle;
    private String buildModePermission;
    private String setupPermission;
    private String chatPermission;

    private boolean isHubLocationSet;
    private boolean isCanChatInHub;
    private boolean isScoreboardEnabled;
    private boolean isAntiListeners;
    private boolean isDoubleJumpEnabled;
    private boolean isDoubleJumpEffectEnabled;
    private boolean isDoubleJumpSoundEnabled;
    private boolean isEnderButtEnabled;
    private boolean isServerSelectorEnabled;
    private boolean isHubSelectorEnabled;
    private boolean isHubSpeedEnabled;

    private float speedMultiply;
    private double doubleJumpMultiply;

    private Sound doubleJumpSound;
    private Effect doubleJumpEffect;

    private int lowestYAxis;

    public HubManager() {
        if (HubPlugin.getInstance().getConfig().getString("locations.spawn-location") == null) {
            this.isHubLocationSet = false;
            HubPlugin.getInstance().getLogger().info("Make sure to setup dHub via /setupdhub!");
        } else {
            try {
                this.hubLocation = LocationUtil.getLocationFromString(HubPlugin.getInstance().getConfig().getString("locations.spawn-location")).orElse(null);
                this.isHubLocationSet = true;
            } catch (Exception e) {
                this.isHubLocationSet = false;
                HubPlugin.getInstance().getLogger().info("Something went wrong while trying to setup your spawn, maybe try setting it up again?");
            }
        }

        try {
            this.lowestYAxis = HubPlugin.getInstance().getSettings().getInt("settings.lowest-y-axis");
        } catch (Exception e) {
            this.lowestYAxis = 0;
        }

        try {
            this.isScoreboardEnabled = HubPlugin.getInstance().getSettings().getBoolean("scoreboard.enabled");
        } catch (Exception e) {
            this.isScoreboardEnabled = true;
        }

        try {
            this.speedMultiply = (float) HubPlugin.getInstance().getSettings().getDouble("speed.multiply");
        } catch (Exception e) {
            this.speedMultiply = 0.5F;
        }

        try {
            this.isDoubleJumpEnabled = HubPlugin.getInstance().getSettings().getBoolean("double-jump.enabled");
        } catch (Exception e) {
            this.isDoubleJumpEnabled = true;
        }

        try {
            this.doubleJumpSound = Sound.valueOf(HubPlugin.getInstance().getSettings().getString("double-jump.sound.value"));
        } catch (Exception e) {
            this.doubleJumpSound = Sound.EXPLODE;
        }

        try {
            this.isDoubleJumpEffectEnabled = HubPlugin.getInstance().getSettings().getBoolean("double-jump.effect.enabled");
        } catch (Exception e) {
            this.isDoubleJumpEffectEnabled = true;
        }

        try {
            this.isDoubleJumpSoundEnabled = HubPlugin.getInstance().getSettings().getBoolean("double-jump.sound.enabled");
        } catch (Exception e) {
            this.isDoubleJumpSoundEnabled = true;
        }

        try {
            this.doubleJumpEffect = Effect.valueOf(HubPlugin.getInstance().getSettings().getString("double-jump.effect.value"));
        } catch (Exception e) {
            this.doubleJumpEffect = Effect.BLAZE_SHOOT;
        }

        try {
            this.doubleJumpMultiply = HubPlugin.getInstance().getSettings().getDouble("double-jump.velocity");
        } catch (Exception e) {
            this.doubleJumpMultiply = 2.5D;
        }

        try {
            this.isEnderButtEnabled = HubPlugin.getInstance().getSettings().getBoolean("ender-butt.enabled");
        } catch (Exception e) {
            this.isEnderButtEnabled = true;
        }

        try {
            this.isAntiListeners = HubPlugin.getInstance().getSettings().getBoolean("settings.setup-anti-listeners");
        } catch (Exception e) {
            this.isAntiListeners = true;
        }

        try {
            this.isCanChatInHub = HubPlugin.getInstance().getSettings().getBoolean("settings.allow-chat");
        } catch (Exception e) {
            this.isCanChatInHub = true;
        }

        try {
            this.scoreboardLines = HubPlugin.getInstance().getSettings().getStringList("scoreboard.lines");
        } catch (Exception e) {
            this.scoreboardLines = Collections.singletonList("&7Check Config!");
        }

        try {
            this.scoreboardTitle = HubPlugin.getInstance().getSettings().getString("scoreboard.title")
                    .replace("<vertical>", Character.toString('┃'));
        } catch (Exception e) {
            this.scoreboardTitle = "&6&ldHub &7<vertical> &fHub-1"
                    .replace("<vertical>", Character.toString('┃'));
        }
    }
}
