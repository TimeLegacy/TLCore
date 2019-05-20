package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bson.Document;

public class PerkHandler {

  private static MongoCollection<Document> players = MongoDB.mongoDatabase.getCollection("players");

  public static void addPerk(String playerName, String perk) {
    if (PlayerHandler.playerExistsName(playerName)) {

      if (getPerks(playerName) == null) {
        players.updateOne(
            Filters.eq("username", playerName),
            new Document("$set", new Document("perks", perk.toUpperCase() + ",")));
      } else {
        players.updateOne(
            Filters.eq("username", playerName),
            new Document("$set",
                new Document("perks", getPerks(playerName) + perk.toUpperCase() + ",")));
      }
    }
  }

  public static String getPerks(String playerName) {
    String perks = null;
    if (PlayerHandler.playerExistsName(playerName)) {
      FindIterable<Document> doc = players.find(Filters.eq("username", playerName));

      perks = doc.first().getString("perks");
    }
    return perks;
  }

  public static boolean hasPerk(String playerName, String perk) {
    return getPerks(playerName).contains(perk.toUpperCase());
  }

  public static void removePerk(String playerName, String perk) {
    if (PlayerHandler.playerExistsName(playerName)) {

      if (getPerks(playerName) != null) {
        if (getPerks(playerName).contains(perk)) {
          players.updateOne(
              Filters.eq("username", playerName),
              new Document("$set",
                  new Document("perks", getPerks(playerName).replace(perk + ",", ""))));
        }
      }
    }
  }
}
