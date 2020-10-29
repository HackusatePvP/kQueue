package cc.fatenetwork.kqueue.queue;

import cc.fatenetwork.kqueue.Core;
import lombok.Getter;
import lombok.Setter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Getter @Setter
public class Queue {
    private Collection<QueuePlayer> players = new HashSet<>();
    private Map<QueuePlayer, Integer> byWeight = new HashMap<>();
    private Map<QueuePlayer, Integer> byPosition = new HashMap<>();
    private String name, server, command;
    private int inQueue, amountToSend, sendDelay;
    @Deprecated int tick;
    private boolean enabled, pause, Server;


    public Queue(String name) {
        this.name = name;
        this.inQueue = 0;
        this.enabled = true;
    }

    public Queue(String name, int inQueue, boolean enabled, boolean isServer) {
        this.name = name;
        this.inQueue = inQueue;
        this.enabled = enabled;
        this.Server = isServer;
    }

    public void send(QueuePlayer queuePlayer) {
        Core.getPlugin().getQueueInterface().sendPlayerToServer(queuePlayer, this);
    }
}
