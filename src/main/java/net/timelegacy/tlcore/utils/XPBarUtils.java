package net.timelegacy.tlcore.utils;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class XPBarUtils {

  private static boolean XPSystemActive = false;
  private static HashMap<Player, Integer> xpLevel = new HashMap<>();
  private static HashMap<Player, Double> xpValue = new HashMap<>();


  public static void xpEvent(PlayerExpChangeEvent event) {
    if (XPSystemActive) {
      event.setAmount(0);
    }
  }

  public static void playerJoin(Player player) {
    if (XPSystemActive) {
      xpLevel.put(player, player.getLevel());
      xpValue.put(player, (double) player.getExp() - player.getLevel());
    }
  }

  public static void playerLeave(Player player) {
    xpLevel.remove(player);
    xpValue.remove(player);
  }

  public static int getLevel(Player player) {
    return xpLevel.get(player);
  }

  public static void setLevel(Player player, int level) {
    xpLevel.put(player, level);
    updatePlayersBar(player);
  }

  public static double getValue(Player player) {
    return xpValue.get(player);
  }

  public static void setValue(Player player, double value) {
    if (value > 1 || value < 0) {
      Bukkit.getLogger().info("setValue() needs a double between 0 and 1");
    }
    xpValue.put(player, value);
    updatePlayersBar(player);
  }

  public static void setXPSystemActive() {
    XPSystemActive = true;
  }

  public static void updatePlayersBar(Player player) {
    int level = xpLevel.get(player);
    double value = xpValue.get(player);
    if (level < 16) {
      // Level 0-15
      player.setTotalExperience(
          ((level * level) + (6 * level)) /*Level*/ + (int) Math.round((2 * level + 7) * value) /*Value*/);

    } else {
      if (level > 30) {
        // Level 31+
        player.setTotalExperience((int) ((4.5 * (level * level) - (162.5 * level) + 2220) /*Level*/ + Math
            .round((9 * level - 158) * value) /*Value*/));
      } else {
        // Level 16-30
        player.setTotalExperience((int) ((2.5 * (level * level) - (40.5 * level) + 360) /*Level*/ + Math
            .round((5 * level - 38) * value) /*Value*/));
      }
    }
  }
}



