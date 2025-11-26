package me.megamagnum.main;


import me.megamagnum.main.files.ColorHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class JoinEvent implements Listener {
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Main mainplugin = Main.getPlugin(Main.class);

        Player p = e.getPlayer();

        String displayName = ColorHelper.getDisplayName(p);
        p.setDisplayName(displayName);
        p.setPlayerListName(displayName);
        
        // Join message met groen icoontje
        e.setJoinMessage(ChatColor.GREEN + "▲ " + ChatColor.RESET + p.getDisplayName() + ChatColor.GREEN + " heeft de server betreden");
        
        // Sound effect voor iedereen
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.playSound(online.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 2.0F);
        }



        final NamespacedKey estarKey;
        final NamespacedKey heartKey;
        final NamespacedKey eyeenderkey;
        final NamespacedKey tpcompaskey;

        estarKey = new NamespacedKey(Main.getPlugin(Main.class), "estarKey");

        eyeenderkey = new NamespacedKey(Main.getPlugin(Main.class), "eyeenderkey");

        tpcompaskey =  new NamespacedKey(Main.getPlugin(Main.class), "tpcompaskey");


        p.discoverRecipe(estarKey);
        p.discoverRecipe(eyeenderkey);
        p.discoverRecipe(tpcompaskey);

        if(mainplugin.getConfig().getBoolean("Lifesteal")) {
            heartKey = new NamespacedKey(Main.getPlugin(Main.class), "heartKey");
            p.discoverRecipe(heartKey);
        }



    }
    
    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        // Leave message met rood icoontje
        event.setQuitMessage(ChatColor.RED + "▼ " + ChatColor.RESET + event.getPlayer().getDisplayName() + ChatColor.RED + " heeft de server verlaten");
        
        // Sound effect voor iedereen
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.playSound(online.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.5F, 0.5F);
        }
    }


}