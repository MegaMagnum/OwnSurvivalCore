package me.megamagnum.main;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;


public class commandTakeHeart implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;

        double maxhealth;
        maxhealth = p.getMaxHealth();
        if(maxhealth > 2) {
            p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxhealth - 2);

            ItemStack heart = new ItemStack(Material.SKELETON_SKULL);
            ItemMeta heartmeta = heart.getItemMeta();
            ArrayList<String> heartlore = new ArrayList();
            heartlore.add(ChatColor.DARK_PURPLE + "This crystal holds 1 heart!");
            heartmeta.setLore(heartlore);
            heartmeta.setDisplayName(ChatColor.MAGIC + "" + ChatColor.RED + "❤");
            heart.setItemMeta(heartmeta);


            p.getInventory().addItem(heart);
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 0.8F);
        }else{
            p.sendMessage(ChatColor.RED + "Ga je nou echt je zelf vermoorden?");
        }

        return true;
    }
}