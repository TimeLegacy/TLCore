package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;
import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bson.Document;

public class BanHandler {

  private static MongoCollection<Document> players = MongoDB.mongoDatabase.getCollection("players");

  public static boolean isBanned(String playerName) {
    if (PlayerHandler.playerExistsName(playerName)) {

      FindIterable<Document> doc = players.find(Filters.eq("username", playerName));
      String banned = doc.first().getString("banned");
      if (banned.equalsIgnoreCase("true")) {
        return true;
      } else if (banned.equalsIgnoreCase("false")) {
        return false;
      } else {
        long banExpire = Long.parseLong(banned);
        return banExpire > System.currentTimeMillis();
      }
    }
    return false;
  }

  public static String getBanExpire(String playerName) {
    if (isBanned(playerName)) {
      FindIterable<Document> doc = players.find(Filters.eq("username", playerName));
      String banned = doc.first().getString("banned");

      if (!banned.equalsIgnoreCase("false") && !banned.equalsIgnoreCase("true")) {
        long banExpire = Long.parseLong(banned);
        if (banExpire > System.currentTimeMillis()) {
          Timestamp stamp = new Timestamp(banExpire);
          Date date = new Date(stamp.getTime());

          return date.toString();
        }
      } else {
        return "false";
      }
    }
    return "false";
  }

  private static long parse(String input) {
    long result = 0;
    StringBuilder number = new StringBuilder();
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      if (Character.isDigit(c)) {
        number.append(c);
      } else if (Character.isLetter(c) && (number.length() > 0)) {
        result += convert(Integer.parseInt(number.toString()), c);
        number = new StringBuilder();
      }
    }
    return result;
  }

  private static long convert(int value, char unit) {
    switch (unit) {
      case 'd':
        return value * 1000 * 60 * 60 * 24;
      case 'h':
        return value * 1000 * 60 * 60;
      case 'm':
        return value * 1000 * 60;
      case 's':
        return value * 1000;
    }
    return 0;
  }

  public static void setBanned(
      String playerName, String isBanned, Punishment reason, String punisherName) {
    String banReason = "OTHER";

    if (!isBanned.equalsIgnoreCase("true") && !isBanned.equalsIgnoreCase("false")) {

      isBanned = System.currentTimeMillis() + parse(isBanned) + "";
    }

    if (!isBanned.equalsIgnoreCase("false")) {
      players.updateOne(
          Filters.eq("username", playerName),
          new Document("$set", new Document("banned", isBanned)));

      if (reason != null) {
        players.updateOne(
            Filters.eq("username", playerName),
            new Document("$set", new Document("ban_reason", reason.toString())));
      } else {
        players.updateOne(
            Filters.eq("username", playerName),
            new Document("$set", new Document("ban_reason", banReason)));
      }

      PunishmentHandler.addPunishmentLog(
          Punishment.Type.BAN,
          PunishmentHandler.comparePunishments(reason != null ? reason.toString() : banReason),
          isBanned.equalsIgnoreCase("true") ? 0 : Long.parseLong(isBanned),
          UUID.fromString(PlayerHandler.getUUID(playerName)),
          UUID.fromString(PlayerHandler.getUUID(punisherName)));
    } else {
      players.updateOne(
          Filters.eq("username", playerName),
          new Document("$set", new Document("banned", "false")));
      players.updateOne(
          Filters.eq("username", playerName), new Document("$set", new Document("ban_reason", "")));

      PunishmentHandler.addPunishmentLog(
          Punishment.Type.UNBAN,
          Punishment.OTHER,
          0,
          UUID.fromString(PlayerHandler.getUUID(playerName)),
          UUID.fromString(PlayerHandler.getUUID(punisherName)));
    }
  }

  public static String getBanReason(String playerName) {
    if (isBanned(playerName)) {

      FindIterable<Document> doc = players.find(Filters.eq("username", playerName));

      return doc.first().getString("ban_reason");

    } else {
      return "Player not banned.";
    }
  }
}
