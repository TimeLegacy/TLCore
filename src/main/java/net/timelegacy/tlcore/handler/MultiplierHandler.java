package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bson.Document;

public class MultiplierHandler {

  private static MongoCollection<Document> settings = MongoDB.mongoDatabase
      .getCollection("settings");

  public static void toggleMultiplier(boolean enabled) {
    settings.updateOne(
        Filters.eq("name", "multiplier"), new Document("$set", new Document("enabled", enabled)));
  }

  public static Integer getMultiplier() {
    FindIterable<Document> doc = settings.find(Filters.eq("name", "multiplier"));
    int multiplier = doc.first().getInteger("amount");

    return multiplier;
  }

  public static void setMultiplier(Integer multiply) {
    settings.updateOne(
        Filters.eq("name", "multiplier"), new Document("$set", new Document("amount", multiply)));
  }

  public static Boolean isMultiplierEnabled() {
    FindIterable<Document> doc = settings.find(Filters.eq("name", "multiplier"));
    return doc.first().getBoolean("enabled");
  }
}
