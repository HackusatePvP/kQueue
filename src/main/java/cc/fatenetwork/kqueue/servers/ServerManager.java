package cc.fatenetwork.kqueue.servers;

import cc.fatenetwork.kqueue.Core;
import cc.fatenetwork.kqueue.configuration.ConfigFile;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
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

    public void setServerState(Server server, ServerState serverState) {
        server.setServerState(serverState);
    }

    void loadServers() {
        ConfigFile config = plugin.getConfiguration("servers");
        for (String s : config.getConfiguration().getConfigurationSection("").getKeys(false)) {
            String path = s + ".";
            Server server = new Server(config.getString(path + "name"));
            server.setServerState(ServerState.parse(config.getString(path + "server-state")));
            server.setPlayerCount(0);
            updateServerCount();
            plugin.getLogger().info("SERVER: " + server.getName() + " loaded.");
            servers.put(server.getName(), server);
        }
    }

    public void updateServerCount() {
        int toReturn = 0;
        for (Server server : servers.values()) {
            toReturn = toReturn + server.getPlayerCount();
        }
        setGetTotalCount(toReturn);
    }

    public int getServerOnlineCount(String ip, int port, Server server) {
        try {
            Socket sock = new Socket(ip, port);

            DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            DataInputStream in = new DataInputStream(sock.getInputStream());

            out.write(0xFE);

            int b;
            StringBuffer str = new StringBuffer();
            while ((b = in.read()) != -1) {
                if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24) {
                    // Not sure what use the two characters are so I omit them
                    str.append((char) b);
                }
            }
            String[] data = str.toString().split("§");
            int onlinePlayers = Integer.parseInt(data[1]);
            server.setPlayerCount(onlinePlayers);
            return onlinePlayers;
        } catch (UnknownHostException e) {
            plugin.getLogger().info(server.getName() + ": Unknown host.");
            return 0;
        } catch (IOException e) {
            plugin.getLogger().info(server.getName() + ": connection refused.");
            return 0;
        }
    }
}
