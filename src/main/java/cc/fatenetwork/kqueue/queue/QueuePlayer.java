package cc.fatenetwork.kqueue.queue;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter @Setter
public class QueuePlayer {
    private UUID uuid;
    private Queue queue;
    private int weight;

    public QueuePlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public QueuePlayer(UUID uuid, Queue queue, int weight) {
        this.uuid = uuid;
        this.queue = queue;
        this.weight = weight;
    }

    public int getPosition() {
        return queue.getByPosition().get(this);
    }

    public boolean inQueue() {
        return queue != null;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
