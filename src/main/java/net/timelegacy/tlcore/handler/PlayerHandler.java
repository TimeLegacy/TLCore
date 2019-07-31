package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.util.UUID;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bson.Document;

public class PlayerHandler {

  private static MongoCollection<Document> players = MongoDB.mongoDatabase.getCollection("players");

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
}
