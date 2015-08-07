package Listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnPlayerMove implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) {
	    Player player = event.getPlayer();
	    Location to = event.getTo();
	    Location from = event.getFrom();

	    
        Block oldBlock = from.getWorld().getBlockAt(from.getBlockX(), from.getBlockY() - 1, from.getBlockZ());
        Block newBlock = to.getWorld().getBlockAt(to.getBlockX(), to.getBlockY() - 1, to.getBlockZ());
        if(newBlock.getType() != Material.AIR){
        	newBlock.setType(Material.TNT);
        }
	    
	}
	 

}
