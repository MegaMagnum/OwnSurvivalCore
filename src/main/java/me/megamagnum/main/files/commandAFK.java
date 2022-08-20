package me.megamagnum.main.files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class commandAFK implements CommandExecutor  {
    public static ArrayList<UUID> afklist = new ArrayList<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player  p = (Player) sender;
            if(afklist.contains(p.getUniqueId())){
                p.setDisplayName(ChatColor.GREEN + ""  + "◆ " + ChatColor.RESET + p.getName());
                p.setPlayerListName(ChatColor.GREEN + ""  + "◆ " + ChatColor.RESET + p.getName());
                p.setSleepingIgnored(false);

                afklist.remove(p.getUniqueId());
                Bukkit.broadcastMessage(p.getDisplayName() + ChatColor.GRAY + "  is vanaf nu niet meer AFK!");

            }else{
                p.setDisplayName(ChatColor.GREEN + ""  + "◆ " + ChatColor.GRAY + p.getName());
                p.setPlayerListName(ChatColor.GREEN + ""  + "◆ " + ChatColor.GRAY + p.getName());
                p.setSleepingIgnored(true);

                afklist.add(p.getUniqueId());
                Bukkit.broadcastMessage(p.getDisplayName() + ChatColor.GRAY + " is vanaf nu AFK!");
            }



            return true;
        }
        return false;
    }
}