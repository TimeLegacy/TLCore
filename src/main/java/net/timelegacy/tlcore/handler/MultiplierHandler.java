package net.timelegacy.tlcore.handler;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import net.timelegacy.tlcore.TLCore;
import org.bson.Document;

public class MultiplierHandler {

  private TLCore core = TLCore.getInstance();

  /**
   * Enable the multiplier
   */
  public void enableMultiplier(boolean enabled) {
    core.mongoDB.settings.updateOne(
        Filters.eq("name", "multiplier"), new Document("$set", new Document("enabled", enabled)));
  }

  /**
   * Get the multiplier
   */
  public Integer getMultiplier() {
    FindIterable<Document> doc = core.mongoDB.settings.find(Filters.eq("name", "multiplier"));
    int multiplier = doc.first().getInteger("amount");

    return multiplier;
  }

  /**
   * Set the multiplier
   */
  public void setMultiplier(Integer multiply) {
    core.mongoDB.settings.updateOne(
        Filters.eq("name", "multiplier"), new Document("$set", new Document("amount", multiply)));
  }

  /**
   * Check if multiplier is enabled
   */
  public Boolean isMultiplierEnabled() {
    FindIterable<Document> doc = core.mongoDB.settings.find(Filters.eq("name", "multiplier"));
    return doc.first().getBoolean("enabled");
  }
}
