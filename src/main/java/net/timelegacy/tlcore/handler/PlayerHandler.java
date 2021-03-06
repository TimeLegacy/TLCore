package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.util.UUID;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bson.Document;
import org.bukkit.entity.Player;

public class PlayerHandler {

  private static MongoCollection<Document> players = MongoDB.mongoDatabase.getCollection("players");

  /**
   * Create a player in the database
   */
  public static void createPlayer(Player player) {
    if (playerExists(player.getUniqueId())) {
      return;
    }

    Document doc = new Document("uuid", player.getUniqueId().toString())
        .append("username", player.getName())
        .append("rank", "DEFAULT")
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

  /**
   * Check if a player exists
   *
   * @param uuid player's uuid
   */
  public static boolean playerExists(UUID uuid) {
    FindIterable<Document> iterable = players.find(new Document("uuid", uuid.toString()));
    return iterable.first() != null;
  }

  /**
   * Check if a player exists
   *
   * @param playerName player's username
   */
  public static boolean playerExists(String playerName) {
    FindIterable<Document> iterable = players.find(new Document("username", playerName));
    return iterable.first() != null;
  }

  /**
   * Get the uuid from player username
   *
   * @param playerName player's username
   */
  public static UUID getUUID(String playerName) {
    UUID uuid = null;

    if (playerExists(playerName)) {
      FindIterable<Document> doc = players.find(Filters.eq("username", playerName));
      String uid = doc.first().getString("uuid");

      uuid = UUID.fromString(uid);
    }

    return uuid;
  }

  /**
   * Update the username of a player
   *
   * @param player player
   */
  public static void updateUsername(Player player) {
    if (playerExists(player.getUniqueId())) {
      players.updateOne(Filters.eq("uuid", player.getUniqueId().toString()),
          new Document("$set", new Document("username", player.getName())));
    }
  }

  /**
   * Update the last IP of a player
   *
   * @param player player
   */
  public static void updateIP(Player player) {
    if (playerExists(player.getUniqueId())) {
      players.updateOne(
          Filters.eq("uuid", player.getUniqueId().toString()),
          new Document("$set", new Document("last_ip", player.getAddress().getAddress().getHostAddress())));

      String previousIPs = getPreviousIPs(player.getUniqueId());
      if (!previousIPs.contains(player.getAddress().getAddress().getHostAddress())) {
        players.updateOne(
            Filters.eq("uuid", player.getUniqueId().toString()),
            new Document("$set",
                new Document("previous_ips", getPreviousIPs(player.getUniqueId())
                    + player.getAddress().getAddress().getHostAddress()
                    + ",")));
      }
    }
  }

  /**
   * Update a player's online status
   */
  public static void updateOnline(UUID uuid, boolean online) {
    if (playerExists(uuid)) {
      players.updateOne(Filters.eq("uuid", uuid.toString()), new Document("$set", new Document("online", online)));
    }
  }

  /**
   * Update the time when a player just joined
   *
   * @param uuid player's uuid
   */
  public static void updateLastConnection(UUID uuid) {
    if (playerExists(uuid)) {
      players.updateOne(Filters.eq("uuid", uuid.toString()),
          new Document("$set", new Document("last_connection", System.currentTimeMillis())));
    }
  }

  /**
   * Get the last IP of a player
   *
   * @param uuid player's uuid
   */
  public static String getLastIP(UUID uuid) {
    FindIterable<Document> doc = players.find(Filters.eq("uuid", uuid.toString()));
    String ip = doc.first().getString("last_ip");
    return ip;
  }

  /**
   * Get previous IPs
   *
   * @param uuid player's uuid
   */
  public static String getPreviousIPs(UUID uuid) {
    FindIterable<Document> doc = players.find(Filters.eq("uuid", uuid.toString()));
    String ip = doc.first().getString("previous_ips");
    return ip;
  }

  /**
   * Get the date the player joined
   *
   * @param uuid player's uuid
   */
  public static long getDateJoined(UUID uuid) {
    FindIterable<Document> doc = players.find(Filters.eq("uuid", uuid.toString()));
    Long date_joined = doc.first().getLong("date_joined");
    return date_joined;
  }

  /**
   * Get the username of a player
   *
   * @param uuid player's uuid
   */
  public static String getUsername(UUID uuid) {
    if (playerExists(uuid)) {
      FindIterable<Document> doc = players.find(Filters.eq("uuid", uuid.toString()));
      String username = doc.first().getString("username");
      return username;
    }

    return null;
  }

  /**
   * Check if a specified player is online
   *
   * @param uuid player
   */
  public static boolean isOnline(UUID uuid) {
    if (playerExists(uuid)) {
      FindIterable<Document> doc = players.find(Filters.eq("uuid", uuid.toString()));
      return doc.first().getBoolean("online");
    }

    return false;
  }
}
