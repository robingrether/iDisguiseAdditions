package de.robingrether.idisguise.additions;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import de.robingrether.idisguise.iDisguise;
import de.robingrether.idisguise.api.DisguiseAPI;
import de.robingrether.idisguise.api.DisguiseEvent;
import de.robingrether.idisguise.api.UndisguiseEvent;
import de.robingrether.idisguise.disguise.PlayerDisguise;

public class EventListener implements Listener {
	
	private Additions plugin;
	private DisguiseAPI api;
	
	public EventListener(Additions plugin) {
		this.plugin = plugin;
		this.api = iDisguise.getInstance().getAPI();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
		if(event.getTarget() instanceof Player) {
			Player target = (Player)event.getTarget();
			if(plugin.getConfiguration().DISABLE_MOB_TARGET && api.isDisguised((OfflinePlayer)target)) {
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
				if(api.isDisguisedTo((Player)damagee, (Player)damager)) {
					api.undisguise((Player)damagee, false);
					((Player)damagee).sendMessage(plugin.getLanguage().UNDISGUISE_PVP);
				}
				if(api.isDisguisedTo((Player)damager, (Player)damagee)) {
					api.undisguise((Player)damager, false);
					((Player)damager).sendMessage(plugin.getLanguage().UNDISGUISE_PVP);
				}
			}
		} else if(damagee instanceof Player) {
			if(api.isDisguised((OfflinePlayer)damagee)) {
				if(plugin.getConfiguration().DISABLE_MOB_DAMAGE) {
					event.setCancelled(true);
				} else {
					if(plugin.getConfiguration().UNDISGUISE_PVE) {
						api.undisguise((Player)damagee, false);
						((Player)damagee).sendMessage(plugin.getLanguage().UNDISGUISE_PVE);
					}
					if(plugin.getConfiguration().UNDISGUISE_PROJECTILE && damager instanceof Projectile) {
						api.undisguise((Player)damagee, false);
						((Player)damagee).sendMessage(plugin.getLanguage().UNDISGUISE_PROJECTILE);
					}
				}
			}
		} else if(damager instanceof Player) {
			if(plugin.getConfiguration().UNDISGUISE_PVE) {
				if(api.isDisguised((OfflinePlayer)damager)) {
					api.undisguise((Player)damager, false);
					((Player)damager).sendMessage(plugin.getLanguage().UNDISGUISE_PVE);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityPickupItem(EntityPickupItemEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		if(plugin.getConfiguration().DISABLE_ITEM_PICK_UP && api.isDisguised((OfflinePlayer)event.getEntity())) {
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
		if(plugin.getConfiguration().MODIFY_DISPLAY_NAME && api.getDisguise((OfflinePlayer)event.getPlayer()) instanceof PlayerDisguise) {
			event.getPlayer().setDisplayName(event.getPlayer().getName());
		}
	}
	
}