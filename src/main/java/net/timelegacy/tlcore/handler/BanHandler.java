package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;
import net.timelegacy.tlcore.datatype.Punishment;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bson.Document;

public class BanHandler {

  private static MongoCollection<Document> players = MongoDB.mongoDatabase.getCollection("players");

  /**
   * Check if a player is banned
   *
   * @param uuid player's uuid
   */
  public static boolean isBanned(UUID uuid) {
    if (!PlayerHandler.playerExists(uuid)) {
      return true;
    }

    FindIterable<Document> doc = players.find(Filters.eq("uuid", uuid.toString()));
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

  /**
   * Get when a player's ban expires
   *
   * @param uuid player's uuid
   */
  public static String getBanExpire(UUID uuid) {
    if (!isBanned(uuid)) {
      return "false";
    }

    FindIterable<Document> doc = players.find(Filters.eq("uuid", uuid.toString()));
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

  /**
   * Set a player as banned/unbanned
   *
   * @param bannedUUID player to be banned/unbanned
   * @param isBanned #d/#m/#y OR true/false
   * @param reason punishment reason
   * @param punisherUUID player that banned the player
   */
  public static void setBanned(UUID bannedUUID, String isBanned, Punishment reason, UUID punisherUUID) {
    String banReason = "OTHER";

    if (!isBanned.equalsIgnoreCase("true") && !isBanned.equalsIgnoreCase("false")) {
      isBanned = System.currentTimeMillis() + parse(isBanned) + "";
    }

    if (!isBanned.equalsIgnoreCase("false")) {
      players.updateOne(Filters.eq("uuid", bannedUUID.toString()), new Document("$set", new Document("banned", isBanned)));

      if (reason != null) {
        players.updateOne(Filters.eq("uuid", bannedUUID.toString()),
            new Document("$set", new Document("ban_reason", reason.toString())));
      } else {
        players.updateOne(Filters.eq("uuid", bannedUUID.toString()),
            new Document("$set", new Document("ban_reason", banReason)));
      }

      PunishmentHandler.addPunishmentLog(
          Punishment.Type.BAN,
          PunishmentHandler.comparePunishments(reason != null ? reason.toString() : banReason),
          isBanned.equalsIgnoreCase("true") ? 0 : Long.parseLong(isBanned),
          bannedUUID,
          punisherUUID);
    } else {
      players.updateOne(Filters.eq("uuid", bannedUUID.toString()), new Document("$set", new Document("banned", "false")));
      players.updateOne(Filters.eq("uuid", bannedUUID.toString()), new Document("$set", new Document("ban_reason", "")));

      PunishmentHandler.addPunishmentLog(
          Punishment.Type.UNBAN,
          Punishment.OTHER,
          0,
          bannedUUID,
          punisherUUID);
    }
  }

  /**
   * Get why a player was banned
   *
   * @param uuid player's uuid
   */
  public static Punishment getBanReason(UUID uuid) {
    if (!isBanned(uuid)) {
      return Punishment.NULL;
    }

    FindIterable<Document> doc = players.find(Filters.eq("uuid", uuid.toString()));

    String resn = doc.first().getString("ban_reason");
    if (resn != null) {
      return Punishment.valueOf(resn);
    } else {
      return Punishment.NULL;
    }

  }
}
