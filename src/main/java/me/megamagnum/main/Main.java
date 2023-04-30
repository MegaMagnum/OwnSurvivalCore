package me.megamagnum.main;

import me.megamagnum.main.files.Storage;
import me.megamagnum.main.files.commandAFK;
import me.megamagnum.main.files.commandPerformance;
import me.megamagnum.main.files.commandRandom;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandExecutor;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public final class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Storage.setup();

        Storage.get().options().copyDefaults(true);
        Storage.save();

        getConfig().options().copyDefaults(true);
        saveConfig();

      getCommand("withdraw").setExecutor(new commandTakeHeart());
        getCommand("afk").setExecutor(new commandAFK());
        getCommand("random").setExecutor(new commandRandom());
            getCommand("performance").setExecutor(new commandPerformance());
        getCommand("meme").setExecutor(new commandtemp());

    getServer().getPluginManager().registerEvents(new warp(), this);
        getServer().getPluginManager().registerEvents(new onMove(), this);
        getServer().getPluginManager().registerEvents(new eventmessage(), this);
        getServer().getPluginManager().registerEvents(new ChatEvent(), this);

    getServer().getPluginManager().registerEvents(new DeathEvent(), this);
    getServer().getPluginManager().registerEvents(new heartevents(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
    recipes();

        onNewDay();






        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void recipes(){



        ItemStack heart = new ItemStack(Material.SKELETON_SKULL);
        ItemMeta heartmeta = heart.getItemMeta();
        ArrayList<String> heartlore = new ArrayList();
        heartlore.add(ChatColor.DARK_PURPLE + "This crystal holds 1 heart!");
        heartmeta.setLore(heartlore);
        heartmeta.setDisplayName(ChatColor.MAGIC + "" + ChatColor.RED  + "❤");
        heart.setItemMeta(heartmeta);

        ItemStack estar = new ItemStack(Material.NETHER_STAR);
        ItemMeta estarmeta = estar.getItemMeta();
        ArrayList<String> estarlore = new ArrayList();
        estarlore.add(ChatColor.DARK_PURPLE + "This star has been enlightened with glowstone!");
        estarmeta.setLore(estarlore);
        estarmeta.setDisplayName( ChatColor.GOLD  + "Enlightened Star");
        estar.setItemMeta(estarmeta);

        ItemStack eyeender = new ItemStack(Material.ENDER_EYE);
        ItemMeta eyeendermeta = eyeender.getItemMeta();
        ArrayList<String> eyeenderlore = new ArrayList();
        eyeenderlore.add(ChatColor.DARK_PURPLE + "This eye will let the teleport to your enlightened star!");
        eyeendermeta.setLore(eyeenderlore);
        eyeendermeta.setDisplayName( ChatColor.GOLD  + "Warping Eye");
        eyeender.setItemMeta(eyeendermeta);
        ItemStack tpcompas = new ItemStack(Material.BOOK);
        ItemMeta tpcompasmeta = tpcompas.getItemMeta();
        tpcompasmeta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
        ArrayList<String> tpcompaslore = new ArrayList();
        tpcompaslore.add( ChatColor.GOLD  + "This codex will bring you to your friends or foes! ");
        tpcompasmeta.setLore(tpcompaslore);
        tpcompasmeta.setDisplayName(ChatColor.MAGIC + "POW" + ChatColor.RESET + "" + ChatColor.DARK_PURPLE + "  Arcane Codex  " + ChatColor.RESET + "" + ChatColor.MAGIC + "POW");
        tpcompas.setItemMeta(tpcompasmeta);




        if(getConfig().getBoolean("Lifesteal")) {
            ShapedRecipe heartcraft = new ShapedRecipe(new NamespacedKey(this, "heartKey"), heart);
            heartcraft.shape("GGG", "GEG", "GGG");
            heartcraft.setIngredient('G', Material.GOLD_INGOT);
            heartcraft.setIngredient('E', Material.WITHER_SKELETON_SKULL);
            Bukkit.addRecipe(heartcraft);
        }

        ShapedRecipe eyeendercraft = new ShapedRecipe(new NamespacedKey(this, "eyeenderkey"), eyeender);
        eyeendercraft.shape("BBB", "EME", "BBB");
        eyeendercraft.setIngredient('B', Material.BLAZE_POWDER);
        eyeendercraft.setIngredient('E', Material.ENDER_PEARL);
        eyeendercraft.setIngredient('M', Material.ENDER_EYE);



        ShapedRecipe estarcraft = new ShapedRecipe(new NamespacedKey(this, "estarKey"), estar);
        estarcraft.shape("GGG", "GEG", "GGG");
        estarcraft.setIngredient('G', Material.GLOWSTONE);
        estarcraft.setIngredient('E', Material.NETHER_STAR);

        ShapedRecipe tpcompascraft = new ShapedRecipe(new NamespacedKey(this, "tpcompaskey"), tpcompas);
        tpcompascraft.shape("LDL", "LBL", "LEL");
        tpcompascraft.setIngredient('L', Material.LEATHER);
        tpcompascraft.setIngredient('D', Material.DIAMOND);
        tpcompascraft.setIngredient('B', Material.BOOK);
        tpcompascraft.setIngredient('E', Material.ENDER_EYE);


        Bukkit.addRecipe(eyeendercraft);
        Bukkit.addRecipe(estarcraft);
        Bukkit.addRecipe(tpcompascraft);

    }
    public static void onNewDay(){
       World world =  Bukkit.getServer().getWorld("world");
        new BukkitRunnable(){
            public void run(){

                if(world.getTime() == 100){
                    Random random = new Random();
                    int randomint = random.nextInt(4) + 1;
                    if(randomint == 3){
                        for(Player player : Bukkit.getOnlinePlayers()){
                        double maxhealth;
                        maxhealth = player.getMaxHealth();
                        if(maxhealth < 20){
                            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxhealth + 2);
                            player.sendMessage(ChatColor.RED + "Nieuwe dag Nieuwe kansen! Je hebt een hartje erbij gekregen!");

                        }else{
                            player.sendMessage(ChatColor.RED + "Nieuwe dag Nieuwe kansen! Maar jij hebt alle kansen nog dus je hebt geen nieuw hartje gekregen!");
                        }
                    }

                    }else{
                        for(Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(ChatColor.RED + "Jammer genoeg vandaag geen nieuwe kansen!  was:" + randomint);
                    }
                    }

                }


            }


        }.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 0, (long) 0.5F);


    }

}