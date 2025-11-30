package me.megamagnum.main.files;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class commandMyCoords implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Alleen spelers kunnen dit commando gebruiken!");
            return true;
        }
        
        Player player = (Player) sender;
        Location loc = player.getLocation();
        World.Environment env = loc.getWorld().getEnvironment();
        
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "📍 MIJN COÖRDINATEN");
        player.sendMessage("");
        
        if(env == World.Environment.NORMAL) {
            // In overworld - show nether coords
            player.sendMessage(ChatColor.GREEN + "Overworld: " + ChatColor.WHITE + x + ", " + y + ", " + z);
            player.sendMessage(ChatColor.RED + "Nether: " + ChatColor.WHITE + (x / 8) + ", " + y + ", " + (z / 8));
            player.sendMessage("");
            player.sendMessage(ChatColor.GRAY + "💡 Deel deze coördinaten door 8 in de Nether");
            
        } else if(env == World.Environment.NETHER) {
            // In nether - show overworld coords
            player.sendMessage(ChatColor.RED + "Nether: " + ChatColor.WHITE + x + ", " + y + ", " + z);
            player.sendMessage(ChatColor.GREEN + "Overworld: " + ChatColor.WHITE + (x * 8) + ", " + y + ", " + (z * 8));
            player.sendMessage("");
            player.sendMessage(ChatColor.GRAY + "💡 Vermenigvuldig deze coördinaten met 8 in de Overworld");
            
        } else if(env == World.Environment.THE_END) {
            // In the end
            player.sendMessage(ChatColor.LIGHT_PURPLE + "The End: " + ChatColor.WHITE + x + ", " + y + ", " + z);
            player.sendMessage("");
            player.sendMessage(ChatColor.GRAY + "💡 The End heeft geen Nether portal conversie");
        }
        
        player.sendMessage("");
        
        return true;
    }
}
