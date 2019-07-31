package net.timelegacy.tlcore.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.timelegacy.tlcore.datatype.PlayerProfile;
import net.timelegacy.tlcore.datatype.PlayerProfile.Status;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AFKHandler {

  public static HashMap<UUID, Long> lastActive = new HashMap<>();
  public static ArrayList<UUID> afk = new ArrayList<>();
  public static ScheduledExecutorService exec;

  public static void check() {
    exec = Executors.newSingleThreadScheduledExecutor();
    exec.scheduleAtFixedRate(
        new Runnable() {
          @Override
          public void run() {
            for (Player p : Bukkit.getOnlinePlayers()) {
              UUID uuid = p.getUniqueId();
              if (!lastActive.keySet().contains(uuid)) {
                playerAction(p);
                continue;
              }
              if (afk.contains(uuid)) {
                continue;
              }

              long time = (System.currentTimeMillis() / 1000);
              if (lastActive.get(uuid) <= (time - 240)) {
                setAFK(uuid);
              }
            }
          }
        }, 0, 1, TimeUnit.MINUTES);
  }

  public static void playerAction(Player player) {
    if (afk.contains(player.getUniqueId())) {
      setActive(player.getUniqueId());
    } else {
      lastActive.put(player.getUniqueId(), System.currentTimeMillis() / 1000);
    }
  }

  public static void setAFK(UUID uuid) {
    afk.add(uuid);
    MessageUtils.sendMessage(Bukkit.getPlayer(uuid), "&7You are now AFK.", "");

    PlayerProfile playerProfile;
    if (CacheHandler.isPlayerCached(uuid)) {
      playerProfile = CacheHandler.getPlayerData(uuid).getPlayerProfile();
    } else {
      playerProfile = new PlayerProfile(uuid);
    }

    if (playerProfile.getStatus() == Status.ACTIVE) {
      playerProfile.setStatus(Status.AWAY);
    }
  }

  public static void setActive(UUID uuid) {
    if (afk.contains(uuid)) {
      afk.remove(uuid);
      MessageUtils.sendMessage(Bukkit.getPlayer(uuid), "&7You are no longer AFK.", "");

      PlayerProfile playerProfile;
      if (CacheHandler.isPlayerCached(uuid)) {
        playerProfile = CacheHandler.getPlayerData(uuid).getPlayerProfile();
      } else {
        playerProfile = new PlayerProfile(uuid);
      }

      if (playerProfile.getStatus() == Status.AWAY) {
        playerProfile.setStatus(Status.ACTIVE);
      }
    }
  }

  public static boolean isAFK(UUID uuid) {
    return afk.contains(uuid);
  }
}
