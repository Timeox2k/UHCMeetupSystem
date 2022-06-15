package com.softwarelara.uhcmeetupsystem.listener;

import com.softwarelara.uhcmeetupsystem.UHCMeetupSystem;
import com.softwarelara.uhcmeetupsystem.utils.ConfigurationUtils;
import com.softwarelara.uhcmeetupsystem.utils.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);
        player.setHealth(20D);
        player.setFoodLevel(20);
        player.getActivePotionEffects().clear();
        player.setFlying(false);
        player.getInventory().clear();

        final UHCMeetupSystem uhcMeetupSystem = UHCMeetupSystem.getInstance();
        final String prefix = uhcMeetupSystem.getPrefix();
        player.sendMessage(prefix + " §aWelcome §d" + player.getName() + " §aon §dUHCSystems.net§a!");
        player.sendTitle("§aHey!", "§eNice to see you!");

        final String arenaChooserMaterial = ConfigurationUtils.getStringOfConfigPath("LOBBY_ARENA_SELECTOR_ITEM");
        final String arenaChooserDisplayName = ConfigurationUtils.getStringOfConfigPath("LOBBY_ARENA_SELECTOR_DISPLAYNAME");

        player.getInventory().setItem(4, ItemStackBuilder.buildItemStack(Material.getMaterial(arenaChooserMaterial), 1, arenaChooserDisplayName));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (!UHCMeetupSystem.getInstance().getArenaUtils().isPlayerInArena(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
            Player player = (Player) event.getPlayer();

            if (!UHCMeetupSystem.getInstance().getArenaUtils().isPlayerInArena(player)) {
                event.setCancelled(true);
            }
    }

    @EventHandler
    public void onInventory(InventoryClickEvent event) {
        if(event.getCurrentItem() == null || !(event.getWhoClicked() instanceof  Player) || event.getCurrentItem().getType() == Material.AIR || event.getCurrentItem().getItemMeta() == null) return;
        final String arenaChooserDisplayName = ConfigurationUtils.getStringOfConfigPath("LOBBY_ARENA_SELECTOR_DISPLAYNAME");

        if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(arenaChooserDisplayName)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (!UHCMeetupSystem.getInstance().getArenaUtils().isPlayerInArena(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(player.getInventory().getItemInMainHand().getType() == Material.AIR) return;

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        final String arenaChooserDisplayName = ConfigurationUtils.getStringOfConfigPath("LOBBY_ARENA_SELECTOR_DISPLAYNAME");

        if(itemInHand.getItemMeta().getDisplayName().equalsIgnoreCase(arenaChooserDisplayName)) {
            event.setCancelled(true);
        }
    }

}
