package net.timelegacy.tlcore.event;

import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.handler.Rank;
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

  private TLCore core = TLCore.getInstance();

  @EventHandler(priority = EventPriority.HIGHEST)
  public void commandPreProcessEvent(PlayerCommandPreprocessEvent event) {
    Player p = event.getPlayer();

    if (event.getMessage().toLowerCase().contains("/pl")
        || event.getMessage().toLowerCase().contains("/plugins")
        || event.getMessage().toLowerCase().contains("/?")
        || event.getMessage().toLowerCase().contains("/help")
        || event.getMessage().toLowerCase().contains("/bukkit:")) {
      event.setCancelled(true);

      Rank r = core.rankHandler.getRank(p.getName());

      if (r.getPriority() >= 9) {
        core.messageUtils.sendMessage(p, core.messageUtils.MAIN_COLOR + "&lPlugins", false);
        for (Plugin pp : Bukkit.getPluginManager().getPlugins()) {
          core.messageUtils.sendMessage(
              p,
              core.messageUtils.SECOND_COLOR
                  + pp.getName()
                  + " v"
                  + pp.getDescription().getVersion(),
              false);
        }
      } else {
        core.messageUtils.noPerm(p);
      }
    } else if (event.getMessage().toLowerCase().startsWith("/me ")) {
      event.setCancelled(true);
      core.messageUtils.noPerm(p);
    } else if (event.getMessage().toLowerCase().equals("/me")) {
      event.setCancelled(true);
      core.messageUtils.noPerm(p);
    }
  }

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent e) {
    Player p = e.getPlayer();

    e.setCancelled(true);

    String message =
        ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', e.getMessage()));

    String format =
        core.messageUtils.c(
            "&r"
                + core.rankHandler
                .chatColors(p.getName())
                .replace("%username%", p.getName())
                .replace("%line%", "\u2758 ")
                .replace("%arrows%", "\u00BB")
                + " &r");

    String cleanMessage = filteredChat(message);

    System.out.println("[CHAT] " + p.getName() + " > " + message);

    if (!core.muteHandler.isMuted(p.getName())) {

      for (Player sp : Bukkit.getOnlinePlayers()) {
        //TextComponent textComponent = new TextComponent(core.perkHandler.getPerks(sp).contains("CHAT.MATURE") ? message : cleanMessage);
        //textComponent.setHoverEvent(new HoverEvent( HoverEvent.Action., new ComponentBuilder( "Visit the Spigot website!" ).create() ) );
        /*msg.text(format)
            .tooltip(
                core.messageUtils.c(
                    core.messageUtils.MAIN_COLOR
                        + "&oCoins: "
                        + core.messageUtils.SECOND_COLOR
                        + core.coinHandler.getBalance(p.getName())),
                core.messageUtils.c(
                    core.messageUtils.MAIN_COLOR
                        + "&oKills: "
                        + core.messageUtils.SECOND_COLOR
                        + core.statsHandler.getKills(p)),
                core.messageUtils.c(
                    core.messageUtils.MAIN_COLOR
                        + "&oWins: "
                        + core.messageUtils.SECOND_COLOR
                        + core.statsHandler.getWins(p)))
            .then();
        msg.send(sp);*/

        sp.sendMessage(
            core.perkHandler.getPerks(sp).contains("CHAT.MATURE") ? message : cleanMessage);
      }
    } else {
      if (!(core.muteHandler.getMuteExpire(p.getName()).equalsIgnoreCase("false")
          || core.muteHandler.getMuteExpire(p.getName()).equalsIgnoreCase("true"))) {

        core.messageUtils.sendMessage(
            p,
            "&4You have been muted. &cReason: &f&o"
                + core.muteHandler.getMuteReason(p.getName())
                + " &cExpire: &f&o"
                + core.muteHandler.getMuteExpire(p.getName()),
            true);
      } else {

        core.messageUtils.sendMessage(
            p,
            "&4You have been muted. &cReason: &f&o" + core.muteHandler.getMuteReason(p.getName()),
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
