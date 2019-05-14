package net.timelegacy.tlcore.handler;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import java.util.HashMap;
import net.timelegacy.tlcore.TLCore;
import org.bson.Document;

public class TopPlayersHandler {

  private TLCore core = TLCore.getInstance();

  /**
   * Get one of the top 3 players
   *
   * @param row (wins, kills, deaths, losses)
   */
  public String getTop3(int index, String row) {
    HashMap<Integer, String> topPlayers = new HashMap<Integer, String>();

    MongoCursor<Document> cursor =
        core.mongoDB.players.find().sort(new BasicDBObject(row, -1)).limit(3).iterator();

    int i = 0;

    while (cursor.hasNext()) {
      i++;
      Document doc = cursor.next();
      String username = doc.getString("username");

      topPlayers.put(i, username);
    }

    return topPlayers.get(index);
  }
}
