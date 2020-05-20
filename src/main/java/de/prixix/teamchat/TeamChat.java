package de.prixix.teamchat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.prixix.teamchat.commands.TC;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public final class TeamChat extends JavaPlugin {

    private static TeamChat instance;

    public FileConfiguration config = this.getConfig();
    public ConsoleCommandSender console = getServer().getConsoleSender();

    private String prefix = "§8[§cTeamChat§8] ";

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        config.addDefault("prefix", "&8[&cTeamChat&8] ");
        config.addDefault("permission_name", "tc.use");
        config.addDefault("no_permission", "&cYou can't use this command.");
        config.addDefault("tc_message", "&e[sender] &8>> &a[message]");
        config.addDefault("tc_no_message", "&cYou have to add a message. &7</teamchat [msg]>");
        config.options().copyDefaults(true);
        saveConfig();

        console.sendMessage(prefix + "Config loaded...");
        startUpdateCheck();

        getCommand("tc").setExecutor(new TC());
        getCommand("teamchat").setExecutor(new TC());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void startUpdateCheck() {
        console.sendMessage(prefix + "Checking newest version...");
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                String out;
                out = new Scanner(new URL("https://api.spiget.org/v2/resources/66565/versions/latest").openStream(), "UTF-8").useDelimiter("\\A").next();
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = (JsonObject) jsonParser.parse(out);
                String versionName = jsonObject.get("name").toString().replace("\"", "");

                if(!versionName.equals(getDescription().getVersion())) {
                    console.sendMessage(prefix + "NEW VERSION AVAILABLE: " + versionName);
                    console.sendMessage(prefix + "DOWNLOAD @ https://www.spigotmc.org/resources/teamchat-chat-with-your-teammates.66565/");
                } else
                    console.sendMessage(prefix + "No new version found...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static TeamChat getInstance() {
        return instance;
    }
}
