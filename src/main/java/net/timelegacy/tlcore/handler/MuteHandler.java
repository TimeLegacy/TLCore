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

public class MuteHandler {

  private static MongoCollection<Document> players = MongoDB.mongoDatabase.getCollection("players");

  /**
   * Check if a player is muted
   *
   * @param uuid player's uuid
   */
  public static boolean isMuted(UUID uuid) {
    if (PlayerHandler.playerExists(uuid)) {
      FindIterable<Document> doc = players.find(Filters.eq("uuid", uuid.toString()));
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

  /**
   * Get when the mute expires
   *
   * @param uuid player's uuid
   */
  public static String getMuteExpire(UUID uuid) {
    if (isMuted(uuid)) {
      FindIterable<Document> doc = players.find(Filters.eq("uuid", uuid.toString()));
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

  /**
   * Set a player as muted or not
   *
   * @param uuid player to be muted/unmuted
   * @param isMuted #d/#m/#y OR true/false
   * @param reason punishment reason
   * @param punisherUUID player that banned the
   */
  public static void setMuted(UUID uuid, String isMuted, Punishment reason, UUID punisherUUID) {
    String muteReason = "OTHER";

    if (!isMuted.equalsIgnoreCase("true") && !isMuted.equalsIgnoreCase("false")) {
      isMuted = System.currentTimeMillis() + parse(isMuted) + "";
    }

    if (!isMuted.equalsIgnoreCase("false")) {
      players.updateOne(
          Filters.eq("username", uuid), new Document("$set", new Document("muted", isMuted)));

      if (reason != null) {
        players.updateOne(
            Filters.eq("username", uuid),
            new Document("$set", new Document("mute_reason", reason.toString())));

      } else {
        players.updateOne(
            Filters.eq("username", uuid),
            new Document("$set", new Document("mute_reason", muteReason)));
      }

      PunishmentHandler.addPunishmentLog(
          Punishment.Type.MUTE,
          PunishmentHandler.comparePunishments(
              reason != null ? reason.toString() : muteReason),
          isMuted.equalsIgnoreCase("true") ? 0 : Long.parseLong(isMuted),
          uuid,
          punisherUUID);
    } else {
      players.updateOne(
          Filters.eq("username", uuid), new Document("$set", new Document("muted", "false")));
      players.updateOne(
          Filters.eq("username", uuid),
          new Document("$set", new Document("mute_reason", "")));

      PunishmentHandler.addPunishmentLog(
          Punishment.Type.UNMUTE,
          Punishment.OTHER,
          0,
          uuid,
          uuid);
    }
  }

  /**
   * Get why a player was muted
   *
   * @param uuid player's uuid
   */
  public static Punishment getMuteReason(UUID uuid) {
    if (!isMuted(uuid)) {
      return Punishment.NULL;
    }

    FindIterable<Document> doc = players.find(Filters.eq("uuid", uuid.toString()));

    String resn = doc.first().getString("mute_reason");
    if (resn != null) {
      return Punishment.valueOf(resn);
    } else {
      return Punishment.NULL;
    }

  }
}
