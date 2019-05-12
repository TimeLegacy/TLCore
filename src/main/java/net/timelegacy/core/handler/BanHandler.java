package net.timelegacy.core.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;
import net.timelegacy.core.Core;
import org.bson.Document;

public class BanHandler {

  private Core core = Core.getInstance();

  /**
   * Check if player is banned
   *
   * @param playerName the player who is being checked if they are banned or not
   * @return true, false
   */
  public boolean isBanned(String playerName) {
    if (core.playerHandler.playerExistsName(playerName)) {
      FindIterable<Document> doc = core.mongoDB.players.find(Filters.eq("username", playerName));
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

  public String getBanExpire(String playerName) {
    if (isBanned(playerName)) {
      FindIterable<Document> doc = core.mongoDB.players.find(Filters.eq("username", playerName));
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
   * Set player banned to true or false
   *
   * @param playerName the player is to be banned or unbanned
   * @param isBanned Time format, true for perm, false for unmute, time is #d or #m or #y
   * @param reason the reason for being banned
   */
  public void setBanned(
      String playerName, String isBanned, Punishment reason, String punisherName) {
    String banReason = "OTHER";

    if (!isBanned.equalsIgnoreCase("true") && !isBanned.equalsIgnoreCase("false")) {

      isBanned = System.currentTimeMillis() + parse(isBanned) + "";
    }

    if (!isBanned.equalsIgnoreCase("false")) {
      core.mongoDB.players.updateOne(
          Filters.eq("username", playerName),
          new Document("$set", new Document("banned", isBanned)));

      if (reason != null) {
        core.mongoDB.players.updateOne(
            Filters.eq("username", playerName),
            new Document("$set", new Document("banreason", reason.toString())));
      } else {
        core.mongoDB.players.updateOne(
            Filters.eq("username", playerName),
            new Document("$set", new Document("banreason", banReason)));
      }

      core.punishmentHandler.addPunishmentLog(
          Punishment.Type.BAN,
          core.punishmentHandler.comparePunishments(reason != null ? reason.toString() : banReason),
          isBanned.equalsIgnoreCase("true") ? 0 : Long.parseLong(isBanned),
          UUID.fromString(core.playerHandler.getUUID(playerName)),
          UUID.fromString(core.playerHandler.getUUID(punisherName)));
    } else {
      core.mongoDB.players.updateOne(
          Filters.eq("username", playerName),
          new Document("$set", new Document("banned", "false")));
      core.mongoDB.players.updateOne(
          Filters.eq("username", playerName), new Document("$set", new Document("banreason", "")));

      core.punishmentHandler.addPunishmentLog(
          Punishment.Type.UNBAN,
          Punishment.OTHER,
          0,
          UUID.fromString(core.playerHandler.getUUID(playerName)),
          UUID.fromString(core.playerHandler.getUUID(punisherName)));
    }
  }

  /**
   * Get the ban reason
   *
   * @param playerName player that you're getting the reason for them being banned
   * @return the reason for being banned
   */
  public String getBanReason(String playerName) {
    if (isBanned(playerName)) {

      FindIterable<Document> doc = core.mongoDB.players.find(Filters.eq("username", playerName));

      return doc.first().getString("banreason");

    } else {
      return "Player not banned.";
    }
  }
}
