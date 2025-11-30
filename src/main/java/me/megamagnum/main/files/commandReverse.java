package me.megamagnum.main.files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class commandReverse implements CommandExecutor {
    
    private static final Map<String, Boolean> reversedPlayers = new HashMap<>();
    private final JavaPlugin plugin;
    
    public commandReverse(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public static boolean isReversed(Player player) {
        return reversedPlayers.getOrDefault(player.getName(), false);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(ChatColor.RED + "Dit command kan alleen vanuit de console gebruikt worden!");
            return true;
        }
        
        int duration = 30; // Default 30 seconden
        
        if(args.length == 0) {
            // Reverse voor iedereen
            for(Player p : Bukkit.getOnlinePlayers()) {
                reversedPlayers.put(p.getName(), true);
            }
            sender.sendMessage(ChatColor.GREEN + "Reverse movement geactiveerd voor iedereen (" + duration + "s)");
            
            // Auto-disable na duration
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    reversedPlayers.remove(p.getName());
                }
                sender.sendMessage(ChatColor.YELLOW + "Reverse movement uitgeschakeld voor iedereen");
            }, 20L * duration);
            
        } else {
            // Reverse voor specifieke speler
            String targetName = args[0];
            Player target = Bukkit.getPlayer(targetName);
            
            if(target == null) {
                sender.sendMessage(ChatColor.RED + "Speler niet gevonden!");
                return true;
            }
            
            // Check voor optionele duration
            if(args.length > 1) {
                try {
                    duration = Integer.parseInt(args[1]);
                } catch(NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Ongeldige duration! Gebruik: /reverse <speler> [seconden]");
                    return true;
                }
            }
            
            reversedPlayers.put(target.getName(), true);
            sender.sendMessage(ChatColor.GREEN + "Reverse movement geactiveerd voor " + target.getName() + " (" + duration + "s)");
            
            // Auto-disable na duration
            final int finalDuration = duration;
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                reversedPlayers.remove(target.getName());
                sender.sendMessage(ChatColor.YELLOW + "Reverse movement uitgeschakeld voor " + target.getName());
            }, 20L * finalDuration);
        }
        
        return true;
    }
}
