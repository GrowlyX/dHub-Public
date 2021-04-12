package com.solexgames.hub;

import com.solexgames.hub.listener.AntiListener;
import com.solexgames.hub.listener.EnderbuttListener;
import com.solexgames.hub.command.BuildCommand;
import com.solexgames.hub.command.DHubCommand;
import com.solexgames.core.CorePlugin;
import com.solexgames.hub.external.ExternalConfig;
import com.solexgames.hub.listener.PlayerListener;
import com.solexgames.hub.manager.HubManager;
import com.solexgames.hub.tablist.TablistAdapter;
import com.solexgames.hub.util.ItemUtil;
import io.github.nosequel.tab.shared.TabHandler;
import io.github.nosequel.tab.v1_10_r1.v1_10_R1TabAdapter;
import io.github.nosequel.tab.v1_12_r1.v1_12_R1TabAdapter;
import io.github.nosequel.tab.v1_14_r1.v1_14_R1TabAdapter;
import io.github.nosequel.tab.v1_15_r1.v1_15_R1TabAdapter;
import io.github.nosequel.tab.v1_16_r3.v1_16_R3TabAdapter;
import io.github.nosequel.tab.v1_7_r4.v1_7_R4TabAdapter;
import io.github.nosequel.tab.v1_8_r3.v1_8_R3TabAdapter;
import io.github.nosequel.tab.v1_9_r1.v1_9_R1TabAdapter;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class HubPlugin extends JavaPlugin {

    @Getter
    private static HubPlugin instance;

    private final List<String> permittedBuilders = new ArrayList<>();

    private ExternalConfig settings;
    private ExternalConfig location;
    private ExternalConfig menus;

    private String enderButt;

    private HubManager hubManager;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();

        this.settings = new ExternalConfig("settings");
        this.location = new ExternalConfig("location");
        this.menus = new ExternalConfig("menus");

        this.hubManager = new HubManager();
        this.enderButt = ItemUtil.getItemFromConfig("items.enderbutt").getItemMeta().getDisplayName();

        this.getCommand("build").setExecutor(new BuildCommand());
        this.getCommand("dhub").setExecutor(new DHubCommand());

        CorePlugin.getInstance().registerListeners(new PlayerListener());

        if (this.hubManager.isAntiListeners()) {
            CorePlugin.getInstance().registerListeners(new AntiListener());
        }

        if (this.hubManager.isEnderButtEnabled()) {
            CorePlugin.getInstance().registerListeners(new EnderbuttListener());
        }

        String version = this.getServer().getVersion();

        if (this.getConfig().getBoolean("tablist.enabled")) {
            if (version.contains("1.7")) {
                new TabHandler(new v1_7_R4TabAdapter(), new TablistAdapter(), this, 20L);
            } else if (version.contains("1.8")) {
                new TabHandler(new v1_8_R3TabAdapter(), new TablistAdapter(), this, 20L);
            } else if (version.contains("1.9")) {
                new TabHandler(new v1_9_R1TabAdapter(), new TablistAdapter(), this, 20L);
            } else if (version.contains("1.10")) {
                new TabHandler(new v1_10_R1TabAdapter(), new TablistAdapter(), this, 20L);
            } else if (version.contains("1.12")) {
                new TabHandler(new v1_12_R1TabAdapter(), new TablistAdapter(), this, 20L);
            } else if (version.contains("1.14")) {
                new TabHandler(new v1_14_R1TabAdapter(), new TablistAdapter(), this, 20L);
            } else if (version.contains("1.15")) {
                new TabHandler(new v1_15_R1TabAdapter(), new TablistAdapter(), this, 20L);
            } else if (version.contains("1.16")) {
                new TabHandler(new v1_16_R3TabAdapter(), new TablistAdapter(), this, 20L);
            }
        }

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    public void reloadAllConfigs() {
        this.settings.reloadConfig();
        this.location.reloadConfig();
        this.menus.reloadConfig();

        this.reloadConfig();

        this.getLogger().info("Reloaded all configs!");
    }
}
