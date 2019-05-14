package net.timelegacy.tlcore;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.timelegacy.tlcore.command.BanCommand;
import net.timelegacy.tlcore.command.ChatTypeCommand;
import net.timelegacy.tlcore.command.CoinManagementCommand;
import net.timelegacy.tlcore.command.CoinsCommand;
import net.timelegacy.tlcore.command.CrateKeyCommand;
import net.timelegacy.tlcore.command.DiscordCommand;
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
import net.timelegacy.tlcore.handler.BanHandler;
import net.timelegacy.tlcore.handler.CoinHandler;
import net.timelegacy.tlcore.handler.CrateKeyHandler;
import net.timelegacy.tlcore.handler.DiscordHandler;
import net.timelegacy.tlcore.handler.MultiplierHandler;
import net.timelegacy.tlcore.handler.MuteHandler;
import net.timelegacy.tlcore.handler.PerkHandler;
import net.timelegacy.tlcore.handler.PermissionHandler;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.handler.PunishmentHandler;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.handler.ServerHandler;
import net.timelegacy.tlcore.handler.StatsHandler;
import net.timelegacy.tlcore.handler.TopPlayersHandler;
import net.timelegacy.tlcore.mongodb.MongoDB;
import net.timelegacy.tlcore.utils.BungeeUtils;
import net.timelegacy.tlcore.utils.EntityUtils;
import net.timelegacy.tlcore.utils.FireworkUtils;
import net.timelegacy.tlcore.utils.HologramUtils;
import net.timelegacy.tlcore.utils.ItemUtils;
import net.timelegacy.tlcore.utils.JarUtils;
import net.timelegacy.tlcore.utils.MessageUtils;
import net.timelegacy.tlcore.utils.ParticleUtils;
import net.timelegacy.tlcore.utils.PotionUtils;
import net.timelegacy.tlcore.utils.WorldDownloaderUtils;
import net.timelegacy.tlcore.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class TLCore extends JavaPlugin {

  private static TLCore plugin = null;
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

  public static TLCore getInstance() {
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
