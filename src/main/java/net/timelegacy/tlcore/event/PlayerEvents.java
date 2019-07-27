package net.timelegacy.tlcore.event;

import java.util.UUID;
import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.datatype.Chat;
import net.timelegacy.tlcore.datatype.CoreSystem;
import net.timelegacy.tlcore.handler.CoreSystemHandler;
import net.timelegacy.tlcore.handler.PerkHandler;
import net.timelegacy.tlcore.handler.PermissionHandler;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.handler.ServerHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import net.timelegacy.tlcore.utils.ScoreboardUtils;
import net.timelegacy.tlcore.utils.XPBarUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.potion.PotionEffect;

public class PlayerEvents implements Listener {

  private static String header;
  private static String footer;

  private TLCore plugin = TLCore.getPlugin();

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    String joinMessage = TLCore.getPlugin().getJoinMessage()
        .replace("%displayName%", RankHandler.chatColors(player.getUniqueId())
            .replace("%username% &8%arrows%", player.getName()));

    event.setJoinMessage(MessageUtils.colorize(joinMessage));

    player.setPlayerListHeaderFooter(
        MessageUtils.colorize(header),
        MessageUtils.colorize(footer.replace("%{online}%", Bukkit.getOnlinePlayers().size() + "")));

    plugin.flySpeed.remove(player.getUniqueId());

    player.setFlying(false);
    player.setFlySpeed(0.1f);
    player.setAllowFlight(false);

    PlayerHandler.updateUsername(player);

    PlayerHandler.createPlayer(player);
    RankHandler.setTabColors(player);
    XPBarUtils.playerJoin(player);

    RankHandler.setTabColors(player);

    PlayerHandler.updateIP(player);
    PlayerHandler.updateLastConnection(player.getUniqueId());
    PlayerHandler.updateOnline(player.getUniqueId(), true);

    player.setScoreboard(ScoreboardUtils.getScoreboard());

    PermissionHandler.attachPermissions(player);

    Chat.addPlayer(player);

    RankHandler.addPermissions(player);
    PerkHandler.addPermissions(player);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerXPEvent(PlayerExpChangeEvent event) {
    XPBarUtils.xpEvent(event);
  }

  @EventHandler
  public void PlayerQuitEvent(PlayerQuitEvent event) {
    Player player = event.getPlayer();

    XPBarUtils.playerLeave(player);

    if (CoreSystemHandler.isEnabled(CoreSystem.InventoryClearOnLeave)) {
      player.getInventory().clear();
    }

    PermissionHandler.detachPermissions(player);

    for (PotionEffect effect : player.getActivePotionEffects()) {
      player.removePotionEffect(effect.getType());
    }

    player.setHealth(20D);
    player.setFoodLevel(20);
    player.setFlying(false);

    event.setQuitMessage(null);

    PlayerHandler.updateOnline(player.getUniqueId(), false);
    for (Player p : Bukkit.getOnlinePlayers()) {
      p.sendMessage("§7§l(§c-§7§l) " + RankHandler.chatColors(player.getUniqueId())
          .replace("%username% &8%arrows%", player.getName())
          .replace("&", "§")); // TODO Cleanup
      player.setPlayerListHeaderFooter(
          MessageUtils.colorize(header),
          MessageUtils.colorize(footer.replace("%{online}%", Bukkit.getOnlinePlayers().size() + "")));
    }
  }

  @EventHandler
  public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
    Player player = event.getPlayer();

    if (event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
      MessageUtils.sendMessage(player,
          MessageUtils.SUCCESS_COLOR + "Successful loaded the resource pack.", false);
    } else if (event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
      MessageUtils.sendMessage(player,
          MessageUtils.ERROR_COLOR + "Error. Failed download resource pack.", false);
    }
  }

  @EventHandler
  @SuppressWarnings("deprecation")
  public void onPreJoin(PlayerPreLoginEvent event) {
    UUID uuid = event.getUniqueId();

    if (!PlayerHandler.playerExists(uuid)) {
      return;
    }
  }

  public static void startUp() {
    String server;
    switch (ServerHandler.getType(ServerHandler.getServerUUID())) {
      case "CREATIVE":
        server = "&6&l&nCREATIVE";
        break;
      case "LOBBY":
        server = "&e&l&nLOBBY";
        break;
      default:
        server = "&f&l" + ServerHandler.getType(ServerHandler.getServerUUID());
        break;
    }

    server = server + "\n";

    header = ("&r\n"
        + "&c&lTIME LEGACY\n"
        + server
        + "&r ").replace("&", "§");
    footer = ("&r\n"
//        + "&7Players Online: &a%{online}%\n" // TODO doesn't update players online count on leave/join
//        + "&r\n"
        + "&7Need info? Go to &e/Forums\n"
        + "&7Toxicity? Report it to a &cStaff Member\n"
        + "&7Ranks, Tags, and more &bhttps://timelegacy.net/store").replace("&", "§");
  }
}
