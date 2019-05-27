package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.datatype.Rank;
import net.timelegacy.tlcore.mongodb.MongoDB;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RankHandler {

  public static List<Rank> rankList = new ArrayList<>();

  private static TLCore plugin = TLCore.getPlugin();

  private static MongoCollection<Document> ranks = MongoDB.mongoDatabase.getCollection("ranks");
  private static MongoCollection<Document> players = MongoDB.mongoDatabase.getCollection("players");

  public static void loadRanks() {

    try {
      MongoCursor<Document> cursor = ranks.find().iterator();
      while (cursor.hasNext()) {
        Document doc = cursor.next();

        String name = doc.getString("name");
        int priority = doc.getInteger("priority");
        String chat = doc.getString("chat_format");
        String primary_color = doc.getString("primary_color");
        String secondary_color = doc.getString("secondary_color");
        String tab = doc.getString("tab_format");

        rankList.add(new Rank(name, priority, chat, primary_color, secondary_color, tab));
      }

      cursor.close();

    } catch (Exception e) {
    }
  }

  public static Rank getRank(String playerName) {
    Rank rank = null;

    if (PlayerHandler.playerExistsName(playerName)) {
      FindIterable<Document> doc = players.find(Filters.eq("username", playerName));
      String rnk = doc.first().getString("rank");

      for (Rank r : rankList) {
        if (r.getName().equalsIgnoreCase(rnk)) {
          rank = r;
        }
      }
    }
    return rank;
  }

  public static boolean isValidRank(String rank) {
    for (Rank r : rankList) {
      if (r.getName().equalsIgnoreCase(rank)) {
        return true;
      }
    }

    return false;
  }

  public static void setRank(String name, String rank) {
    if (PlayerHandler.playerExistsName(name)) {

      players.updateOne(
          Filters.eq("username", name), new Document("$set", new Document("rank", rank)));
    }
  }

  public static void removeRank(String name) {

    if (PlayerHandler.playerExistsName(name)) {

      players.updateOne(
          Filters.eq("username", name), new Document("$set", new Document("rank", "DEFAULT")));
    }
  }

  public static String chatColors(String p) {
    Rank rank = getRank(p);

    String format = rank.getChat();

    return format;
  }

  public static void tabColors() {

    Bukkit.getScheduler()
        .scheduleSyncRepeatingTask(
            plugin,
            () -> {
              for (Player p : Bukkit.getOnlinePlayers()) {

                setTabColors(p);
              }
            },
            0,
            15 * 20);
  }

  public static void setTabColors(Player player) {
    String name = player.getName();
    Rank rank = getRank(name);

    String format = rank.getTab() + name;

    if (format.length() > 15) {
      format = format.substring(0, 16);
    }

    player.setPlayerListName(MessageUtils.colorize(format));
  }
}
