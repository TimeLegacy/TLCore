package net.timelegacy.tlcore.event;

import net.timelegacy.tlcore.TLCore;
import org.bukkit.Effect;
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

  private TLCore core = TLCore.getInstance();

  private void hideAdvancementsFor(World world) {
    world.setGameRuleValue("announceAdvancements", "false");
  }

  /* Hide advancements for new worlds */
  @EventHandler
  public void onWorldLoad(WorldLoadEvent e) {
    hideAdvancementsFor(e.getWorld());
  }

  @EventHandler
  public void onBlockFromTo(BlockFromToEvent event) {
    if (!core.physics) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockIgnite(BlockIgniteEvent event) {
    if (!core.physics) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockBurn(BlockBurnEvent event) {
    if (!core.physics) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockPhysics(BlockPhysicsEvent event) {
    if (!core.physics) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onLeavesDecay(LeavesDecayEvent event) {
    if (!core.physics) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockForm(BlockFormEvent event) {
    if (!core.physics) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onEntityBlockForm(EntityBlockFormEvent event) {
    if (!core.physics) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void EntityChangeBlockEvent(EntityChangeBlockEvent event) {
    if (!core.physics) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockSpread(BlockSpreadEvent event) {
    if (!core.physics) {
      event.setCancelled(true);
    }
  }

  @SuppressWarnings("deprecation")
  @EventHandler
  public void onExplode(EntityExplodeEvent event) {
    if (!core.physics) {
      for (Block block : event.blockList()) {
        block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
      }

      event.setYield(0);
      event.setCancelled(true);
    }
  }
}
