package cc.fatenetwork.kqueue;

import cc.fatenetwork.kqueue.queue.Queue;
import cc.fatenetwork.kqueue.queue.QueuePlayer;
import cc.fatenetwork.kqueue.servers.Server;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

public class QueueAPI {

    public QueueAPI() {
        throw new UnsupportedOperationException("Cannot initiate util class");
    }

    public static Queue getQueue(String name) {
        return Core.getPlugin().getQueueInterface().getQueue(name);
    }

    public static boolean isQueue(String name) {
        return Core.getPlugin().getQueueInterface().isQueue(name);
    }

    public static void addPlayerToQueue(Player player, Queue queue) {
        QueuePlayer queuePlayer = getQueuePlayer(player.getUniqueId());
        if (queue != null && queuePlayer != null) {
            Core.getPlugin().getQueueInterface().sendPlayerToServer(queuePlayer, queue);
        }
    }

    public static void addPlayerToQueue(Player player, String queue) {
        QueuePlayer queuePlayer = getQueuePlayer(player.getUniqueId());
        if (isQueue(queue) && queuePlayer != null) {
            Queue queueAct = getQueue(queue);
            Core.getPlugin().getQueueInterface().sendPlayerToServer(queuePlayer, queueAct);
        }
    }

    public static void addPlayerToQueue(QueuePlayer queuePlayer, Queue queue) {
        if (queue != null && queuePlayer != null) {
            Core.getPlugin().getQueueInterface().sendPlayerToServer(queuePlayer, queue);
        }
    }

    public static void addPlayerToQueue(QueuePlayer queuePlayer, String queue) {
        if (isQueue(queue) && queuePlayer != null) {
            Queue queueAct = getQueue(queue);
            Core.getPlugin().getQueueInterface().sendPlayerToServer(queuePlayer, queueAct);
        }
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
