package cc.fatenetwork.kqueue.queue;

import cc.fatenetwork.kqueue.Core;
import cc.fatenetwork.kqueue.configuration.ConfigFile;
import cc.fatenetwork.kqueue.events.QueueSendEvent;
import cc.fatenetwork.kqueue.utils.StringUtil;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Getter
public class QueueManager implements QueueInterface {
    private Map<String, Queue> queues = new HashMap<>();
    private Map<UUID, QueuePlayer> queuePlayers = new HashMap<>();
    private final Core plugin;

    public QueueManager(Core plugin) {
        this.plugin = plugin;
        loadQueues();
    }

    @Override
    public Map<String, Queue> getQueues() {
        return queues;
    }

    @Override
    public Map<UUID, QueuePlayer> getQueuePlayers() {
        return queuePlayers;
    }

    @Override
    public Queue getQueue(String name) {
        return queues.get(name);
    }

    @Override
    public QueuePlayer getQueuePlayer(UUID uuid) {
        return queuePlayers.get(uuid);
    }

    @Override
    public boolean isQueue(String name) {
        return queues.containsKey(name);
    }

    @Override
    public void addToQueue(QueuePlayer queuePlayer, Queue queue) {
        queuePlayer.setQueue(queue);
        queue.getPlayers().add(queuePlayer);
        queue.getByWeight().put(queuePlayer, queuePlayer.getWeight());
        updateQueue(queue);
    }

    @Override
    public void removeFromQueue(QueuePlayer queuePlayer, Queue queue) {
        queue.getPlayers().remove(queuePlayer);
        queue.getByPosition().remove(queuePlayer);
        queue.getByWeight().remove(queuePlayer);
        updateQueue(queue);
    }

    @Override
    public void sendPlayerToServer(QueuePlayer queuePlayer, Queue queue) {
        QueueSendEvent event = new QueueSendEvent(queue.getName(), queuePlayer);
        if (!event.isCancelled()) {
            return;
        }
        String command = "send " + queuePlayer.getPlayer().getName() + " " + queue.getName();
        sendPlayer(queuePlayer, "get", command, queue);
    }

    @Override
    public void updateQueue(Queue queue) {
        //We need 2 maps so we can properly sort the queue based by the players priority and by position
        Map<QueuePlayer, Integer> sortedByPlayers;
        Map<QueuePlayer, Integer> sortedByPosition;

        //next lets sort the players based on weight, so the ones with the highest value appear first
        sortedByPlayers = queue.getByWeight().entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        queue.setByWeight(sortedByPlayers);

        //todo we could simply this process by getting the position based on the index + 1

        //once the map is sorted by priority we can now set their positions based on there placement in the map
       AtomicInteger pos = new AtomicInteger();
       sortedByPlayers.forEach((key, value) -> {
            pos.getAndIncrement();
            queue.getByPosition().put(key, pos.intValue());
       });

       //now lets resort the map based on the positions. This will be in reversed order, so the player with the lowest position appear first
       sortedByPosition = queue.getByPosition().entrySet().stream()
               .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
               .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                       (oldValue, newValue) -> oldValue, LinkedHashMap::new));
       queue.setByPosition(sortedByPosition);

       //lets send all players in the the queue a messages when its updated.
        for (QueuePlayer queuePlayer : queue.getByWeight().keySet()) {
            ConfigFile config = plugin.getConfiguration("lang");
            queuePlayer.getPlayer().sendMessage(StringUtil.format(config.getString("queue-donor")));
            queuePlayer.getPlayer().sendMessage(StringUtil.format(format(config.getString("queue-update-position"), queuePlayer)));
        }
    }

    @Override
    public void loadQueues() {
        ConfigFile config = plugin.getConfiguration("queues");
        for (String s : config.getConfiguration().getConfigurationSection("").getKeys(false)) {
            String path = s + ".";
            Queue queue = new Queue(config.getConfiguration().getString(path + "name"));
            queue.setEnabled(config.getBoolean(path + "enabled"));
            queue.setHasServer(config.getConfiguration().getBoolean(path + "isServer"));
            queue.setAmountToSend(config.getInt("send-amount"));
            queue.setSendDelay(config.getInt(path + "send-delay"));
            queue.setCommand(config.getString(path + "command"));
            queues.put(queue.getName().toLowerCase(), queue);
            plugin.getLogger().info( "QUEUE: " + queue.getName() + " loaded");
        }
    }

    private void sendPlayer(QueuePlayer queuePlayer, String channel, String command, Queue queue){
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF(channel);
            out.writeUTF(command);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "[Queue] Target server is not online. Check configuration and server status.");
            removeFromQueue(queuePlayer, queue);
            queuePlayer.getPlayer().sendMessage(StringUtil.format("&cServer not found or is not online."));
            removeFromQueue(queuePlayer, queue);
        }
        queuePlayer.getPlayer().sendPluginMessage(Core.getPlugin(Core.class), "BungeeCord", b.toByteArray());
    }

    private String format(String message, QueuePlayer queuePlayer) {
        message = message.replace("%QUEUE%", queuePlayer.getQueue().getName());
        message = message.replace("%POSITION%", queuePlayer.getPosition() + "");
        message = message.replace("%QUEUESIZE%", queuePlayer.getQueue().getPlayers().size() + "");
        return message;
    }
}

