package net.timelegacy.tlcore.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.timelegacy.tlcore.datatype.PlayerProfile;

public class FriendHandler {

  /**
   * Get a list of friends
   *
   * @param uuid player's uuid, they must be a valid UUID in the database
   */
  public static List<UUID> getFriends(UUID uuid) {
    List<UUID> friends = new ArrayList<>();

    PlayerProfile profile;
    if (CacheHandler.isPlayerCached(uuid)) {
      profile = CacheHandler.getPlayerData(uuid).getPlayerProfile();
    } else {
      profile = new PlayerProfile(uuid);
    }

    if (!profile.getFriends().isEmpty()) {
      String[] friend = profile.getFriends().split(",");
      for (String id : friend) {
        friends.add(UUID.fromString(id));
      }
    }
    return friends;
  }

  /**
   * Get a list of pending friend invites
   *
   * @param uuid player's uuid, they must be a valid UUID in the database
   */
  public static List<UUID> getPendingFriends(UUID uuid) {
    List<UUID> pendingFriends = new ArrayList<>();

    PlayerProfile profile;
    if (CacheHandler.isPlayerCached(uuid)) {
      profile = CacheHandler.getPlayerData(uuid).getPlayerProfile();
    } else {
      profile = new PlayerProfile(uuid);
    }

    if (!profile.getFriendsPending().isEmpty()) {
      String[] pending = profile.getFriendsPending().split(",");
      for (String id : pending) {
        pendingFriends.add(UUID.fromString(id));
      }
    }

    return pendingFriends;
  }

  /**
   * Send a friend request.
   *
   * @param sender uuid of player that sends friend request
   * @param receiver uuid of player to receive the friend request
   */
  public static void sendRequest(UUID sender, UUID receiver) {
    PlayerProfile profile;
    if (CacheHandler.isPlayerCached(receiver)) {
      profile = CacheHandler.getPlayerData(receiver).getPlayerProfile();
    } else {
      profile = new PlayerProfile(receiver);
    }

      profile.setFriendsPending(profile.getFriendsPending() + sender.toString() + ",");
  }

  /**
   * Deny a pending friend request.
   *
   * @param sender player that is denying the request
   * @param receiver player that is being denied
   */
  public static void denyRequest(UUID sender, UUID receiver) {
    PlayerProfile profile;
    if (CacheHandler.isPlayerCached(sender)) {
      profile = CacheHandler.getPlayerData(sender).getPlayerProfile();
    } else {
      profile = new PlayerProfile(sender);
    }

    profile.setFriendsPending(profile.getFriendsPending().replace(receiver.toString() + ",", ""));
  }

  /**
   * Accept a pending friend request.
   *
   * @param sender player that is accepting a request
   * @param receiver player that their request is being accepted
   */
  public static void acceptRequest(UUID sender, UUID receiver) {
    PlayerProfile profile;
    if (CacheHandler.isPlayerCached(sender)) {
      profile = CacheHandler.getPlayerData(sender).getPlayerProfile();
    } else {
      profile = new PlayerProfile(sender);
    }

      profile.setFriendsPending(profile.getFriendsPending().replace(receiver.toString() + ",", ""));
      profile.setFriends(profile.getFriends() + receiver.toString() + ",");

    PlayerProfile profileReceiver;
    if (CacheHandler.isPlayerCached(receiver)) {
      profileReceiver = CacheHandler.getPlayerData(receiver).getPlayerProfile();
    } else {
      profileReceiver = new PlayerProfile(receiver);
    }

    profileReceiver
        .setFriends(profileReceiver.getFriends()
          + sender.toString() + ",");
  }

  /**
   * Remove a friend.
   *
   * @param sender player that is removing their friend
   * @param receiver player that is being removed
   */
  public static void removeFriend(UUID sender, UUID receiver) {
    PlayerProfile profile;
    if (CacheHandler.isPlayerCached(sender)) {
      profile = CacheHandler.getPlayerData(sender).getPlayerProfile();
    } else {
      profile = new PlayerProfile(sender);
    }
      profile.setFriends(profile.getFriends().replace(receiver.toString() + ",", ""));

    PlayerProfile profileReceiver;
    if (CacheHandler.isPlayerCached(receiver)) {
      profileReceiver = CacheHandler.getPlayerData(receiver).getPlayerProfile();
    } else {
      profileReceiver = new PlayerProfile(receiver);
    }

      profileReceiver.setFriends(profileReceiver.getFriends().replace(sender.toString() + ",", ""));
  }
}
