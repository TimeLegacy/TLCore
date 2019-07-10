package net.timelegacy.tlcore.utils;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreboardUtils {

  private static HashMap<Player, Scoreboard> scoreboards = new HashMap<>();

  public static Scoreboard getScoreboard(Player player) {
    if (!scoreboards.containsKey(player)) {
      ScoreboardManager manager = Bukkit.getScoreboardManager();
      Scoreboard board = manager.getNewScoreboard();
      scoreboards.put(player, board);

      player.setScoreboard(board);

      return board;
    } else {
      return scoreboards.get(player);
    }
  }

}
