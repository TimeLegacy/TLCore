package net.timelegacy.tlcore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
  public Logger logger = Bukkit.getLogger();
  public List<UUID> flySpeed = new ArrayList<>();
  public boolean physics = false;

  public static TLCore getPlugin() {
    return plugin;
  }

  @Override
  public void onEnable() {
    plugin = this;
    config = this.getConfig();

    saveDefaultConfig();
    getConfig().options().copyDefaults(true);
    saveConfig();

    logger = plugin.getLogger();

    Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");

    MongoDB.connect(config.getString("URI"));

    ServerHandler.createServer();

    getCommand("coins").setExecutor(new CoinsCommand());
    getCommand("cratekeys").setExecutor(new CrateKeyCommand());
    getCommand("teleport").setExecutor(new TeleportCommand());
    getCommand("teleporthere").setExecutor(new TeleportHereCommand());
    getCommand("coinmanagement").setExecutor(new CoinManagementCommand());
    getCommand("fly").setExecutor(new FlyCommand());
    getCommand("togglephysics").setExecutor(new TogglePhysicsCommand());
    getCommand("list").setExecutor(new ListCommand());
    getCommand("gamemode").setExecutor(new GamemodeCommand());
    getCommand("multipliermanagement").setExecutor(new MultiplierCommand());
    getCommand("rankmanagement").setExecutor(new RankManagementCommand());
    getCommand("reboot").setExecutor(new RebootCommand());
    getCommand("reboot").setExecutor(new RebootCommand());
    getCommand("ban").setExecutor(new BanCommand());
    getCommand("unban").setExecutor(new UnBanCommand());
    getCommand("mute").setExecutor(new MuteCommand());
    getCommand("unmute").setExecutor(new UnMuteCommand());
    getCommand("chat").setExecutor(new ChatTypeCommand());

    getServer().getPluginManager().registerEvents(new FilterEvents(), plugin);
    getServer().getPluginManager().registerEvents(new PlayerEvents(), plugin);
    getServer().getPluginManager().registerEvents(new PhysicsEvents(), plugin);

    RankHandler.loadRanks();
  }

  @Override
  public void onDisable() {
    PermissionHandler.clearPermissions();
    MongoDB.disconnect();
  }

  public void log(String msg, Level level) {
    logger.log(level, msg);
  }
}
