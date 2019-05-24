package net.timelegacy.tlcore.event;

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
    Player p = event.getPlayer();

    p.sendMessage("");
    p.sendMessage("");

    /*p.getInventory().clear();

    for (PotionEffect effect : p.getActivePotionEffects()) {
      p.removePotionEffect(effect.getType());
    }

    p.setHealth(20D);
    p.setFoodLevel(20);*/

    event.setJoinMessage(null);

    plugin.flySpeed.remove(p.getName());

    p.setFlying(false);
    p.setFlySpeed(0.1f);
    p.setAllowFlight(false);

    PlayerHandler.updateUsername(p);

    PlayerHandler.createPlayer(p);
    RankHandler.setTabColors(p);

    PlayerHandler.updateIP(p);
    PlayerHandler.updateLastConnection(p);
    PlayerHandler.updateOnline(p, true);
  }

  @EventHandler
  public void PlayerQuitEvent(PlayerQuitEvent event) {
    Player p = event.getPlayer();

    p.getInventory().clear();

    for (PotionEffect effect : p.getActivePotionEffects()) {
      p.removePotionEffect(effect.getType());
    }

    p.setHealth(20D);
    p.setFoodLevel(20);
    p.setFlying(false);

    event.setQuitMessage(null);

    PlayerHandler.updateOnline(p, false);
  }

  @EventHandler
  public void onResourcePackStatus(PlayerResourcePackStatusEvent e) {
    Player p = e.getPlayer();

    if (e.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
      MessageUtils.sendMessage(
          p, MessageUtils.SUCCESS_COLOR + "Successful loaded the resource pack.", false);
    } else if (e.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {

      MessageUtils.sendMessage(
          p, MessageUtils.ERROR_COLOR + "Error. Failed download resource pack.", false);
    }
  }

  @EventHandler
  public void onPreJoin(PlayerPreLoginEvent e) {
    String p = e.getName();

    if (PlayerHandler.playerExistsName(p)) {
      if (BanHandler.isBanned(p)) {
        if (!BanHandler.getBanExpire(p).equalsIgnoreCase("false")) {
          e.disallow(
              Result.KICK_BANNED,
              ChatColor.translateAlternateColorCodes(
                  '&',
                  MessageUtils.messagePrefix
                      + "&4You have been banned. &cReason: &f&o"
                      + BanHandler.getBanReason(p)
                      + " &cExpires: &f&o"
                      + BanHandler.getBanExpire(p)));
        } else {
          e.disallow(
              Result.KICK_BANNED,
              ChatColor.translateAlternateColorCodes(
                  '&',
                  MessageUtils.messagePrefix
                      + "&4You have been banned. &cReason: &f&o"
                      + BanHandler.getBanReason(p)));
        }
      }
    }
  }
}
