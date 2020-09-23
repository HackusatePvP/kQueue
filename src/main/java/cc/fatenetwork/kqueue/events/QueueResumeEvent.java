package cc.fatenetwork.kqueue.events;

import cc.fatenetwork.kqueue.queue.Queue;
import cc.fatenetwork.kqueue.queue.QueuePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class QueueResumeEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final String queue;
    private final QueuePlayer queuePlayer;
    private boolean cancelled = false;

    public QueueResumeEvent(String queue, QueuePlayer queuePlayer) {
        super(queuePlayer.getPlayer());
        this.queue = queue;
        this.queuePlayer = queuePlayer;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    public String getQueue() {
        return queue;
    }

    public QueuePlayer getQueuePlayer() {
        return queuePlayer;
    }

    public boolean isCancelled(){
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled){
        this.cancelled = cancelled;
    }

    public HandlerList getHandlers(){
        return handlers;
    }

}

