package com.softwarelara.uhcmeetupsystem;

import com.softwarelara.uhcmeetupsystem.commands.CommandHUB;
import com.softwarelara.uhcmeetupsystem.commands.CommandSetLobbySpawn;
import com.softwarelara.uhcmeetupsystem.listener.PlayerListener;
import com.softwarelara.uhcmeetupsystem.listener.WorldListener;
import com.softwarelara.uhcmeetupsystem.utils.ArenaUtils;
import com.softwarelara.uhcmeetupsystem.utils.ConfigurationUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

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

        //Clear All Mobs that we dont need.


        int entityCount = 0;

        for(World world : getServer().getWorlds()) {
            for(Entity entity : world.getEntities()) {
                if(!(entity instanceof ArmorStand) && !(entity instanceof Player) && !(entity instanceof ItemFrame)) {
                    entity.remove();
                    entityCount++;
                }
            }
        }
        getLogger().info("Removed " + entityCount + " useless Mobs/Animals/Items.");
    }

    private void loadConfig() {
        Configuration config = getInstance().getConfig();

        config.options().copyDefaults(true);
        config.addDefault("PREFIX", "&e⋆&dUHC&5Systems&r&e⋆");
        config.addDefault("LOBBY_ARENA_SELECTOR_ITEM", "SNOWBALL");
        config.addDefault("LOBBY_ARENA_SELECTOR_DISPLAYNAME", "&aSelect Arena to join");
        config.addDefault("ARENA_MAX_PLAYERS", 10);

        getLogger().info("Server got started, this means we will start 3 Arenas to start with. If we need more, we will create more dynamically.");
        for (int i = 0; i < 3; i++) {
            getArenaUtils().createArena();
        }
        getLogger().info("Arenas created!");
        saveConfig();

        Objects.requireNonNull(getCommand("hub")).setExecutor(new CommandHUB());
        Objects.requireNonNull(getCommand("setlobbyspawn")).setExecutor(new CommandSetLobbySpawn());

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
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer("§c§lImportant Restart!\n\n§cWe will be back in a few Seconds!");
        }
        getArenaUtils().deleteAllArenas();
    }
}
