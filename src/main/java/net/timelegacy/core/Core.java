package net.timelegacy.core;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.timelegacy.core.command.BanCommand;
import net.timelegacy.core.command.ChatTypeCommand;
import net.timelegacy.core.command.CoinManagementCommand;
import net.timelegacy.core.command.CoinsCommand;
import net.timelegacy.core.command.CrateKeyCommand;
import net.timelegacy.core.command.DiscordCommand;
import net.timelegacy.core.command.FlyCommand;
import net.timelegacy.core.command.GamemodeCommand;
import net.timelegacy.core.command.ListCommand;
import net.timelegacy.core.command.MultiplierCommand;
import net.timelegacy.core.command.MuteCommand;
import net.timelegacy.core.command.RankManagementCommand;
import net.timelegacy.core.command.RebootCommand;
import net.timelegacy.core.command.TeleportCommand;
import net.timelegacy.core.command.TeleportHereCommand;
import net.timelegacy.core.command.TogglePhysicsCommand;
import net.timelegacy.core.command.UnBanCommand;
import net.timelegacy.core.command.UnMuteCommand;
import net.timelegacy.core.event.FilterEvents;
import net.timelegacy.core.event.PhysicsEvents;
import net.timelegacy.core.event.PlayerEvents;
import net.timelegacy.core.handler.BanHandler;
import net.timelegacy.core.handler.CoinHandler;
import net.timelegacy.core.handler.CrateKeyHandler;
import net.timelegacy.core.handler.DiscordHandler;
import net.timelegacy.core.handler.MultiplierHandler;
import net.timelegacy.core.handler.MuteHandler;
import net.timelegacy.core.handler.PerkHandler;
import net.timelegacy.core.handler.PermissionHandler;
import net.timelegacy.core.handler.PlayerHandler;
import net.timelegacy.core.handler.PunishmentHandler;
import net.timelegacy.core.handler.RankHandler;
import net.timelegacy.core.handler.ServerHandler;
import net.timelegacy.core.handler.StatsHandler;
import net.timelegacy.core.handler.TopPlayersHandler;
import net.timelegacy.core.mongodb.MongoDB;
import net.timelegacy.core.utils.BungeeUtils;
import net.timelegacy.core.utils.EntityUtils;
import net.timelegacy.core.utils.FireworkUtils;
import net.timelegacy.core.utils.HologramUtils;
import net.timelegacy.core.utils.ItemUtils;
import net.timelegacy.core.utils.JarUtils;
import net.timelegacy.core.utils.MessageUtils;
import net.timelegacy.core.utils.ParticleUtils;
import net.timelegacy.core.utils.PotionUtils;
import net.timelegacy.core.utils.WorldDownloaderUtils;
import net.timelegacy.core.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {

  private static Core plugin = null;
  public FileConfiguration config;
  public Logger log = Bukkit.getLogger();
  public ArrayList<String> flySpeed = new ArrayList<>();
  public boolean physics = true;

  // Handlers
  public BanHandler banHandler;
  public MuteHandler muteHandler;
  public CoinHandler coinHandler;
  public MultiplierHandler multiplierHandler;
  public PerkHandler perkHandler;
  public PlayerHandler playerHandler;
  public RankHandler rankHandler;
  public ServerHandler serverHandler;
  public StatsHandler statsHandler;
  public TopPlayersHandler topPlayersHandler;
  public PermissionHandler permissionHandler;
  public CrateKeyHandler crateKeyHandler;
  public DiscordHandler discordHandler;
  public PunishmentHandler punishmentHandler;

  // Utils
  public BungeeUtils bungeeUtils;
  public EntityUtils entityUtils;
  public FireworkUtils fireworkUtils;
  public HologramUtils hologramUtils;
  public MessageUtils messageUtils;
  public PotionUtils potionUtils;
  public WorldUtils worldUtils;
  public ItemUtils itemUtils;
  public ParticleUtils particleUtils;
  public JarUtils jarUtils;
  public WorldDownloaderUtils worldDownloader;

  // MongoDB
  public MongoDB mongoDB;

  public static Core getInstance() {
    return plugin;
  }

  private void init() {

    // Handlers
    banHandler = new BanHandler();
    coinHandler = new CoinHandler();
    multiplierHandler = new MultiplierHandler();
    perkHandler = new PerkHandler();
    playerHandler = new PlayerHandler();
    rankHandler = new RankHandler();
    serverHandler = new ServerHandler();
    statsHandler = new StatsHandler();
    topPlayersHandler = new TopPlayersHandler();
    permissionHandler = new PermissionHandler();
    muteHandler = new MuteHandler();
    crateKeyHandler = new CrateKeyHandler();
    discordHandler = new DiscordHandler();
    punishmentHandler = new PunishmentHandler();

    // Utils
    bungeeUtils = new BungeeUtils();
    entityUtils = new EntityUtils();
    fireworkUtils = new FireworkUtils();
    hologramUtils = new HologramUtils();
    itemUtils = new ItemUtils();
    messageUtils = new MessageUtils();
    potionUtils = new PotionUtils();
    worldUtils = new WorldUtils();
    itemUtils = new ItemUtils();
    worldDownloader = new WorldDownloaderUtils();
    particleUtils = new ParticleUtils();

    // MongoDB
    mongoDB = new MongoDB();
  }

  public void onEnable() {

    plugin = this;
    config = this.getConfig();

    plugin.saveDefaultConfig();
    plugin.getConfig().options().copyDefaults(true);
    plugin.saveConfig();

    log = plugin.getLogger();

    Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");

    jarUtils = new JarUtils();

    init();

    mongoDB.connect(config.getString("URI"));

    serverHandler.createServer();

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
    plugin.getCommand("discord").setExecutor(new DiscordCommand());

    plugin.getServer().getPluginManager().registerEvents(new FilterEvents(), plugin);
    plugin.getServer().getPluginManager().registerEvents(new PlayerEvents(), plugin);
    plugin.getServer().getPluginManager().registerEvents(new PhysicsEvents(), plugin);

    rankHandler.loadRanks();
    rankHandler.tabColors();
  }

  @Override
  public void onDisable() {

    permissionHandler.clearPermissions();

    mongoDB.disconnect();
    // ac.unregister();
  }

  public void log(String msg, Level level) {
    log.log(level, msg);
  }
}
