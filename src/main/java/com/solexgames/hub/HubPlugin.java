package com.solexgames.hub;

import com.solexgames.hub.board.BoardAdapter;
import com.solexgames.hub.command.NeonCommand;
import com.solexgames.hub.external.ExternalConfig;
import com.solexgames.hub.handler.CosmeticHandler;
import com.solexgames.hub.handler.SubMenuHandler;
import com.solexgames.hub.listener.AntiListener;
import com.solexgames.hub.listener.EnderbuttListener;
import com.solexgames.hub.listener.PlayerListener;
import com.solexgames.hub.processor.NeonSettingsProcessor;
import com.solexgames.hub.processor.adapter.PositionObjectAdapter;
import com.solexgames.hub.queue.IQueue;
import com.solexgames.hub.queue.impl.DefaultQueueImpl;
import com.solexgames.hub.queue.impl.PortalQueueImpl;
import com.solexgames.hub.task.GlobalStatusUpdateTask;
import com.solexgames.hub.util.ItemUtil;
import com.solexgames.lib.commons.processor.AcfCommandProcessor;
import com.solexgames.lib.processor.config.ConfigFactory;
import com.solexgames.lib.processor.config.internal.adapt.AdapterHandler;
import io.github.nosequel.scoreboard.ScoreboardHandler;
import lombok.Getter;
import me.lucko.helper.serialize.Position;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

@Getter
public final class HubPlugin extends JavaPlugin {

    private final List<Player> permittedBuilders = new ArrayList<>();
    private final Map<String, AbstractMap.SimpleEntry<Integer, ItemStack>> itemCache = new HashMap<>();

    private NeonSettingsProcessor settingsProcessor;
    private ExternalConfig menus;

    private CosmeticHandler cosmeticHandler;
    private SubMenuHandler subMenuHandler;
    private IQueue queueImpl;

    private final ConfigFactory configFactory = ConfigFactory.newFactory(this);

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        AdapterHandler.registerAdapter(Position.class, new PositionObjectAdapter());

        this.settingsProcessor = this.configFactory.fromFile("options", NeonSettingsProcessor.class);
        this.menus = new ExternalConfig("menus", this);

        this.cosmeticHandler = new CosmeticHandler(this);
        this.cosmeticHandler.loadArmorCosmetics();
        this.cosmeticHandler.loadParticleCosmetics();

        this.subMenuHandler = new SubMenuHandler(this);
        this.subMenuHandler.registerSubMenusFromConfig();

        final AcfCommandProcessor commandProcessor = new AcfCommandProcessor(this);
        commandProcessor.enableUnstableAPI("help");
        commandProcessor.registerCommand(new NeonCommand(this));

        this.loadItems();
        this.loadQueueImpl();

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        if (this.settingsProcessor.isAntiGrief()) {
            this.getServer().getPluginManager().registerEvents(new AntiListener(this), this);
        }

        if (this.settingsProcessor.isScoreboardEnabled()) {
            new ScoreboardHandler(this, new BoardAdapter(this), 20L);
        }

        if (this.itemCache.get("enderbutt") != null) {
            this.getServer().getPluginManager().registerEvents(new EnderbuttListener(this), this);
        }

        new GlobalStatusUpdateTask().runTaskTimerAsynchronously(this, 0L, 20L);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onDisable() {
        this.configFactory.save("options", this.settingsProcessor);
    }

    private void loadQueueImpl() {
        if (this.settingsProcessor.getQueueSystem().equals("PORTAL")) {
            this.queueImpl = new PortalQueueImpl();
        } else {
            this.queueImpl = new DefaultQueueImpl();
        }
    }

    private void loadItems() {
        this.itemCache.put("enderbutt", ItemUtil.getInventoryItemFromConfig("items.enderbutt", this));
        this.itemCache.put("hub-selector", ItemUtil.getInventoryItemFromConfig("items.hub-selector", this));
        this.itemCache.put("cosmetics", ItemUtil.getInventoryItemFromConfig("items.cosmetics", this));
        this.itemCache.put("server-selector", ItemUtil.getInventoryItemFromConfig("items.server-selector", this));
        this.itemCache.put("profile", ItemUtil.getInventoryItemFromConfig("items.profile", this));
    }

    public void reloadAllConfigs() {
        this.menus.reloadConfig();

        this.reloadConfig();
    }
}
