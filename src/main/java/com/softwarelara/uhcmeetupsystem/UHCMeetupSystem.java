package com.softwarelara.uhcmeetupsystem;

import com.softwarelara.uhcmeetupsystem.listener.PlayerListener;
import com.softwarelara.uhcmeetupsystem.listener.WorldListener;
import com.softwarelara.uhcmeetupsystem.utils.ArenaUtils;
import com.softwarelara.uhcmeetupsystem.utils.ConfigurationUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class UHCMeetupSystem extends JavaPlugin {


    private static UHCMeetupSystem instance;
    private ConfigurationUtils configurationUtils;

    private ArenaUtils arenaUtils;
    @Override
    public void onEnable() {
      final PluginManager pluginManager = getServer().getPluginManager();
        instance = this;
        configurationUtils = new ConfigurationUtils();
        arenaUtils = new ArenaUtils();

        pluginManager.registerEvents(new PlayerListener(), this);
        pluginManager.registerEvents(new WorldListener(), this);
        loadConfig();
    }

    private void loadConfig() {
        Configuration config = getInstance().getConfig();

        config.options().copyDefaults(true);
        config.addDefault("PREFIX", "&e⋆&dUHC&5Systems&r&e⋆");
        config.addDefault("LOBBY_ARENA_SELECTOR_ITEM", "SNOWBALL");
        config.addDefault("LOBBY_ARENA_SELECTOR_DISPLAYNAME", "&aSelect Arena to join");

        saveConfig();
    }

    public static UHCMeetupSystem getInstance() {
        return instance;
    }

    public ConfigurationUtils getConfigurationUtils() {
        return configurationUtils;
    }

    public String getPrefix() {
        return ConfigurationUtils.getStringOfConfigPath("PREFIX");
    }

    public ArenaUtils getArenaUtils() {
        return arenaUtils;
    }

    @Override
    public void onDisable() {
    }
}
