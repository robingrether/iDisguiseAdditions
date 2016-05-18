package de.robingrether.idisguise.additions;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import de.robingrether.idisguise.management.DisguiseManager;

public class EventListener implements Listener {
	
	private Additions plugin;
	
	public EventListener(Additions plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
		if(event.getTarget() instanceof Player) {
			Player target = (Player)event.getTarget();
			if(plugin.getConfiguration().DISABLE_MOB_TARGET && DisguiseManager.getInstance().isDisguised(target)) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if(event.isCancelled()) return;
		Entity damagee = event.getEntity();
		Entity damager = event.getDamager();
		if(damagee instanceof Player && damager instanceof Player) {
			if(plugin.getConfiguration().UNDISGUISE_PVP) {
				if(DisguiseManager.getInstance().isDisguised((Player)damagee)) {
					DisguiseManager.getInstance().undisguise((Player)damagee);
					((Player)damagee).sendMessage(ChatColor.GOLD + "You were undisguised because PvP is not allowed while you are disguised.");
				}
				if(DisguiseManager.getInstance().isDisguised((Player)damager)) {
					DisguiseManager.getInstance().undisguise((Player)damager);
					((Player)damager).sendMessage(ChatColor.GOLD + "You were undisguised because PvP is not allowed while you are disguised.");
				}
			}
		} else if(damagee instanceof Player) {
			if(DisguiseManager.getInstance().isDisguised((Player)damagee)) {
				if(plugin.getConfiguration().DISABLE_MOB_DAMAGE) {
					event.setCancelled(true);
				} else {
					if(plugin.getConfiguration().UNDISGUISE_PVE) {
						DisguiseManager.getInstance().undisguise((Player)damagee);
						((Player)damagee).sendMessage(ChatColor.GOLD + "You were undisguised because PvE is not allowed while you are disguised.");
					}
					if(plugin.getConfiguration().UNDISGUISE_PROJECTILE && damager instanceof Projectile) {
						DisguiseManager.getInstance().undisguise((Player)damagee);
						((Player)damagee).sendMessage(ChatColor.GOLD + "You were undisguised because you were hit by a projectile.");
					}
				}
			}
		} else if(damager instanceof Player) {
			if(plugin.getConfiguration().UNDISGUISE_PVE) {
				if(DisguiseManager.getInstance().isDisguised((Player)damager)) {
					DisguiseManager.getInstance().undisguise((Player)damager);
					((Player)damager).sendMessage(ChatColor.GOLD + "You were undisguised because PvE is not allowed while you are disguised.");
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if(plugin.getConfiguration().DISABLE_ITEM_PICK_UP && DisguiseManager.getInstance().isDisguised(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
}