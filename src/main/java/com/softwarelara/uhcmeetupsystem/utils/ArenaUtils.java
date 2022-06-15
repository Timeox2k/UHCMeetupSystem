package com.softwarelara.uhcmeetupsystem.utils;

import com.softwarelara.uhcmeetupsystem.UHCMeetupSystem;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class ArenaUtils {

    //ARENAID
    private final ArrayList<String> existingArenas = new ArrayList<>();

    //Player:ARENAID
    private final HashMap<UUID, String> playersInArena = new HashMap<>();
    private final UHCMeetupSystem uhcMeetupSystem = UHCMeetupSystem.getInstance();
    private final Logger logger = uhcMeetupSystem.getLogger();

    public void createArena() {
        String arenaID = "UMS-" + System.currentTimeMillis();
        if(!existingArenas.contains(arenaID)) {
            existingArenas.add(arenaID);
            //TODO:
            //Create ArenaWorld by ArenaID
            logger.info("Created Arena with ID: " + arenaID);
        }
    }

    public void deleteArena() {
        String arenaID = "UMS-" + System.currentTimeMillis();
        if(!existingArenas.contains(arenaID)) {
            existingArenas.add(arenaID);
            //TODO:
            //Delete ArenaWorld by ArenaID
            logger.info("Created Arena with ID: " + arenaID);
        }
    }

    public boolean isPlayerInArenaByArenaID(Player player, String arenaID) {
        UUID uuid = player.getUniqueId();
        return playersInArena.containsKey(uuid) && playersInArena.get(uuid).equalsIgnoreCase(arenaID);
    }

    public boolean isPlayerInArena(Player player) {
        UUID uuid = player.getUniqueId();
        return playersInArena.containsKey(uuid);
    }

}
