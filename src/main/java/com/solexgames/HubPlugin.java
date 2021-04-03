package com.solexgames;

import com.solexgames.command.BuildCommand;
import com.solexgames.command.DHubCommand;
import com.solexgames.core.CorePlugin;
import com.solexgames.external.ExternalConfig;
import com.solexgames.listener.AntiListener;
import com.solexgames.listener.EnderbuttListener;
import com.solexgames.listener.PlayerListener;
import com.solexgames.manager.HubManager;
import com.solexgames.util.ItemUtil;
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
