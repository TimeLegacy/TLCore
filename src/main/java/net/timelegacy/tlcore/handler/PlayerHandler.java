package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bson.Document;
import org.bukkit.entity.Player;

public class PlayerHandler {

  private static MongoCollection<Document> players = MongoDB.mongoDatabase.getCollection("players");

  public static void createPlayer(Player player) {
    if (!playerExists(player)) {

      Document doc =
          new Document("uuid", player.getUniqueId().toString())
              .append("username", player.getName())
              .append("rank", "DEFAULT")
              .append("banned", "false")
              .append("ban_reason", "")
              .append("muted", "false")
              .append("mute_reason", "")
              .append("coins", 0)
              .append("crate_keys", 0)
              .append("perks", "")
              .append("online", true)
              .append("last_ip", player.getAddress().getHostName())
              .append("previous_ips", player.getAddress().getHostName() + ",")
              .append("date_joined", System.currentTimeMillis())
              .append("last_connection", System.currentTimeMillis());

      players.insertOne(doc);
    }
  }

  public static boolean playerExists(Player player) {
    FindIterable<Document> iterable =
        players.find(new Document("uuid", player.getUniqueId().toString()));
    return iterable.first() != null;
  }

  public static boolean playerExistsName(String playerName) {
    FindIterable<Document> iterable =
        players.find(new Document("username", playerName));
    return iterable.first() != null;
  }

  public static String getUUID(String playerName) {
    String uuid = null;

    if (playerExistsName(playerName)) {
      FindIterable<Document> doc = players.find(Filters.eq("username", playerName));
      String uid = doc.first().getString("uuid");

      uuid = uid;
    }
    return uuid;
  }

  public static void updateUsername(Player player) {
    if (playerExists(player)) {
      players.updateOne(
          Filters.eq("uuid", player.getUniqueId().toString()),
          new Document("$set", new Document("username", player.getName())));
    }
  }

  public static void updateIP(Player player) {
    if (playerExists(player)) {
      players.updateOne(
          Filters.eq("uuid", player.getUniqueId().toString()),
          new Document(
              "$set", new Document("last_ip", player.getAddress().getAddress().getHostAddress())));

      String previousIPs = getPreviousIPs(player.getName());
      if (!previousIPs.contains(player.getAddress().getHostName())) {
        players.updateOne(
            Filters.eq("uuid", player.getUniqueId().toString()),
            new Document(
                "$set",
                new Document(
                    "previous_ips",
                    getPreviousIPs(player.getName())
                        + player.getAddress().getAddress().getHostAddress()
                        + ",")));
      }
    }
  }

  public static void updateOnline(Player player, boolean online) {
    if (playerExists(player)) {
      players.updateOne(
          Filters.eq("uuid", player.getUniqueId().toString()),
          new Document("$set", new Document("online", online)));
    }
  }

  public static void updateLastConnection(Player player) {
    if (playerExists(player)) {
      players.updateOne(
          Filters.eq("uuid", player.getUniqueId().toString()),
          new Document("$set", new Document("last_connection", System.currentTimeMillis())));
    }
  }

  public static String getLastIP(String playerName) {
    FindIterable<Document> doc =
        players.find(Filters.eq("username", playerName));
    String ip = doc.first().getString("last_ip");
    return ip;
  }

  public static String getPreviousIPs(String playerName) {
    FindIterable<Document> doc =
        players.find(Filters.eq("username", playerName));
    String ip = doc.first().getString("previous_ips");
    return ip;
  }

  public static String getDateJoined(String playerName) {
    FindIterable<Document> doc =
        players.find(Filters.eq("username", playerName));
    String date_joined = doc.first().getString("date_joined");
    return date_joined;
  }

  public static String getUsername(Player playerName) {
    String uuid = null;

    if (playerExists(playerName)) {
      FindIterable<Document> doc =
          players.find(Filters.eq("uuid", playerName.getUniqueId().toString()));
      String uid = doc.first().getString("username");

      uuid = uid;
    }
    return uuid;
  }
}
