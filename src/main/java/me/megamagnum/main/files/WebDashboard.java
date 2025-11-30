package me.megamagnum.main.files;

import spark.Spark;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static spark.Spark.*;

public class WebDashboard {
    private final JavaPlugin plugin;
    private final PlayerDataManager playerDataManager;
    private final ServerStatsManager serverStatsManager;
    private int port;
    private boolean enabled;
    
    public WebDashboard(JavaPlugin plugin) {
        this.plugin = plugin;
        this.playerDataManager = PlayerDataManager.getInstance();
        this.serverStatsManager = ServerStatsManager.getInstance();
        loadConfig();
    }
    
    private void loadConfig() {
        this.port = plugin.getConfig().getInt("webdashboard.port", 8080);
        this.enabled = plugin.getConfig().getBoolean("webdashboard.enabled", true);
    }
    
    public void startWebServer() {
        if (!enabled) {
            plugin.getLogger().info("Web Dashboard is uitgeschakeld in de config.");
            return;
        }
        
        try {
            // Configureer host en poort ALLEREERST
            ipAddress("0.0.0.0");
            port(port);
            
            // CORS headers voor cross-origin requests
            before((request, response) -> {
                response.header("Access-Control-Allow-Origin", "*");
                response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                response.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            });
            
            // Main dashboard page
            get("/", (req, res) -> {
                res.type("text/html");
                return getDashboardHTML();
            });
            
            // API Endpoints
            path("/api", () -> {
                
                // Server status endpoint
                get("/server", (req, res) -> {
                    res.type("application/json");
                    return serverStatsManager.getServerStats().toString();
                });
                
                // Detailed server performance
                get("/server/performance", (req, res) -> {
                    res.type("application/json");
                    return serverStatsManager.getDetailedPerformance().toString();
                });
                
                // All players endpoint
                get("/players", (req, res) -> {
                    res.type("application/json");
                    JsonObject response = new JsonObject();
                    response.add("players", playerDataManager.getAllPlayersJson());
                    response.addProperty("total", playerDataManager.getAllPlayersJson().size());
                    return response.toString();
                });
                
                // Specific player endpoint
                get("/player/:name", (req, res) -> {
                    res.type("application/json");
                    String playerName = req.params(":name");
                    JsonObject player = playerDataManager.getPlayerByName(playerName);
                    
                    if (player == null) {
                        res.status(404);
                        JsonObject error = new JsonObject();
                        error.addProperty("error", "Speler niet gevonden: " + playerName);
                        return error.toString();
                    }
                    
                    return player.toString();
                });
                
                // Leaderboards endpoint
                get("/leaderboard", (req, res) -> {
                    res.type("application/json");
                    return playerDataManager.getLeaderboard().toString();
                });
                
                // Online players count
                get("/online", (req, res) -> {
                    res.type("application/json");
                    JsonObject response = new JsonObject();
                    response.addProperty("count", Bukkit.getOnlinePlayers().size());
                    response.addProperty("maxPlayers", Bukkit.getMaxPlayers());
                    
                    JsonArray onlinePlayers = new JsonArray();
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        JsonObject playerObj = new JsonObject();
                        playerObj.addProperty("name", player.getName());
                        playerObj.addProperty("hearts", (int) (player.getMaxHealth() / 2));
                        onlinePlayers.add(playerObj);
                    });
                    response.add("players", onlinePlayers);
                    
                    return response.toString();
                });
                
                // Admin Commands - POST endpoints
                post("/admin/broadcast", (req, res) -> {
                    res.type("application/json");
                    JsonObject response = new JsonObject();
                    
                    String message = req.queryParams("message");
                    if (message == null || message.isEmpty()) {
                        res.status(400);
                        response.addProperty("success", false);
                        response.addProperty("error", "Bericht mag niet leeg zijn");
                        return response.toString();
                    }
                    
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        Bukkit.broadcastMessage("§6[§bBroadcast§6] §f" + message);
                    });
                    
                    response.addProperty("success", true);
                    response.addProperty("message", "Broadcast verstuurd");
                    return response.toString();
                });
                
                post("/admin/giveheart/:player", (req, res) -> {
                    res.type("application/json");
                    JsonObject response = new JsonObject();
                    String playerName = req.params(":player");
                    
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        org.bukkit.entity.Player player = Bukkit.getPlayer(playerName);
                        if (player != null) {
                            player.setMaxHealth(player.getMaxHealth() + 2);
                        }
                    });
                    
