package com.softwarelara.uhcmeetupsystem.commands;

import com.softwarelara.uhcmeetupsystem.UHCMeetupSystem;
import com.softwarelara.uhcmeetupsystem.utils.LocationUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetLobbySpawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        final Player player = (Player) sender;
        final String prefix = UHCMeetupSystem.getInstance().getPrefix();

        if (player.hasPermission("UHCMeetupSystem.admin")) {
            LocationUtils.setLobbySpawn(player.getLocation());
            player.sendMessage("§aLobbyspawn set.");
        } else {
            player.sendMessage(prefix + " §cYou dont Permissions to use this Command! (UHCMeetupSystem.admin)");
        }

        return true;
    }

}
