package cc.fatenetwork.kqueue.utils;

import cc.fatenetwork.kqueue.servers.Server;
import cc.fatenetwork.kqueue.servers.ServerState;

import java.util.ArrayList;
import java.util.List;

public class Color {

    public Color() {
        throw new UnsupportedOperationException("Cannot initiate a util class.");
    }

    public static String translateState(ServerState serverState) {
        switch (serverState) {
            case ONLINE:
                return "&aOnline";
            case OFFLINE:
                return "&cOffline";
            case WHITELIST:
                return "&fWhitelisted";
        }

        return "&cOffline";
    }

    public static List<String> formatServerLore(List<String> lore, Server server) {
        List<String> toReturn = new ArrayList<>();

        for (String s : lore) {
            s = s.replace("%STATE%", translateState(server.getServerState()));
            s = s.replace("%ONLINE%", server.getPlayerCount() + "");
            s = StringUtil.format(s);
            toReturn.add(s);
        }

        return toReturn;
    }

}
