package cc.fatenetwork.kqueue.queue;

import java.util.Map;
import java.util.UUID;

public interface QueueInterface {

    Map<String, Queue> getQueues();

    Map<UUID, QueuePlayer> getQueuePlayers();

    Queue getQueue(String name);

    QueuePlayer getQueuePlayer(UUID uuid);

    boolean isQueue(String name);

    void addToQueue(QueuePlayer queuePlayer, Queue queue);

    void removeFromQueue(QueuePlayer queuePlayer, Queue queue);

    void sendPlayerToServer(QueuePlayer queuePlayer, Queue queue);

    void updateQueue(Queue queue);

    void loadQueues();
}
