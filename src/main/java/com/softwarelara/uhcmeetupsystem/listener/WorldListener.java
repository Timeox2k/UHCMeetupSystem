package com.softwarelara.uhcmeetupsystem.listener;

import com.softwarelara.uhcmeetupsystem.UHCMeetupSystem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class WorldListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if(!UHCMeetupSystem.getInstance().getArenaUtils().isPlayerInArena(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if(!UHCMeetupSystem.getInstance().getArenaUtils().isPlayerInArena(player)) {
            event.setCancelled(true);
        }
    }

}
