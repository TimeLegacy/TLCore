package net.timelegacy.tlcore.handler;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bson.Document;

public class PunishmentHandler {

  private static MongoCollection<Document> punishments = MongoDB.mongoDatabase
      .getCollection("punishments");

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

  public static Punishment comparePunishments(String string) {
    for (Punishment punishment : Punishment.values()) {
      if (punishment.toString().equalsIgnoreCase(string)) {
        return punishment;
      }
    }

    return Punishment.NULL;
  }
}
