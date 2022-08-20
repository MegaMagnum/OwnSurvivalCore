package me.megamagnum.main;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class commandtemp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player target =  Bukkit.getPlayerExact(args[0]);
        target.playSound(target.getLocation(), Sound.ITEM_SPYGLASS_USE, 1, 1);



        return false;
    }
}