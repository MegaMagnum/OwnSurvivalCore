package me.megamagnum.main.files;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ColorHelper {
    
    public static ChatColor getStarColor(Player player) {
        ChatColor starColor = ChatColor.WHITE; // Default kleur
        
        if (player.isOp()) {
            starColor = ChatColor.RED; // Rood voor OP
        } else {
            String savedColor = Storage.get().getString("kleur." + player.getUniqueId().toString());
            if (savedColor != null) {
                try {
                    starColor = ChatColor.valueOf(savedColor);
                } catch (IllegalArgumentException ex) {
                    starColor = ChatColor.WHITE;
                }
            }
        }
        
        return starColor;
    }
    
    public static String getDisplayName(Player player) {
        return getStarColor(player) + "★ " + ChatColor.RESET + player.getName();
    }
}
