package Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import net.minecraft.server.v1_8_R3.EntityPlayer;

public class OnPlayerJoin implements Listener{

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerJoinEvent event) {
    	
    	Server srv = event.getPlayer().getServer();
    	int players = srv.getOnlinePlayers().size();
    	srv.broadcastMessage(""+players);
    	int bordersize = players * 100;
    	srv.broadcastMessage(""+bordersize);
    	Player player = event.getPlayer();
    	event.setJoinMessage("[" + ChatColor.GREEN + "+" + ChatColor.RESET + "] " + event.getPlayer().getDisplayName());
    	player.setCustomName("[0] " + event.getPlayer().getDisplayName());
    	player.setCustomNameVisible(true);
    	srv.dispatchCommand(srv.getConsoleSender(),"worldborder center -14.5 -35.5");
    	srv.dispatchCommand(srv.getConsoleSender(),"worldborder set " + bordersize + " 30");
    	
    }
}
