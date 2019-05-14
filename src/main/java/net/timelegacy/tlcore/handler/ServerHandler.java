package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.timelegacy.tlcore.TLCore;
import org.bson.Document;
import org.bukkit.Bukkit;

public class ServerHandler {

  private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private TLCore core = TLCore.getInstance();

  public static String randomAlphaNumeric(int count) {
    StringBuilder builder = new StringBuilder();
    while (count-- != 0) {
      int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
      builder.append(ALPHA_NUMERIC_STRING.charAt(character));
    }
    return builder.toString();
  }

  /**
   * Get the UID of a server.
   */
  public String getServerUID() {

    if (core.config.getString("UID").length() < 5) {

      String source = randomAlphaNumeric(16);
      byte[] bytes = null;
      bytes = source.getBytes(StandardCharsets.UTF_8);
      UUID uuid = UUID.nameUUIDFromBytes(bytes);
      core.config.set("UID", uuid.toString());
      core.saveConfig();
    }

    return core.config.getString("UID");
  }

  /**
   * Create a server if it doesn't exist in the database, will auto generate a UID if it doesn't
   * exist in config
   */
  public void createServer() {
    if (!serverExists()) {

      Document doc =
          new Document("uid", getServerUID())
              .append("ip", Bukkit.getServer().getIp())
              .append("port", Bukkit.getServer().getPort())
              .append("type", "NONE")
              .append("game", "N/A")
              .append("state", "N/A")
              .append("map", "N/A")
              .append("onlineplayers", 0)
              .append("maxplayers", 0);

      core.mongoDB.servers.insertOne(doc);
    }
  }

  /**
   * Get the type of the server
   */
  public String getType(String serverName) {
    FindIterable<Document> doc = core.mongoDB.servers.find(Filters.eq("uid", serverName));
    String state = doc.first().getString("type");

    return state;
  }

  /**
   * Set the type of the server
   */
  public void setType(String serverName, String type) {
    core.mongoDB.servers.updateOne(
        Filters.eq("uid", serverName), new Document("$set", new Document("type", type)));
  }

  /**
   * Get the game type of the server
   */
  public String getGame(String serverName) {
    FindIterable<Document> doc = core.mongoDB.servers.find(Filters.eq("uid", serverName));
    String state = doc.first().getString("game");

    return state;
  }

  /**
   * Set the game type of the server
   */
  public void setGame(String serverName, String game) {
    core.mongoDB.servers.updateOne(
        Filters.eq("uid", serverName), new Document("$set", new Document("game", game)));
  }

  /**
   * Get the state of the game running on that server
   */
  public String getState(String serverName) {
    FindIterable<Document> doc = core.mongoDB.servers.find(Filters.eq("uid", serverName));
    String state = doc.first().getString("state");

    return state;
  }

  /**
   * Get the state of the game running on that server
   */
  public void setState(String serverName, String state) {
    if (state.equalsIgnoreCase("WAITING")
        || state.equalsIgnoreCase("STARTING")
        || state.equalsIgnoreCase("FULL")
        || state.equalsIgnoreCase("INGAME")
        || state.equalsIgnoreCase("RESTARTING")) {
      core.mongoDB.servers.updateOne(
          Filters.eq("uid", serverName), new Document("$set", new Document("state", state)));
    }
  }

  /**
   * Set the max player count
   */
  public void setMaxPlayers(String serverName, Integer max) {
    core.mongoDB.servers.updateOne(
        Filters.eq("uid", serverName), new Document("$set", new Document("maxplayers", max)));
  }

  /**
   * Set the online player count of the server
   */
  public void setOnlinePlayers(String serverName, Integer online) {
    core.mongoDB.servers.updateOne(
        Filters.eq("uid", serverName), new Document("$set", new Document("onlineplayers", online)));
  }

  /**
   * Set the map of the game server
   */
  public void setMap(String serverName, String map) {
    core.mongoDB.servers.updateOne(
        Filters.eq("uid", serverName), new Document("$set", new Document("map", map)));
  }

  /**
   * Get the map of the game server
   */
  public String getMap(String serverName) {
    FindIterable<Document> doc = core.mongoDB.servers.find(Filters.eq("uid", serverName));
    String map = doc.first().getString("map");

    return map;
  }

  /**
   * Get the max players on the server
   */
  public Integer getMaxPlayers(String serverName) {
    FindIterable<Document> doc = core.mongoDB.servers.find(Filters.eq("uid", serverName));
    Integer maxplayers = doc.first().getInteger("maxplayers");

    return maxplayers;
  }

  /**
   * Get the online players on the server
   */
  public Integer getOnlinePlayers(String serverName) {
    FindIterable<Document> doc = core.mongoDB.servers.find(Filters.eq("uid", serverName));
    Integer onlineplayers = doc.first().getInteger("onlineplayers");

    return onlineplayers;
  }

  /**
   * Check if server is already in the database
   */
  public boolean serverExists(String serverName) {
    FindIterable<Document> iterable = core.mongoDB.servers.find(new Document("uid", serverName));
    return iterable.first() != null;
  }

  /**
   * Check if server is already in the database
   */
  public boolean serverExists() {
    FindIterable<Document> iterable =
        core.mongoDB.servers.find(new Document("uid", getServerUID()));
    return iterable.first() != null;
  }
}
