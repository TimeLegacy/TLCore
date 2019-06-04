package net.timelegacy.tlcore.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDB {

  public static MongoDatabase mongoDatabase;
  private static MongoClient mongoClient;

  /**
   * Connect to MongoDB using URI
   *
   * @param uri uri of server
   */
  public static boolean connect(String uri) {
    mongoClient = MongoClients.create(uri);
    mongoDatabase = mongoClient.getDatabase("mineaqua");
    return true;
  }

  /**
   * Disconnect from mongodb
   */
  public static boolean disconnect() {
    mongoClient.close();
    return true;
  }
}
