package cc.fatenetwork.kqueue.queue;

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
    private int inQueue, amountToSend, sendDelay, tick;
    private boolean enabled, pause, hasServer;

    public Queue(String name) {
        this.name = name;
        this.inQueue = 0;
        this.enabled = true;
    }

    public Queue(String name, int inQueue, boolean enabled, boolean isServer) {
        this.name = name;
        this.inQueue = inQueue;
        this.enabled = enabled;
        this.hasServer = isServer;
    }
}
