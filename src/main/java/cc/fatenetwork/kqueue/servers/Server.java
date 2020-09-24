package cc.fatenetwork.kqueue.servers;

import cc.fatenetwork.kqueue.Core;
import lombok.Data;
import org.bukkit.Bukkit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

@Data
public class Server {
    private String name, ip;
    private int playerCount, port;
    private ServerState serverState;

    public Server(String name) {
        this.name = name;
    }

    public int getTotalCount() {
        return playerCount;
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
        } catch (IOException e) {
            Core.getPlugin().getLogger().info(this.getName() + ": connection refused. Make sure the server is online.");
        }
    }
}
