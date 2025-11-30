package me.megamagnum.main.files;

import me.megamagnum.main.RandomChallengeEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class commandTriggerEvent implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(ChatColor.RED + "Dit command kan alleen vanuit de console gebruikt worden!");
            return true;
        }
        
        RandomChallengeEvent eventManager = RandomChallengeEvent.getInstance();
        if(eventManager == null) {
            sender.sendMessage(ChatColor.RED + "RandomChallengeEvent is niet geïnitialiseerd!");
            return true;
        }
        
        eventManager.triggerChallenge();
        sender.sendMessage(ChatColor.GREEN + "Random challenge event getriggered!");
        
        return true;
    }
}
