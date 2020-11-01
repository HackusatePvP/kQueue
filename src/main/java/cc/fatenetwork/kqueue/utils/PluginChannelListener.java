package cc.fatenetwork.kqueue.utils;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import java.util.HashMap;

public class PluginChannelListener implements PluginMessageListener {

    private static HashMap<Player, Object> obj = new HashMap<Player, Object>();

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("get")) {
            String input = in.readUTF();
            obj.put(player, input);
            notifyAll();
        }
    }
}