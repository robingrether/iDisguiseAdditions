package de.robingrether.idisguise.additions;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import de.robingrether.idisguise.api.DisguiseEvent;
import de.robingrether.idisguise.api.UndisguiseEvent;
import de.robingrether.idisguise.disguise.PlayerDisguise;
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
					((Player)damagee).sendMessage(plugin.getLanguage().UNDISGUISE_PVP);
				}
				if(DisguiseManager.getInstance().isDisguised((Player)damager)) {
					DisguiseManager.getInstance().undisguise((Player)damager);
					((Player)damager).sendMessage(plugin.getLanguage().UNDISGUISE_PVP);
				}
			}
		} else if(damagee instanceof Player) {
			if(DisguiseManager.getInstance().isDisguised((Player)damagee)) {
				if(plugin.getConfiguration().DISABLE_MOB_DAMAGE) {
					event.setCancelled(true);
				} else {
					if(plugin.getConfiguration().UNDISGUISE_PVE) {
						DisguiseManager.getInstance().undisguise((Player)damagee);
						((Player)damagee).sendMessage(plugin.getLanguage().UNDISGUISE_PVE);
					}
					if(plugin.getConfiguration().UNDISGUISE_PROJECTILE && damager instanceof Projectile) {
						DisguiseManager.getInstance().undisguise((Player)damagee);
						((Player)damagee).sendMessage(plugin.getLanguage().UNDISGUISE_PROJECTILE);
					}
				}
			}
		} else if(damager instanceof Player) {
			if(plugin.getConfiguration().UNDISGUISE_PVE) {
				if(DisguiseManager.getInstance().isDisguised((Player)damager)) {
					DisguiseManager.getInstance().undisguise((Player)damager);
					((Player)damager).sendMessage(plugin.getLanguage().UNDISGUISE_PVE);
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
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDisguise(DisguiseEvent event) {
		if(plugin.getConfiguration().MODIFY_DISPLAY_NAME && event.getDisguise() instanceof PlayerDisguise) {
			event.getPlayer().setDisplayName(((PlayerDisguise)event.getDisguise()).getDisplayName());
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onUndisguise(UndisguiseEvent event) {	
		if(plugin.getConfiguration().MODIFY_DISPLAY_NAME && event.getDisguise() instanceof PlayerDisguise) {
			event.getPlayer().setDisplayName(event.getPlayer().getName());
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if(plugin.getConfiguration().MODIFY_DISPLAY_NAME && DisguiseManager.getInstance().getDisguise(event.getPlayer()) instanceof PlayerDisguise) {
			event.getPlayer().setDisplayName(event.getPlayer().getName());
		}
	}
	
}