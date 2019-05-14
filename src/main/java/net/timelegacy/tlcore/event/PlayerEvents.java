package net.timelegacy.tlcore.event;

import net.timelegacy.tlcore.TLCore;
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

  private TLCore core = TLCore.getInstance();

  @EventHandler
  public void PlayerJoinEvent(PlayerJoinEvent event) {
    Player p = event.getPlayer();

    p.sendMessage("");
    p.sendMessage("");

    p.getInventory().clear();

    for (PotionEffect effect : p.getActivePotionEffects()) {
      p.removePotionEffect(effect.getType());
    }

    p.setHealth(20D);
    p.setFoodLevel(20);

    event.setJoinMessage(null);

    core.flySpeed.remove(p.getName());

    p.setFlying(false);
    p.setFlySpeed(0.1f);
    p.setAllowFlight(false);

    core.playerHandler.createPlayer(p);
    core.rankHandler.setTabColors(p);

    core.playerHandler.updateUsername(p);

    core.playerHandler.updateIP(p);
    core.playerHandler.updateLastConnection(p);
    core.playerHandler.updateOnline(p, true);
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

    core.rankHandler.playerCache.remove(p.getName());
    core.playerHandler.updateOnline(p, false);
  }

  @EventHandler
  public void onResourcePackStatus(PlayerResourcePackStatusEvent e) {
    Player p = e.getPlayer();

    if (e.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
      core.messageUtils.sendMessage(
          p, core.messageUtils.SUCCESS_COLOR + "Successful loaded the resource pack.", false);
    } else if (e.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {

      core.messageUtils.sendMessage(
          p, core.messageUtils.ERROR_COLOR + "Error. Failed download resource pack.", false);
    }
  }

  @EventHandler
  public void onPreJoin(PlayerPreLoginEvent e) {
    String p = e.getName();

    if (core.playerHandler.playerExistsName(p)) {
      if (core.banHandler.isBanned(p)) {
        if (!core.banHandler.getBanExpire(p).equalsIgnoreCase("false")) {
          e.disallow(
              Result.KICK_BANNED,
              ChatColor.translateAlternateColorCodes(
                  '&',
                  core.messageUtils.messagePrefix
                      + "&4You have been banned. &cReason: &f&o"
                      + core.banHandler.getBanReason(p)
                      + " &cExpires: &f&o"
                      + core.banHandler.getBanExpire(p)));
        } else {
          e.disallow(
              Result.KICK_BANNED,
              ChatColor.translateAlternateColorCodes(
                  '&',
                  core.messageUtils.messagePrefix
                      + "&4You have been banned. &cReason: &f&o"
                      + core.banHandler.getBanReason(p)));
        }
      }
    }
  }
}
