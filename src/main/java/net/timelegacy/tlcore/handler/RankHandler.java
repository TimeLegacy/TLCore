package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.timelegacy.tlcore.TLCore;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RankHandler {

  public HashMap<String, String> playerCache = new HashMap<>();
  public List<Rank> rankList = new ArrayList<>();

  private TLCore core = TLCore.getInstance();

  public void loadRanks() {

    try {
      MongoCursor<Document> cursor = TLCore.getInstance().mongoDB.ranks.find().iterator();
      while (cursor.hasNext()) {
        Document doc = cursor.next();

        String name = doc.getString("name");
        int priority = doc.getInteger("priority");
        String chat = doc.getString("chat");
        String maincolor = doc.getString("maincolor");
        String cocolor = doc.getString("cocolor");
        String tab = doc.getString("tab");

        rankList.add(new Rank(name, priority, chat, maincolor, cocolor, tab));
      }

      cursor.close();

    } catch (Exception e) {
    }
  }

  /**
   * Get the rank of a player
   */
  public Rank getRank(String p) {
    Rank rank = null;

    if (core.playerHandler.playerExistsName(p)) {
      FindIterable<Document> doc = core.mongoDB.players.find(Filters.eq("username", p));
      String rnk = doc.first().getString("rank");

      for (Rank r : rankList) {
        if (r.getName().equalsIgnoreCase(rnk)) {
          rank = r;
        }
      }
    }
    return rank;
  }

  /**
   * Check if the rank is valid
   */
  public boolean isValidRank(String rank) {
    for (Rank r : rankList) {
      if (r.getName().equalsIgnoreCase(rank)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Set the rank of a player
   */
  public void setRank(String name, String rank) {
    if (core.playerHandler.playerExistsName(name)) {

      core.mongoDB.players.updateOne(
          Filters.eq("username", name), new Document("$set", new Document("rank", rank)));
    }
  }

  /**
   * Remove the rank and set them to DEFAULT
   */
  public void removeRank(String name) {

    if (core.playerHandler.playerExistsName(name)) {

      core.mongoDB.players.updateOne(
          Filters.eq("username", name), new Document("$set", new Document("rank", "DEFAULT")));
    }
  }

  public String chatColors(String p) {
    Rank rank = getRank(p);

    String format = rank.getChat();

    return format;
  }

  public void tabColors() {

    Bukkit.getScheduler()
        .scheduleSyncRepeatingTask(
            core,
            () -> {
              for (Player p : Bukkit.getOnlinePlayers()) {

                setTabColors(p);
              }
            },
            0,
            15 * 20);
  }

  public void setTabColors(Player p) {
    String name = p.getName();
    Rank rank = getRank(name);

    String format = rank.getTab() + name;

    if (format.length() > 15) {
      format = format.substring(0, 16);
    }

    p.setPlayerListName(core.messageUtils.c(format));
  }
}
