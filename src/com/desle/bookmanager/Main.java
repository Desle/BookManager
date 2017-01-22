package com.desle.bookmanager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	
	@Override
	public void onEnable() {
	}
	
	@Override
	public void onDisable() {
		
	}	
	
	
	@EventHandler
	public void onLogout(PlayerQuitEvent e) {
		BookManager.get(e.getPlayer()).destroy();
	}
}
