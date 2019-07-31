package net.timelegacy.tlcore.event;

import net.timelegacy.tlcore.datatype.Chat;
import net.timelegacy.tlcore.datatype.PlayerProfile;
import net.timelegacy.tlcore.datatype.Punishment;
import net.timelegacy.tlcore.datatype.Punishment.Type;
import net.timelegacy.tlcore.datatype.Rank;
import net.timelegacy.tlcore.handler.CacheHandler;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.Plugin;

public class FilterEvents implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void commandPreProcessEvent(PlayerCommandPreprocessEvent event) {
    Player player = event.getPlayer();

    if (event.getMessage().toLowerCase().equalsIgnoreCase("/pl")
        || event.getMessage().toLowerCase().equalsIgnoreCase("/plugins")
        || event.getMessage().toLowerCase().startsWith("/?")
        || event.getMessage().toLowerCase().startsWith("/help")
        || event.getMessage().toLowerCase().startsWith("/bukkit:")
        || event.getMessage().toLowerCase().startsWith("/canihasbukkit")
        || event.getMessage().toLowerCase().startsWith("/version")) {
      event.setCancelled(true);

      Rank rank = RankHandler.getRank(player.getUniqueId());

      if (rank.getPriority() < 9) {
        MessageUtils.noPerm(player);
        return;
      }

      MessageUtils.sendMessage(player, MessageUtils.MAIN_COLOR + "&lPlugins", false);
      for (Plugin pp : Bukkit.getPluginManager().getPlugins()) {
        MessageUtils.sendMessage(player,
            MessageUtils.SECOND_COLOR
                + pp.getName()
                + " v"
                + pp.getDescription().getVersion(),
            false);
      }
    } else if (event.getMessage().toLowerCase().startsWith("/me ")) {
      event.setCancelled(true);
      MessageUtils.noPerm(player);
    } else if (event.getMessage().toLowerCase().equals("/me")) {
      event.setCancelled(true);
      MessageUtils.noPerm(player);
    }
  }

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();

    event.setCancelled(true);

    String message = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', event.getMessage()));

    System.out.println("[CHAT] " + player.getName() + " > " + message);

    Punishment punishment = new Punishment(player.getUniqueId());

    Chat chat = CacheHandler.getPlayerData(player.getUniqueId()).getChat();

    if (!punishment.isPunished(Type.MUTE)) {

        for (Player sp : Bukkit.getOnlinePlayers()) {
          PlayerProfile playerProfile = CacheHandler.getPlayerData(sp.getUniqueId()).getPlayerProfile();
          switch (playerProfile.getChatFilter()) {
            case CHILD:
              sp.sendMessage(chat.getFormat() + MessageUtils.filterMessage(message));
              break;
            case MATURE:
              sp.sendMessage(chat.getFormat() + message);
              break;
          }
        }

    } else {
      if (!(punishment.getPunishmentExpire(Type.MUTE).isEmpty())) {

        MessageUtils.sendMessage(player,
            "&4You have been muted. &cReason: &f&o"
                + punishment.getPunishmentReason(Type.MUTE).toString()
                + " &cExpire: &f&o"
                + punishment.getPunishmentExpire(Type.MUTE),
            true);
      } else {

        MessageUtils.sendMessage(player,
            "&4You have been muted. &cReason: &f&o" + punishment.getPunishmentReason(Type.MUTE).toString(), true);
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onLightningStrike(LightningStrikeEvent event) {
    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onThunderChange(ThunderChangeEvent event) {
    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onWeatherChange(WeatherChangeEvent event) {
    event.setCancelled(true);
  }
}
