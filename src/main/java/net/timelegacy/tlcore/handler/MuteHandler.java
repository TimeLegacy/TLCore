package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import net.timelegacy.tlcore.datatype.Punishment;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bson.Document;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

public class MuteHandler {

  private static MongoCollection<Document> players = MongoDB.mongoDatabase.getCollection("players");

  public static boolean isMuted(String playerName) {
    if (PlayerHandler.playerExistsName(playerName)) {
      FindIterable<Document> doc = players.find(Filters.eq("username", playerName));
      String muted = doc.first().getString("muted");
      if (muted.equalsIgnoreCase("true")) {
        return true;
      } else if (muted.equalsIgnoreCase("false")) {
        return false;
      } else {
        long muteExpire = Long.parseLong(muted);
        return muteExpire > System.currentTimeMillis();
      }
    }
    return false;
  }

  public static String getMuteExpire(String playerName) {
    if (isMuted(playerName)) {
      FindIterable<Document> doc = players.find(Filters.eq("username", playerName));
      String muted = doc.first().getString("muted");

      if (!muted.equalsIgnoreCase("false") && !muted.equalsIgnoreCase("true")) {
        long muteExpire = Long.parseLong(muted);
        if (muteExpire > System.currentTimeMillis()) {
          Timestamp stamp = new Timestamp(muteExpire);
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

  public static void setMuted(String playerName, String isMuted, Punishment reason,
      String punisherName) {
    String muteReason = "OTHER";

    if (!isMuted.equalsIgnoreCase("true") && !isMuted.equalsIgnoreCase("false")) {

      isMuted = System.currentTimeMillis() + parse(isMuted) + "";
    }

    if (!isMuted.equalsIgnoreCase("false")) {
      players.updateOne(
          Filters.eq("username", playerName), new Document("$set", new Document("muted", isMuted)));

      if (reason != null) {
        players.updateOne(
            Filters.eq("username", playerName),
            new Document("$set", new Document("mute_reason", reason.toString())));

      } else {
        players.updateOne(
            Filters.eq("username", playerName),
            new Document("$set", new Document("mute_reason", muteReason)));
      }

      PunishmentHandler.addPunishmentLog(
          Punishment.Type.MUTE,
          PunishmentHandler.comparePunishments(
              reason != null ? reason.toString() : muteReason),
          isMuted.equalsIgnoreCase("true") ? 0 : Long.parseLong(isMuted),
          UUID.fromString(PlayerHandler.getUUID(playerName)),
          UUID.fromString(PlayerHandler.getUUID(punisherName)));
    } else {
      players.updateOne(
          Filters.eq("username", playerName), new Document("$set", new Document("muted", "false")));
      players.updateOne(
          Filters.eq("username", playerName),
          new Document("$set", new Document("mute_reason", "")));

      PunishmentHandler.addPunishmentLog(
          Punishment.Type.UNMUTE,
          Punishment.OTHER,
          0,
          UUID.fromString(PlayerHandler.getUUID(playerName)),
          UUID.fromString(PlayerHandler.getUUID(punisherName)));
    }
  }

  public static String getMuteReason(String playerName) {
    if (isMuted(playerName)) {

      FindIterable<Document> doc = players.find(Filters.eq("username", playerName));
      String reason = doc.first().getString("mute_reason");

      return reason;

    } else {
      return "Player not muted.";
    }
  }
}
