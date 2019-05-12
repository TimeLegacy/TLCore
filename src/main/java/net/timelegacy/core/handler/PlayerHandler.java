package net.timelegacy.core.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import net.timelegacy.core.Core;
import org.bson.Document;
import org.bukkit.entity.Player;

public class PlayerHandler {

  private Core core = Core.getInstance();

  /**
   * Create a new player in the database.
   *
   * @param player Player that is online
   */
  public void createPlayer(Player player) {
    if (!playerExists(player)) {

      Document doc =
          new Document("uuid", player.getUniqueId().toString())
              .append("username", player.getName())
              .append("rank", "DEFAULT")
              .append("banned", "false")
              .append("banreason", "")
              .append("muted", "false")
              .append("mutereason", "")
              .append("coins", 0)
              .append("perks", "")
              .append("kills", 0)
              .append("deaths", 0)
              .append("cratekeys", 0)
              .append("wins", 0)
              .append("losses", 0)
              .append("lastip", player.getAddress().getHostName())
              .append("online", true)
              .append("lastconnection", System.currentTimeMillis())
              .append("discordid", "")
              .append("discordtoken", "");

      core.mongoDB.players.insertOne(doc);
    }
  }

  /**
   * Check if a player exists in the database
   */
  public boolean playerExists(Player player) {
    FindIterable<Document> iterable =
        core.mongoDB.players.find(new Document("uuid", player.getUniqueId().toString()));
    return iterable.first() != null;
  }

  /**
   * Check if a player exists based on their username
   */
  public boolean playerExistsName(String playerName) {
    FindIterable<Document> iterable =
        core.mongoDB.players.find(new Document("username", playerName));
    return iterable.first() != null;
  }

  /**
   * Get the uuid of a player from the database
   */
  public String getUUID(String playerName) {
    String uuid = null;

    if (core.playerHandler.playerExistsName(playerName)) {
      FindIterable<Document> doc = core.mongoDB.players.find(Filters.eq("username", playerName));
      String uid = doc.first().getString("uuid");

      uuid = uid;
    }
    return uuid;
  }

  /**
   * Update the username if it's different from the one in the DB
   */
  public void updateUsername(Player player) {
    if (core.playerHandler.playerExists(player)) {
      core.mongoDB.players.updateOne(
          Filters.eq("uuid", player.getUniqueId().toString()),
          new Document("$set", new Document("username", player.getName())));
    }
  }

  /**
   * Update the IP that a player connected from.
   */
  public void updateIP(Player player) {
    if (core.playerHandler.playerExists(player)) {
      core.mongoDB.players.updateOne(
          Filters.eq("uuid", player.getUniqueId().toString()),
          new Document(
              "$set", new Document("lastip", player.getAddress().getAddress().getHostAddress())));
    }
  }

  // mServerSocket.getInetAddress().getHostAddress()

  /**
   * Update the online status of a player.
   */
  public void updateOnline(Player player, boolean online) {
    if (core.playerHandler.playerExists(player)) {
      core.mongoDB.players.updateOne(
          Filters.eq("uuid", player.getUniqueId().toString()),
          new Document("$set", new Document("online", online)));
    }
  }

  /**
   * Update when the player last joined a server on the network
   */
  public void updateLastConnection(Player player) {
    if (core.playerHandler.playerExists(player)) {
      core.mongoDB.players.updateOne(
          Filters.eq("uuid", player.getUniqueId().toString()),
          new Document("$set", new Document("lastconnection", System.currentTimeMillis())));
    }
  }

  //

  /**
   * Get the uuid of a player on the server from the database
   */
  public String getUsername(Player playerName) {
    String uuid = null;

    if (core.playerHandler.playerExists(playerName)) {
      FindIterable<Document> doc =
          core.mongoDB.players.find(Filters.eq("uuid", playerName.getUniqueId().toString()));
      String uid = doc.first().getString("username");

      uuid = uid;
    }
    return uuid;
  }
}
