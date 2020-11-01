package cc.fatenetwork.kqueue;

import cc.fatenetwork.kqueue.commands.QueueCommand;
import cc.fatenetwork.kqueue.configuration.ConfigFile;
import cc.fatenetwork.kqueue.listener.PlayerListener;
import cc.fatenetwork.kqueue.listener.QueueListener;
import cc.fatenetwork.kqueue.queue.QueueInterface;
import cc.fatenetwork.kqueue.queue.QueueManager;
import cc.fatenetwork.kqueue.servers.ServerManager;
import cc.fatenetwork.kqueue.tasking.QueueTask;
import cc.fatenetwork.kqueue.utils.PluginChannelListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

@Getter
public final class Core extends JavaPlugin {
    private static PluginChannelListener pcl;
    private QueueInterface queueInterface;
    private ServerManager serverManager;
    private QueueTask queueTask;
    private final Collection<ConfigFile> files = new HashSet<>();
    @Getter private static Core plugin;

    @Override
    public void onEnable() {
        plugin = this;
        loadConfigs();
        if (!plugin.getDescription().getName().equals("kQueue")) {
            getServer().getPluginManager().disablePlugin(this);
        }
        registerManagers();
        registerEvents();
        registerCommands();
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        // allow to send to BungeeCord
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "Return", pcl = new PluginChannelListener());
    }

    @Override
    public void onDisable() {
        files.forEach(config -> config.save());
        plugin = null;
    }

    public ConfigFile getConfiguration(String name) {
        return files.stream().filter(config -> config.getName().equals(name)).findFirst().orElse(null);
    }

    void loadConfigs() {
        files.addAll(Arrays.asList(
                new ConfigFile("config", this),
                new ConfigFile("queues", this),
                new ConfigFile("lang", this),
                new ConfigFile("servers", this)
        ));
    }

    private void registerManagers() {
        if (getConfiguration("config").getBoolean("server")) {
            serverManager = new ServerManager(plugin);
        } else {
            getLogger().warning("Server functionality has been disabled, plugin may not work properly.");
        }
        queueInterface = new QueueManager(this);
        queueTask = new QueueTask(this);
        queueTask.runTaskTimer(this, 0, 20);
    }

    private void registerEvents() {
        Arrays.asList(new PlayerListener(this), new QueueListener(this, getConfiguration("lang"))).forEach(listener -> {
            Bukkit.getPluginManager().registerEvents(listener, this);
        });
    }

     private void registerCommands() {
        getCommand("queue").setExecutor(new QueueCommand(this));
    }

}
