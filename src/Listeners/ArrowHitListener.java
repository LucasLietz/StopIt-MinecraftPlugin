package Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ArrowHitListener implements Listener {


	@EventHandler(priority = EventPriority.HIGHEST)
    public void onProjectileHit(ProjectileHitEvent e) {
    	Location locationHit = e.getEntity().getLocation();
    		Server _srv = e.getEntity().getServer();

    	Creeper creeper = (Creeper) _srv.getWorld("world").spawnEntity(locationHit,EntityType.CREEPER);
    	creeper.setPowered(true);
    	creeper.setCustomName(ChatColor.RED + "DEATH");
    	creeper.setCustomNameVisible(true);
    	creeper.setRemoveWhenFarAway(true);
    	creeper.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60 * 20, 3));
    	creeper.setLastDamage(100.00);
    }

}
