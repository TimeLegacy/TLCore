package net.timelegacy.tlcore;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.timelegacy.tlcore.command.BanCommand;
import net.timelegacy.tlcore.command.ChatTypeCommand;
import net.timelegacy.tlcore.command.CoinManagementCommand;
import net.timelegacy.tlcore.command.CoinsCommand;
import net.timelegacy.tlcore.command.CrateKeyCommand;
import net.timelegacy.tlcore.command.FlyCommand;
import net.timelegacy.tlcore.command.GamemodeCommand;
import net.timelegacy.tlcore.command.ListCommand;
import net.timelegacy.tlcore.command.MultiplierCommand;
import net.timelegacy.tlcore.command.MuteCommand;
import net.timelegacy.tlcore.command.RankManagementCommand;
import net.timelegacy.tlcore.command.RebootCommand;
import net.timelegacy.tlcore.command.TeleportCommand;
import net.timelegacy.tlcore.command.TeleportHereCommand;
import net.timelegacy.tlcore.command.TogglePhysicsCommand;
import net.timelegacy.tlcore.command.UnBanCommand;
import net.timelegacy.tlcore.command.UnMuteCommand;
import net.timelegacy.tlcore.event.FilterEvents;
import net.timelegacy.tlcore.event.PhysicsEvents;
import net.timelegacy.tlcore.event.PlayerEvents;
import net.timelegacy.tlcore.handler.PermissionHandler;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.handler.ServerHandler;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class TLCore extends JavaPlugin {

  private static TLCore plugin = null;
  public FileConfiguration config;
  public Logger log = Bukkit.getLogger();
  public ArrayList<String> flySpeed = new ArrayList<>();
  public boolean physics = true;

  // MongoDB
  public MongoDB mongoDB;

  public static TLCore getPlugin() {
    return plugin;
  }

  public void onEnable() {

    plugin = this;
    config = this.getConfig();

    plugin.saveDefaultConfig();
    plugin.getConfig().options().copyDefaults(true);
    plugin.saveConfig();

    log = plugin.getLogger();

    Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");

    mongoDB.connect(config.getString("URI"));

    ServerHandler.createServer();

    plugin.getCommand("coins").setExecutor(new CoinsCommand());
    plugin.getCommand("cratekeys").setExecutor(new CrateKeyCommand());
    plugin.getCommand("teleport").setExecutor(new TeleportCommand());
    plugin.getCommand("teleporthere").setExecutor(new TeleportHereCommand());
    plugin.getCommand("coinmanagement").setExecutor(new CoinManagementCommand());
    plugin.getCommand("fly").setExecutor(new FlyCommand());
    plugin.getCommand("togglephysics").setExecutor(new TogglePhysicsCommand());
    plugin.getCommand("list").setExecutor(new ListCommand());
    plugin.getCommand("gamemode").setExecutor(new GamemodeCommand());
    plugin.getCommand("multipliermanagement").setExecutor(new MultiplierCommand());
    plugin.getCommand("rankmanagement").setExecutor(new RankManagementCommand());
    plugin.getCommand("reboot").setExecutor(new RebootCommand());
    plugin.getCommand("reboot").setExecutor(new RebootCommand());
    plugin.getCommand("ban").setExecutor(new BanCommand());
    plugin.getCommand("unban").setExecutor(new UnBanCommand());
    plugin.getCommand("mute").setExecutor(new MuteCommand());
    plugin.getCommand("unmute").setExecutor(new UnMuteCommand());
    plugin.getCommand("chat").setExecutor(new ChatTypeCommand());

    plugin.getServer().getPluginManager().registerEvents(new FilterEvents(), plugin);
    plugin.getServer().getPluginManager().registerEvents(new PlayerEvents(), plugin);
    plugin.getServer().getPluginManager().registerEvents(new PhysicsEvents(), plugin);

    RankHandler.loadRanks();
    RankHandler.tabColors();
  }

  @Override
  public void onDisable() {

    PermissionHandler.clearPermissions();

    mongoDB.disconnect();
  }

  public void log(String msg, Level level) {
    log.log(level, msg);
  }
}
