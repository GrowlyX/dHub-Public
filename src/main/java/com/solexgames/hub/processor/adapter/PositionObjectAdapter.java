package com.solexgames.hub.processor.adapter;

import com.google.gson.JsonParser;
import com.solexgames.lib.processor.config.internal.adapt.ObjectAdapter;
import me.lucko.helper.serialize.Position;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author GrowlyX
 * @since 6/29/2021
 */

public class PositionObjectAdapter implements ObjectAdapter<Position, String> {

    @Override
    public Position read(String key, ConfigurationSection configurationSection) {
        final String value = configurationSection.getString(key);
        final JsonParser jsonParser = new JsonParser();

        if (value.equals("Unknown")) {
            return null;
        }

        return Position.deserialize(jsonParser.parse(value));
    }

    @Override
    public String write(Position position) {
        return position == null ? "Unknown" : position.serialize().toString();
    }
}
