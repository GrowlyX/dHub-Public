package com.solexgames.pear;

import com.mongodb.client.model.Filters;
import com.solexgames.core.CorePlugin;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.core.util.external.Button;
import com.solexgames.lib.commons.game.PlayerCache;
import com.solexgames.lib.commons.game.impl.BasicPlayerCache;
import com.solexgames.pear.board.BoardAdapter;
import com.solexgames.pear.command.PearCommand;
import com.solexgames.pear.external.ExternalConfig;
import com.solexgames.pear.handler.CosmeticHandler;
import com.solexgames.pear.handler.StorageHandler;
import com.solexgames.pear.handler.SubMenuHandler;
import com.solexgames.pear.listener.AntiListener;
import com.solexgames.pear.listener.EnderbuttListener;
import com.solexgames.pear.listener.PlayerListener;
import com.solexgames.pear.module.HubModule;
import com.solexgames.pear.player.impl.PersistentPearPlayer;
import com.solexgames.pear.processor.PearChatProcessor;
import com.solexgames.pear.processor.PearServerPlaceholderProcessor;
import com.solexgames.pear.processor.PearSettingsProcessor;
import com.solexgames.pear.queue.IQueue;
import com.solexgames.pear.queue.impl.AGQQueueImpl;
import com.solexgames.pear.queue.impl.DefaultQueueImpl;
import com.solexgames.pear.queue.impl.PortalQueueImpl;
import com.solexgames.pear.tab.PearTabProcessor;
import com.solexgames.pear.task.GlobalStatusUpdateTask;
import com.solexgames.pear.task.server.UpdateTask;
import com.solexgames.pear.task.server.impl.MultiServerUpdateTask;
import com.solexgames.pear.task.server.impl.SingleServerUpdateTask;
import com.solexgames.pear.util.ItemUtil;
import com.solexgames.lib.commons.CommonLibsBukkit;
import com.solexgames.lib.commons.hologram.CommonsHologram;
import com.solexgames.lib.commons.processor.AcfCommandProcessor;
import com.solexgames.lib.processor.config.ConfigFactory;
import io.github.nosequel.scoreboard.ScoreboardHandler;
import io.github.nosequel.tab.shared.TabHandler;
import io.github.nosequel.tab.v1_12_r1.v1_12_R1TabAdapter;
import io.github.nosequel.tab.v1_8_r3.v1_8_R3TabAdapter;
import lombok.Getter;
import lombok.Setter;
import me.lucko.helper.Events;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Getter
public final class PearSpigotPlugin extends JavaPlugin {

    public static final Button GLASS = new ItemBuilder(Material.STAINED_GLASS_PANE, 15)
            .setDisplayName(" ")
            .toButton();

    private final Map<String, UpdateTask<?>> updateTaskMap = new HashMap<>();

    private final List<Player> permittedBuilders = new ArrayList<>();
    private final Map<String, AbstractMap.SimpleEntry<Integer, ItemStack>> itemCache = new HashMap<>();

    private PearSettingsProcessor settingsProcessor;
    private ExternalConfig menus;

    private CosmeticHandler cosmeticHandler;
    private SubMenuHandler subMenuHandler;
    private StorageHandler storageHandler;

    private IQueue queueImpl;

    @Setter
    private HubModule hubModule;

    private final ConfigFactory configFactory = ConfigFactory.newFactory(this);

    private final PlayerCache<PersistentPearPlayer> persistentPlayerCache = new BasicPlayerCache<>();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.settingsProcessor = this.configFactory.fromFile("options", PearSettingsProcessor.class);
        this.menus = new ExternalConfig("menus", this);

        this.storageHandler = new StorageHandler();

        this.cosmeticHandler = new CosmeticHandler(this);
        this.cosmeticHandler.loadArmorCosmetics();
        this.cosmeticHandler.loadParticleCosmetics();

        this.subMenuHandler = new SubMenuHandler(this);
        this.subMenuHandler.registerSubMenusFromConfig();

