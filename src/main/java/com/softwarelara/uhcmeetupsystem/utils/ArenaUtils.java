package com.softwarelara.uhcmeetupsystem.utils;

import com.softwarelara.uhcmeetupsystem.UHCMeetupSystem;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ArenaUtils {

    //ARENAID
    private final ArrayList<String> existingArenas = new ArrayList<>();
    //Player:ARENAID
    private final ConcurrentHashMap<UUID, String> playersInArena = new ConcurrentHashMap<>();
    private final UHCMeetupSystem uhcMeetupSystem = UHCMeetupSystem.getInstance();
    private final Logger logger = uhcMeetupSystem.getLogger();

    public void createArena() {
        String arenaID = "Meetup-" + (existingArenas.size());
        if (!existingArenas.contains(arenaID)) {
            existingArenas.add(arenaID);
            WorldCreator wc = new WorldCreator(arenaID);
            wc.environment(World.Environment.NORMAL);

            //TODO: Change this back to "NORMAL" before release. This is just to improve the reload/restart speed.
            wc.type(WorldType.FLAT);
            wc.generateStructures(false);
            wc.createWorld();

            Bukkit.createWorld(wc);
            World world = Bukkit.getWorld(arenaID);
            assert world != null;
            world.setBiome(world.getSpawnLocation(), Biome.PLAINS);
            WorldBorder worldBorder = world.getWorldBorder();

            worldBorder.setCenter(0, 0);
            worldBorder.setSize(50);
            worldBorder.setDamageAmount(1D);

            logger.info("Created Arena with ID: " + arenaID);
        }
    }

    public void deleteArena(String arenaID) {
        if (existingArenas.contains(arenaID)) {
            if (Bukkit.getWorld(arenaID) != null) {
                World world = Bukkit.getWorld(arenaID);

                if (world != null) {

                    for (Player all : Bukkit.getOnlinePlayers()) {
                        if (all.getWorld().getName().equalsIgnoreCase(arenaID)) {
                            all.teleport(LocationUtils.getLobbySpawn());
                        }
                    }

                    Bukkit.unloadWorld(world, false);
                    deleteWorld(world.getWorldFolder());
                }
                existingArenas.remove(arenaID);
            }

            logger.info("Deleted Arena with ID: " + arenaID);
        }
    }

    public void deleteAllArenas() {
        logger.info("Found " + existingArenas.size() + " Arenas! Will delete them now..");

        for (String arena : existingArenas) {

            for (Player all : Bukkit.getOnlinePlayers()) {
                if (all.getWorld().getName().equalsIgnoreCase(arena)) {
                    all.teleport(LocationUtils.getLobbySpawn());
                }
            }

            World world = Bukkit.getWorld(arena);

            if (world != null) {
                Bukkit.unloadWorld(arena, false);
                deleteWorld(world.getWorldFolder());
            }
        }

        existingArenas.clear();
        logger.info("Arenas deleted. You can now shutdown the Server.");
    }

    public boolean isPlayerInArenaByArenaID(Player player, String arenaID) {
        UUID uuid = player.getUniqueId();
        return playersInArena.containsKey(uuid) && playersInArena.get(uuid).equalsIgnoreCase(arenaID);
    }

    public boolean isPlayerInArena(Player player) {
        UUID uuid = player.getUniqueId();
        return playersInArena.containsKey(uuid);
    }

    public void openArenaInventory(Player player) {
        final String arenaChooserDisplayName = ConfigurationUtils.getStringOfConfigPath("LOBBY_ARENA_SELECTOR_DISPLAYNAME");
        Inventory inventory = Bukkit.createInventory(player, 5 * 9, arenaChooserDisplayName);

        int arenaCount = 0;
        for (String arena : existingArenas) {
            World arenaWorld = Bukkit.getWorld(arena);

            if (arenaWorld != null) {

                int playerCount = arenaWorld.getPlayers().size();

                int maxPlayerCount = uhcMeetupSystem.getConfigurationUtils().getIntegerOfConfigPath("ARENA_MAX_PLAYERS");
                ItemStack defaultArenaIcon = new ItemStack(Material.GREEN_DYE, 1);
                ItemMeta defaultArenaIconItemMeta = defaultArenaIcon.getItemMeta();
                defaultArenaIconItemMeta.setDisplayName("§aUHCMeetup-" + arenaCount);
                if (playerCount >= maxPlayerCount) {
                    defaultArenaIconItemMeta.setLore(Arrays.asList("§cPlayers§8: §c" + playerCount + "§8/§c" + maxPlayerCount, "§4§lGame is full!"));
                    defaultArenaIcon.setItemMeta(defaultArenaIconItemMeta);
                } else {
                    defaultArenaIconItemMeta.setDisplayName("§aUHCMeetup-" + arenaCount);
                    defaultArenaIconItemMeta.setLore(Arrays.asList("§aPlayers§8: §a" + playerCount + "§8/§a" + maxPlayerCount, "§a§lClick to join!"));
                }
                defaultArenaIcon.setItemMeta(defaultArenaIconItemMeta);
                inventory.addItem(defaultArenaIcon);

            }

            arenaCount++;
        }

        player.openInventory(inventory);
    }


    public void joinArena(Player player, String arenaID) {
        UUID uuid = player.getUniqueId();
        playersInArena.put(uuid, arenaID);
        World world = Bukkit.getWorld(arenaID);
        player.teleport(new Location(world, 0, world.getHighestBlockYAt(0, 0) + 1, 0));
        player.getInventory().clear();
        player.sendMessage(uhcMeetupSystem.getPrefix() + " §aYou joined the Arena " + arenaID);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 10);
    }

    public void leaveArena(Player player, String arenaID) {
        UUID uuid = player.getUniqueId();
        playersInArena.remove(uuid, arenaID);
        World world = Bukkit.getWorlds().get(0);
        LocationUtils.teleportToLobby(player);
        player.sendMessage(uhcMeetupSystem.getPrefix() + " §aYou left the Arena " + arenaID);
    }

    public boolean doesArenaExist(String arenaID) {
        return existingArenas.contains(arenaID);
    }

    public void terminatePlayer(Player player) {
        if (playersInArena.containsKey(player.getUniqueId())) {
            String arenaID = playersInArena.get(player.getUniqueId());
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (all.getWorld().getName().equalsIgnoreCase(arenaID)) {
                    all.sendMessage(UHCMeetupSystem.getInstance().getPrefix() + " §4" + player.getName() + " §cgot terminated!");
                }
            }
            playersInArena.remove(player.getUniqueId());
        }
    }
    //THANKS ThunderWaffeMC
    public boolean deleteWorld(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

}
