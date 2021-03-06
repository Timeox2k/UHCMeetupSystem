package com.softwarelara.uhcmeetupsystem.listener;

import com.softwarelara.uhcmeetupsystem.UHCMeetupSystem;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.world.WorldInitEvent;

public class WorldListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (!UHCMeetupSystem.getInstance().getArenaUtils().isPlayerInArena(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof ArmorStand) && !(entity instanceof Player) && !(entity instanceof ItemFrame)) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onWorldInit(WorldInitEvent event) {
        if (event.getWorld().getName().contains("Meetup")) {
            event.getWorld().setKeepSpawnInMemory(false);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (!UHCMeetupSystem.getInstance().getArenaUtils().isPlayerInArena(player)) {
            event.setCancelled(true);
        }
    }

}
