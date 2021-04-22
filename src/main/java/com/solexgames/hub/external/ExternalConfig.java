package com.solexgames.hub.external;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.solexgames.hub.HubPlugin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class ExternalConfig {

    private File file;
    private YamlConfiguration configuration;
    private String name;

    private JavaPlugin plugin;

    public ExternalConfig(String name, JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), name + ".yml");

        if (!this.file.getParentFile().exists()) {
            this.file.getParentFile().mkdir();
        }

        this.name = name + ".yml";

        plugin.saveResource(name + ".yml", false);

        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    public double getDouble(String path) {
        return this.configuration.contains(path) ? this.configuration.getDouble(path) : 0.5D;
    }

    public int getInt(String path) {
        return this.configuration.contains(path) ? this.configuration.getInt(path) : 0;
    }

    public boolean getBoolean(String path) {
        return this.configuration.contains(path) && this.configuration.getBoolean(path);
    }

    public String getString(String path) {
        return this.configuration.contains(path) ? ChatColor.translateAlternateColorCodes('&', this.configuration.getString(path)) : "ERROR: STRING NOT FOUND";
    }

    public String getString(String path, String callback, boolean colorize) {
        if (!this.configuration.contains(path)) {
            return callback;
        } else {
            return colorize ? ChatColor.translateAlternateColorCodes('&', this.configuration.getString(path)) : this.configuration.getString(path);
        }
    }

    public List<String> getReversedStringList(String path) {
        List<String> list = this.getStringList(path);
        if (list == null) {
            return Collections.singletonList("ERROR: STRING LIST NOT FOUND!");
        } else {
            int size = list.size();
            List<String> toReturn = new ArrayList<>();

            for (int i = size - 1; i >= 0; --i) {
                toReturn.add(list.get(i));
            }

            return toReturn;
        }
    }

    public void reloadConfig() {
        this.configuration = YamlConfiguration.loadConfiguration(this.getFile());

        InputStream defConfigStream = this.plugin.getResource(this.name);
        if (defConfigStream == null) return;

        YamlConfiguration defConfig;
        if (FileConfiguration.UTF8_OVERRIDE) {
            defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8));
        } else {
            final byte[] contents;
            defConfig = new YamlConfiguration();
            try {
                contents = ByteStreams.toByteArray(defConfigStream);
            } catch (final IOException e) {

                return;
            }

            String text = new String(contents, Charset.defaultCharset());

            try {
                defConfig.loadFromString(text);
            } catch (Exception e) {
                this.plugin.getLogger().info("Could not reload " + name + "!");
            }
        }

        this.configuration.setDefaults(defConfig);
    }

    public List<String> getStringList(String path) {
        if (!this.configuration.contains(path)) {
            return Collections.singletonList("ERROR: STRING LIST NOT FOUND!");
        } else {
            ArrayList<String> strings = new ArrayList<>();

            for (String string : this.configuration.getStringList(path)) {
                strings.add(ChatColor.translateAlternateColorCodes('&', string));
            }

            return strings;
        }
    }

    public List<String> getStringListOrDefault(String path, List<String> toReturn) {
        if (!this.configuration.contains(path)) {
            return toReturn;
        } else {
            ArrayList<String> strings = new ArrayList();
            this.configuration.getStringList(path).forEach((s) -> {
                strings.add(ChatColor.translateAlternateColorCodes('&', s));
            });
            return strings;
        }
    }
}
