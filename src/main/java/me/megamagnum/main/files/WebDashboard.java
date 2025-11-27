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
            port(port);
            
            // CORS headers voor cross-origin requests
            before((request, response) -> {
                response.header("Access-Control-Allow-Origin", "*");
                response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                response.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            });
            
            // Static files (CSS, JS, images)
            staticFileLocation("/web");
            
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
            
            plugin.getLogger().info("Web Dashboard gestart op poort " + port);
            plugin.getLogger().info("Toegang via: http://localhost:" + port);
            
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
        return "<!DOCTYPE html>\\n" +
                "<html lang=\\"nl\\">\\n" +
                "<head>\\n" +
                "    <meta charset=\\"UTF-8\\">\\n" +
                "    <meta name=\\"viewport\\" content=\\"width=device-width, initial-scale=1.0\\">\\n" +
                "    <title>CorevanTim Dashboard</title>\\n" +
                "    <link rel=\\"stylesheet\\" href=\\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css\\">\\n" +
                "    <style>\\n" +
                "        * { margin: 0; padding: 0; box-sizing: border-box; }\\n" +
                "        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%); min-height: 100vh; color: white; }\\n" +
                "        .header { background: rgba(0,0,0,0.2); padding: 20px; text-align: center; border-bottom: 2px solid rgba(255,255,255,0.1); }\\n" +
                "        .header h1 { font-size: 2.5em; text-shadow: 2px 2px 4px rgba(0,0,0,0.5); }\\n" +
                "        .container { max-width: 1200px; margin: 0 auto; padding: 20px; }\\n" +
                "        .grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 20px; margin-bottom: 30px; }\\n" +
                "        .card { background: rgba(255,255,255,0.1); border-radius: 15px; padding: 20px; backdrop-filter: blur(10px); border: 1px solid rgba(255,255,255,0.2); }\\n" +
                "        .card h3 { margin-bottom: 15px; color: #ffd700; display: flex; align-items: center; }\\n" +
                "        .card h3 i { margin-right: 10px; }\\n" +
                "        .stat-value { font-size: 2em; font-weight: bold; color: #4CAF50; text-shadow: 1px 1px 2px rgba(0,0,0,0.5); }\\n" +
                "        .table-container { overflow-x: auto; margin-top: 15px; }\\n" +
                "        table { width: 100%; border-collapse: collapse; }\\n" +
                "        th, td { padding: 10px; text-align: left; border-bottom: 1px solid rgba(255,255,255,0.2); }\\n" +
                "        th { background: rgba(255,255,255,0.1); font-weight: bold; }\\n" +
                "        .status-online { color: #4CAF50; }\\n" +
                "        .status-offline { color: #f44336; }\\n" +
                "        .hearts { color: #e91e63; }\\n" +
                "        .refresh-btn { background: #4CAF50; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; margin: 10px 0; }\\n" +
                "        .refresh-btn:hover { background: #45a049; }\\n" +
                "        .loading { text-align: center; padding: 20px; opacity: 0.7; }\\n" +
                "        @keyframes pulse { 0% { opacity: 1; } 50% { opacity: 0.5; } 100% { opacity: 1; } }\\n" +
                "        .loading { animation: pulse 1.5s infinite; }\\n" +
                "    </style>\\n" +
                "</head>\\n" +
                "<body>\\n" +
                "    <div class=\\"header\\">\\n" +
                "        <h1><i class=\\"fas fa-server\\"></i> CorevanTim Dashboard</h1>\\n" +
                "        <p>Real-time server monitoring en speler statistieken</p>\\n" +
                "    </div>\\n" +
                "    \\n" +
                "    <div class=\\"container\\">\\n" +
                "        <button class=\\"refresh-btn\\" onclick=\\"refreshAll()\\"><i class=\\"fas fa-sync\\"></i> Ververs Data</button>\\n" +
                "        \\n" +
                "        <div class=\\"grid\\">\\n" +
                "            <div class=\\"card\\">\\n" +
                "                <h3><i class=\\"fas fa-tachometer-alt\\"></i> Server Performance</h3>\\n" +
                "                <div id=\\"server-stats\\">\\n" +
                "                    <div class=\\"loading\\">Laden...</div>\\n" +
                "                </div>\\n" +
                "            </div>\\n" +
                "            \\n" +
                "            <div class=\\"card\\">\\n" +
                "                <h3><i class=\\"fas fa-users\\"></i> Online Spelers</h3>\\n" +
                "                <div id=\\"online-players\\">\\n" +
                "                    <div class=\\"loading\\">Laden...</div>\\n" +
                "                </div>\\n" +
                "            </div>\\n" +
                "            \\n" +
                "            <div class=\\"card\\">\\n" +
                "                <h3><i class=\\"fas fa-memory\\"></i> Memory Usage</h3>\\n" +
                "                <div id=\\"memory-stats\\">\\n" +
                "                    <div class=\\"loading\\">Laden...</div>\\n" +
                "                </div>\\n" +
                "            </div>\\n" +
                "        </div>\\n" +
                "        \\n" +
                "        <div class=\\"card\\">\\n" +
                "            <h3><i class=\\"fas fa-trophy\\"></i> Leaderboards</h3>\\n" +
                "            <div id=\\"leaderboards\\">\\n" +
                "                <div class=\\"loading\\">Laden...</div>\\n" +
                "            </div>\\n" +
                "        </div>\\n" +
                "        \\n" +
                "        <div class=\\"card\\">\\n" +
                "            <h3><i class=\\"fas fa-list\\"></i> Alle Spelers</h3>\\n" +
                "            <div id=\\"all-players\\">\\n" +
                "                <div class=\\"loading\\">Laden...</div>\\n" +
                "            </div>\\n" +
                "        </div>\\n" +
                "    </div>\\n" +
                "    \\n" +
                "    <script>\\n" +
                "        async function fetchData(endpoint) {\\n" +
                "            try {\\n" +
                "                const response = await fetch(`/api${endpoint}`);\\n" +
                "                return await response.json();\\n" +
                "            } catch (error) {\\n" +
                "                console.error('Error fetching data:', error);\\n" +
                "                return null;\\n" +
                "            }\\n" +
                "        }\\n" +
                "        \\n" +
                "        async function updateServerStats() {\\n" +
                "            const data = await fetchData('/server');\\n" +
                "            if (!data) return;\\n" +
                "            \\n" +
                "            document.getElementById('server-stats').innerHTML = `\\n" +
                "                <div><strong>TPS:</strong> <span class=\\"stat-value\\">${data.tps}</span></div>\\n" +
                "                <div><strong>Uptime:</strong> ${data.uptime}</div>\\n" +
                "                <div><strong>Versie:</strong> ${data.bukkitVersion}</div>\\n" +
                "                <div><strong>Werelden:</strong> ${data.worldCount}</div>\\n" +
                "                <div><strong>Entities:</strong> ${data.totalEntities}</div>\\n" +
                "                <div><strong>Chunks:</strong> ${data.totalChunksLoaded}</div>\\n" +
                "            `;\\n" +
                "        }\\n" +
                "        \\n" +
                "        async function updateOnlinePlayers() {\\n" +
                "            const data = await fetchData('/online');\\n" +
                "            if (!data) return;\\n" +
                "            \\n" +
                "            let html = `<div class=\\"stat-value\\">${data.count}/${data.maxPlayers}</div>`;\\n" +
                "            if (data.players.length > 0) {\\n" +
                "                html += '<div class=\\"table-container\\"><table><tr><th>Naam</th><th>Hearts</th></tr>';\\n" +
                "                data.players.forEach(player => {\\n" +
                "                    html += `<tr><td>${player.name}</td><td class=\\"hearts\\">❤ ${player.hearts}</td></tr>`;\\n" +
                "                });\\n" +
                "                html += '</table></div>';\\n" +
                "            }\\n" +
                "            document.getElementById('online-players').innerHTML = html;\\n" +
                "        }\\n" +
                "        \\n" +
                "        async function updateMemoryStats() {\\n" +
                "            const data = await fetchData('/server');\\n" +
                "            if (!data) return;\\n" +
                "            \\n" +
                "            document.getElementById('memory-stats').innerHTML = `\\n" +
                "                <div><strong>Gebruikt:</strong> ${data.usedMemory}</div>\\n" +
                "                <div><strong>Vrij:</strong> ${data.freeMemory}</div>\\n" +
                "                <div><strong>Maximum:</strong> ${data.maxMemory}</div>\\n" +
                "                <div><strong>Gebruik:</strong> <span class=\\"stat-value\\">${data.memoryUsagePercentage}%</span></div>\\n" +
                "            `;\\n" +
                "        }\\n" +
                "        \\n" +
                "        async function updateLeaderboards() {\\n" +
                "            const data = await fetchData('/leaderboard');\\n" +
                "            if (!data) return;\\n" +
                "            \\n" +
                "            let html = '<div class=\\"grid\\">\\n";\\n" +
                "            \\n" +
                "            // Hearts leaderboard\\n" +
                "            html += '<div><h4>🏆 Meeste Hearts</h4><table>';\\n" +
                "            data.hearts.forEach((player, index) => {\\n" +
                "                html += `<tr><td>#${index + 1}</td><td>${player.name}</td><td class=\\"hearts\\">❤ ${player.hearts}</td></tr>`;\\n" +
                "            });\\n" +
                "            html += '</table></div>';\\n" +
                "            \\n" +
                "            // Kills leaderboard\\n" +
                "            html += '<div><h4>⚔️ Meeste Kills</h4><table>';\\n" +
                "            data.kills.forEach((player, index) => {\\n" +
                "                html += `<tr><td>#${index + 1}</td><td>${player.name}</td><td>${player.kills}</td></tr>`;\\n" +
                "            });\\n" +
                "            html += '</table></div>';\\n" +
                "            \\n" +
                "            // KDR leaderboard\\n" +
                "            html += '<div><h4>📊 Beste KDR</h4><table>';\\n" +
                "            data.kdr.forEach((player, index) => {\\n" +
                "                html += `<tr><td>#${index + 1}</td><td>${player.name}</td><td>${player.kdr}</td></tr>`;\\n" +
                "            });\\n" +
                "            html += '</table></div>';\\n" +
                "            \\n" +
                "            html += '</div>';\\n" +
                "            document.getElementById('leaderboards').innerHTML = html;\\n" +
                "        }\\n" +
                "        \\n" +
                "        async function updateAllPlayers() {\\n" +
                "            const data = await fetchData('/players');\\n" +
                "            if (!data) return;\\n" +
                "            \\n" +
                "            let html = '<div class=\\"table-container\\"><table>';\\n" +
                "            html += '<tr><th>Naam</th><th>Status</th><th>Hearts</th><th>Kills</th><th>Deaths</th><th>KDR</th><th>Playtime</th></tr>';\\n" +
                "            \\n" +
                "            data.players.forEach(player => {\\n" +
                "                const status = player.isOnline ? '🟢 Online' : '🔴 Offline';\\n" +
                "                const statusClass = player.isOnline ? 'status-online' : 'status-offline';\\n" +
                "                html += `\\n" +
                "                    <tr>\\n" +
                "                        <td>${player.name}</td>\\n" +
                "                        <td class=\\"${statusClass}\\">${status}</td>\\n" +
                "                        <td class=\\"hearts\\">❤ ${player.hearts}</td>\\n" +
                "                        <td>${player.kills}</td>\\n" +
                "                        <td>${player.deaths}</td>\\n" +
                "                        <td>${player.kdr.toFixed(2)}</td>\\n" +
                "                        <td>${player.playtime}</td>\\n" +
                "                    </tr>\\n" +
                "                `;\\n" +
                "            });\\n" +
                "            \\n" +
                "            html += '</table></div>';\\n" +
                "            document.getElementById('all-players').innerHTML = html;\\n" +
                "        }\\n" +
                "        \\n" +
                "        function refreshAll() {\\n" +
                "            updateServerStats();\\n" +
                "            updateOnlinePlayers();\\n" +
                "            updateMemoryStats();\\n" +
                "            updateLeaderboards();\\n" +
                "            updateAllPlayers();\\n" +
                "        }\\n" +
                "        \\n" +
                "        // Initial load\\n" +
                "        refreshAll();\\n" +
                "        \\n" +
                "        // Auto-refresh every 30 seconds\\n" +
                "        setInterval(refreshAll, 30000);\\n" +
                "    </script>\\n" +
                "</body>\\n" +
                "</html>";
    }
    
    public int getPort() {
        return port;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
}