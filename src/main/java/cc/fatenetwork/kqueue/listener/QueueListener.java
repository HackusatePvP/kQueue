package cc.fatenetwork.kqueue.listener;

import cc.fatenetwork.kqueue.Core;
import cc.fatenetwork.kqueue.QueueAPI;
import cc.fatenetwork.kqueue.configuration.ConfigFile;
import cc.fatenetwork.kqueue.events.QueueJoinEvent;
import cc.fatenetwork.kqueue.events.QueueLeaveEvent;
import cc.fatenetwork.kqueue.events.QueuePauseEvent;
import cc.fatenetwork.kqueue.queue.Queue;
import cc.fatenetwork.kqueue.queue.QueuePlayer;
import cc.fatenetwork.kqueue.utils.StringUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class QueueListener implements Listener {
    private final ConfigFile config;
    private final Core plugin;

    public QueueListener(Core plugin, ConfigFile config) {
        this.plugin = plugin;
        this.config = config;
    }

    @EventHandler
    public void onQueueJoin(QueueJoinEvent event) {
        Player player = event.getPlayer();
        QueuePlayer queuePlayer = event.getQueuePlayer();
        String queueName = event.getEventName();
        if (!plugin.getQueueInterface().isQueue(queueName)) {
            player.sendMessage(StringUtil.format(config.getString("queue-not-found")));
            event.setCancelled(true);
            return;
        }
        if (queuePlayer.inQueue()) {
            player.sendMessage(StringUtil.format(format(config.getString("already-in-queue"), queuePlayer)));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQueueLeave(QueueLeaveEvent event) {
        Player player = event.getPlayer();
        QueuePlayer queuePlayer = event.getQueuePlayer();
        String queueName = event.getQueue();
        if (!plugin.getQueueInterface().isQueue(queueName)) {
            player.sendMessage(StringUtil.format(config.getString("queue-not-found")));
            event.setCancelled(true);
            return;
        }
        Queue queue = plugin.getQueueInterface().getQueue(queueName);
        if (queuePlayer.inQueue()) {
            player.sendMessage(StringUtil.format(format(config.getString("already-in-queue"), queuePlayer)));
            event.setCancelled(true);
        }
        if (queue.isPause()) {
            player.sendMessage(StringUtil.format(format(config.getString("queue-paused"), queue)));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQueuePause(QueuePauseEvent event) {
        Player player = event.getPlayer();
        QueuePlayer queuePlayer = event.getQueuePlayer();
        String queueName = event.getQueue();

        if (plugin.getQueueInterface().isQueue(queueName)) {
            player.sendMessage(StringUtil.format(config.getString("queue-not-found")));
            event.setCancelled(true);
        }
    }

    private String format(String message, QueuePlayer queuePlayer) {
        message = message.replace("%QUEUE%", queuePlayer.getQueue().getName());
        return message;
    }

    private String format(String message, Queue queue) {
        message = message.replace("%QUEUE%", queue.getName());
        return message;
    }
}