        final AcfCommandProcessor commandProcessor = new AcfCommandProcessor(this);
        commandProcessor.registerCommand(new PearCommand(this));

        this.loadItems();
        this.loadQueueImpl();

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        if (this.settingsProcessor.isAntiGrief()) {
            this.getServer().getPluginManager().registerEvents(new AntiListener(this), this);
        }

        if (this.settingsProcessor.isScoreboardEnabled()) {
            new ScoreboardHandler(this, new BoardAdapter(this), 20L);
        }

        if (this.settingsProcessor.isTablistEnabled()) {
            new TabHandler(Bukkit.getVersion().contains("12") ? new v1_12_R1TabAdapter() : new v1_8_R3TabAdapter(), new PearTabProcessor(), this, 10L);
        }

        if (this.itemCache.get("enderbutt") != null) {
            this.getServer().getPluginManager().registerEvents(new EnderbuttListener(this), this);
        }

        if (!this.settingsProcessor.isChatEnabled()) {
            CorePlugin.getInstance().getChatCheckList().add(new PearChatProcessor(this));
        }

        this.setupListeners();

        new PearServerPlaceholderProcessor().register();

//        Arrays.asList("UHC", "Meetup", "SkyWars").forEach(id -> {
//            this.updateTaskMap.put(id, new MultiServerUpdateTask(id, NetworkServerType.valueOf(id.toUpperCase())));
//        });
//
//        Arrays.asList("NA_Practice", "NA_HCF").forEach(id -> {
//            this.updateTaskMap.put(id, new SingleServerUpdateTask(id, id.replace("_", "-").toLowerCase()));
//        });

        new GlobalStatusUpdateTask(CorePlugin.getInstance().getJedisManager())
                .runTaskTimerAsynchronously(this, 0L, 20L);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onDisable() {
        this.updateTaskMap.forEach((s, updateTask) -> {
            if (updateTask instanceof SingleServerUpdateTask) {
                final SingleServerUpdateTask updateTask1 = (SingleServerUpdateTask) updateTask;
                final CommonsHologram hologram = CommonLibsBukkit.getInstance().getHologramManager().fetchHologram(updateTask1.getHologramName());

                if (hologram != null) {
                    hologram.remove();
                }

                return;
            }

            if (updateTask instanceof MultiServerUpdateTask) {
                final MultiServerUpdateTask updateTask1 = (MultiServerUpdateTask) updateTask;
                final CommonsHologram hologram = CommonLibsBukkit.getInstance().getHologramManager().fetchHologram(updateTask1.getHologramName());

                if (hologram != null) {
                    hologram.remove();
                }
            }
        });

        this.configFactory.save("options", this.settingsProcessor);
    }

    private void loadQueueImpl() {
        if (this.settingsProcessor.getQueueSystem().equals("PORTAL")) {
            this.queueImpl = new PortalQueueImpl();
        } else if (this.settingsProcessor.getQueueSystem().equals("AGQ")) {
            this.queueImpl = new AGQQueueImpl();
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

    private void setupListeners() {
        Events.subscribe(AsyncPlayerPreLoginEvent.class).handler(event -> {
            final PersistentPearPlayer player = new PersistentPearPlayer(
                    event.getName(),
                    event.getUniqueId()
            );

            final CompletableFuture<Document> completableFuture = CompletableFuture.supplyAsync(() ->
                    this.storageHandler.getPlayerCollection().find(Filters.eq("uuid", event.getUniqueId().toString())).first()
            );

            player.load(completableFuture);

            this.persistentPlayerCache.getPlayerTypeMap().put(event.getUniqueId(), player);
        });

        Events.subscribe(PlayerQuitEvent.class).handler(playerQuitEvent -> {
            final PersistentPearPlayer player = this.persistentPlayerCache.getByPlayer(playerQuitEvent.getPlayer());

            if (player != null) {
                player.save();

                this.persistentPlayerCache.getPlayerTypeMap().remove(player.getUniqueId());
            }
        });
    }


    public void reloadAllConfigs() {
        this.menus.reloadConfig();

        this.reloadConfig();
    }
}
