package net.timelegacy.tlcore.utils;

import java.util.HashMap;
import java.util.UUID;
import net.timelegacy.tlcore.datatype.CustomScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardUtils {

  private static Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

  public static Scoreboard getScoreboard() {
    return scoreboard;
  }

  private static HashMap<UUID, CustomScoreboard> customScoreboards = new HashMap<>();

  public static void saveCustomScoreboard(UUID uuid, CustomScoreboard customScoreboard) {
    customScoreboards.put(uuid, customScoreboard);
  }

  public static CustomScoreboard getCustomScoreboard(UUID uuid) {
    if (customScoreboards.containsKey(uuid)) {
      return customScoreboards.get(uuid);
    } else {
      return null;
    }
  }
}
