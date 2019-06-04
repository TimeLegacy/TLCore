package net.timelegacy.tlcore.event;

import net.timelegacy.tlcore.TLCore;
import org.bukkit.Effect;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class PhysicsEvents implements Listener {

  private static TLCore plugin = TLCore.getPlugin();

  private static void hideAdvancementsFor(World world) {
    world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
  }

  /* Hide advancements for new worlds */
  @EventHandler
  public void onWorldLoad(WorldLoadEvent event) {
    hideAdvancementsFor(event.getWorld());
  }

  @EventHandler
  public void onBlockFromTo(BlockFromToEvent event) {
    if (!plugin.physics) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockIgnite(BlockIgniteEvent event) {
    if (!plugin.physics) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockBurn(BlockBurnEvent event) {
    if (!plugin.physics) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockPhysics(BlockPhysicsEvent event) {
    if (!plugin.physics) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onLeavesDecay(LeavesDecayEvent event) {
    if (!plugin.physics) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockForm(BlockFormEvent event) {
    if (!plugin.physics) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onEntityBlockForm(EntityBlockFormEvent event) {
    if (!plugin.physics) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void EntityChangeBlockEvent(EntityChangeBlockEvent event) {
    if (!plugin.physics) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockSpread(BlockSpreadEvent event) {
    if (!plugin.physics) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onExplode(EntityExplodeEvent event) {
    if (!plugin.physics) {
      for (Block block : event.blockList()) {
        block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
      }

      event.setYield(0);
      event.setCancelled(true);
    }
  }
}
