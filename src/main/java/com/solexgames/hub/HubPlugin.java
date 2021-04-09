package com.solexgames.hub;

import com.solexgames.hub.listener.AntiListener;
import com.solexgames.hub.listener.EnderbuttListener;
import com.solexgames.hub.command.BuildCommand;
import com.solexgames.hub.command.DHubCommand;
import com.solexgames.core.CorePlugin;
import com.solexgames.hub.external.ExternalConfig;
import com.solexgames.hub.listener.PlayerListener;
import com.solexgames.hub.manager.HubManager;
import com.solexgames.hub.util.ItemUtil;
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
