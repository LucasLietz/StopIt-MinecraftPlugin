package Listeners;

import org.apache.logging.log4j.core.appender.db.jpa.JPAAppender;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.github.fate0608.f3rdinand.JoinedPlayer;
import com.github.fate0608.f3rdinand.f3rdinand;

public class OnPlayerJoin implements Listener{
	
    private f3rdinand plugin;
    private Server s;
    private JoinedPlayer _jp;

    public OnPlayerJoin(f3rdinand instance, Server server) {
        plugin = instance;
        s=server;
    }
	


	private boolean canceled = false;
	private int invincibleTime = 20;
	

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerJoinEvent event) {
    	JoinedPlayer jp = new JoinedPlayer();
    	jp.invincible = 20;
    	jp.player = event.getPlayer();

    	event.setJoinMessage(ChatColor.GOLD + jp.player.getDisplayName() + ChatColor.DARK_AQUA + " hat den Server betreten. Er ist in " + invincibleTime + " Sekunden angreifbar!");  	
    	Server s = event.getPlayer().getServer();
    	startScheduler(jp);
    	
    	
    }
    
    public void startScheduler(JoinedPlayer jp){
    	jp.taskId = s.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){

			public void run(){
    			s.broadcastMessage("" + jp.invincible);
    			if(jp.invincible <= 0){
    				s.broadcastMessage(ChatColor.GOLD + jp.player.getDisplayName() + ChatColor.DARK_AQUA + " ist jetzt angreifbar!");
    			}else{
    				s.broadcastMessage(ChatColor.GOLD + jp.player.getDisplayName() + ChatColor.DARK_AQUA + " ist in " + jp.invincible + " Sekunden angreifbar!");
    			}

    			if(jp.invincible <= 0){
    				//set canceled
    				s.broadcastMessage(jp.player.getDisplayName() + " " + jp.taskId);
    				cancelScheduler(jp);
    			}
    			jp.invincible -=5;
    		}
    	}, 0, 5*20);
    }
    
    public void cancelScheduler(JoinedPlayer tId){
    	s.getScheduler().cancelTask(tId.taskId);	
    }
    
    @EventHandler(priority=EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent bbe)
    {
    	if(!canceled){
    		bbe.setCancelled(true);
    	}
    	
    }
    
    @EventHandler(priority=EventPriority.NORMAL)
    public void move(PlayerMoveEvent move, JoinedPlayer jp)
    {
		 if(!canceled){
				        Location from=move.getFrom();
				        Location to=move.getTo();
				        double x=Math.floor(from.getX());
				        double z=Math.floor(from.getZ());
				        if(Math.floor(to.getX())!=x||Math.floor(to.getZ())!=z)
				        {
				            x+=.5;
				            z+=.5;
				            move.getPlayer().teleport(new Location(from.getWorld(),x,from.getY(),z,from.getYaw(),from.getPitch()));
				        }
		    		}
    }
    
    @EventHandler(priority=EventPriority.NORMAL)
    public void move(PlayerItemDamageEvent damage)
    {
    	if(!canceled){
        	Player player = damage.getPlayer();
        	damage.setCancelled(true);
        	player.sendMessage(ChatColor.RED + "Die Schutzzeit läuft noch! Du kannst nicht angreifen oder angegriffen werden!");
    	}
    }
}
