package Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.github.fate0608.f3rdinand.JoinedPlayer;
import com.github.fate0608.f3rdinand.f3rdinand;

public class OnPlayerDeath implements Listener{
	
    private f3rdinand plugin;
    private Server s;

    public OnPlayerDeath(f3rdinand instance, Server server) {
        plugin = instance;
        s=server;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerDeathEvent event) {
    	String victim = event.getEntity().getDisplayName();
    	Player victimObj = event.getEntity();
    	Player killerObj = event.getEntity().getKiller();
    	
    	String killer = "";
    	
    	if(killerObj != null){
    		killer = killerObj.getDisplayName();
    	}else{
    		killer = "";
    	}
    	if(!killer.isEmpty()){
    		event.setDeathMessage(ChatColor.GOLD + victim + ChatColor.DARK_AQUA + " wurde von " +ChatColor.GOLD + killer + ChatColor.DARK_AQUA + " getötet!");
    		s.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
    			public void run(){
    				victimObj.kickPlayer(ChatColor.RED + "Du bist gestorben." + ChatColor.BOLD.RED + " Damit bist du aus Battlegrounds ausgeschieden!");
    			}
    		},5*20);
    		
    	}else{
    		event.setDeathMessage(ChatColor.GOLD + victim + ChatColor.DARK_AQUA + " wurde getötet!");
    		s.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
    			public void run(){
    				victimObj.kickPlayer(ChatColor.RED + "Du bist gestorben." + ChatColor.BOLD.RED + " Damit bist du aus Battlegrounds ausgeschieden!");
    			}
    		},5*20);
    	}
    }

}
