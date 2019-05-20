package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class CoinHandler implements Listener {

  private static MongoCollection<Document> players = MongoDB.mongoDatabase.getCollection("players");

  public static boolean checkTransaction(String playerName, Integer amount) {
    return getBalance(playerName) >= amount;
  }

  public static int getBalance(String playerName) {
    int balance = 0;
    if (PlayerHandler.playerExistsName(playerName)) {
      FindIterable<Document> doc = players.find(Filters.eq("username", playerName));

      balance = doc.first().getInteger("coins");
    } else {
      PlayerHandler.createPlayer(Bukkit.getPlayer(playerName));
      getBalance(playerName);
    }
    return balance;
  }

  public static void addCoins(String playerName, Integer amount) {
    int am = amount;
    if (PlayerHandler.playerExistsName(playerName)) {
      if (MultiplierHandler.isMultiplierEnabled()) {
        am = MultiplierHandler.getMultiplier() * amount;
      }
      setBalance(playerName, getBalance(playerName) + am);
    } else {
      PlayerHandler.createPlayer(Bukkit.getPlayer(playerName));
      addCoins(playerName, amount);
    }
  }

  public static void setBalance(String playerName, Integer amount) {
    if (PlayerHandler.playerExistsName(playerName)) {
      players.updateOne(
          Filters.eq("username", playerName), new Document("$set", new Document("coins", amount)));
    } else {
      PlayerHandler.createPlayer(Bukkit.getPlayer(playerName));
      setBalance(playerName, amount);
    }
  }

  public static void removeCoins(String playerName, Integer amount) {
    if (PlayerHandler.playerExistsName(playerName)) {
      if (checkTransaction(playerName, amount)) {
        setBalance(playerName, getBalance(playerName) - amount);
      }
    } else {
      PlayerHandler.createPlayer(Bukkit.getPlayer(playerName));
    }
  }
}
