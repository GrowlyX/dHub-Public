package com.solexgames.pear.player.impl;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.solexgames.pear.PearSpigotPlugin;
import com.solexgames.pear.model.PersistentModel;
import com.solexgames.pear.player.PearPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author GrowlyX
 * @since 8/9/2021
 */

@Getter @Setter
@RequiredArgsConstructor
public class PersistentPearPlayer extends PearPlayer implements PersistentModel<Document> {

    private final String name;
    private final UUID uniqueId;

    private String armor;
    private String trail;
    private String gadget;

    private boolean ruleAgreed;

    @Override
    public void load(CompletableFuture<Document> documentCompletableFuture) {
        documentCompletableFuture.whenComplete((document, throwable) -> {
            if (document == null) {
                this.save().whenComplete((unused, throwable1) -> {
                    if (throwable1 != null) {
                        throwable1.printStackTrace();
                    }
                });

                return;
            }

            this.armor = document.getString("armor");
            this.trail = document.getString("trail");
            this.gadget = document.getString("gadget");

            this.ruleAgreed = document.getBoolean("ruleAgreed");
        });
    }

    @Override
    public CompletableFuture<Void> save() {
        return CompletableFuture.runAsync(() -> {
            JavaPlugin.getPlugin(PearSpigotPlugin.class).getStorageHandler().getPlayerCollection().replaceOne(
                    Filters.eq("uuid", this.uniqueId.toString()),
                    this.getDocument(), new ReplaceOptions().upsert(true)
            );
        });
    }

    public Player getBukkitEntity() {
        return Bukkit.getPlayer(this.uniqueId);
    }

    public Document getDocument() {
        final Document document = new Document("_id", this.uniqueId);

        document.put("uuid", this.uniqueId.toString());

        document.put("armor", this.armor);
        document.put("trail", this.trail);
        document.put("gadget", this.gadget);

        document.put("ruleAgreed", this.ruleAgreed);

        return document;
    }
}
