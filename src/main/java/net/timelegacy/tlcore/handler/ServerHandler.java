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

  public static String randomAlphaNumeric(int count) {
    StringBuilder builder = new StringBuilder();
    while (count-- != 0) {
      int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
      builder.append(ALPHA_NUMERIC_STRING.charAt(character));
    }
    return builder.toString();
  }

  public static String getServerUID() {

    if (core.config.getString("UUID").length() < 5) {

      String source = randomAlphaNumeric(16);
      byte[] bytes = null;
      bytes = source.getBytes(StandardCharsets.UTF_8);
      UUID uuid = UUID.nameUUIDFromBytes(bytes);
      core.config.set("UUID", uuid.toString());
      core.saveConfig();
    }

    return core.config.getString("UUID");
  }

  public static void createServer() {
    if (!serverExists()) {

      Document doc =
          new Document("uuid", getServerUID())
              .append("ip", Bukkit.getServer().getIp())
              .append("port", Bukkit.getServer().getPort())
              .append("type", "NONE")
              .append("online_players", 0)
              .append("max_players", 0);

      servers.insertOne(doc);
    }
  }

  public static String getType(String serverName) {
    FindIterable<Document> doc = servers.find(Filters.eq("uuid", serverName));
    String state = doc.first().getString("type");

    return state;
  }

  public static void setType(String serverName, String type) {
    servers.updateOne(
        Filters.eq("uuid", serverName), new Document("$set", new Document("type", type)));
  }

  public static void setMaxPlayers(String serverName, Integer max) {
    servers.updateOne(
        Filters.eq("uuid", serverName), new Document("$set", new Document("max_players", max)));
  }

  public static void setOnlinePlayers(String serverName, Integer online) {
    servers.updateOne(
        Filters.eq("uuid", serverName),
        new Document("$set", new Document("online_players", online)));
  }

  public static Integer getMaxPlayers(String serverName) {
    FindIterable<Document> doc = servers.find(Filters.eq("uuid", serverName));
    Integer maxplayers = doc.first().getInteger("max_players");

    return maxplayers;
  }

  public static Integer getOnlinePlayers(String serverName) {
    FindIterable<Document> doc = servers.find(Filters.eq("uuid", serverName));
    Integer onlineplayers = doc.first().getInteger("online_players");

    return onlineplayers;
  }

  public static boolean serverExists(String serverName) {
    FindIterable<Document> iterable = servers.find(new Document("uuid", serverName));
    return iterable.first() != null;
  }

  public static boolean serverExists() {
    FindIterable<Document> iterable =
        servers.find(new Document("uuid", getServerUID()));
    return iterable.first() != null;
  }
}
