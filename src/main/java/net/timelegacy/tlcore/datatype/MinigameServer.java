package net.timelegacy.tlcore.datatype;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import java.util.HashMap;
import java.util.UUID;
import net.timelegacy.tlcore.handler.ServerHandler;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bson.Document;

public class MinigameServer {

  private static MongoCollection<Document> minigameServers = MongoDB.mongoDatabase.getCollection("minigame_servers");

  private UUID uuid;
  private State state;
  private String game;
  private String map;

  public MinigameServer(UUID uuid) {
    this.uuid = uuid;

    if (ServerHandler.serverExists(uuid) && !serverExists(uuid)) {
      Document doc =
          new Document("uuid", uuid.toString())
              .append("state", State.RESTARTING.toString())
              .append("game", "UNKNOWN")
              .append("map", "UNKNOWN");

      minigameServers.insertOne(doc);
    }

    if (serverExists(uuid)) {
      FindIterable<Document> doc = minigameServers.find(Filters.eq("uuid", uuid.toString()));
      this.state = State.valueOf(doc.first().getString("state"));
      this.game = doc.first().getString("game");
      this.map = doc.first().getString("map");
    }
  }

  public static HashMap<UUID, String> getMinigameServers() {
    FindIterable<Document> doc = minigameServers.find();

    HashMap<UUID, String> servers = new HashMap<>();

    MongoCursor<Document> cursor = doc.iterator();

    while (cursor.hasNext()) {
      Document document = cursor.next();
      servers.put(UUID.fromString(document.getString("uuid")), document.getString("game"));
    }

    return servers;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    minigameServers.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("state", state.toString())));

    this.state = state;
  }

  public String getGame() {
    return game;
  }

  public void setGame(String game) {
    minigameServers.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("game", game)));

    this.game = game;
  }

  public String getMap() {
    return map;
  }

  public void setMap(String map) {
    minigameServers.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("map", map)));

    this.map = map;
  }

  private boolean serverExists(UUID uuid) {
    FindIterable<Document> iterable = minigameServers.find(new Document("uuid", uuid.toString()));
    return iterable.first() != null;
  }

  public enum State {
    WAITING, STARTING, INGAME, RESTARTING;
  }
}
