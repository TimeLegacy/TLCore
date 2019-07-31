package net.timelegacy.tlcore.datatype;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.util.UUID;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.handler.RankHandler;
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

  private Chat chat;
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
  - Add command to add new ranks
  - Make it so if a player has a rank that doesn't exist anymore, they get set to DEFAULT.
  - Add clear chat and chat disable command.
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
      this.username = doc.first().getString("username");
      this.rank = RankHandler.stringToRank(doc.first().getString("rank"));
      this.coins = doc.first().getInteger("coins");
      this.crateKeys = doc.first().getInteger("crate_keys");
      this.perks = doc.first().getString("perks");
      this.online = doc.first().getBoolean("online");
      this.lastIP = doc.first().getString("last_ip");
      this.previousIPs = doc.first().getString("previous_ips");
      this.dateJoined = doc.first().getLong("date_joined");
      this.lastConnection = doc.first().getLong("last_connection");
      this.playerProfile = new PlayerProfile(this.uuid);
      this.chat = new Chat(this);
    }
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    players.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("username", username)));

    this.username = username;
  }

  public Rank getRank() {
    return rank;
  }

  public void setRank(Rank rank) {
    players.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("rank", rank.getName())));

    this.rank = rank;
  }

  public int getCoins() {
    return coins;
  }

  public void setCoins(int coins) {
    players.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("coins", coins)));

    this.coins = coins;
  }

  public int getCrateKeys() {
    return crateKeys;
  }

  public void setCrateKeys(int crateKeys) {
    players.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("crate_keys", crateKeys)));

    this.crateKeys = crateKeys;
  }

  public String getPerks() {
    return perks;
  }

  public void setPerks(String perks) {
    this.perks = perks;
  }

  public boolean isOnline() {
    return online;
  }

  public void setOnline(boolean online) {
    players.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("online", online)));

    this.online = online;
  }

  public String getLastIP() {
    return lastIP;
  }

  public void setLastIP(String lastIP) {
    players.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("last_ip", lastIP)));

    this.lastIP = lastIP;
  }

  public String getPreviousIPs() {
    return previousIPs;
  }

  public void setPreviousIPs(String previousIPs) {
    players.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("previous_ips", previousIPs)));

    this.previousIPs = previousIPs;
  }

  public long getDateJoined() {
    return dateJoined;
  }

  public void setDateJoined(long dateJoined) {
    players.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("date_joined", dateJoined)));

    this.dateJoined = dateJoined;
  }

  public long getLastConnection() {
    return lastConnection;
  }

  public void setLastConnection(long lastConnection) {
    players.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("last_connection", lastConnection)));

    this.lastConnection = lastConnection;
  }

  public PlayerProfile getPlayerProfile() {
    return playerProfile;
  }

  public void updatePlayer(Player player) {

    //Update username
    setUsername(player.getName());

    //Update player online status
    if (player.isOnline()) {
      setOnline(true);

      //Update last connection
      players.updateOne(Filters.eq("uuid", this.uuid.toString()),
          new Document("$set", new Document("last_connection", System.currentTimeMillis())));
    } else {
      setOnline(false);
    }

    //Update player IPs
    players.updateOne(
        Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("last_ip", player.getAddress().getAddress().getHostAddress())));

    String previousIPs = getPreviousIPs();
    if (!previousIPs.contains(player.getAddress().getAddress().getHostAddress())) {
      players.updateOne(
          Filters.eq("uuid", this.uuid.toString()),
          new Document("$set",
              new Document("previous_ips", getPreviousIPs()
                  + player.getAddress().getAddress().getHostAddress()
                  + ",")));
    }
  }

  public Chat getChat() {
    return this.chat;
  }
}
