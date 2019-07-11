package net.timelegacy.tlcore.event;

import java.util.HashMap;
import net.timelegacy.tlcore.handler.AFKHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AFKEvents implements Listener {

  public static HashMap<Player, Float> lastYaw = new HashMap<>();

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    AFKHandler.playerAction(e.getPlayer());
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent e) {
    AFKHandler.lastActive.remove(e.getPlayer().getUniqueId());
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent e) {
    if (lastYaw.containsKey(e.getPlayer())) {
      if (lastYaw.get(e.getPlayer()).floatValue() != e.getPlayer().getLocation().getYaw()) {
        AFKHandler.playerAction(e.getPlayer());
        lastYaw.put(e.getPlayer(), e.getPlayer().getLocation().getYaw());
      }
    } else {
      lastYaw.put(e.getPlayer(), e.getPlayer().getLocation().getYaw());
    }
  }

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent e) {
    AFKHandler.playerAction(e.getPlayer());
  }

}
