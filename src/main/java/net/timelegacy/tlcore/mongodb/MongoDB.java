package net.timelegacy.tlcore.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDB {

  public MongoCollection<Document> players;
  public MongoCollection<Document> servers;
  public MongoCollection<Document> settings;
  public MongoCollection<Document> anticheat;
  public MongoCollection<Document> punishments;
  public MongoCollection<Document> ranks;

  private MongoDatabase mongoDatabase;
  private MongoClientURI clientURI;
  private MongoClient client;

  public boolean connect(String uri) {
    // Connect to the specified ip and port
    // Default is localhost, 27017
    try {
      clientURI = new MongoClientURI(uri);
      client = new MongoClient(clientURI);
    } catch (Exception e) {
      // When you end up here, the server the db is running on could not be found!
      System.out.println("Could not connect to database!");
      e.printStackTrace();
      return false;
    }
    // Get the database called "cryslix"
    // If it does not exist it will be created automatically
    // once you save something in it
    mongoDatabase = client.getDatabase("mineaqua");
    // Get the collection called "players" in the database "cryslix"
    // Equivalent to the table in MySQL, you can store objects in here
    players = mongoDatabase.getCollection("players");
    servers = mongoDatabase.getCollection("servers");
    settings = mongoDatabase.getCollection("settings");
    anticheat = mongoDatabase.getCollection("anticheat");
    punishments = mongoDatabase.getCollection("punishments");
    ranks = mongoDatabase.getCollection("ranks");

    return true;
  }

  public boolean disconnect() {

    client.close();

    return true;
  }
}
