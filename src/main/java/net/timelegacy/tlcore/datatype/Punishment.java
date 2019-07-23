package net.timelegacy.tlcore.datatype;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bson.Document;

public class Punishment {

  private static MongoCollection<Document> punishments = MongoDB.mongoDatabase.getCollection("punishments");

  private UUID uuid;

  public Punishment(UUID
      uuid) {
    this.uuid = uuid;
  }

  /**
   * Convert from time format such as #d/#m/#y to the final timestamp which a player's ban is no longer valid
   */

  public static long parseExpire(String input) {
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
      case 'y':
        return value * 1000 * 60 * 60 * 24 * 30 * 12;
      case 'm':
        return value * 1000 * 60 * 60 * 24 * 30;
      case 'd':
        return value * 1000 * 60 * 60 * 24;
    }

    return 0;
  }

  public static boolean validReason(String reason) {
    for (Reason c : Reason.values()) {
      if (c.name().equals(reason)) {
        return true;
      }
    }

    return false;
  }

  public Reason getPunishmentReason(Type type) {
    MongoCursor<Document> punish = punishments
        .find(Filters.and(
            Filters.eq("punished_uuid", uuid.toString()),
            Filters.eq("type", type.toString()))).sort(new BasicDBObject("timestamp", -1)).iterator();

    while (punish.hasNext()) {
      Document document = punish.next();
      long timestamp = document.getLong("timestamp");
      Reason reason = Reason.valueOf(document.getString("reason"));
      if (!isUnPunished(Type.valueOf("UN" + type.toString()), timestamp)) {
        return reason;
      }
    }
    return null;
  }

  public String getPunishmentExpire(Type type) {
    MongoCursor<Document> punish = punishments
        .find(Filters.and(
            Filters.eq("punished_uuid", uuid.toString()),
            Filters.eq("type", type.toString()))).sort(new BasicDBObject("timestamp", -1)).iterator();

    while (punish.hasNext()) {
      Document document = punish.next();
      long expire = document.getLong("expire");
      long timestamp = document.getLong("timestamp");
      if (!isUnPunished(Type.valueOf("UN" + type.toString()), timestamp)) {
        if (expire == 0) {
          return "NEVER";
        } else if (expire > System.currentTimeMillis()) {
          Timestamp stamp = new Timestamp(expire);
          Date date = new Date(stamp.getTime());

          return date.toString();
        }
      }
    }
    return null;
  }

  public boolean isPunished(Type type) {
    MongoCursor<Document> punish = punishments
        .find(Filters.and(
            Filters.eq("punished_uuid", uuid.toString()),
            Filters.eq("type", type.toString()))).sort(new BasicDBObject("timestamp", -1)).iterator();

    while (punish.hasNext()) {
      Document document = punish.next();
      long expire = document.getLong("expire");
      long timestamp = document.getLong("timestamp");
      if (isUnPunished(Type.valueOf("UN" + type.toString()), timestamp)) {
        return false;
      } else {
        if (expire == 0) {
          return true;
        } else if (expire > System.currentTimeMillis()) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean isUnPunished(Type type, long timestamp) {
    MongoCursor<Document> unpunish = punishments
        .find(Filters.and(
            Filters.eq("punished_uuid", uuid.toString()),
            Filters.eq("type", type.toString()))).sort(new BasicDBObject("timestamp", -1)).iterator();

    while (unpunish.hasNext()) {
      Document document = unpunish.next();
      long unpunishTimestamp = document.getLong("timestamp");
      if (unpunishTimestamp > timestamp) {
        return true;
      }
    }
    return false;
  }

  /**
   * Punish a player.
   *
   * @param type Punishment type
   * @param reason Punishment reason
   * @param expire Punishment expire (NEVER for permanent)
   * @param punisherUUID UUID of the person who punished them
   */

  public void punish(Type type, Reason reason, String expire, UUID punisherUUID) {
    long expireTimestamp = 0;

    if (!expire.equalsIgnoreCase("NEVER")) {
      expireTimestamp = System.currentTimeMillis() + parseExpire(expire);
    }

    Document doc = new Document("punished_uuid", uuid.toString())
        .append("type", type.toString())
        .append("reason", reason.toString())
        .append("timestamp", System.currentTimeMillis())
        .append("expire", expireTimestamp)
        .append("punisher_uuid", punisherUUID.toString());

    punishments.insertOne(doc);
  }

  public enum Type {
    BAN,
    MUTE,
    UNBAN,
    UNMUTE
  }

  public enum Reason{
    HACKING, PROFANITY, OTHER, NULL
  }
}
