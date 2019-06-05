package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bson.Document;
import org.bukkit.Bukkit;

public class ServerHandler {

  private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static TLCore core = TLCore.getPlugin();

  private static MongoCollection<Document> servers = MongoDB.mongoDatabase.getCollection("servers");

  private static String randomAlphaNumeric(int count) {
    StringBuilder builder = new StringBuilder();
    while (count-- != 0) {
      int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
      builder.append(ALPHA_NUMERIC_STRING.charAt(character));
    }

    return builder.toString();
  }

  /**
   * Get the server's UUID
   */
  public static UUID getServerUUID() {
    if (core.config.getString("UUID").length() < 5) {
      String source = randomAlphaNumeric(16);
      byte[] bytes;
      bytes = source.getBytes(StandardCharsets.UTF_8);
      UUID uuid = UUID.nameUUIDFromBytes(bytes);
      core.config.set("UUID", uuid.toString());
      core.saveConfig();
    }

    return UUID.fromString(core.config.getString("UUID"));
  }

  /**
   * Create a new server in the database
   */
  public static void createServer() {
    if (serverExists()) {
      return;
    }

    Document doc = new Document("uuid", getServerUUID().toString())
        .append("ip", Bukkit.getServer().getIp())
        .append("port", Bukkit.getServer().getPort())
        .append("type", "NONE")
        .append("online_players", 0)
        .append("max_players", 0)
        .append("online", true);

    servers.insertOne(doc);
  }

  /**
   * Get the type of the server
   *
   * @param uuid server's uuid
   */
  public static String getType(UUID uuid) {
    FindIterable<Document> doc = servers.find(Filters.eq("uuid", uuid.toString()));
    String state = doc.first().getString("type");

    return state;
  }

  /**
   * Set the type of the server
   *
   * @param uuid server's uuid
   * @param type server type
   */
  public static void setType(UUID uuid, String type) {
    servers.updateOne(Filters.eq("uuid", uuid.toString()), new Document("$set", new Document("type", type)));
  }

  /**
   * Set the max player's of a server
   *
   * @param uuid server's uuid
   * @param max max players
   */
  public static void setMaxPlayers(UUID uuid, Integer max) {
    servers.updateOne(Filters.eq("uuid", uuid.toString()), new Document("$set", new Document("max_players", max)));
  }

  /**
   * Set the online player's of a server
   *
   * @param uuid server's uuid
   * @param online online count
   */
  public static void setOnlinePlayers(UUID uuid, Integer online) {
    servers.updateOne(Filters.eq("uuid", uuid.toString()), new Document("$set", new Document("online_players", online)));
  }

  /**
   * Get the max count of players on the server
   *
   * @param uuid server's uuid
   * @return
   */
  public static Integer getMaxPlayers(UUID uuid) {
    FindIterable<Document> doc = servers.find(Filters.eq("uuid", uuid.toString()));
    Integer maxPlayers = doc.first().getInteger("max_players");

    return maxPlayers;
  }

  /**
   * Get the online players
   *
   * @param uuid server's uuid
   * @return
   */
  public static Integer getOnlinePlayers(UUID uuid) {
    FindIterable<Document> doc = servers.find(Filters.eq("uuid", uuid.toString()));
    Integer onlinePlayers = doc.first().getInteger("online_players");

    return onlinePlayers;
  }

  /**
   * Set the online status of a server
   *
   * @param online true/false
   */
  public static void setOnline(UUID uuid, boolean online) {
    servers.updateOne(Filters.eq("uuid", uuid.toString()), new Document("$set", new Document("online", online)));
  }

  /**
   * Get the status of the server
   *
   * @param uuid server's uuid
   * @return
   */
  public static boolean isOnline(UUID uuid) {
    FindIterable<Document> doc = servers.find(Filters.eq("uuid", uuid.toString()));
    boolean online = doc.first().getBoolean("online");

    return online;
  }

  /**
   * Check if a server exists
   *
   * @param uuid server's uuid
   * @return
   */
  public static boolean serverExists(UUID uuid) {
    FindIterable<Document> iterable = servers.find(new Document("uuid", uuid.toString()));
    return iterable.first() != null;
  }

  /**
   * Check if server exists
   * @return
   */
  public static boolean serverExists() {
    FindIterable<Document> iterable = servers.find(new Document("uuid", getServerUUID().toString()));
    return iterable.first() != null;
  }
}
