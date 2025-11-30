package me.megamagnum.main.files;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import okhttp3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AIConspiracyCommand implements CommandExecutor {
    
    private static final String API_KEY = "b8983cd54fac44918c715a7a2c0bf95a";
    private static final String API_URL = "https://api.aimlapi.com/chat/completions";
    private static final int COOLDOWN_SECONDS = 30;
    
    private final OkHttpClient client;
    private final Map<String, Long> cooldowns = new HashMap<>();
    private final JavaPlugin plugin;
    
    public AIConspiracyCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        this.client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Alleen spelers kunnen dit commando gebruiken!");
            return true;
        }
        
        Player player = (Player) sender;
        
        // Check cooldown
        if(cooldowns.containsKey(player.getName())) {
            long timeLeft = (cooldowns.get(player.getName()) + (COOLDOWN_SECONDS * 1000)) - System.currentTimeMillis();
            if(timeLeft > 0) {
                player.sendMessage(ChatColor.RED + "⏰ Wacht nog " + (timeLeft / 1000) + " seconden!");
                return true;
            }
        }
        
        if(args.length == 0) {
            player.sendMessage(ChatColor.RED + "Gebruik: !ai <vraag>");
            return true;
        }
        
        String question = String.join(" ", args);
        
        // Set cooldown
        cooldowns.put(player.getName(), System.currentTimeMillis());
        
        player.sendMessage(ChatColor.GOLD + "🤖 AI Complottheorie Generator denkt na...");
        
        // Call API async
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String response = getAIResponse(question);
                
                // Send response on main thread
                Bukkit.getScheduler().runTask(plugin, () -> {
                    // Broadcast to everyone
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "🔺 COMPLOTTHEORIE 🔺");
                    Bukkit.broadcastMessage(ChatColor.GRAY + player.getName() + " vroeg: " + ChatColor.WHITE + question);
                    Bukkit.broadcastMessage(ChatColor.YELLOW + response);
                    Bukkit.broadcastMessage("");
                });
                
            } catch (Exception e) {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    player.sendMessage(ChatColor.RED + "❌ AI is momenteel niet beschikbaar. De overheid houdt ons tegen!");
                    cooldowns.remove(player.getName()); // Remove cooldown on error
                });
            }
        });
        
        return true;
    }
    
    private String getAIResponse(String question) throws IOException {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "gpt-4o-mini");
        
        JsonArray messages = new JsonArray();
        
        // System message voor conspiracy tone
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", 
            "Je bent een complottheorie generator. Geef wilde, absurde maar grappige complottheorieën. " +
            "Gebruik woorden zoals 'de overheid', 'aliens', 'geheime organisaties', 'reptielen', etc. " +
            "Maximaal 60 woorden. Wees creatief en overdreven. In het Nederlands.");
        messages.add(systemMessage);
        
        // User message
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", question);
        messages.add(userMessage);
        
        requestBody.add("messages", messages);
        requestBody.addProperty("max_tokens", 100);
        requestBody.addProperty("temperature", 1.2);
        
        RequestBody body = RequestBody.create(
            requestBody.toString(),
            MediaType.parse("application/json")
        );
        
        Request request = new Request.Builder()
            .url(API_URL)
            .addHeader("Authorization", "Bearer " + API_KEY)
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build();
        
        try (Response response = client.newCall(request).execute()) {
            if(!response.isSuccessful()) {
                throw new IOException("API Error: " + response.code());
            }
            
            String responseBody = response.body().string();
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            
            return jsonResponse
                .getAsJsonArray("choices")
                .get(0).getAsJsonObject()
                .getAsJsonObject("message")
                .get("content").getAsString()
                .trim();
        }
    }
}
