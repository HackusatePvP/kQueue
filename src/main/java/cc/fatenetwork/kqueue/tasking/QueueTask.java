package cc.fatenetwork.kqueue.tasking;

import cc.fatenetwork.kqueue.Core;
import cc.fatenetwork.kqueue.queue.Queue;
import cc.fatenetwork.kqueue.queue.QueuePlayer;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitRunnable;

public class QueueTask extends BukkitRunnable {
    private int count;
    private final Core plugin;

    public QueueTask(Core plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        count++; //this keeps track of time

        for (Queue queue : plugin.getQueueInterface().getQueues().values()) {
            if (queue.getPlayers().size() > 0) {
                int d = queue.getTick();
                d++;
                plugin.getLogger().info(queue.getName() + ": " + d + "/" + queue.getSendDelay());
                if (d == queue.getSendDelay()) {
                    int amountToSend = 0;
                    while (amountToSend < queue.getAmountToSend()) {
                        QueuePlayer queuePlayer = queue.getByPosition().keySet().stream().findFirst().orElse(null);
                        if (queuePlayer != null) {
                            if (!queue.isPause()) {
                                plugin.getQueueInterface().sendPlayerToServer(queuePlayer, queue);
                                amountToSend++;
                            }
                        }
                    }
                    plugin.getQueueInterface().updateQueue(queue);
                    d = 0;
                    queue.setTick(d);
                }
            }
        }
        if (count == 120) {
            //every 2min this will run queue updates (playercounts, servers online, ect)
            plugin.getServerManager().updateServerCount();
        }
    }
}
