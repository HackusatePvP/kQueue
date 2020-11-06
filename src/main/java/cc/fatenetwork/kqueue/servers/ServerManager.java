package cc.fatenetwork.kqueue.servers;

import cc.fatenetwork.kqueue.Core;
import cc.fatenetwork.kqueue.configuration.ConfigFile;
import lombok.Getter;
import lombok.Setter;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ServerManager {
    private Map<String, Server> servers = new HashMap<>();
    @Setter private int getTotalCount;
    private final Core plugin;

    public ServerManager(Core plugin) {
        this.plugin = plugin;
        loadServers();
    }

    public Server getServer(String name) {
        return servers.get(name);
    }

    public boolean isServer(String name) {
        return servers.containsKey(name);
    }

    public void setServerState(Server server, ServerState serverState) {
        server.setServerState(serverState);
    }

    private void loadServers() {
        ConfigFile config = plugin.getConfiguration("servers");
        for (String s : config.getConfiguration().getConfigurationSection("").getKeys(false)) {
            String path = s + ".";
            Server server = new Server(config.getString(path + "name"));
            server.setServerState(ServerState.parse(config.getString(path + "server-state")));
            server.setPlayerCount(0);
            server.setEnabled(config.getBoolean(path + "enabled"));
            server.setIp(config.getString(path + "ip"));
            server.setPort(config.getInt(path + "port"));
            updateServerCount();
            plugin.getLogger().info("SERVER: " + server.getName() + " loaded.");
            servers.put(server.getName(), server);
        }
    }

    public void updateServerCount() {
        int toReturn = 0;
        for (Server server : servers.values()) {
            server.updatePlayerCount();
            toReturn = toReturn + server.getPlayerCount();
        }
        setGetTotalCount(toReturn);
    }
}
