package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.datatype.Rank;
import net.timelegacy.tlcore.mongodb.MongoDB;
import net.timelegacy.tlcore.utils.MessageUtils;
import net.timelegacy.tlcore.utils.ScoreboardUtils;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class RankHandler {

  private static TLCore plugin = TLCore.getPlugin();

  public static List<Rank> rankList = new ArrayList<>();

  private static MongoCollection<Document> ranks = MongoDB.mongoDatabase.getCollection("ranks");
  private static MongoCollection<Document> players = MongoDB.mongoDatabase.getCollection("players");

  /**
   * Load the ranks from the database
   */
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

    } catch (Exception ignored) {
    }

    for (Rank rr : rankList) {
        Team tabRank = ScoreboardUtils.getScoreboard().registerNewTeam(rr.getName());
        tabRank.setPrefix(MessageUtils.colorize(rr.getTab()));
        tabRank.setColor(ChatColor.getByChar(rr.getPrimaryColor().replace("&", "")));
    }
  }

  /**
   * Get the rank of a player
   *
   * @param uuid player's uuid
   */
  public static Rank getRank(UUID uuid) {
    if (PlayerHandler.playerExists(uuid)) {
      FindIterable<Document> doc = players.find(Filters.eq("uuid", uuid.toString()));
      String rnk = doc.first().getString("rank");

      String[] ranks = rnk.split(",");
      //list of ranks
      for (String r : ranks) {
        String[] rr = r.split(":");
        Rank ranka = stringToRank(rr[0]);
        if (ranka.getPriority() >= 6) {
          return ranka;
        } else if (rr[1].equalsIgnoreCase(ServerHandler.getType(ServerHandler.getServerUUID()))) {
          return ranka;
        }
      }
    }

    return stringToRank("DEFAULT");
  }

  /**
   * Convert string to a rank if it exists
   */
  public static Rank stringToRank(String rank) {
    for (Rank r : rankList) {
      if (r.getName().equalsIgnoreCase(rank)) {
        return r;
      }
    }

    return null;
  }

  /**
   * Check if the rank is valid
   *
   * @param rank rank name as string
   */
  public static boolean isValidRank(String rank) {
    for (Rank r : rankList) {
      if (r.getName().equalsIgnoreCase(rank)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Set the rank of a player
   *
   * @param uuid player's uuid
   * @param rank rank to set
   */
  public static void setRank(UUID uuid, Rank rank) {
    if (!PlayerHandler.playerExists(uuid)) {
      return;
    }

    FindIterable<Document> doc = players.find(Filters.eq("uuid", uuid.toString()));
    String rnk = doc.first().getString("rank");

    String[] ranks = rnk.split(",");
    //list of ranks
    for (String r : ranks) {
      String[] rr = r.split(":");
      if (rank.getPriority() >= 6 && rr[1].equalsIgnoreCase("GLOBAL")) {
        players.updateOne(
            Filters.eq("uuid", uuid.toString()),
            new Document("$set", new Document("rank", rnk.replace(r, rank.getName() + ":GLOBAL"))));
        break;
        //check if server is the same and override
      } else if (rr[1].equalsIgnoreCase(ServerHandler.getType(ServerHandler.getServerUUID()))) {
        if (!rank.getName().equalsIgnoreCase("DEFAULT")) {
          players.updateOne(
              Filters.eq("uuid", uuid.toString()), new Document("$set", new Document("rank",
                  rnk.replace(r, rank.getName() + ":" + ServerHandler.getType(ServerHandler.getServerUUID())))));
          break;
        } else {
          removeRank(uuid);
          break;
        }
      } else if (!rnk.contains(ServerHandler.getType(ServerHandler.getServerUUID()))) {
        players.updateOne(
            Filters.eq("uuid", uuid.toString()), new Document("$set", new Document("rank",
                rnk + rank.getName() + ":" + ServerHandler.getType(ServerHandler.getServerUUID()) + ",")));
        break;
      }
    }

  }

  /**
   * Remove the rank of a player
   *
   * @param uuid player's uuid
   */
  public static void removeRank(UUID uuid) {
    if (!PlayerHandler.playerExists(uuid)) {
      return;
    }

    FindIterable<Document> doc = players.find(Filters.eq("uuid", uuid.toString()));
    String rnk = doc.first().getString("rank");

    String[] ranks = rnk.split(",");
    //list of ranks
    for (String r : ranks) {
      String[] rr = r.split(":");
      if (stringToRank(rr[0]).getPriority() >= 6 && rr[1].equalsIgnoreCase("GLOBAL")) {
        //remove staff rank if they're staff
        players.updateOne(Filters.eq("uuid", uuid.toString()),
            new Document("$set", new Document("rank", rnk.replace(r, "DEFAULT:GLOBAL"))));

        //check if server is the same and override
        break;
      } else if (rr[1].equalsIgnoreCase(ServerHandler.getType(ServerHandler.getServerUUID()))) {
        players.updateOne(Filters.eq("uuid", uuid.toString()),
            new Document("$set", new Document("rank", rnk.replace(r + ",", ""))));
        break;
      }
    }

  }

  /**
   * Chat colors for a player
   *
   * @param uuid player's uuid
   */
  public static String chatColors(UUID uuid) {
    Rank rank = getRank(uuid);
    String format = rank.getChat();

    return format;
  }

  /**
   * Set the tab colors for a player
   *
   * @param player player
   */
  public static void setTabColors(Player player) {
    Rank rank = getRank(player.getUniqueId());

    Team tabRank = ScoreboardUtils.getScoreboard().getTeam(rank.getName());
    tabRank.addEntry(player.getName());
  }
}
