package net.timelegacy.tlcore;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.timelegacy.tlcore.command.BanCommand;
import net.timelegacy.tlcore.command.CoinManagementCommand;
import net.timelegacy.tlcore.command.CoinsCommand;
import net.timelegacy.tlcore.command.CrateKeyCommand;
import net.timelegacy.tlcore.command.FlyCommand;
import net.timelegacy.tlcore.command.GamemodeCommand;
import net.timelegacy.tlcore.command.ListCommand;
import net.timelegacy.tlcore.command.MultiplierCommand;
import net.timelegacy.tlcore.command.MuteCommand;
import net.timelegacy.tlcore.command.ProfileCommand;
import net.timelegacy.tlcore.command.RankManagementCommand;
import net.timelegacy.tlcore.command.RebootCommand;
import net.timelegacy.tlcore.command.TeleportCommand;
import net.timelegacy.tlcore.command.TeleportHereCommand;
import net.timelegacy.tlcore.command.UnBanCommand;
import net.timelegacy.tlcore.command.UnMuteCommand;
import net.timelegacy.tlcore.event.FilterEvents;
import net.timelegacy.tlcore.event.PlayerEvents;
import net.timelegacy.tlcore.handler.PermissionHandler;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.handler.ServerHandler;
import net.timelegacy.tlcore.menus.friends.FriendsMenu;
import net.timelegacy.tlcore.menus.friends.FriendsPendingMenu;
import net.timelegacy.tlcore.menus.profile.GenderSelectorMenu;
import net.timelegacy.tlcore.menus.profile.ProfileMenu;
import net.timelegacy.tlcore.menus.profile.YourSettingsMenu;
import net.timelegacy.tlcore.menus.profile.YourStateSettingsMenu;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class TLCore extends JavaPlugin {

  private static TLCore plugin = null;
  public static ProtocolManager protocolManager;

  public FileConfiguration config;
  public Logger logger = Bukkit.getLogger();
  public List<UUID> flySpeed = new ArrayList<>();

  public static TLCore getPlugin() {
    return plugin;
  }

  @Override
  public void onEnable() {
    protocolManager = ProtocolLibrary.getProtocolManager();
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
    getCommand("profile").setExecutor(new ProfileCommand());

    getServer().getPluginManager().registerEvents(new FilterEvents(), plugin);
    getServer().getPluginManager().registerEvents(new PlayerEvents(), plugin);

    getServer().getPluginManager().registerEvents(new FriendsMenu(), plugin);
    getServer().getPluginManager().registerEvents(new FriendsPendingMenu(), plugin);

    getServer().getPluginManager().registerEvents(new ProfileMenu(), plugin);

    getServer().getPluginManager().registerEvents(new GenderSelectorMenu(), plugin);
    getServer().getPluginManager().registerEvents(new YourSettingsMenu(), plugin);
    getServer().getPluginManager().registerEvents(new YourStateSettingsMenu(), plugin);

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
