package net.timelegacy.tlcore.datatype;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.util.UUID;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.mongodb.MongoDB;
import org.bson.Document;

public class PlayerProfile {

  private static MongoCollection<Document> profiles = MongoDB.mongoDatabase.getCollection("player_profiles");

  private UUID uuid;
  private Status status;
  private ChatFilter chatFilter;
  private String nickname;
  private Gender gender;
  private String favouriteColour;
  private String friends;
  private String friendsPending;
  private String twitter;
  private String instagram;
  private String youtube;
  private String twitch;

  public PlayerProfile(UUID uuid) {
    this.uuid = uuid;

    if (PlayerHandler.playerExists(uuid) && !profileExists(uuid)) {
      Document doc = new Document("uuid", uuid.toString())
          .append("status", Status.ACTIVE.toString())
          .append("chat_filter", ChatFilter.CHILD.toString())
          .append("nickname", "")
          .append("gender", Gender.OTHER.toString())
          .append("favourite_colour", "")
          .append("friends", "")
          .append("friends_pending", "")
          .append("twitter", "")
          .append("instagram", "")
          .append("youtube", "")
          .append("twitch", "");

      profiles.insertOne(doc);
    }

    if (profileExists(uuid)) {
      FindIterable<Document> doc = profiles.find(Filters.eq("uuid", uuid.toString()));
      this.status = Status.valueOf(doc.first().getString("status"));
      this.chatFilter = ChatFilter.valueOf(doc.first().getString("chat_filter"));
      this.nickname = doc.first().getString("nickname");
      this.gender = Gender.valueOf(doc.first().getString("gender"));
      this.favouriteColour = doc.first().getString("favourite_colour");
      this.friends = doc.first().getString("friends");
      this.friendsPending = doc.first().getString("friends_pending");
      this.twitter = doc.first().getString("twitter");
      this.instagram = doc.first().getString("instagram");
      this.youtube = doc.first().getString("youtube");
      this.twitch = doc.first().getString("twitch");
    }
  }

  private boolean profileExists(UUID uuid) {
    FindIterable<Document> iterable = profiles.find(new Document("uuid", uuid.toString()));
    return iterable.first() != null;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    profiles.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("status", status.toString())));
    this.status = status;
  }

  public ChatFilter getChatFilter() {
    return chatFilter;
  }

  public void setChatFilter(ChatFilter chatFilter) {
    profiles.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("chat_filter", chatFilter.toString())));
    this.chatFilter = chatFilter;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    profiles.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("nickname", nickname)));
    this.nickname = nickname;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    profiles.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("gender", gender.toString())));
    this.gender = gender;
  }

  public String getFavouriteColour() {
    return favouriteColour;
  }

  public void setFavouriteColour(String favouriteColour) {
    profiles.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("favourite_colour", favouriteColour)));
    this.favouriteColour = favouriteColour;
  }

  public String getFriends() {
    return friends;
  }

  public void setFriends(String friends) {
    profiles.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("friends", friends)));
    this.friends = friends;
  }

  public String getFriendsPending() {
    return friendsPending;
  }

  public void setFriendsPending(String friendsPending) {
    profiles.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("friends_pending", friendsPending)));
    this.friendsPending = friendsPending;
  }

  public String getTwitter() {
    return twitter;
  }

  public void setTwitter(String twitter) {
    profiles.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("twitter", twitter)));
    this.twitter = twitter;
  }

  public String getInstagram() {
    return instagram;
  }

  public void setInstagram(String instagram) {
    profiles.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("instagram", instagram)));
    this.instagram = instagram;
  }

  public String getYoutube() {
    return youtube;
  }

  public void setYoutube(String youtube) {
    profiles.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("youtube", youtube)));
    this.youtube = youtube;
  }

  public String getTwitch() {
    return twitch;
  }

  public void setTwitch(String twitch) {
    profiles.updateOne(Filters.eq("uuid", this.uuid.toString()),
        new Document("$set", new Document("twitch", twitch)));
    this.twitch = twitch;
  }

  public enum Status {
    ACTIVE, AWAY, DND;
  }

  public enum ChatFilter {
    CHILD, MATURE;
  }

  public enum Gender {
    MALE, FEMALE, OTHER;
  }

}
