package net.timelegacy.tlcore.datatype;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.util.UUID;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerData {

  private static MongoCollection<Document> players = MongoDB.mongoDatabase.getCollection("players");

  private UUID uuid;
  private String username;
  private Rank rank;
  private int coins;
  private int crateKeys;
  private String perks;
  private boolean online;
  private String lastIP;
  private String previousIPs;
  private long dateJoined;
  private long lastConnection;

  private PlayerProfile playerProfile;

  /*
  TODO:
  - Cache chat and make it grab information from the cache such as rank and nickname and update in realtime without
  relogging.
  - Cache player profile and if the player is online grab the cached version.
  - Add method to set rank of a player in the cached thing.
  - Keep some player handler methods such as player exists, and get player by username, but return a PlayerData type.
  - Update tab and change tab team if they're online
  - Implement a disguise system (after caching)
  - Add nickname command (donators & staff, permission based + priority based)
  - Add command to add permissions to ranks.
   */


  public PlayerData(UUID uuid) {
    this.uuid = uuid;

    if (!PlayerHandler.playerExists(uuid)) {
      Player player = Bukkit.getPlayer(uuid);
      if (player.isOnline()) {
        Document doc =
            new Document("uuid", uuid)
                .append("username", player.getName())
                .append("rank", "DEFAULT")
                .append("coins", 0)
                .append("crate_keys", 0)
                .append("perks", "")
                .append("online", true)
                .append("last_ip", player.getAddress().getHostName())
                .append("previous_ips", player.getAddress().getHostName() + ",")
                .append("date_joined", System.currentTimeMillis())
                .append("last_connection", System.currentTimeMillis());

        players.insertOne(doc);
      }
    } else {
      FindIterable<Document> doc = players.find(Filters.eq("uuid", uuid.toString()));
      //this.status = doc.first().getString("status");
    }
  }
}
