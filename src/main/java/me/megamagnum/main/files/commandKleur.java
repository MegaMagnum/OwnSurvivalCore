package me.megamagnum.main.files;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class commandKleur implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Dit command kan alleen door spelers gebruikt worden!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "Gebruik: /kleur <kleur>");
            player.sendMessage(ChatColor.YELLOW + "Beschikbare kleuren: " + 
                              ChatColor.DARK_GREEN + "donkergroen" + ChatColor.YELLOW + ", " + 
                              ChatColor.DARK_BLUE + "donkerblauw" + ChatColor.YELLOW + ", " + 
                              ChatColor.DARK_PURPLE + "paars" + ChatColor.YELLOW + ", " + 
                              ChatColor.GOLD + "goud" + ChatColor.YELLOW + ", " + 
                              ChatColor.GRAY + "grijs" + ChatColor.YELLOW + ", " + 
                              ChatColor.DARK_GRAY + "donkergrijs" + ChatColor.YELLOW + ", " + 
                              ChatColor.BLUE + "blauw" + ChatColor.YELLOW + ", " + 
                              ChatColor.GREEN + "groen" + ChatColor.YELLOW + ", " + 
                              ChatColor.AQUA + "aqua" + ChatColor.YELLOW + ", " + 
                              ChatColor.LIGHT_PURPLE + "roze" + ChatColor.YELLOW + ", " + 
                              ChatColor.YELLOW + "geel" + ChatColor.YELLOW + ", " + 
                              ChatColor.WHITE + "wit");
            return true;
        }

        String kleur = args[0].toLowerCase();
        ChatColor gekozenKleur = null;

        switch (kleur) {
            case "rood":
            case "red":
            case "donkerrood":
            case "darkred":
                player.sendMessage(ChatColor.RED + "Alle soorten rood zijn niet beschikbaar! Deze kleur is gereserveerd voor OP.");
                return true;
            case "donkergroen":
            case "darkgreen":
                gekozenKleur = ChatColor.DARK_GREEN;
                break;
            case "donkerblauw":
            case "darkblue":
                gekozenKleur = ChatColor.DARK_BLUE;
                break;
            case "paars":
            case "purple":
                gekozenKleur = ChatColor.DARK_PURPLE;
                break;
            case "goud":
            case "gold":
                gekozenKleur = ChatColor.GOLD;
                break;
            case "grijs":
            case "gray":
            case "grey":
                gekozenKleur = ChatColor.GRAY;
                break;
            case "donkergrijs":
            case "darkgray":
            case "darkgrey":
                gekozenKleur = ChatColor.DARK_GRAY;
                break;
            case "blauw":
            case "blue":
                gekozenKleur = ChatColor.BLUE;
                break;
            case "groen":
            case "green":
                gekozenKleur = ChatColor.GREEN;
                break;
            case "aqua":
            case "cyan":
                gekozenKleur = ChatColor.AQUA;
                break;
            case "roze":
            case "pink":
            case "lightpurple":
                gekozenKleur = ChatColor.LIGHT_PURPLE;
                break;
            case "geel":
            case "yellow":
                gekozenKleur = ChatColor.YELLOW;
                break;
            case "wit":
            case "white":
                gekozenKleur = ChatColor.WHITE;
                break;
            default:
                player.sendMessage(ChatColor.RED + "Onbekende kleur! Gebruik /kleur voor een lijst van beschikbare kleuren.");
                return true;
        }

        // Sla de kleur op in storage
        Storage.get().set("kleur." + player.getUniqueId().toString(), gekozenKleur.name());
        Storage.save();

        // Update de displayname en playerlistname direct
        String displayName = gekozenKleur + "★ " + ChatColor.RESET + player.getName();
        player.setDisplayName(displayName);
        player.setPlayerListName(displayName);

        player.sendMessage(ChatColor.GREEN + "Je sterretje kleur is nu ingesteld op " + gekozenKleur + "★");
        
        return true;
    }
}
