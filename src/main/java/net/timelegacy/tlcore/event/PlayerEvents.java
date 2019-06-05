package net.timelegacy.tlcore.event;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import java.util.UUID;
import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.handler.BanHandler;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.potion.PotionEffect;

@SuppressWarnings("deprecation")
public class PlayerEvents implements Listener {

  private TLCore plugin = TLCore.getPlugin();

  @EventHandler
  public void PlayerJoinEvent(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    event.setJoinMessage(null);

    for (Player p : Bukkit.getOnlinePlayers()) {
      p.sendMessage("§7§l(§a+§7§l) "
          + RankHandler.chatColors(player.getUniqueId())
          .replace("%username% &8%arrows%", player.getName())
          .replace("&", "§")); // TODO Cleanup
    }

    plugin.flySpeed.remove(player.getUniqueId());

    player.setFlying(false);
    player.setFlySpeed(0.1f);
    player.setAllowFlight(false);

    PlayerHandler.updateUsername(player);

    PlayerHandler.createPlayer(player);
    RankHandler.setTabColors(player);

    PlayerHandler.updateIP(player);
    PlayerHandler.updateLastConnection(player.getUniqueId());
    PlayerHandler.updateOnline(player.getUniqueId(), true);

    // Tab Menu
    PacketContainer pc = TLCore.protocolManager
        .createPacket(com.comphenix.protocol.PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);

    pc.getChatComponents().write(0, WrappedChatComponent.fromText(MessageUtils.colorize("&c&lTIME LEGACY")))
        .write(1, WrappedChatComponent.fromText(MessageUtils.colorize("&eplay.timelegacy.net")));
    try {
      TLCore.protocolManager.sendServerPacket(event.getPlayer(), pc);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @EventHandler
  public void PlayerQuitEvent(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    RankHandler.removeTabColors(player);
    player.getInventory().clear();

    for (PotionEffect effect : player.getActivePotionEffects()) {
      player.removePotionEffect(effect.getType());
    }

    player.setHealth(20D);
    player.setFoodLevel(20);
    player.setFlying(false);

    event.setQuitMessage(null);

    PlayerHandler.updateOnline(player.getUniqueId(), false);
    for (Player p : Bukkit.getOnlinePlayers()) {
      p.sendMessage("§7§l(§c-§7§l) "
          + RankHandler.chatColors(player.getUniqueId())
          .replace("%username% &8%arrows%", player.getName())
          .replace("&", "§")); // TODO Cleanup
    }
  }

  @EventHandler
  public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
    Player player = event.getPlayer();

    if (event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
      MessageUtils.sendMessage(player, MessageUtils.SUCCESS_COLOR + "Successful loaded the resource pack.", false);
    } else if (event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
      MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR + "Error. Failed download resource pack.", false);
    }
  }

  @EventHandler
  public void onPreJoin(PlayerPreLoginEvent event) {
    UUID uuid = event.getUniqueId();

    if (!PlayerHandler.playerExists(uuid)) {
      return;
    }

    if (!BanHandler.isBanned(uuid)) {
      return;
    }

    if (BanHandler.getBanExpire(uuid).equalsIgnoreCase("false")) {
      event.disallow(Result.KICK_BANNED, ChatColor.translateAlternateColorCodes('&',
          MessageUtils.messagePrefix
              + "&4You have been banned. &cReason: &f&o"
              + BanHandler.getBanReason(uuid)
              + " &cExpires: &f&o"
              + BanHandler.getBanExpire(uuid)));
      return;
    }

    event.disallow(Result.KICK_BANNED, ChatColor.translateAlternateColorCodes('&',
        MessageUtils.messagePrefix
            + "&4You have been banned. &cReason: &f&o"
            + BanHandler.getBanReason(uuid)));


  }
}
