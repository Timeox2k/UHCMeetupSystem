package com.softwarelara.uhcmeetupsystem.listener;

import com.softwarelara.uhcmeetupsystem.UHCMeetupSystem;
import com.softwarelara.uhcmeetupsystem.utils.ConfigurationUtils;
import com.softwarelara.uhcmeetupsystem.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);
        player.setHealth(20D);
        player.setFoodLevel(20);
        player.getActivePotionEffects().clear();
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.sendMessage(UHCMeetupSystem.getInstance().getPrefix() + " §aWelcome §d" + player.getName() + " §aon §dUHCSystems.net§a!");
        player.sendTitle("§aHey!", "§eNice to see you!");


        LocationUtils.teleportToLobby(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();

        final UHCMeetupSystem uhcMeetupSystem = UHCMeetupSystem.getInstance();

        if (uhcMeetupSystem.getArenaUtils().isPlayerInArena(player)) {
            uhcMeetupSystem.getArenaUtils().terminatePlayer(player);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String prefix = UHCMeetupSystem.getInstance().getPrefix();

        if(event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            for(Player all : Bukkit.getOnlinePlayers()) {
                if (all.getWorld() == player.getWorld()) {
                    all.sendMessage(prefix + " §cPlayer §4" + player.getName() + " §cgot killed by §4" + killer.getName() + "§c!");
                }
            }
        } else {
            for(Player all : Bukkit.getOnlinePlayers()) {
                if(all.getWorld() == player.getWorld()) {
                    all.sendMessage(prefix + " §4" + player.getName() + " §cdied.");
                }
            }
        }
        UHCMeetupSystem.getInstance().getArenaUtils().terminatePlayer(player);
        player.sendMessage(prefix + " §cYou died.");
        event.setDeathMessage("");
        event.setDroppedExp(0);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        event.setRespawnLocation(player.getLocation());
        player.setAllowFlight(true);
        player.setFlying(true);
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
            final Player player = event.getPlayer();

            if (!UHCMeetupSystem.getInstance().getArenaUtils().isPlayerInArena(player)) {
                event.setCancelled(true);
            }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupArrowEvent event) {
        final Player player = event.getPlayer();

        if (!UHCMeetupSystem.getInstance().getArenaUtils().isPlayerInArena(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (!UHCMeetupSystem.getInstance().getArenaUtils().isPlayerInArena(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (!UHCMeetupSystem.getInstance().getArenaUtils().isPlayerInArena(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventory(InventoryClickEvent event) {
        UHCMeetupSystem uhcMeetupSystem = UHCMeetupSystem.getInstance();
        if (event.getCurrentItem() == null || !(event.getWhoClicked() instanceof Player) || event.getCurrentItem().getType() == Material.AIR || event.getCurrentItem().getItemMeta() == null)
            return;
        final String arenaChooserDisplayName = ConfigurationUtils.getStringOfConfigPath("LOBBY_ARENA_SELECTOR_DISPLAYNAME");


        final ItemStack currentItem = event.getCurrentItem();
        final Player player = (Player) event.getWhoClicked();
        if (currentItem.getItemMeta().getDisplayName().equalsIgnoreCase(arenaChooserDisplayName)) {
            event.setCancelled(true);
        }

        if (event.getView().getTitle().equalsIgnoreCase(arenaChooserDisplayName)) {
            event.setCancelled(true);

            if (currentItem.getType() == Material.GREEN_DYE) {
                String parsedArenaID = "Meetup-" + currentItem.getItemMeta().getDisplayName().split("UHCMeetup-")[1];

                if (uhcMeetupSystem.getArenaUtils().doesArenaExist(parsedArenaID)) {
                    player.closeInventory();
                    uhcMeetupSystem.getArenaUtils().joinArena(player, parsedArenaID);
                }
            }
        }

    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (!UHCMeetupSystem.getInstance().getArenaUtils().isPlayerInArena(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) return;

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        ItemMeta itemInHandMeta = itemInHand.getItemMeta();
        final String arenaChooserDisplayName = ConfigurationUtils.getStringOfConfigPath("LOBBY_ARENA_SELECTOR_DISPLAYNAME");

        assert itemInHandMeta != null;
        if (itemInHandMeta.getDisplayName().equalsIgnoreCase(arenaChooserDisplayName)) {
            UHCMeetupSystem.getInstance().getArenaUtils().openArenaInventory(player);
            event.setCancelled(true);
        }
    }

}
