package com.softwarelara.uhcmeetupsystem.commands;

import com.softwarelara.uhcmeetupsystem.UHCMeetupSystem;
import com.softwarelara.uhcmeetupsystem.utils.LocationUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHUB implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        final Player player = (Player) sender;
        final String prefix = UHCMeetupSystem.getInstance().getPrefix();
        if (LocationUtils.getLobbySpawn().getWorld() != player.getWorld()) {
            LocationUtils.teleportToLobby(player);
            UHCMeetupSystem.getInstance().getArenaUtils().terminatePlayer(player);
        } else {
            player.sendMessage(prefix + " §cYou are already at the HUB!");
        }

        return true;
    }
}
