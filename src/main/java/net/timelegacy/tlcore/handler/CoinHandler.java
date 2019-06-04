package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.util.UUID;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class CoinHandler implements Listener {

  private static MongoCollection<Document> players = MongoDB.mongoDatabase.getCollection("players");

  /**
   * Check if a player has enough coins
   *
   * @param uuid player's uuid
   * @param amount amount to check if they have enough
   */
  public static boolean hasEnough(UUID uuid, Integer amount) {
    return getBalance(uuid) >= amount;
  }

  /**
   * Get the balance of a player
   *
   * @param uuid player's uuid
   */
  public static int getBalance(UUID uuid) {
    int balance = 0;

    if (!PlayerHandler.playerExists(uuid)) {
      PlayerHandler.createPlayer(Bukkit.getPlayer(uuid));
      getBalance(uuid);
      return balance;
    }

    FindIterable<Document> doc = players.find(Filters.eq("uuid", uuid.toString()));

    balance = doc.first().getInteger("coins");

    return balance;
  }

  /**
   * Add coins to a player
   *
   * @param uuid player's uuid
   * @param amount amount of coins
   */
  public static void addCoins(UUID uuid, Integer amount) {
    int am = amount;

    if (!PlayerHandler.playerExists(uuid)) {
      PlayerHandler.createPlayer(Bukkit.getPlayer(uuid));
      addCoins(uuid, amount);
      return;
    }

    if (MultiplierHandler.isMultiplierEnabled()) {
      am = MultiplierHandler.getMultiplier() * amount;
    }

    setBalance(uuid, getBalance(uuid) + am);
  }

  /**
   * Set the balance of a player's coins
   *
   * @param uuid player's uuid
   * @param amount amount of coins
   */
  public static void setBalance(UUID uuid, Integer amount) {
    if (!PlayerHandler.playerExists(uuid)) {
      PlayerHandler.createPlayer(Bukkit.getPlayer(uuid));
      setBalance(uuid, amount);
      return;
    }

    players.updateOne(Filters.eq("uuid", uuid.toString()), new Document("$set", new Document("coins", amount)));
  }

  /**
   * Remove coins from a player
   *
   * @param uuid player's uuid
   * @param amount amount of coins
   */
  public static void removeCoins(UUID uuid, Integer amount) {
    if (!PlayerHandler.playerExists(uuid)) {
      PlayerHandler.createPlayer(Bukkit.getPlayer(uuid));
      return;
    }

    if (hasEnough(uuid, amount)) {
      setBalance(uuid, getBalance(uuid) - amount);
    }

  }
}
