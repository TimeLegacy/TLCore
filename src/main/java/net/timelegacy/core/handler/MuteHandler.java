package net.timelegacy.core.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;
import net.timelegacy.core.Core;
import org.bson.Document;

public class MuteHandler {

  private Core core = Core.getInstance();

  /**
   * Check if player is muted
   *
   * @param playerName the player who is being checked if they are muted or not
   * @return true, false
   */
  public boolean isMuted(String playerName) {
    if (core.playerHandler.playerExistsName(playerName)) {
      FindIterable<Document> doc = core.mongoDB.players.find(Filters.eq("username", playerName));
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

  public String getMuteExpire(String playerName) {
    if (isMuted(playerName)) {
      FindIterable<Document> doc = core.mongoDB.players.find(Filters.eq("username", playerName));
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

  private long parse(String input) {
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

  private long convert(int value, char unit) {
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
   * Set player muted to true or false
   *
   * @param playerName the player is to be muted or unmuted
   * @param isMuted Time format, true for perm, false for unmute, time is #d or #m or #y
   * @param reason the reason for being muted
   */
  public void setMuted(String playerName, String isMuted, Punishment reason, String punisherName) {
    String muteReason = "OTHER";

    if (!isMuted.equalsIgnoreCase("true") && !isMuted.equalsIgnoreCase("false")) {

      isMuted = System.currentTimeMillis() + parse(isMuted) + "";
    }

    if (!isMuted.equalsIgnoreCase("false")) {
      core.mongoDB.players.updateOne(
          Filters.eq("username", playerName), new Document("$set", new Document("muted", isMuted)));

      if (reason != null) {
        core.mongoDB.players.updateOne(
            Filters.eq("username", playerName),
            new Document("$set", new Document("mutereason", reason.toString())));

      } else {
        core.mongoDB.players.updateOne(
            Filters.eq("username", playerName),
            new Document("$set", new Document("mutereason", muteReason)));
      }

      core.punishmentHandler.addPunishmentLog(
          Punishment.Type.MUTE,
          core.punishmentHandler.comparePunishments(
              reason != null ? reason.toString() : muteReason),
          isMuted.equalsIgnoreCase("true") ? 0 : Long.parseLong(isMuted),
          UUID.fromString(core.playerHandler.getUUID(playerName)),
          UUID.fromString(core.playerHandler.getUUID(punisherName)));
    } else {
      core.mongoDB.players.updateOne(
          Filters.eq("username", playerName), new Document("$set", new Document("muted", "false")));
      core.mongoDB.players.updateOne(
          Filters.eq("username", playerName), new Document("$set", new Document("mutereason", "")));

      core.punishmentHandler.addPunishmentLog(
          Punishment.Type.UNMUTE,
          Punishment.OTHER,
          0,
          UUID.fromString(core.playerHandler.getUUID(playerName)),
          UUID.fromString(core.playerHandler.getUUID(punisherName)));
    }
  }

  /**
   * Get the mute reason
   *
   * @param playerName player that you're getting the reason for them being muted
   * @return the reason for being muted
   */
  public String getMuteReason(String playerName) {
    if (isMuted(playerName)) {

      FindIterable<Document> doc = core.mongoDB.players.find(Filters.eq("username", playerName));
      String reason = doc.first().getString("mutereason");

      return reason;

    } else {
      return "Player not muted.";
    }
  }
}
