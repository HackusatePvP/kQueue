package cc.fatenetwork.kqueue.commands;

import cc.fatenetwork.kqueue.QueueAPI;
import cc.fatenetwork.kqueue.servers.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        Server server = QueueAPI.getServer("skyblock");
        if (server == null) {
            player.sendMessage("No work!");
            return true;
        }
        player.sendMessage("Skyblock: " + server.getPlayerCount());
        return false;
    }
}
