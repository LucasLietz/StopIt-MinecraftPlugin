package Listeners;


import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class OnEntityKilled implements Listener{
	
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDeath(EntityDeathEvent event) {
    	String name = event.getEntity().getName();
    	Player killer = event.getEntity().getKiller();
    	Server srv = event.getEntity().getServer();
    	
    	srv.broadcastMessage(ChatColor.GOLD + killer.getDisplayName() + ChatColor.DARK_AQUA + " hat einen " + ChatColor.RED + name + ChatColor.DARK_AQUA + " gekillt!");
    	
    	String kills = killer.getCustomName().substring(killer.getCustomName().indexOf("[")+1, killer.getCustomName().indexOf("]"));
    	int k = Integer.parseInt(kills);
    	k++;
    	
    	event.getEntity().getServer().broadcastMessage(ChatColor.GOLD + killer.getDisplayName() + "'s Kills:" + ChatColor.DARK_AQUA + k);
    	killer.setCustomName("[" + k +"] " + killer.getDisplayName());
    	
    	srv.dispatchCommand(killer,"worldborder set 5 10");
    	
    }

}
