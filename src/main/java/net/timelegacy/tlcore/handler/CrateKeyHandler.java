package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import net.timelegacy.tlcore.TLCore;
import org.bson.Document;
import org.bukkit.entity.Player;

public class CrateKeyHandler {

  private TLCore core = TLCore.getInstance();

  /**
   * Add key to a player
   */
  public void addKey(Player p, int amount) {
    int am;
    if (core.playerHandler.playerExists(p)) {

      am = Integer.valueOf(amount);
      setKeys(p, Integer.valueOf(getKeys(p).intValue() + am));

    } else {
      core.playerHandler.createPlayer(p);
      addKey(p, amount);
    }
  }

  /**
   * Set keys of a player
   */
  public void setKeys(Player p, Integer amount) {

    if (core.playerHandler.playerExists(p)) {
      core.mongoDB.players.updateOne(
          Filters.eq("uuid", p.getUniqueId().toString()),
          new Document("$set", new Document("cratekeys", amount)));
    } else {
      core.playerHandler.createPlayer(p);
      setKeys(p, amount);
    }
  }

  /**
   * Get the keys of a player
   */
  public Integer getKeys(Player p) {
    int retval = 0;
    if (core.playerHandler.playerExists(p)) {
      FindIterable<Document> doc =
          core.mongoDB.players.find(Filters.eq("uuid", p.getUniqueId().toString()));
      int coins = doc.first().getInteger("cratekeys");

      retval = coins;
    } else {
      core.playerHandler.createPlayer(p);
      getKeys(p);
    }
    return retval;
  }
}
