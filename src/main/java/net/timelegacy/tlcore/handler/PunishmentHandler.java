package net.timelegacy.tlcore.handler;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.timelegacy.tlcore.datatype.Punishment;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bson.Document;

public class PunishmentHandler {

  private static MongoCollection<Document> punishments = MongoDB.mongoDatabase.getCollection("punishments");

  /**
   * Add a punishment logger
   *
   * @param punishmentType punishment (ex: BAN, MUTE)
   * @param punishment punishment reason (ex: HACKING)
   */
  public static void addPunishmentLog(
      Punishment.Type punishmentType,
      Punishment punishment,
      long expire,
      UUID uuidPunished,
      UUID uuidPunisher) {

    Document doc = new Document("punished_uuid", uuidPunished.toString())
        .append("punishment_type", punishmentType.toString())
        .append("punishment", punishment.toString())
        .append("timestamp", System.currentTimeMillis())
        .append("expire", expire)
        .append("punisher_uuid", uuidPunisher.toString());

    punishments.insertOne(doc);
  }

  /**
   * Get 10 most recent punishmnets
   *
   * @param uuid player's uuid
   */
  public static List<Document> getRecentPunishments(UUID uuid) {
    List<Document> recentPunishments = new ArrayList<>();

    for (Document doc : punishments.find()
        .sort(new BasicDBObject("timestamp", -1))
        .sort(new BasicDBObject("uuid", uuid))
        .limit(10)) {
      recentPunishments.add(doc);
    }

    return recentPunishments;
  }

  /**
   * Compare punishments (String to Punishment)
   *
   * @param string punishment string
   */
  public static Punishment comparePunishments(String string) {
    for (Punishment punishment : Punishment.values()) {
      if (punishment.toString().equalsIgnoreCase(string)) {
        return punishment;
      }
    }

    return Punishment.NULL;
  }
}
