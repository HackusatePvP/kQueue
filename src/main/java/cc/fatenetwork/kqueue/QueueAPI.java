package cc.fatenetwork.kqueue;

import cc.fatenetwork.kqueue.queue.Queue;
import cc.fatenetwork.kqueue.queue.QueuePlayer;
import cc.fatenetwork.kqueue.servers.Server;

import java.util.Collection;
import java.util.UUID;

public class QueueAPI {

    public static Queue getQueue(String name) {
        return Core.getPlugin().getQueueInterface().getQueue(name);
    }

    public static boolean isQueue(String name) {
        return Core.getPlugin().getQueueInterface().isQueue(name);
    }

    public static Collection<Queue> getQueues() {
        return Core.getPlugin().getQueueInterface().getQueues().values();
    }

    public static QueuePlayer getQueuePlayer(UUID uuid) {
        return Core.getPlugin().getQueueInterface().getQueuePlayer(uuid);
    }

    public static Server getServer(String name) {
        return Core.getPlugin().getServerManager().getServer(name);
    }

    public static int getTotalCunt() {
        int toReturn = 0;
        for (Server server : Core.getPlugin().getServerManager().getServers().values()) {
            toReturn = toReturn + server.getPlayerCount();
        }
        return toReturn;
    }
}
