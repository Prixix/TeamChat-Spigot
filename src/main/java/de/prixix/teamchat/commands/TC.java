package de.prixix.teamchat.commands;

import de.prixix.teamchat.TeamChat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TC implements CommandExecutor {

    TeamChat teamChat = TeamChat.getInstance();

    private String prefix = teamChat.config.getString("prefix").replace("&", "§");
    private String permission = teamChat.config.getString("permission_name").replace("&", "§");
    private String tc_message = prefix + teamChat.config.getString("tc_message").replace("&", "§");
    private String no_permission = prefix + teamChat.config.getString("no_permission").replace("&", "§");
    private String tc_no_message = prefix + teamChat.config.getString("tc_no_message").replace("&", "§");


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;

            if(player.hasPermission(permission)) {
                if(args.length >= 1) {
                    String message = "";
                    for(int i = 0; i < args.length; i++)
                        message = String.valueOf(message) + args[i] + " ";
                    for(Player team : Bukkit.getOnlinePlayers()) {
                        if(team.hasPermission(permission)) {
                            team.sendMessage(tc_message.replace("[sender]", player.getName()).replace("[message]", message));
                            teamChat.console.sendMessage(prefix + player.getPlayer().getName() + " texted to teamchat: " + message);
                        }
                    }
                } else
                    player.sendMessage(tc_no_message);
            } else
                player.sendMessage(no_permission);
        } else {
            sender.sendMessage(prefix + "You have to be a player!");
        }
        return false;
    }

}
