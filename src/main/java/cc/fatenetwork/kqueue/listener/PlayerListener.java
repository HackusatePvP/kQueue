package cc.fatenetwork.kqueue.listener;

import cc.fatenetwork.kqueue.Core;
import cc.fatenetwork.kqueue.queue.Queue;
import cc.fatenetwork.kqueue.queue.QueuePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final Core plugin;

    public PlayerListener(Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        QueuePlayer queuePlayer = new QueuePlayer(event.getUniqueId());
        if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            plugin.getQueueInterface().getQueuePlayers().put(event.getUniqueId(), queuePlayer);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        QueuePlayer queuePlayer = plugin.getQueueInterface().getQueuePlayer(event.getPlayer().getUniqueId());
        if (queuePlayer.inQueue()) {
            Queue queue = queuePlayer.getQueue();
            queue.getPlayers().remove(queuePlayer);
            queue.getByWeight().remove(queuePlayer);
            queue.getByPosition().remove(queuePlayer);
        }
        plugin.getQueueInterface().getQueuePlayers().remove(event.getPlayer().getUniqueId());
    }
}
