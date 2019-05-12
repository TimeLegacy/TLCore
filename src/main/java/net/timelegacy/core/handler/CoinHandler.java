package net.timelegacy.core.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import net.timelegacy.core.Core;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class CoinHandler implements Listener {

  private Core core = Core.getInstance();

  /**
   * Check if player has enough coins
   */
  public boolean checkTransaction(String p, Integer amount) {
    return getBalance(p) >= amount;
  }

  /**
   * Get the players balance
   */
  public int getBalance(String playerName) {
    int retval = 0;
    if (core.playerHandler.playerExistsName(playerName)) {
      FindIterable<Document> doc = core.mongoDB.players.find(Filters.eq("username", playerName));

      retval = doc.first().getInteger("coins");
    } else {
      core.playerHandler.createPlayer(Bukkit.getPlayer(playerName));
      getBalance(playerName);
    }
    return retval;
  }

  /**
   * Add coins to a player
   */
  public void addCoins(String playerName, Integer amount) {
    int am = amount;
    if (core.playerHandler.playerExistsName(playerName)) {

      Player player = Bukkit.getPlayer(playerName);

      if (core.multiplierHandler.isMultiplierEnabled()) {
        am = core.multiplierHandler.getMultiplier() * amount;
      }

      setBalance(playerName, getBalance(playerName) + am);

      core.messageUtils.sendMessage(
          player,
          core.messageUtils.MAIN_COLOR
              + "You have received "
              + core.messageUtils.SECOND_COLOR
              + am
              + core.messageUtils.MAIN_COLOR
              + " coins.",
          true);
    } else {
      core.playerHandler.createPlayer(Bukkit.getPlayer(playerName));
      addCoins(playerName, amount);
    }
  }

  /**
   * Set the balance of a players coins
   */
  public void setBalance(String p, Integer amount) {

    if (core.playerHandler.playerExistsName(p)) {
      core.mongoDB.players.updateOne(
          Filters.eq("username", p), new Document("$set", new Document("coins", amount)));
    } else {
      core.playerHandler.createPlayer(Bukkit.getPlayer(p));
      setBalance(p, amount);
    }
  }

  /**
   * Figure out how many coins to add depending on the players rank
   */
  public Integer coinsToAdd(String playerName, Integer amount) {
    int am = amount;
    if (core.playerHandler.playerExistsName(playerName)) {

      if (core.multiplierHandler.isMultiplierEnabled()) {
        am = core.multiplierHandler.getMultiplier() * amount;
      }

    } else {
      core.playerHandler.createPlayer(Bukkit.getPlayer(playerName));

      am = amount;
    }

    return am;
  }

  /**
   * Subtract coins from a player
   */
  public void removeCoins(String playerName, Integer amount) {
    if (core.playerHandler.playerExistsName(playerName)) {
      if (checkTransaction(playerName, amount)) {
        setBalance(playerName, getBalance(playerName) - amount);
      }
    } else {
      core.playerHandler.createPlayer(Bukkit.getPlayer(playerName));
    }
  }
}
