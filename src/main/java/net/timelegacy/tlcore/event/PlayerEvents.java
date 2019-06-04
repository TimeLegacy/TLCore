package net.timelegacy.tlcore.event;

import java.util.UUID;
import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.handler.BanHandler;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
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

    player.sendMessage("");
    player.sendMessage("");

    event.setJoinMessage(null);

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
  }

  @EventHandler
  public void PlayerQuitEvent(PlayerQuitEvent event) {
    Player player = event.getPlayer();

    player.getInventory().clear();

    for (PotionEffect effect : player.getActivePotionEffects()) {
      player.removePotionEffect(effect.getType());
    }

    player.setHealth(20D);
    player.setFoodLevel(20);
    player.setFlying(false);

    event.setQuitMessage(null);

    PlayerHandler.updateOnline(player.getUniqueId(), false);
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
