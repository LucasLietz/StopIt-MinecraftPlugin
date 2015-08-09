package com.github.fate0608.f3rdinand;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class f3rdinand extends JavaPlugin {
	
	@Override
	public void onEnable(){
		
		getServer().getPluginManager().registerEvents(new Listeners.OnPlayerJoin(this,getServer()),this);
		//getServer().getPluginManager().registerEvents(new Listeners.ArrowHitListener(), this);
		//getServer().getPluginManager().registerEvents(new Listeners.OnEntityKilled(), this);
		//getServer().getPluginManager().registerEvents(new Listeners.OnPlayerMove(), this);
		
		this.getLogger().info(ChatColor.GREEN + "Battlegrounds wurde erfolgreich aktiviert.");
	}

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        Player player = null;
        if(sender instanceof Player){
            player = (Player) sender;
        }

        if(cmd.getName().equalsIgnoreCase("testplug")){ 
            sender.sendMessage(ChatColor.RED + "Das Plugin funktioniert soweit, " + ChatColor.GREEN +  player.getDisplayName().toString() + ".");
            return true;
        } 
        return false;
    }
    

    
	@Override 
	public void onDisable(){

        this.getLogger().info(ChatColor.RED + "f3rdinand wurde deaktiviert.");
	}
	
}
