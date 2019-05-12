package net.timelegacy.core.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import net.timelegacy.core.Core;
import org.bson.Document;
import org.bukkit.entity.Player;

public class DiscordHandler {

  private Core core = Core.getInstance();

  /**
   * Get discord ID that a player has linked
   */
  public String getDiscordID(Player player) {
    String uuid = null;

    if (core.playerHandler.playerExists(player)) {
      FindIterable<Document> doc =
          core.mongoDB.players.find(Filters.eq("uuid", player.getUniqueId().toString()));
      String uid = doc.first().getString("discordid");

      uuid = uid;
    }
    return uuid;
  }

  /**
   * Update the discord ID
   */
  public void updateDiscordID(Player player, String discordID) {
    if (core.playerHandler.playerExists(player)) {
      core.mongoDB.players.updateOne(
          Filters.eq("uuid", player.getUniqueId().toString()),
          new Document("$set", new Document("discordid", discordID)));
    }
  }

  /**
   * Get discord token that a player has generated
   */
  public String getDiscordToken(Player player) {
    String uuid = null;

    if (core.playerHandler.playerExists(player)) {
      FindIterable<Document> doc =
          core.mongoDB.players.find(Filters.eq("uuid", player.getUniqueId().toString()));
      String uid = doc.first().getString("discordtoken");

      uuid = uid;
    }
    return uuid;
  }

  /**
   * Update the discord token
   */
  public void updateDiscordToken(Player player, String discordToken) {
    if (core.playerHandler.playerExists(player)) {
      core.mongoDB.players.updateOne(
          Filters.eq("uuid", player.getUniqueId().toString()),
          new Document("$set", new Document("discordtoken", discordToken)));
    }
  }
}
