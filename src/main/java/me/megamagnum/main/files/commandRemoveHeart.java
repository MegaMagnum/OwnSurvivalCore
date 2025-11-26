package me.megamagnum.main.files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class commandRemoveHeart implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Alleen console kan dit command gebruiken
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.RED + "Dit command kan alleen vanuit de console worden uitgevoerd!");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Gebruik: removeheart <speler> <aantal>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Speler niet gevonden!");
            return true;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Gebruik een geldig nummer!");
            return true;
        }

        if (amount <= 0) {
            sender.sendMessage(ChatColor.RED + "Aantal moet groter zijn dan 0!");
            return true;
        }

        double currentMaxHealth = target.getMaxHealth();
        double newMaxHealth = currentMaxHealth - (amount * 2);
        
        if (newMaxHealth < 2) {
            sender.sendMessage(ChatColor.RED + "Kan niet minder dan 1 hartje geven!");
            return true;
        }
        
        target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newMaxHealth);
        
        // Geluidjes en bericht
        target.sendMessage(ChatColor.RED + "✦ Je hebt " + amount + " hartje(s) verloren!");
        target.playSound(target.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 0.8F);
        target.playSound(target.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.5F, 1.5F);
        
        sender.sendMessage(ChatColor.YELLOW + "✔ " + target.getName() + " heeft " + amount + " hartje(s) verloren!");
        
        return true;
    }
}
