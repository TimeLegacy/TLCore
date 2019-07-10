package net.timelegacy.tlcore.event;

import net.timelegacy.tlcore.datatype.PlayerProfile;
import net.timelegacy.tlcore.datatype.Rank;
import net.timelegacy.tlcore.handler.MuteHandler;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import net.timelegacy.tlcore.utils.WebRequestUtils;
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

    if (event.getMessage().toLowerCase().contains("/pl")
        || event.getMessage().toLowerCase().contains("/plugins")
        || event.getMessage().toLowerCase().contains("/?")
        || event.getMessage().toLowerCase().contains("/help")
        || event.getMessage().toLowerCase().contains("/bukkit:")) {
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

    String username = player.getName();
    if (username.equalsIgnoreCase("Chriskadetv")) {
      username = "Chisrkdaevt";
    } else if (username.equalsIgnoreCase("Verade")) {
      username = "Vredae";
    }

    PlayerProfile profile = new PlayerProfile(player.getUniqueId());

    String format = MessageUtils.colorize("&r" + RankHandler
        .chatColors(player.getUniqueId())
        .replace("%username%", profile.getNickname().isEmpty() ? username : profile.getNickname())
        .replace("%line%", "\u2758 ")
        .replace("%arrows%", "\u00BB")
        + " &r");

    System.out.println("[CHAT] " + player.getUniqueId() + " > " + message);

    if (!MuteHandler.isMuted(player.getUniqueId())) {

      for (Player sp : Bukkit.getOnlinePlayers()) {
        //TextComponent textComponent = new TextComponent(core.perkHandler.getPerks(sp).contains("CHAT.MATURE") ? message : cleanMessage);
        //textComponent.setHoverEvent(new HoverEvent( HoverEvent.Action., new ComponentBuilder( "Visit the Spigot website!" ).create() ) );
        /*msg.text(format)
            .tooltip(
                MessageUtils.c(
                    MessageUtils.MAIN_COLOR
                        + "&oCoins: "
                        + MessageUtils.SECOND_COLOR
                        + core.coinHandler.getBalance(player.getUniqueId())),
                MessageUtils.c(
                    MessageUtils.MAIN_COLOR
                        + "&oKills: "
                        + MessageUtils.SECOND_COLOR
                        + core.statsHandler.getKills(player)),
                MessageUtils.c(
                    MessageUtils.MAIN_COLOR
                        + "&oWins: "
                        + MessageUtils.SECOND_COLOR
                        + core.statsHandler.getWins(player)))
            .then();
        msg.send(sp);*/

        //TODO fix the swear filter
        /*sp.sendMessage(
            core.perkHandler.getPerks(sp).contains("CHAT.MATURE") ? message : cleanMessage);*/

        sp.sendMessage(format + message);
      }
    } else {
      if (!(MuteHandler.getMuteExpire(player.getUniqueId()).equalsIgnoreCase("false")
          || MuteHandler.getMuteExpire(player.getUniqueId()).equalsIgnoreCase("true"))) {

        MessageUtils.sendMessage(player,
            "&4You have been muted. &cReason: &f&o"
                + MuteHandler.getMuteReason(player.getUniqueId())
                + " &cExpire: &f&o"
                + MuteHandler.getMuteExpire(player.getUniqueId()),
            true);
      } else {

        MessageUtils.sendMessage(player,
            "&4You have been muted. &cReason: &f&o" + MuteHandler.getMuteReason(player.getUniqueId()), true);
      }
    }
  }

  private String filteredChat(String message) {
    // https://dec0de.xyz/api/swearfilter/?message=fuck

    try {
      //TODO fix
      String response = WebRequestUtils.executePost(
          "http://nossl.dec0de.xyz/api/swearfilter/?message=" + message.replace(" ", "%20"), "");
      return response;

    } catch (Exception e) {
      e.printStackTrace();
    }

    return message;
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
