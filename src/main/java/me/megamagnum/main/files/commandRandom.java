package me.megamagnum.main.files;

import me.megamagnum.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class commandRandom implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0 || args.length > 1){
            sender.sendMessage(ChatColor.RED + "Geef een bound weer. Voorbeeld /random 10 ");
        }else {
            Player p = (Player) sender;
            Random random = new Random();
         int nummer =   random.nextInt(Integer.parseInt(args[0])) + 1;
         sender.sendMessage(ChatColor.RED + "Jouw random nummer isss.....");
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 0.2F);

            new BukkitRunnable(){
            public void run(){
                sender.sendMessage(ChatColor.RED + "" +nummer+"");

                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 2);



            }

        }.runTaskLater(Main.getPlugin(Main.class), 60);
        }

        return true;
    }
}