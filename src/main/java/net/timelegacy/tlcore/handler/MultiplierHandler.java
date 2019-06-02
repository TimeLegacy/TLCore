package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bson.Document;

public class MultiplierHandler {

  private static MongoCollection<Document> settings = MongoDB.mongoDatabase
      .getCollection("settings");

  /**
   * Toggle global multiplier on or off
   *
   * @param enabled true/false
   */
  public static void toggleMultiplier(boolean enabled) {
    settings.updateOne(
            Filters.eq("name", "multiplier"), new Document("$set", new Document("enabled", enabled)));
  }

  /**
   * Get the multiplier amount
   *
   * @return
   */
  public static Integer getMultiplier() {
    FindIterable<Document> doc = settings.find(Filters.eq("name", "multiplier"));
    int multiplier = doc.first().getInteger("amount");

    return multiplier;
  }

  /**
   * Set the multiplier amount
   *
   * @param multiply amount of how many times to multiply earned coins
   */
  public static void setMultiplier(Integer multiply) {
    settings.updateOne(
            Filters.eq("name", "multiplier"), new Document("$set", new Document("amount", multiply)));
  }

  /**
   * Check if multiplier is on
   *
   * @return
   */
  public static Boolean isMultiplierEnabled() {
    FindIterable<Document> doc = settings.find(Filters.eq("name", "multiplier"));
    return doc.first().getBoolean("enabled");
  }
}
