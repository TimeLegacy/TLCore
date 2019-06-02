package net.timelegacy.tlcore.handler;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import net.timelegacy.tlcore.datatype.Punishment;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PunishmentHandler {

  private static MongoCollection<Document> punishments = MongoDB.mongoDatabase
      .getCollection("punishments");

  /**
   * Add a punishment log
   *
   * @param punishmentType punishment (ex: BAN, MUTE)
   * @param punishment     punishment reason (ex: HACKING)
   * @param expire
   * @param uuidPunished
   * @param uuidPunisher
   */
  public static void addPunishmentLog(
          Punishment.Type punishmentType,
          Punishment punishment,
          long expire,
          UUID uuidPunished,
          UUID uuidPunisher) {

    Document doc =
            new Document("punished_uuid", uuidPunished.toString())
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
   * @return
   */
  public static List<Document> getRecentPunishments(UUID uuid) {
    List<Document> recentPunishments = new ArrayList<>();

    MongoCursor<Document> cursor =
            punishments
                    .find()
                    .sort(new BasicDBObject("timestamp", -1))
                    .sort(new BasicDBObject("uuid", uuid))
                    .limit(10)
                    .iterator();

    int i = 0;

    while (cursor.hasNext()) {
      i++;
      Document doc = cursor.next();
      recentPunishments.add(doc);
    }

    return recentPunishments;
  }

  /**
   * Compare punishments (String to Punishment)
   *
   * @param string punishment string
   * @return
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
