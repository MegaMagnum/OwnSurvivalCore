package me.megamagnum.main;


import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;



public class JoinEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Main mainplugin = Main.getPlugin(Main.class);


        Player p = e.getPlayer();

        if(p.isOp()) {
            p.setDisplayName(ChatColor.DARK_RED + ""  + "◆ " + ChatColor.RESET + p.getName());
            p.setPlayerListName(ChatColor.DARK_RED + ""  + "◆ " + ChatColor.RESET + p.getName());

        }
        else{
            p.setDisplayName(ChatColor.GREEN + ""  + "◆ " + ChatColor.RESET + p.getName());
            p.setPlayerListName(ChatColor.GREEN + ""  + "◆ " + ChatColor.RESET + p.getName());

        }



        final NamespacedKey estarKey;
        final NamespacedKey heartKey;
        final NamespacedKey eyeenderkey;

        estarKey = new NamespacedKey(Main.getPlugin(Main.class), "estarKey");

        eyeenderkey = new NamespacedKey(Main.getPlugin(Main.class), "eyeenderkey");


        p.discoverRecipe(estarKey);
        p.discoverRecipe(eyeenderkey);


        if(mainplugin.getConfig().getBoolean("Lifesteal")) {
            heartKey = new NamespacedKey(Main.getPlugin(Main.class), "heartKey");
            p.discoverRecipe(heartKey);
        }



    }


}
