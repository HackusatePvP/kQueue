package cc.fatenetwork.kqueue.servers;

import cc.fatenetwork.kqueue.Core;
import cc.fatenetwork.kqueue.configuration.ConfigFile;
import cc.fatenetwork.kqueue.utils.Color;
import cc.fatenetwork.kqueue.utils.StringUtil;
import com.google.common.annotations.Beta;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

@Data
public class Server {
    private String name, ip;
    private boolean enabled;
    private int playerCount, port;
    private ServerState serverState;

    public Server(String name) {
        this.name = name;
    }

    public int getTotalCount() {
        return playerCount;
    }

    public ItemStack getServerItem() {
        ConfigFile config = Core.getPlugin().getConfiguration("servers");
        try {
            ItemStack itemStack = new ItemStack(Material.getMaterial(config.getString(getName() + ".item")));
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(StringUtil.format(config.getString(getName() + ".display-name")));
            itemMeta.setLore(Color.formatServerLore(config.getStringList(getName() + ".lore"), this));
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        } catch (Exception e) {
            e.printStackTrace();
            return new ItemStack(Material.GRASS); //default item
        }
    }

    public void updatePlayerCount() {
        try {
            Socket sock = new Socket(ip, port);

            DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            DataInputStream in = new DataInputStream(sock.getInputStream());

            out.write(0xFE);

            int b;
            StringBuffer str = new StringBuffer();
            while ((b = in.read()) != -1) {
                if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24) {
                    // Not sure what use the two characters are so I omit them
                    str.append((char) b);
                }
            }
            String[] data = str.toString().split("§");
            int onlinePlayers = Integer.parseInt(data[1]);
            this.setPlayerCount(onlinePlayers);
        } catch (UnknownHostException e) {
            Core.getPlugin().getLogger().info(this.getName() + ": Unknown host. Make sure you typed the ip and port correctly.");
            this.setServerState(ServerState.OFFLINE);
        } catch (IOException e) {
            Core.getPlugin().getLogger().info(this.getName() + ": connection refused. Make sure the server is online.");
            this.setServerState(ServerState.OFFLINE);
        }
    }
}
