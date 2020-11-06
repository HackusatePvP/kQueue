package cc.fatenetwork.kqueue;

import cc.fatenetwork.kqueue.queue.Queue;
import cc.fatenetwork.kqueue.queue.QueuePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

public class QueueAPI {

    public QueueAPI() {
        throw new UnsupportedOperationException("Cannot initiate util class");
    }

    /**
     * This will return a Queue object based on a string name. This can return null if you do not do null checks.
     * @param name String
     * @return Queue
     */
    public static Queue getQueue(String name) {
        return Core.getPlugin().getQueueInterface().getQueue(name);
    }

    /**
     * This will check to see if a queue exists with the string name that is defined.
     * @param name String
     * @return Queue
     */
    public static boolean isQueue(String name) {
        return Core.getPlugin().getQueueInterface().isQueue(name);
    }

    /**
     * This will add a player to a queue.
     * @param player Player
     * @param queue Queue
     */
    public static void addPlayerToQueue(Player player, Queue queue) {
        QueuePlayer queuePlayer = getQueuePlayer(player.getUniqueId());
        if (queue != null && queuePlayer != null) {
            Core.getPlugin().getQueueInterface().sendPlayerToServer(queuePlayer, queue);
        }
    }

    /**
     * This will add a player to a queue.
     * @param player Player
     * @param queue Queue
     */
    public static void addPlayerToQueue(Player player, String queue) {
        QueuePlayer queuePlayer = getQueuePlayer(player.getUniqueId());
        if (isQueue(queue) && queuePlayer != null) {
            Queue queueAct = getQueue(queue);
            Core.getPlugin().getQueueInterface().sendPlayerToServer(queuePlayer, queueAct);
        }
    }

    /**
     * This will add a player to a queue.
     * @param queuePlayer QueuePlayer
     * @param queue Queue
     */
    public static void addPlayerToQueue(QueuePlayer queuePlayer, Queue queue) {
        if (queue != null && queuePlayer != null) {
            Core.getPlugin().getQueueInterface().sendPlayerToServer(queuePlayer, queue);
        }
    }

    /**
     * This will add a player to a queue.
     * @param queuePlayer QueuePlayer
     * @param queue Queue
     */
    public static void addPlayerToQueue(QueuePlayer queuePlayer, String queue) {
        if (isQueue(queue) && queuePlayer != null) {
            Queue queueAct = getQueue(queue);
            Core.getPlugin().getQueueInterface().sendPlayerToServer(queuePlayer, queueAct);
        }
    }

    /**
     * This will return all queues that are registered.
     * @return Collection<Queue>
     */
    public static Collection<Queue> getQueues() {
        return Core.getPlugin().getQueueInterface().getQueues().values();
    }

    /**
     * This will return a QueuePlayer based on a player's uuid
     * @param uuid UUID
     * @return queuePlayer QueuePlayer
     */
    public static QueuePlayer getQueuePlayer(UUID uuid) {
        return Core.getPlugin().getQueueInterface().getQueuePlayer(uuid);
    }
}
