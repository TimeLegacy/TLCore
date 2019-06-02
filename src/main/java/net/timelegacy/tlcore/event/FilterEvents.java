package net.timelegacy.tlcore.event;

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
    Player p = event.getPlayer();

    if (event.getMessage().toLowerCase().contains("/pl")
        || event.getMessage().toLowerCase().contains("/plugins")
        || event.getMessage().toLowerCase().contains("/?")
        || event.getMessage().toLowerCase().contains("/help")
        || event.getMessage().toLowerCase().contains("/bukkit:")) {
      event.setCancelled(true);

      Rank r = RankHandler.getRank(p.getUniqueId());

      if (r.getPriority() >= 9) {
        MessageUtils.sendMessage(p, MessageUtils.MAIN_COLOR + "&lPlugins", false);
        for (Plugin pp : Bukkit.getPluginManager().getPlugins()) {
          MessageUtils.sendMessage(
              p,
              MessageUtils.SECOND_COLOR
                  + pp.getName()
                  + " v"
                  + pp.getDescription().getVersion(),
              false);
        }
      } else {
        MessageUtils.noPerm(p);
      }
    } else if (event.getMessage().toLowerCase().startsWith("/me ")) {
      event.setCancelled(true);
      MessageUtils.noPerm(p);
    } else if (event.getMessage().toLowerCase().equals("/me")) {
      event.setCancelled(true);
      MessageUtils.noPerm(p);
    }
  }

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent e) {
    Player p = e.getPlayer();

    e.setCancelled(true);

    String message =
        ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', e.getMessage()));

    String username = p.getName();
    if (username.equalsIgnoreCase("Chriskadetv")) {
      username = "Chisrkdaevt";
    } else if (username.equalsIgnoreCase("Verade")) {
      username = "Vredae";
    }

    String format =
        MessageUtils.colorize(
            "&r"
                + RankHandler
                    .chatColors(p.getUniqueId())
                .replace("%username%", username)
                .replace("%line%", "\u2758 ")
                .replace("%arrows%", "\u00BB")
                + " &r");

    System.out.println("[CHAT] " + p.getUniqueId() + " > " + message);

    if (!MuteHandler.isMuted(p.getUniqueId())) {

      for (Player sp : Bukkit.getOnlinePlayers()) {
        //TextComponent textComponent = new TextComponent(core.perkHandler.getPerks(sp).contains("CHAT.MATURE") ? message : cleanMessage);
        //textComponent.setHoverEvent(new HoverEvent( HoverEvent.Action., new ComponentBuilder( "Visit the Spigot website!" ).create() ) );
        /*msg.text(format)
            .tooltip(
                MessageUtils.c(
                    MessageUtils.MAIN_COLOR
                        + "&oCoins: "
                        + MessageUtils.SECOND_COLOR
                        + core.coinHandler.getBalance(p.getUniqueId())),
                MessageUtils.c(
                    MessageUtils.MAIN_COLOR
                        + "&oKills: "
                        + MessageUtils.SECOND_COLOR
                        + core.statsHandler.getKills(p)),
                MessageUtils.c(
                    MessageUtils.MAIN_COLOR
                        + "&oWins: "
                        + MessageUtils.SECOND_COLOR
                        + core.statsHandler.getWins(p)))
            .then();
        msg.send(sp);*/

        //TODO fix the swear filter
        /*sp.sendMessage(
            core.perkHandler.getPerks(sp).contains("CHAT.MATURE") ? message : cleanMessage);*/

        sp.sendMessage(format + message);
      }
    } else {
      if (!(MuteHandler.getMuteExpire(p.getUniqueId()).equalsIgnoreCase("false")
              || MuteHandler.getMuteExpire(p.getUniqueId()).equalsIgnoreCase("true"))) {

        MessageUtils.sendMessage(
            p,
            "&4You have been muted. &cReason: &f&o"
                    + MuteHandler.getMuteReason(p.getUniqueId())
                + " &cExpire: &f&o"
                    + MuteHandler.getMuteExpire(p.getUniqueId()),
            true);
      } else {

        MessageUtils.sendMessage(
            p,
                "&4You have been muted. &cReason: &f&o" + MuteHandler.getMuteReason(p.getUniqueId()),
            true);
      }
    }
  }

  private String filteredChat(String message) {
    // https://dec0de.xyz/api/swearfilter/?message=fuck

    try {

      //TODO fix

      String response =
          WebRequestUtils.executePost(
              "http://nossl.dec0de.xyz/api/swearfilter/?message=" + message.replace(" ", "%20"),
              "");
      return response;

    } catch (Exception e) {
      System.out.println(e);
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
