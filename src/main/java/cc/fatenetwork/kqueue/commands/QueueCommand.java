package cc.fatenetwork.kqueue.commands;

import cc.fatenetwork.kqueue.Core;
import cc.fatenetwork.kqueue.configuration.ConfigFile;
import cc.fatenetwork.kqueue.events.QueueJoinEvent;
import cc.fatenetwork.kqueue.events.QueuePauseEvent;
import cc.fatenetwork.kqueue.events.QueueResumeEvent;
import cc.fatenetwork.kqueue.queue.Queue;
import cc.fatenetwork.kqueue.queue.QueuePlayer;
import cc.fatenetwork.kqueue.servers.Server;
import cc.fatenetwork.kqueue.utils.Color;
import cc.fatenetwork.kqueue.utils.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class QueueCommand implements CommandExecutor {
    private final Core plugin;

    public QueueCommand(Core plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        QueuePlayer queuePlayer = plugin.getQueueInterface().getQueuePlayer(player.getUniqueId());
        ConfigFile config = plugin.getConfiguration("lang");
        if (args.length == 0) {
            List<String> message = new ArrayList<>();
            message.add("&7&m------------------------------------------");
            message.add("&b&lQueue &3Help");
            message.add("&3/queue join &b<queue>");
            message.add("&3/queue leave &b<queue>");
            if (player.hasPermission("queue.admin")) {
                message.add("");
                message.add("&b&lQueue &3Admin");
                message.add("&3/queue pause &b<queue>");
                message.add("&3/queue resume &b<queue>");
            }
            message.add("&7&m------------------------------------------");
            message.forEach(msg -> {
                player.sendMessage(StringUtil.format(msg));
            });
        } else {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("join")) {
                    player.sendMessage(StringUtil.format(config.getString("queue-command-join-error")));
                } else if (args[0].equalsIgnoreCase("leave")) {
                    if (!queuePlayer.inQueue()) {
                        player.sendMessage(StringUtil.format(config.getString("not-in-queue")));
                        return true;
                    }
                    player.sendMessage(StringUtil.format(format(config.getString("queue-leave"), queuePlayer.getQueue())));
                    plugin.getQueueInterface().removeFromQueue(queuePlayer, queuePlayer.getQueue());
                } else if (args[0].equalsIgnoreCase("pause")) {
                    if (player.hasPermission("queue.admin")) {
                        player.sendMessage(StringUtil.format(config.getString("queue-command-pause-error")));
                        return true;
                    }
                    player.sendMessage(StringUtil.format(config.getString("no-permissions")));
                } else if (args[0].equalsIgnoreCase("resume")) {
                    if (player.hasPermission("queue.admin")) {
                        player.sendMessage(StringUtil.format(config.getString("queue-command-resume-error")));
                        return true;
                    }
                    player.sendMessage(StringUtil.format(config.getString("no-permissions")));
                } else if (args[0].equalsIgnoreCase("list")) {
                    List<String> message = new ArrayList<>();
                    message.add("&7&m---------------------------------------");
                    for (Queue queue : plugin.getQueueInterface().getQueues().values()) {
                        if (queue.isLink()) {
                            Server server = plugin.getServerManager().getServer(queue.getServer());
                            if (server != null) {
                                message.add(" &7* &c" + queue.getName() + ": " + Color.translateState(server.getServerState()));
                            } else {
                                message.add(" &7* &c" + queue.getName());
                            }
                        } else {
                            message.add(" &7* &c" + queue.getName());
                        }
                    }
                    message.add("&7&m---------------------------------------");
                    message.forEach(msg -> player.sendMessage(StringUtil.format(msg)));
                } else {
                    player.sendMessage(StringUtil.format(format(config.getString("argument-not-found"), label, command.getName())));
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("join")) {
                    QueueJoinEvent event = new QueueJoinEvent(args[1].toLowerCase(), queuePlayer);
                    if (event.isCancelled()) {
                        return true;
                    }
                    Queue queue = plugin.getQueueInterface().getQueue(args[1].toLowerCase());
                    if (queue == null) {
                        player.sendMessage(StringUtil.format(format(config.getString("queue-not-found"), args[1])));
                        return true;
                    }
                    plugin.getQueueInterface().addToQueue(queuePlayer, queue);
                } else if (args[0].equalsIgnoreCase("pause")) {
                    if (!(player.hasPermission("queue.admin"))) {
                        player.sendMessage(StringUtil.format(config.getString("no-permissions")));
                        return true;
                    }
                    QueuePauseEvent event = new QueuePauseEvent(args[1].toLowerCase(), queuePlayer);
                    if (event.isCancelled()) {
                        return true;
                    }
                    Queue queue = plugin.getQueueInterface().getQueue(args[1].toLowerCase());
                    if (queue == null) {
                        player.sendMessage(StringUtil.format(format(config.getString("queue-not-found"), args[1])));
                        return true;
                    }
                    if (queue.isPause()) {
                        player.sendMessage(StringUtil.format(format(config.getString("resume-queue"), queue)));
                    } else {
                        player.sendMessage(StringUtil.format(format(config.getString("pause-queue"), queue)));
                    }
                    queue.setPause(!queue.isPause());
                } else if (args[0].equalsIgnoreCase("resume")) {
                    if (!(player.hasPermission("queue.admin"))) {
                        player.sendMessage(StringUtil.format(config.getString("no-permissions")));
                        return true;
                    }
                    QueueResumeEvent event = new QueueResumeEvent(args[1].toLowerCase(), queuePlayer);
                    if (event.isCancelled()) {
                        return true;
                    }
                    Queue queue = plugin.getQueueInterface().getQueue(args[1].toLowerCase());
                    if (queue == null) {
                        player.sendMessage(StringUtil.format(format(config.getString("queue-not-found"), args[1])));
                        return true;
                    }
                    if (queue.isPause()) {
                        player.sendMessage(StringUtil.format(format(config.getString("resume-queue"), queuePlayer.getQueue())));
                    } else {
                        player.sendMessage(StringUtil.format(format(config.getString("pause-queue"), queuePlayer.getQueue())));
                    }
                    queue.setPause(!queue.isPause());
                } else {
                    player.sendMessage(StringUtil.format(config.getString("no-permissions")));
                }
            }
        }
        return false;
    }

    private String format(String message, String arg, String cmd) {
        message = message.replace("%ARG%", arg);
        message = message.replace("%CMD%", cmd);
        return message;
    }

    private String format(String message, QueuePlayer queuePlayer) {
        message = message.replace("%QUEUE%", queuePlayer.getQueue().getName());
        return message;
    }

    private String format(String message, Queue queue) {
        message = message.replace("%QUEUE%", queue.getName());
        return message;
    }

    private String format(String message, String queue) {
        message = message.replace("%QUEUE%", queue);
        return message;
    }
}
