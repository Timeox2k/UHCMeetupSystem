package com.softwarelara.uhcmeetupsystem.utils;

import com.softwarelara.uhcmeetupsystem.UHCMeetupSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

public class LocationUtils {

    public static void setLobbySpawn(Location location) {
        UHCMeetupSystem uhcMeetupSystem = UHCMeetupSystem.getInstance();

        Configuration configuration = uhcMeetupSystem.getConfig();

        configuration.set("LOBBY_SPAWN_X", location.getX());
        configuration.set("LOBBY_SPAWN_Y", location.getY());
        configuration.set("LOBBY_SPAWN_Z", location.getZ());
        configuration.set("LOBBY_SPAWN_WORLD", location.getWorld().getName());
        configuration.set("LOBBY_SPAWN_YAW", location.getYaw());
        configuration.set("LOBBY_SPAWN_PITCH", location.getPitch());
        uhcMeetupSystem.saveConfig();
    }

    public static Location getLobbySpawn() {
        UHCMeetupSystem uhcMeetupSystem = UHCMeetupSystem.getInstance();

        Configuration configuration = uhcMeetupSystem.getConfig();
        double x = configuration.getDouble("LOBBY_SPAWN_X");
        double y = configuration.getDouble("LOBBY_SPAWN_Y");
        double z = configuration.getDouble("LOBBY_SPAWN_Z");
        String worldName = configuration.getString("LOBBY_SPAWN_WORLD");
        float yaw = (float) configuration.getDouble("LOBBY_SPAWN_YAW");
        float pitch = (float) configuration.getDouble("LOBBY_SPAWN_PITCH");
        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }

    public static boolean isLobbySet() {
        Configuration configuration = UHCMeetupSystem.getInstance().getConfig();

        return configuration.isSet("LOBBY_SPAWN_X");
    }

    public static void teleportToLobby(Player player) {
        if (!LocationUtils.isLobbySet()) {
            player.sendMessage("§4Warning§c: Lobbyspawn is not set! Please go to your Lobbyspawn and type §c/setLobbySpawn");
        } else {
            player.teleport(LocationUtils.getLobbySpawn());
        }
        final UHCMeetupSystem uhcMeetupSystem = UHCMeetupSystem.getInstance();
        final String prefix = uhcMeetupSystem.getPrefix();

        final String arenaChooserMaterial = ConfigurationUtils.getStringOfConfigPath("LOBBY_ARENA_SELECTOR_ITEM");
        final String arenaChooserDisplayName = ConfigurationUtils.getStringOfConfigPath("LOBBY_ARENA_SELECTOR_DISPLAYNAME");
        player.getInventory().setItem(4, ItemStackBuilder.buildItemStack(Material.getMaterial(arenaChooserMaterial), 1, arenaChooserDisplayName));
    }

}
