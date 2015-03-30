package com.github.fate0608.f3rdinand;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class f3rdinand extends JavaPlugin implements Listener{
	
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this,this);
		this.getLogger().info("&3f3rdinand wurde erfolgreich aktiviert.");
	}
	
	@Override 
	public void onDisable(){
		this.getLogger().info("&4f3rdinand wurde deaktiviert.");
	}
	
}
