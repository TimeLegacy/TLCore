package net.timelegacy.tlcore;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import de.zabuza.grawlox.Grawlox;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import net.timelegacy.tlcore.command.BanCommand;
import net.timelegacy.tlcore.command.CoinManagementCommand;
import net.timelegacy.tlcore.command.CoinsCommand;
import net.timelegacy.tlcore.command.CrateKeyCommand;
import net.timelegacy.tlcore.command.FlyCommand;
import net.timelegacy.tlcore.command.FriendCommand;
import net.timelegacy.tlcore.command.GamemodeCommand;
import net.timelegacy.tlcore.command.ListCommand;
import net.timelegacy.tlcore.command.MultiplierCommand;
import net.timelegacy.tlcore.command.MuteCommand;
import net.timelegacy.tlcore.command.PerkCommand;
import net.timelegacy.tlcore.command.ProfileCommand;
import net.timelegacy.tlcore.command.RankManagementCommand;
import net.timelegacy.tlcore.command.RebootCommand;
import net.timelegacy.tlcore.command.SetSpawnCommand;
import net.timelegacy.tlcore.command.SpawnCommand;
import net.timelegacy.tlcore.command.TeleportCommand;
import net.timelegacy.tlcore.command.TeleportHereCommand;
import net.timelegacy.tlcore.command.UnBanCommand;
import net.timelegacy.tlcore.command.UnMuteCommand;
import net.timelegacy.tlcore.command.media.DiscordCommand;
import net.timelegacy.tlcore.command.media.ForumsCommand;
import net.timelegacy.tlcore.command.media.GimmieSomePizzaCommand;
import net.timelegacy.tlcore.command.media.StoreCommand;
import net.timelegacy.tlcore.command.media.WebsiteCommand;
import net.timelegacy.tlcore.event.AFKEvents;
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
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TLCore extends JavaPlugin {

  private static TLCore plugin = null;
  public static ProtocolManager protocolManager;

  public FileConfiguration config;
  public Logger logger = Bukkit.getLogger();
  public List<UUID> flySpeed = new ArrayList<>();

  public static Grawlox grawlox;

  public static TLCore getPlugin() {
    return plugin;
  }

  private String joinMessage;

  @Override
  public void onEnable() {
    protocolManager = ProtocolLibrary.getProtocolManager();
    plugin = this;
    config = this.getConfig();

    saveResource("config.yml", false);
//    saveDefaultConfig();
//    getConfig().options().copyDefaults(true);
//    saveConfig();

    joinMessage = getConfig().getString("join-message");

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
    getCommand("perks").setExecutor(new PerkCommand());
    getCommand("spawn").setExecutor(new SpawnCommand(this)); // Steven sends plugin to command class, not me -piajesse
    getCommand("setspawn").setExecutor(new SetSpawnCommand(this));

    getCommand("discord").setExecutor(new DiscordCommand());
    getCommand("forums").setExecutor(new ForumsCommand());
    getCommand("gimmesomepizza").setExecutor(new GimmieSomePizzaCommand());
    getCommand("store").setExecutor(new StoreCommand());
    getCommand("website").setExecutor(new WebsiteCommand());

    getCommand("friends").setExecutor(new FriendCommand());

    PluginManager pm = getServer().getPluginManager();

    pm.registerEvents(new FilterEvents(), plugin);
    pm.registerEvents(new PlayerEvents(), plugin);
    pm.registerEvents(new AFKEvents(), plugin);

    pm.registerEvents(new FriendsMenu(), plugin);
    pm.registerEvents(new FriendsPendingMenu(), plugin);

    pm.registerEvents(new ProfileMenu(), plugin);

    pm.registerEvents(new GenderSelectorMenu(), plugin);
    pm.registerEvents(new YourSettingsMenu(), plugin);
    pm.registerEvents(new YourStateSettingsMenu(), plugin);

    RankHandler.loadRanks();

    ServerHandler.setOnline(ServerHandler.getServerUUID(), true);

    PlayerEvents.startUp();

    try {
      grawlox = Grawlox.createFromSwearWordsPath(new File(TLCore.getPlugin().getDataFolder(), "swearWords.txt"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onDisable() {
    ServerHandler.setOnline(ServerHandler.getServerUUID(), false);

    PermissionHandler.clearPermissions();
    MongoDB.disconnect();
  }

  public String getJoinMessage() {
    return joinMessage;
  }

  public void setJoinMessage(String joinMessage) {
    this.joinMessage = joinMessage;
  }
}
