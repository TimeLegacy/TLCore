package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import net.timelegacy.tlcore.datatype.Rank;
import net.timelegacy.tlcore.mongodb.MongoDB;
import net.timelegacy.tlcore.utils.MessageUtils;
import net.timelegacy.tlcore.utils.ScoreboardUtils;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class RankHandler {
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
        String color = doc.getString("color");
        String tab = doc.getString("tab_format");
        String perms = doc.getString("permissions");

        rankList.add(new Rank(name, priority, chat, color, tab, perms));
      }

      cursor.close();

    } catch (Exception ignored) {
    }

    for (Rank rr : rankList) {
      Team tabRank = ScoreboardUtils.getScoreboard().registerNewTeam(rr.getName());
      tabRank.setPrefix(MessageUtils.colorize(rr.getTab()));
      tabRank.setColor(ChatColor.getByChar(rr.getColor().replace("&", "")));
    }
  }

  /**
   * Get the rank of a player
   *
   * @param uuid player's uuid
   */
  public static Rank getRank(UUID uuid) {
    if (PlayerHandler.playerExists(uuid)) {

      if (CacheHandler.isPlayerCached(uuid)) {
        return CacheHandler.getPlayerData(uuid).getRank();
      } else {
        FindIterable<Document> doc = players.find(Filters.eq("uuid", uuid.toString()));
        String rnk = doc.first().getString("rank");
        return stringToRank(rnk);
      }
    }

    return stringToRank("DEFAULT");
  }

  /**
   * Set the permissions along with inheritance for a player on the current server
   *
   * @param player Player
   */
  public static void addPermissions(Player player) {
    String currentServerType = ServerHandler.getType(ServerHandler.getServerUUID());

    Rank rank = RankHandler.getRank(player.getUniqueId());

    //NORMAL RANK'S PERMISSIONS
    if (!rank.getPermissions().isEmpty()) {
      System.out.println(rank.getPermissions());

      HashMap<String, String> permissionsServerBased = new HashMap<>();

      String[] serverTypes = rank.getPermissions().split("-");
      for (String server : serverTypes) {
        String srvType = server.split(":")[0];
        String pp = server.split(":")[1];
        permissionsServerBased.put(srvType, pp);
      }
      String[] permissionsSplit;
      if (permissionsServerBased.get(currentServerType) != null) {

        permissionsSplit = permissionsServerBased.get(currentServerType.toUpperCase()).split(",");

        for (String perm : permissionsSplit) {
          PermissionHandler.addPermission(player, perm);
        }
      }
    }

    if (rank.getPriority() != 0) {
      // INHERITANCE
      for (Rank rankInherit : rankList) {
        if (!rankInherit.getPermissions().isEmpty()) {

          HashMap<String, String> permsSrv = new HashMap<>();

          String[] types = rankInherit.getPermissions().split("-");
          for (String server : types) {
            String srvType = server.split(":")[0];
            String pp = server.split(":")[1];
            permsSrv.put(srvType, pp);
          }

          if (rankInherit.getPriority() < rank.getPriority()
              && permsSrv.get(currentServerType.toUpperCase()) != null) {

            if (isStaffRank(rank) && rankInherit.getPriority() >= 5) {
              String[] permissionsInherit =
                  permsSrv.get(currentServerType.toUpperCase()).split(",");
              for (String perm : permissionsInherit) {
                PermissionHandler.addPermission(player, perm);
              }
            } else if (!isStaffRank(rank) && rankInherit.getPriority() >= 1) {
              String[] permissionsInherit =
                  permsSrv.get(currentServerType.toUpperCase()).split(",");
              for (String perm : permissionsInherit) {
                PermissionHandler.addPermission(player, perm);
              }
            }

            if (rankInherit.getPriority() == 0) {
              String[] permissionsInherit =
                  permsSrv.get(currentServerType.toUpperCase()).split(",");
              for (String perm : permissionsInherit) {
                PermissionHandler.addPermission(player, perm);
              }
            }
          }
        }
      }
    }
  }

  /**
   * Check if a rank is a staff rank
   */

  public static boolean isStaffRank(Rank rank) {
    return (rank.getPriority() >= 5);
  }

  /** Convert string to a rank if it exists */
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

    if (CacheHandler.isPlayerCached(uuid)) {
      CacheHandler.getPlayerData(uuid).setRank(rank);
    } else {
      players.updateOne(
          Filters.eq("uuid", uuid.toString()),
          new Document("$set", new Document("rank", rank.getName())));
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

    if (CacheHandler.isPlayerCached(uuid)) {
      CacheHandler.getPlayerData(uuid).setRank(stringToRank("DEFAULT"));
    } else {
      players.updateOne(
          Filters.eq("uuid", uuid.toString()),
          new Document("$set", new Document("rank", "DEFAULT")));
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
