package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import net.timelegacy.tlcore.TLCore;
import org.bson.Document;
import org.bukkit.entity.Player;

public class StatsHandler {

  private TLCore core = TLCore.getInstance();

  // losses and wins

  /**
   * Add a win to a player
   */
  public void addWin(Player p) {
    int am;
    if (core.playerHandler.playerExists(p)) {

      am = Integer.valueOf(1);
      setWins(p, Integer.valueOf(getWins(p).intValue() + am));

    } else {
      core.playerHandler.createPlayer(p);
      addWin(p);
    }
  }

  /**
   * Add a loss to a player
   */
  public void addLoss(Player p) {
    int am;
    if (core.playerHandler.playerExists(p)) {

      am = Integer.valueOf(1);
      setLosses(p, Integer.valueOf(getWins(p).intValue() + am));

    } else {
      core.playerHandler.createPlayer(p);
      addLoss(p);
    }
  }

  /**
   * Set the wins of a player
   */
  public void setWins(Player p, Integer amount) {

    if (core.playerHandler.playerExists(p)) {
      core.mongoDB.players.updateOne(
          Filters.eq("uuid", p.getUniqueId().toString()),
          new Document("$set", new Document("wins", amount)));
    } else {
      core.playerHandler.createPlayer(p);
      setWins(p, amount);
    }
  }

  /**
   * Set losses of a player
   */
  public void setLosses(Player p, Integer amount) {

    if (core.playerHandler.playerExists(p)) {
      core.mongoDB.players.updateOne(
          Filters.eq("uuid", p.getUniqueId().toString()),
          new Document("$set", new Document("losses", amount)));
    } else {
      core.playerHandler.createPlayer(p);
      setLosses(p, amount);
    }
  }

  /**
   * Get wins of a player
   */
  public Integer getWins(Player p) {
    int retval = 0;
    if (core.playerHandler.playerExists(p)) {
      FindIterable<Document> doc =
          core.mongoDB.players.find(Filters.eq("uuid", p.getUniqueId().toString()));
      int coins = doc.first().getInteger("wins");

      retval = coins;
    } else {
      core.playerHandler.createPlayer(p);
      getWins(p);
    }
    return retval;
  }

  /**
   * Get losses of a player
   */
  public Integer getLosses(Player p) {
    int retval = 0;
    if (core.playerHandler.playerExists(p)) {
      FindIterable<Document> doc =
          core.mongoDB.players.find(Filters.eq("uuid", p.getUniqueId().toString()));
      int coins = doc.first().getInteger("losses");

      retval = coins;
    } else {
      core.playerHandler.createPlayer(p);
      getLosses(p);
    }
    return retval;
  }

  /**
   * Add a kill to a player
   */
  public void addKill(Player p) {
    int am;
    if (core.playerHandler.playerExists(p)) {

      am = Integer.valueOf(1);
      setKills(p, Integer.valueOf(getKills(p).intValue() + am));

    } else {
      core.playerHandler.createPlayer(p);
      addKill(p);
    }
  }

  /**
   * Add a death to a player
   */
  public void addDeath(Player p) {
    int am;
    if (core.playerHandler.playerExists(p)) {

      am = Integer.valueOf(1);
      setDeaths(p, Integer.valueOf(getDeaths(p).intValue() + am));

    } else {
      core.playerHandler.createPlayer(p);
      addDeath(p);
    }
  }

  /**
   * Set kills of a player
   */
  public void setKills(Player p, Integer amount) {

    if (core.playerHandler.playerExists(p)) {
      core.mongoDB.players.updateOne(
          Filters.eq("uuid", p.getUniqueId().toString()),
          new Document("$set", new Document("kills", amount)));
    } else {
      core.playerHandler.createPlayer(p);
      setKills(p, amount);
    }
  }

  /**
   * Set deaths of a player
   */
  public void setDeaths(Player p, Integer amount) {

    if (core.playerHandler.playerExists(p)) {
      core.mongoDB.players.updateOne(
          Filters.eq("uuid", p.getUniqueId().toString()),
          new Document("$set", new Document("deaths", amount)));
    } else {
      core.playerHandler.createPlayer(p);
      setDeaths(p, amount);
    }
  }

  /**
   * Get kills of a player
   */
  public Integer getKills(Player p) {
    int retval = 0;
    if (core.playerHandler.playerExists(p)) {
      FindIterable<Document> doc =
          core.mongoDB.players.find(Filters.eq("uuid", p.getUniqueId().toString()));
      int coins = doc.first().getInteger("kills");

      retval = coins;
    } else {
      core.playerHandler.createPlayer(p);
      getKills(p);
    }
    return retval;
  }

  /**
   * Get deaths of a player
   */
  public Integer getDeaths(Player p) {
    int retval = 0;
    if (core.playerHandler.playerExists(p)) {
      FindIterable<Document> doc =
          core.mongoDB.players.find(Filters.eq("uuid", p.getUniqueId().toString()));
      int coins = doc.first().getInteger("deaths");

      retval = coins;
    } else {
      core.playerHandler.createPlayer(p);
      getDeaths(p);
    }
    return retval;
  }
}
