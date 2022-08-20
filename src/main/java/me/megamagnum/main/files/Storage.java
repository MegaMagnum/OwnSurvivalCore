package me.megamagnum.main.files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Storage {

    private static File file;
    private static FileConfiguration storage;


    // Finds || generates info storage file
    public static void setup(){
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("CorevanTim").getDataFolder(), "storage.yml");

        if(!file.exists()) {
            try {
                file.createNewFile();
            }catch (IOException e){
                //oww
            }

        }
        storage = YamlConfiguration.loadConfiguration(file);


    }


    public static FileConfiguration get(){
        return storage;
    }
    public static void save(){
        try {
            storage.save(file);
        }catch (IOException e) {
            System.out.println(ChatColor.RED + "ERROR: Couldn't save the storage file!");
        }

    }
    public static void reload(){
        storage = YamlConfiguration.loadConfiguration(file);
    }

}