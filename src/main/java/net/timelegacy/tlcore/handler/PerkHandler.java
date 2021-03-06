package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PerkHandler {

  private static MongoCollection<Document> players = MongoDB.mongoDatabase.getCollection("players");

  /**
   * Add a perk to a player
   *
   * @param uuid player's uuid
   * @param perk perk node (ex: "LOBBY.HAT.GIFT") as string
   */
  public static void addPerk(UUID uuid, String perk) {
    if (!PlayerHandler.playerExists(uuid)) {
      return;
    }

    if (!hasPerk(uuid, perk)) {

      if (perksString(uuid) == null) {
        players.updateOne(
            Filters.eq("uuid", uuid.toString()),
            new Document("$set", new Document("perks", perk.toLowerCase() + ",")));

        if (Bukkit.getPlayer(uuid).isOnline()) {
          PermissionHandler.addPermission(Bukkit.getPlayer(uuid), perk);
        }
      } else {
        players.updateOne(
            Filters.eq("uuid", uuid.toString()),
            new Document(
                "$set", new Document("perks", perksString(uuid) + perk.toLowerCase() + ",")));
        if (Bukkit.getPlayer(uuid).isOnline()) {
          PermissionHandler.addPermission(Bukkit.getPlayer(uuid), perk);
        }
      }
    }
  }

  /**
   * Add the player's perks as permissions.
   */

  public static void addPermissions(Player player) {
    String perksString = perksString(player.getUniqueId());

    if (perksString != null) {
      for (String perms : perksString.split(",")) {
        PermissionHandler.addPermission(player, perms);
      }
    }
  }

  private static String perksString(UUID uuid) {
    String perkList = "";

    if (PlayerHandler.playerExists(uuid)) {
      FindIterable<Document> doc = players.find(Filters.eq("uuid", uuid.toString()));

      perkList = doc.first().getString("perks");
    }

    return perkList;
  }


  /**
   * Get a list of a player's perks
   *
   * @param uuid uuid of a player
   */
  public static List<String> getPerks(UUID uuid) {
    List<String> perkList = new ArrayList<>();

    if (PlayerHandler.playerExists(uuid)) {
      FindIterable<Document> doc = players.find(Filters.eq("uuid", uuid.toString()));

      String[] perks = doc.first().getString("perks").split(",");

      Collections.addAll(perkList, perks);
    }
    return perkList;
  }

  private static boolean hasPerk(UUID uuid, String perk) {
    return perksString(uuid).contains(perk.toLowerCase());
  }

  /**
   * Remove a perk from a player
   *
   * @param uuid player's uuid
   * @param perk perk node (ex: "LOBBY.HAT.GIFT") as string
   */
  public static void removePerk(UUID uuid, String perk) {
    if (!PlayerHandler.playerExists(uuid)) {
      return;
    }

    if (perksString(uuid) == null) {
      return;
    }

    if (hasPerk(uuid, perk)) {
      players.updateOne(Filters.eq("uuid", uuid.toString()),
          new Document("$set", new Document("perks", perksString(uuid).replace(perk + ",", ""))));

      if( Bukkit.getPlayer(uuid).isOnline()) {
        PermissionHandler.removePermission(Bukkit.getPlayer(uuid), perk);
      }
    }
  }
}
