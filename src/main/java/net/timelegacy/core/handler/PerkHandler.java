package net.timelegacy.core.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import net.timelegacy.core.Core;
import org.bson.Document;
import org.bukkit.entity.Player;

public class PerkHandler {

  private Core core = Core.getInstance();

  /**
   * Add a perk to a player
   */
  public void addPerk(Player p, String perk) {
    if (core.playerHandler.playerExists(p)) {

      if (getPerks(p) == null) {
        core.mongoDB.players.updateOne(
            Filters.eq("username", p.getName()),
            new Document("$set", new Document("perks", perk.toUpperCase() + " ")));
      } else {
        core.mongoDB.players.updateOne(
            Filters.eq("username", p.getName()),
            new Document("$set", new Document("perks", getPerks(p) + perk.toUpperCase() + " ")));
      }
    }
  }

  /**
   * Get the perks of a player
   */
  public String getPerks(Player p) {
    String perks = null;
    if (core.playerHandler.playerExistsName(p.getName())) {
      FindIterable<Document> doc = core.mongoDB.players.find(Filters.eq("username", p.getName()));

      perks = doc.first().getString("perks");
    }
    return perks;
  }

  /**
   * Check if a player has a perk
   */
  public boolean hasPerk(Player p, String perk) {
    return getPerks(p).contains(perk.toUpperCase());
  }

  /**
   * Remove a perk of a player
   */
  public void removePerk(Player p, String perk) {
    if (core.playerHandler.playerExists(p)) {

      if (getPerks(p) != null) {
        if (getPerks(p).contains(perk)) {
          core.mongoDB.players.updateOne(
              Filters.eq("username", p.getName()),
              new Document("$set", new Document("perks", getPerks(p).replace(perk + " ", ""))));
        }
      }
    }
  }
}
