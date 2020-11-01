package cc.fatenetwork.kqueue.tasking;

import cc.fatenetwork.kqueue.Core;
import cc.fatenetwork.kqueue.queue.Queue;
import cc.fatenetwork.kqueue.queue.QueuePlayer;
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
                queue.setTick(d);
                plugin.getLogger().info(queue.getName() + ": " + d + "/" + queue.getSendDelay());
                if (d == queue.getSendDelay()) {
                    int amountToSend = 0;
                    do {
                        QueuePlayer queuePlayer = queue.getByPosition().keySet().stream().findFirst().orElse(null);
                        if (queuePlayer != null) {
                            if (!queue.isPause()) {
                                plugin.getQueueInterface().sendPlayerToServer(queuePlayer, queuePlayer.getQueue());
                                plugin.getLogger().info("sent " + queuePlayer.getPlayer().getName() + " to " + queuePlayer.getQueue().getServer());
                                amountToSend++;
                            }
                        }
                    } while (amountToSend < queue.getAmountToSend());
                    plugin.getQueueInterface().updateQueue(queue);
                    queue.setTick(0);
                }
            } else {
                if (queue.getTick() != 0) {
                    plugin.getLogger().info("[QUEUE] Player went missing.");
                    queue.setTick(0);
                }
            }
        }
        if (count == 120) {
            //every 2min this will run queue updates (playercounts, servers online, ect)
            plugin.getServerManager().updateServerCount();
        }
    }
}