                    response.addProperty("success", true);
                    response.addProperty("message", "Heart gegeven aan " + playerName);
                    return response.toString();
                });
                
                post("/admin/removeheart/:player", (req, res) -> {
                    res.type("application/json");
                    JsonObject response = new JsonObject();
                    String playerName = req.params(":player");
                    
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        org.bukkit.entity.Player player = Bukkit.getPlayer(playerName);
                        if (player != null && player.getMaxHealth() > 2) {
                            player.setMaxHealth(player.getMaxHealth() - 2);
                        }
                    });
                    
                    response.addProperty("success", true);
                    response.addProperty("message", "Heart verwijderd van " + playerName);
                    return response.toString();
                });
                
                post("/admin/kick/:player", (req, res) -> {
                    res.type("application/json");
                    JsonObject response = new JsonObject();
                    String playerName = req.params(":player");
                    String reason = req.queryParams("reason");
                    if (reason == null) reason = "Kicked door admin";
                    
                    String finalReason = reason;
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        org.bukkit.entity.Player player = Bukkit.getPlayer(playerName);
                        if (player != null) {
                            player.kickPlayer(finalReason);
                        }
                    });
                    
                    response.addProperty("success", true);
                    response.addProperty("message", playerName + " gekicked");
                    return response.toString();
                });
                
                post("/admin/teleport/:player/:target", (req, res) -> {
                    res.type("application/json");
                    JsonObject response = new JsonObject();
                    String playerName = req.params(":player");
                    String targetName = req.params(":target");
                    
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        org.bukkit.entity.Player player = Bukkit.getPlayer(playerName);
                        org.bukkit.entity.Player target = Bukkit.getPlayer(targetName);
                        if (player != null && target != null) {
                            player.teleport(target);
                        }
                    });
                    
                    response.addProperty("success", true);
                    response.addProperty("message", playerName + " geteleporteerd naar " + targetName);
                    return response.toString();
                });
                
                // Console command
                post("/admin/console", (req, res) -> {
                    res.type("application/json");
                    JsonObject response = new JsonObject();
                    String command = req.queryParams("command");
                    
                    if (command == null || command.isEmpty()) {
                        res.status(400);
                        response.addProperty("success", false);
                        response.addProperty("error", "Command mag niet leeg zijn");
                        return response.toString();
                    }
                    
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    });
                    
                    response.addProperty("success", true);
                    response.addProperty("message", "Command uitgevoerd: " + command);
                    return response.toString();
                });
            });
            
            // Health check endpoint
            get("/health", (req, res) -> {
                res.type("application/json");
                JsonObject health = new JsonObject();
                health.addProperty("status", "healthy");
                health.addProperty("timestamp", System.currentTimeMillis());
                health.addProperty("server", "CorevanTim");
                return health.toString();
            });
            
            exception(Exception.class, (exception, request, response) -> {
                response.status(500);
                response.body("Internal Server Error: " + exception.getMessage());
                plugin.getLogger().warning("Web Dashboard Error: " + exception.getMessage());
            });
            
            plugin.getLogger().info("Web Dashboard gestart op alle interfaces op poort " + port);
            plugin.getLogger().info("Lokaal toegang: http://localhost:" + port);
            plugin.getLogger().info("Extern toegang: http://[SERVER-IP]:" + port);
            
        } catch (Exception e) {
            plugin.getLogger().severe("Kon Web Dashboard niet starten: " + e.getMessage());
        }
    }
    
    public void stopWebServer() {
        try {
            Spark.stop();
            plugin.getLogger().info("Web Dashboard gestopt.");
        } catch (Exception e) {
            plugin.getLogger().warning("Error bij stoppen Web Dashboard: " + e.getMessage());
        }
    }
    
    private String getDashboardHTML() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"nl\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>CorevanTim Dashboard</title>\n" +
                "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css\">\n" +
                "    <style>\n" +
                "        * { margin: 0; padding: 0; box-sizing: border-box; }\n" +
                "        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%); min-height: 100vh; color: white; }\n" +
                "        .header { background: rgba(0,0,0,0.2); padding: 20px; text-align: center; border-bottom: 2px solid rgba(255,255,255,0.1); }\n" +
                "        .header h1 { font-size: 2.5em; text-shadow: 2px 2px 4px rgba(0,0,0,0.5); }\n" +
                "        .container { max-width: 1200px; margin: 0 auto; padding: 20px; }\n" +
                "        .grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 20px; margin-bottom: 30px; }\n" +
                "        .card { background: rgba(255,255,255,0.1); border-radius: 15px; padding: 20px; backdrop-filter: blur(10px); border: 1px solid rgba(255,255,255,0.2); }\n" +
                "        .card h3 { margin-bottom: 15px; color: #ffd700; display: flex; align-items: center; }\n" +
                "        .card h3 i { margin-right: 10px; }\n" +
                "        .stat-value { font-size: 2em; font-weight: bold; color: #4CAF50; text-shadow: 1px 1px 2px rgba(0,0,0,0.5); }\n" +
                "        .table-container { overflow-x: auto; margin-top: 15px; }\n" +
                "        table { width: 100%; border-collapse: collapse; }\n" +
                "        th, td { padding: 10px; text-align: left; border-bottom: 1px solid rgba(255,255,255,0.2); }\n" +
                "        th { background: rgba(255,255,255,0.1); font-weight: bold; }\n" +
                "        .status-online { color: #4CAF50; }\n" +
                "        .status-offline { color: #f44336; }\n" +
                "        .hearts { color: #e91e63; }\n" +
                "        .refresh-btn { background: #4CAF50; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; margin: 10px 0; }\n" +
                "        .refresh-btn:hover { background: #45a049; }\n" +
                "        .loading { text-align: center; padding: 20px; opacity: 0.7; }\n" +
                "        @keyframes pulse { 0% { opacity: 1; } 50% { opacity: 0.5; } 100% { opacity: 1; } }\n" +
                "        .loading { animation: pulse 1.5s infinite; }\n" +
                "        .admin-panel { background: rgba(255,0,0,0.1); border: 2px solid #ff6b6b; }\n" +
                "        .admin-panel h3 { color: #ff6b6b !important; }\n" +
                "        .input-group { margin: 10px 0; }\n" +
                "        .input-group label { display: block; margin-bottom: 5px; }\n" +
                "        .input-group input, .input-group select { width: 100%; padding: 8px; border-radius: 5px; border: 1px solid rgba(255,255,255,0.3); background: rgba(255,255,255,0.1); color: white; }\n" +
                "        .btn { padding: 10px 15px; border: none; border-radius: 5px; cursor: pointer; margin: 5px; font-weight: bold; }\n" +
                "        .btn-danger { background: #f44336; color: white; }\n" +
                "        .btn-warning { background: #ff9800; color: white; }\n" +
                "        .btn-info { background: #2196F3; color: white; }\n" +
                "        .btn-success { background: #4CAF50; color: white; }\n" +
                "        .btn:hover { opacity: 0.8; }\n" +
                "        .notification { position: fixed; top: 20px; right: 20px; padding: 15px 25px; border-radius: 5px; background: #4CAF50; color: white; display: none; z-index: 1000; }\n" +
                "        .notification.show { display: block; animation: slideIn 0.3s; }\n" +
                "        @keyframes slideIn { from { transform: translateX(400px); } to { transform: translateX(0); } }\n" +
                "        .action-btns { display: flex; gap: 5px; flex-wrap: wrap; }\n" +
                "        .tabs { display: flex; gap: 10px; margin-bottom: 20px; border-bottom: 2px solid rgba(255,255,255,0.2); }\n" +
                "        .tab { padding: 10px 20px; cursor: pointer; border-bottom: 3px solid transparent; transition: all 0.3s; }\n" +
                "        .tab:hover { background: rgba(255,255,255,0.1); }\n" +
                "        .tab.active { border-bottom-color: #ffd700; color: #ffd700; }\n" +
                "        .tab-content { display: none; }\n" +
                "        .tab-content.active { display: block; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div id=\"notification\" class=\"notification\"></div>\n" +
                "    <div class=\"header\">\n" +
                "        <h1><i class=\"fas fa-server\"></i> CorevanTim Dashboard</h1>\n" +
                "        <p>Real-time server monitoring en speler statistieken</p>\n" +
                "    </div>\n" +
                "    \n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"tabs\">\n" +
                "            <div class=\"tab active\" onclick=\"switchTab('stats')\"><i class=\"fas fa-chart-line\"></i> Statistieken</div>\n" +
                "            <div class=\"tab\" onclick=\"switchTab('admin')\"><i class=\"fas fa-shield-alt\"></i> Admin Panel</div>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div id=\"stats-tab\" class=\"tab-content active\">\n" +
                "            <button class=\"refresh-btn\" onclick=\"refreshAll()\"><i class=\"fas fa-sync\"></i> Ververs Data</button>\n" +
                "            \n" +
                "            <div class=\"grid\">\n" +
                "                <div class=\"card\">\n" +
                "                    <h3><i class=\"fas fa-tachometer-alt\"></i> Server Performance</h3>\n" +
                "                    <div id=\"server-stats\"><div class=\"loading\">Laden...</div></div>\n" +
                "                </div>\n" +
                "                <div class=\"card\">\n" +
                "                    <h3><i class=\"fas fa-users\"></i> Online Spelers</h3>\n" +
                "                    <div id=\"online-players\"><div class=\"loading\">Laden...</div></div>\n" +
                "                </div>\n" +
                "                <div class=\"card\">\n" +
                "                    <h3><i class=\"fas fa-memory\"></i> Memory Usage</h3>\n" +
                "                    <div id=\"memory-stats\"><div class=\"loading\">Laden...</div></div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            \n" +
                "            <div class=\"card\">\n" +
                "                <h3><i class=\"fas fa-trophy\"></i> Leaderboards</h3>\n" +
                "                <div id=\"leaderboards\"><div class=\"loading\">Laden...</div></div>\n" +
                "            </div>\n" +
                "            \n" +
                "            <div class=\"card\">\n" +
                "                <h3><i class=\"fas fa-list\"></i> Alle Spelers</h3>\n" +
                "                <div id=\"all-players\"><div class=\"loading\">Laden...</div></div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div id=\"admin-tab\" class=\"tab-content\">\n" +
                "            <div class=\"grid\">\n" +
                "                <div class=\"card admin-panel\">\n" +
                "                    <h3><i class=\"fas fa-broadcast-tower\"></i> Broadcast</h3>\n" +
                "                    <div class=\"input-group\">\n" +
                "                        <label>Bericht:</label>\n" +
                "                        <input type=\"text\" id=\"broadcast-msg\" placeholder=\"Type je bericht...\">\n" +
                "                    </div>\n" +
                "                    <button class=\"btn btn-info\" onclick=\"sendBroadcast()\"><i class=\"fas fa-bullhorn\"></i> Verstuur</button>\n" +
                "                </div>\n" +
                "                \n" +
                "                <div class=\"card admin-panel\">\n" +
                "                    <h3><i class=\"fas fa-heart\"></i> Hearts Beheer</h3>\n" +
                "                    <div class=\"input-group\">\n" +
                "                        <label>Speler:</label>\n" +
                "                        <select id=\"heart-player\"></select>\n" +
                "                    </div>\n" +
                "                    <div class=\"action-btns\">\n" +
                "                        <button class=\"btn btn-success\" onclick=\"giveHeart()\"><i class=\"fas fa-plus\"></i> Geef Heart</button>\n" +
                "                        <button class=\"btn btn-danger\" onclick=\"removeHeart()\"><i class=\"fas fa-minus\"></i> Verwijder Heart</button>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "                \n" +
                "                <div class=\"card admin-panel\">\n" +
                "                    <h3><i class=\"fas fa-user-slash\"></i> Speler Acties</h3>\n" +
                "                    <div class=\"input-group\">\n" +
                "                        <label>Speler:</label>\n" +
                "                        <select id=\"action-player\"></select>\n" +
                "                    </div>\n" +
                "                    <div class=\"input-group\">\n" +
                "                        <label>Kick Reden:</label>\n" +
                "                        <input type=\"text\" id=\"kick-reason\" placeholder=\"Reden (optioneel)\">\n" +
                "                    </div>\n" +
                "                    <button class=\"btn btn-danger\" onclick=\"kickPlayer()\"><i class=\"fas fa-door-open\"></i> Kick Speler</button>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            \n" +
                "            <div class=\"grid\">\n" +
                "                <div class=\"card admin-panel\">\n" +
                "                    <h3><i class=\"fas fa-location-arrow\"></i> Teleporteer</h3>\n" +
                "                    <div class=\"input-group\">\n" +
                "                        <label>Teleporteer:</label>\n" +
                "                        <select id=\"tp-player\"></select>\n" +
                "                    </div>\n" +
                "                    <div class=\"input-group\">\n" +
                "                        <label>Naar:</label>\n" +
                "                        <select id=\"tp-target\"></select>\n" +
                "                    </div>\n" +
                "                    <button class=\"btn btn-info\" onclick=\"teleportPlayer()\"><i class=\"fas fa-magic\"></i> Teleporteer</button>\n" +
                "                </div>\n" +
                "                \n" +
                "                <div class=\"card admin-panel\">\n" +
                "                    <h3><i class=\"fas fa-terminal\"></i> Console Command</h3>\n" +
                "                    <div class=\"input-group\">\n" +
                "                        <label>Command:</label>\n" +
                "                        <input type=\"text\" id=\"console-cmd\" placeholder=\"say Hello World\">\n" +
                "                    </div>\n" +
                "                    <button class=\"btn btn-warning\" onclick=\"executeCommand()\"><i class=\"fas fa-play\"></i> Uitvoeren</button>\n" +
                "                    <p style=\"margin-top:10px; font-size:0.9em; color:#ff9800;\">⚠️ Pas op! Dit voert commands rechtstreeks uit.</p>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    \n" +
                "    <script>\n" +
                "        async function fetchData(endpoint) {\n" +
                "            try {\n" +
                "                const response = await fetch(`/api${endpoint}`);\n" +
                "                return await response.json();\n" +
                "            } catch (error) {\n" +
                "                console.error('Error fetching data:', error);\n" +
                "                return null;\n" +
                "            }\n" +
                "        }\n" +
                "        \n" +
                "        async function updateServerStats() {\n" +
                "            const data = await fetchData('/server');\n" +
                "            if (!data) return;\n" +
                "            \n" +
                "            document.getElementById('server-stats').innerHTML = `\n" +
                "                <div><strong>TPS:</strong> <span class=\"stat-value\">${data.tps}</span></div>\n" +
                "                <div><strong>Uptime:</strong> ${data.uptime}</div>\n" +
                "                <div><strong>Versie:</strong> ${data.bukkitVersion}</div>\n" +
                "                <div><strong>Werelden:</strong> ${data.worldCount}</div>\n" +
                "                <div><strong>Entities:</strong> ${data.totalEntities}</div>\n" +
                "                <div><strong>Chunks:</strong> ${data.totalChunksLoaded}</div>\n" +
                "            `;\n" +
                "        }\n" +
                "        \n" +
                "        async function updateOnlinePlayers() {\n" +
                "            const data = await fetchData('/online');\n" +
                "            if (!data) return;\n" +
                "            \n" +
                "            let html = `<div class=\"stat-value\">${data.count}/${data.maxPlayers}</div>`;\n" +
                "            if (data.players.length > 0) {\n" +
                "                html += '<div class=\"table-container\"><table><tr><th>Naam</th><th>Hearts</th></tr>';\n" +
                "                data.players.forEach(player => {\n" +
                "                    html += `<tr><td>${player.name}</td><td class=\"hearts\">❤ ${player.hearts}</td></tr>`;\n" +
                "                });\n" +
                "                html += '</table></div>';\n" +
                "            }\n" +
                "            document.getElementById('online-players').innerHTML = html;\n" +
                "            \n" +
                "            // Update admin panel player selects\n" +
                "            updatePlayerSelects(data.players);\n" +
                "        }\n" +
                "        \n" +
                "        function updatePlayerSelects(players) {\n" +
                "            const selects = ['heart-player', 'action-player', 'tp-player', 'tp-target'];\n" +
                "            selects.forEach(id => {\n" +
                "                const select = document.getElementById(id);\n" +
                "                if (select) {\n" +
                "                    select.innerHTML = players.map(p => `<option value=\"${p.name}\">${p.name}</option>`).join('');\n" +
                "                }\n" +
                "            });\n" +
                "        }\n" +
                "        \n" +
                "        async function updateMemoryStats() {\n" +
                "            const data = await fetchData('/server');\n" +
                "            if (!data) return;\n" +
                "            \n" +
                "            document.getElementById('memory-stats').innerHTML = `\n" +
                "                <div><strong>Gebruikt:</strong> ${data.usedMemory}</div>\n" +
                "                <div><strong>Vrij:</strong> ${data.freeMemory}</div>\n" +
                "                <div><strong>Maximum:</strong> ${data.maxMemory}</div>\n" +
                "                <div><strong>Gebruik:</strong> <span class=\"stat-value\">${data.memoryUsagePercentage}%</span></div>\n" +
                "            `;\n" +
                "        }\n" +
                "        \n" +
                "        async function updateLeaderboards() {\n" +
                "            const data = await fetchData('/leaderboard');\n" +
                "            if (!data) return;\n" +
                "            \n" +
                "            let html = '<div class=\"grid\">';\n" +
                "            \n" +
                "            // Hearts leaderboard\n" +
                "            html += '<div><h4>🏆 Meeste Hearts</h4><table>';\n" +
                "            data.hearts.forEach((player, index) => {\n" +
                "                html += `<tr><td>#${index + 1}</td><td>${player.name}</td><td class=\"hearts\">❤ ${player.hearts}</td></tr>`;\n" +
                "            });\n" +
                "            html += '</table></div>';\n" +
                "            \n" +
                "            // Kills leaderboard\n" +
                "            html += '<div><h4>⚔️ Meeste Kills</h4><table>';\n" +
                "            data.kills.forEach((player, index) => {\n" +
                "                html += `<tr><td>#${index + 1}</td><td>${player.name}</td><td>${player.kills}</td></tr>`;\n" +
                "            });\n" +
                "            html += '</table></div>';\n" +
                "            \n" +
                "            // KDR leaderboard\n" +
                "            html += '<div><h4>📊 Beste KDR</h4><table>';\n" +
                "            data.kdr.forEach((player, index) => {\n" +
                "                html += `<tr><td>#${index + 1}</td><td>${player.name}</td><td>${player.kdr}</td></tr>`;\n" +
                "            });\n" +
                "            html += '</table></div>';\n" +
                "            \n" +
                "            html += '</div>';\n" +
                "            document.getElementById('leaderboards').innerHTML = html;\n" +
                "        }\n" +
                "        \n" +
                "        async function updateAllPlayers() {\n" +
                "            const data = await fetchData('/players');\n" +
                "            if (!data) return;\n" +
                "            \n" +
                "            let html = '<div class=\"table-container\"><table>';\n" +
                "            html += '<tr><th>Naam</th><th>Status</th><th>Hearts</th><th>Kills</th><th>Deaths</th><th>KDR</th><th>Playtime</th></tr>';\n" +
                "            \n" +
                "            data.players.forEach(player => {\n" +
                "                const status = player.isOnline ? '🟢 Online' : '🔴 Offline';\n" +
                "                const statusClass = player.isOnline ? 'status-online' : 'status-offline';\n" +
                "                html += `\n" +
                "                    <tr>\n" +
                "                        <td>${player.name}</td>\n" +
                "                        <td class=\"${statusClass}\">${status}</td>\n" +
                "                        <td class=\"hearts\">❤ ${player.hearts}</td>\n" +
                "                        <td>${player.kills}</td>\n" +
                "                        <td>${player.deaths}</td>\n" +
                "                        <td>${player.kdr.toFixed(2)}</td>\n" +
                "                        <td>${player.playtime}</td>\n" +
                "                    </tr>\n" +
                "                `;\n" +
                "            });\n" +
                "            \n" +
                "            html += '</table></div>';\n" +
                "            document.getElementById('all-players').innerHTML = html;\n" +
                "        }\n" +
                "        \n" +
                "        function refreshAll() {\n" +
                "            updateServerStats();\n" +
                "            updateOnlinePlayers();\n" +
                "            updateMemoryStats();\n" +
                "            updateLeaderboards();\n" +
                "            updateAllPlayers();\n" +
                "        }\n" +
                "        \n" +
                "        function switchTab(tab) {\n" +
                "            document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));\n" +
                "            document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));\n" +
                "            event.target.classList.add('active');\n" +
                "            document.getElementById(tab + '-tab').classList.add('active');\n" +
                "        }\n" +
                "        \n" +
                "        function showNotification(message, isError = false) {\n" +
                "            const notif = document.getElementById('notification');\n" +
                "            notif.textContent = message;\n" +
                "            notif.style.background = isError ? '#f44336' : '#4CAF50';\n" +
                "            notif.classList.add('show');\n" +
                "            setTimeout(() => notif.classList.remove('show'), 3000);\n" +
                "        }\n" +
                "        \n" +
                "        async function sendBroadcast() {\n" +
                "            const msg = document.getElementById('broadcast-msg').value;\n" +
                "            if (!msg) return showNotification('Voer een bericht in!', true);\n" +
                "            \n" +
                "            try {\n" +
                "                const response = await fetch(`/api/admin/broadcast?message=${encodeURIComponent(msg)}`, { method: 'POST' });\n" +
                "                const data = await response.json();\n" +
                "                showNotification(data.message);\n" +
                "                document.getElementById('broadcast-msg').value = '';\n" +
                "            } catch (error) {\n" +
                "                showNotification('Fout: ' + error.message, true);\n" +
                "            }\n" +
                "        }\n" +
                "        \n" +
                "        async function giveHeart() {\n" +
                "            const player = document.getElementById('heart-player').value;\n" +
                "            if (!player) return;\n" +
                "            \n" +
                "            try {\n" +
                "                const response = await fetch(`/api/admin/giveheart/${player}`, { method: 'POST' });\n" +
                "                const data = await response.json();\n" +
                "                showNotification(data.message);\n" +
                "                setTimeout(refreshAll, 500);\n" +
                "            } catch (error) {\n" +
                "                showNotification('Fout: ' + error.message, true);\n" +
                "            }\n" +
                "        }\n" +
                "        \n" +
                "        async function removeHeart() {\n" +
                "            const player = document.getElementById('heart-player').value;\n" +
                "            if (!player) return;\n" +
                "            \n" +
                "            try {\n" +
                "                const response = await fetch(`/api/admin/removeheart/${player}`, { method: 'POST' });\n" +
                "                const data = await response.json();\n" +
                "                showNotification(data.message);\n" +
                "                setTimeout(refreshAll, 500);\n" +
                "            } catch (error) {\n" +
                "                showNotification('Fout: ' + error.message, true);\n" +
                "            }\n" +
                "        }\n" +
                "        \n" +
                "        async function kickPlayer() {\n" +
                "            const player = document.getElementById('action-player').value;\n" +
                "            const reason = document.getElementById('kick-reason').value || 'Kicked door admin';\n" +
                "            if (!player) return;\n" +
                "            \n" +
                "            if (!confirm(`Weet je zeker dat je ${player} wilt kicken?`)) return;\n" +
                "            \n" +
                "            try {\n" +
                "                const response = await fetch(`/api/admin/kick/${player}?reason=${encodeURIComponent(reason)}`, { method: 'POST' });\n" +
                "                const data = await response.json();\n" +
                "                showNotification(data.message);\n" +
                "                setTimeout(refreshAll, 500);\n" +
                "            } catch (error) {\n" +
                "                showNotification('Fout: ' + error.message, true);\n" +
                "            }\n" +
                "        }\n" +
                "        \n" +
                "        async function teleportPlayer() {\n" +
                "            const player = document.getElementById('tp-player').value;\n" +
                "            const target = document.getElementById('tp-target').value;\n" +
                "            if (!player || !target) return;\n" +
                "            \n" +
                "            try {\n" +
                "                const response = await fetch(`/api/admin/teleport/${player}/${target}`, { method: 'POST' });\n" +
                "                const data = await response.json();\n" +
                "                showNotification(data.message);\n" +
                "            } catch (error) {\n" +
                "                showNotification('Fout: ' + error.message, true);\n" +
                "            }\n" +
                "        }\n" +
                "        \n" +
                "        async function executeCommand() {\n" +
                "            const cmd = document.getElementById('console-cmd').value;\n" +
                "            if (!cmd) return showNotification('Voer een command in!', true);\n" +
                "            \n" +
                "            if (!confirm(`Command uitvoeren: ${cmd}?`)) return;\n" +
                "            \n" +
                "            try {\n" +
                "                const response = await fetch(`/api/admin/console?command=${encodeURIComponent(cmd)}`, { method: 'POST' });\n" +
                "                const data = await response.json();\n" +
                "                showNotification(data.message);\n" +
                "                document.getElementById('console-cmd').value = '';\n" +
                "            } catch (error) {\n" +
                "                showNotification('Fout: ' + error.message, true);\n" +
                "            }\n" +
                "        }\n" +
                "        \n" +
                "        // Initial load\n" +
                "        refreshAll();\n" +
                "        \n" +
                "        // Auto-refresh every 30 seconds\n" +
                "        setInterval(refreshAll, 30000);\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }
    
    public int getPort() {
        return port;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
}