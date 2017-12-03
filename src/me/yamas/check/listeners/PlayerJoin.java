package me.yamas.check.listeners;

import me.yamas.check.Main;
import me.yamas.check.utils.Util;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener{
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		if(e.getPlayer().getName().equalsIgnoreCase("walkyGaming")){
			Util.sendMessage(e.getPlayer(), "&7Serwer uzywa twojego pluginu: &4YamasCheck v"+ Main.getInst().getDescription().getVersion());
		}
	}

}
