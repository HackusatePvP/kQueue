package cc.fatenetwork.kqueue;

import cc.fatenetwork.kqueue.servers.Server;
import cc.fatenetwork.kqueue.servers.ServerState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class ServerAPI {

    public ServerAPI() {
        throw new UnsupportedOperationException("Cannot initiate a utils class.");
    }

    /**
     * This will returned all registered servers
     * @return Server
     */
    public static Collection<Server> getServers() {
        return Core.getPlugin().getServerManager().getServers().values();
    }

    /**
     * This will return a Server based on a string name.
     * @param name String
     * @return Server
     */
    public static Server getServer(String name) {
        return Core.getPlugin().getServerManager().getServer(name);
    }

    /**
     * This will return the weather a server that is registered has the String name defined.
     * @param name String
     * @return boolean
     */
    public static boolean isServer(String name) {
        return Core.getPlugin().getServerManager().isServer(name);
    }

    /**
     * This will return the Server-State of the Server object (online, offline, whitelist)
     * @param server Server
     * @return ServerState
     */
    public static ServerState getServerState(Server server) {
        return server.getServerState();
    }

    /**
     * This will set the state of the server based on the object ServerState
     * @param server Server
     * @param serverState ServerState
     */
    public static void setServerState(Server server, ServerState serverState) {
        server.setServerState(serverState);
    }

    /**
     * This wil set the state if the server based off a string. This can produce errors if the String of the state is not correct
     * @param server Server
     * @param serverState ServerState
     */
    public static void setServerState(Server server, String serverState) {
        ServerState state = ServerState.parse(serverState);
        server.setServerState(state);
    }

    /**
     * This will return the Integer player amount of all servers registered
     * @return int
     */
    public static int getTotalCount() {
        int toReturn = 0;
        for (Server server : Core.getPlugin().getServerManager().getServers().values()) {
            toReturn = toReturn + server.getPlayerCount();
        }
        return toReturn + Bukkit.getOnlinePlayers().size();
    }
}
