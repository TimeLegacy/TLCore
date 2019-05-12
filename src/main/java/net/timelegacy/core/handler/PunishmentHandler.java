package net.timelegacy.core.handler;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.timelegacy.core.Core;
import org.bson.Document;

public class PunishmentHandler {

  Core core = Core.getInstance();

  public void addPunishmentLog(
      Punishment.Type punishmentType,
      Punishment punishment,
      long expire,
      UUID uuidPunished,
      UUID uuidPunisher) {

    Document doc =
        new Document("punisheduuid", uuidPunished.toString())
            .append("punishmenttype", punishmentType.toString())
            .append("punishment", punishment.toString())
            .append("timestamp", System.currentTimeMillis())
            .append("expire", expire)
            .append("punisheruuid", uuidPunisher.toString());

    core.mongoDB.punishments.insertOne(doc);
  }

  public List<Document> getRecentPunishments(UUID uuid) {
    List<Document> recentPunishments = new ArrayList<>();

    MongoCursor<Document> cursor =
        core.mongoDB
            .punishments
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

  public Punishment comparePunishments(String string) {
    for (Punishment punishment : Punishment.values()) {
      if (punishment.toString().equalsIgnoreCase(string)) {
        return punishment;
      }
    }

    return Punishment.NULL;
  }
}
