package com.softwarelara.uhcmeetupsystem.utils;

import com.softwarelara.uhcmeetupsystem.UHCMeetupSystem;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
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
        String arenaID = "UMS-" + System.nanoTime();
        if(!existingArenas.contains(arenaID)) {
            existingArenas.add(arenaID);
            WorldCreator wc = new WorldCreator(arenaID);

            wc.environment(World.Environment.NORMAL);
            wc.type(WorldType.NORMAL);
            wc.generateStructures(false);
            wc.createWorld();

            Bukkit.createWorld(wc);
            logger.info("Created Arena with ID: " + arenaID);
        }
    }

    public void deleteArena(String arenaID) {
        if(existingArenas.contains(arenaID)) {
            if(Bukkit.getWorld(arenaID) != null) {
                 World world = Bukkit.getWorld(arenaID);

                if(world != null) {
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

        for(String arena : existingArenas) {
            World world = Bukkit.getWorld(arena);

            if(world != null) {
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
        Inventory inventory = Bukkit.createInventory(player, 5*9, "§aSelect Arena to play");

        player.openInventory(inventory);
    }


    //THANKS ThunderWaffeMC
    public boolean deleteWorld(File path) {
        if(path.exists()) {
            File files[] = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return(path.delete());
    }

}
