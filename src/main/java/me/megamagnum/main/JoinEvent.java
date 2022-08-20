package me.megamagnum.main;


import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
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

        if(p.isOp()) {
            p.setDisplayName(ChatColor.DARK_RED + ""  + "◆ " + ChatColor.RESET + p.getName());
            p.setPlayerListName(ChatColor.DARK_RED + ""  + "◆ " + ChatColor.RESET + p.getName());

        }
        else{
            p.setDisplayName(ChatColor.GREEN + ""  + "◆ " + ChatColor.RESET + p.getName());
            p.setPlayerListName(ChatColor.GREEN + ""  + "◆ " + ChatColor.RESET + p.getName());

        }
        String joinmessage = ChatColor.YELLOW + " is weer minecraft gaan spelen in plaats van reports doen!";
        e.setJoinMessage(p.getDisplayName() + joinmessage);



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
        String leavemessage = ChatColor.YELLOW + " gaat weer reports doen!";
        event.setQuitMessage(event.getPlayer().getDisplayName() + leavemessage);
    }


}