package net.timelegacy.tlcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardUtils {

  private static Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

  public static Scoreboard getScoreboard() {
    return scoreboard;
  }

}
