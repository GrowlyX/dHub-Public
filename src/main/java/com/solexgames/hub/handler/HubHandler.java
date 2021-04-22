package com.solexgames.hub.handler;

import com.cryptomorin.xseries.XSound;
import com.solexgames.core.util.Color;
import com.solexgames.hub.HubPlugin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Effect;
import org.bukkit.Sound;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class HubHandler {

    private final HubPlugin plugin;

    private List<String> scoreboardLinesNormal;
    private List<String> scoreboardLinesQueued;

    private String scoreboardTitle;

    private String buildModePermission;
    private String setupPermission;
    private String chatPermission;

    private boolean isHubLocationSet;
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

    public HubHandler(HubPlugin plugin) {
        this.plugin = plugin;

        try {
            this.lowestYAxis = this.plugin.getSettings().getInt("settings.lowest-y-axis");
        } catch (Exception e) {
            this.lowestYAxis = 0;
        }

        try {
            this.isScoreboardEnabled = this.plugin.getSettings().getBoolean("scoreboard.enabled");
        } catch (Exception e) {
            this.isScoreboardEnabled = true;
        }

        try {
            this.speedMultiply = (float) this.plugin.getSettings().getDouble("speed.multiply");
        } catch (Exception e) {
            this.speedMultiply = 0.5F;
        }

        try {
            this.isDoubleJumpEnabled = this.plugin.getSettings().getBoolean("double-jump.enabled");
        } catch (Exception e) {
            this.isDoubleJumpEnabled = true;
        }

        try {
            this.doubleJumpSound = Sound.valueOf(this.plugin.getSettings().getString("double-jump.sound.value"));
        } catch (Exception e) {
            this.doubleJumpSound = XSound.ENTITY_DRAGON_FIREBALL_EXPLODE.parseSound();
        }

        try {
            this.isDoubleJumpEffectEnabled = this.plugin.getSettings().getBoolean("double-jump.effect.enabled");
        } catch (Exception e) {
            this.isDoubleJumpEffectEnabled = true;
        }

        try {
            this.isDoubleJumpSoundEnabled = this.plugin.getSettings().getBoolean("double-jump.sound.enabled");
        } catch (Exception e) {
            this.isDoubleJumpSoundEnabled = true;
        }

        try {
            this.doubleJumpEffect = Effect.valueOf(this.plugin.getSettings().getString("double-jump.effect.value"));
        } catch (Exception e) {
            this.doubleJumpEffect = Effect.BLAZE_SHOOT;
        }

        try {
            this.doubleJumpMultiply = this.plugin.getSettings().getDouble("double-jump.velocity");
        } catch (Exception e) {
            this.doubleJumpMultiply = 2.5D;
        }

        try {
            this.isEnderButtEnabled = this.plugin.getSettings().getBoolean("ender-butt.enabled");
        } catch (Exception e) {
            this.isEnderButtEnabled = true;
        }

        try {
            this.isAntiListeners = this.plugin.getSettings().getBoolean("settings.setup-anti-listeners");
        } catch (Exception e) {
            this.isAntiListeners = true;
        }

        try {
            this.scoreboardLinesNormal = Color.translate(this.plugin.getSettings().getStringList("scoreboard.type.normal"));
        } catch (Exception e) {
            this.scoreboardLinesNormal = Collections.singletonList("&7Check Config!");
        }

        try {
            this.scoreboardLinesQueued = Color.translate(this.plugin.getSettings().getStringList("scoreboard.type.queued"));
        } catch (Exception e) {
            this.scoreboardLinesQueued = Collections.singletonList("&7Check Config!");
        }

        try {
            this.scoreboardTitle = Color.translate(this.plugin.getSettings().getString("scoreboard.title")
                    .replace("<vertical>", Character.toString('┃')));
        } catch (Exception e) {
            this.scoreboardTitle = Color.translate("&6&lNeon &7<vertical> &fHub-1"
                    .replace("<vertical>", Character.toString('┃')));
        }
    }
}
