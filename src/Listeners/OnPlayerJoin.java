package Listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoin implements Listener{

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerJoinEvent event) {
    	
    	Player player = event.getPlayer();
    	event.setJoinMessage("[" + ChatColor.GREEN + "+" + ChatColor.RESET + "] " + event.getPlayer().getDisplayName());
    	player.setCustomName("[0] " + event.getPlayer().getDisplayName());
    	player.setCustomNameVisible(true);
    }
}
