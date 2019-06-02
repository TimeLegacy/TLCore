package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PerkHandler {

  private static MongoCollection<Document> players = MongoDB.mongoDatabase.getCollection("players");

  /**
   * Add a perk to a player
   *
   * @param uuid player's uuid
   * @param perk perk as a string
   */
  public static void addPerk(UUID uuid, String perk) {
    if (PlayerHandler.playerExists(uuid)) {

      if (perksString(uuid) == null) {
        players.updateOne(
                Filters.eq("uuid", uuid.toString()),
            new Document("$set", new Document("perks", perk.toUpperCase() + ",")));
      } else {
        players.updateOne(
                Filters.eq("uuid", uuid.toString()),
            new Document("$set",
                    new Document("perks", perksString(uuid) + perk.toUpperCase() + ",")));
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
   * @param uuid
   * @return
   */
  public static List<String> getPerks(UUID uuid) {
    List<String> perkList = new ArrayList<>();

    if (PlayerHandler.playerExists(uuid)) {
      FindIterable<Document> doc = players.find(Filters.eq("uuid", uuid.toString()));

      String[] perks = doc.first().getString("perks").split(",");


      for (String perk : perks) {
        perkList.add(perk);
      }
    }
    return perkList;
  }

  public static boolean hasPerk(UUID uuid, String perk) {
    return perksString(uuid).contains(perk);
  }

  public static void removePerk(UUID uuid, String perk) {
    if (PlayerHandler.playerExists(uuid)) {

      if (perksString(uuid) != null) {
        if (hasPerk(uuid, perk)) {
          players.updateOne(
                  Filters.eq("uuid", uuid.toString()),
              new Document("$set",
                      new Document("perks", perksString(uuid).replace(perk + ",", ""))));
        }
      }
    }
  }
}
