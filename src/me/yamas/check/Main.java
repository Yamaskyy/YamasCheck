package me.yamas.check;

import me.yamas.check.commands.SprawdzCmd;
import me.yamas.check.commands.YamasCheckCmd;
import me.yamas.check.data.FileManager;
import me.yamas.check.listeners.BlockBreak;
import me.yamas.check.listeners.BlockPlace;
import me.yamas.check.listeners.CommandUse;
import me.yamas.check.listeners.ItemDrop;
import me.yamas.check.listeners.PlayerChat;
import me.yamas.check.listeners.PlayerJoin;
import me.yamas.check.listeners.PlayerQuit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	
	private static Main inst;
	
	public Main() {
		inst = this; 
	}
	
	public void onEnable(){
		FileManager.checkFiles();
		getCommand("sprawdz").setExecutor(new SprawdzCmd());
		getCommand("yamascheck").setExecutor(new YamasCheckCmd());
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new BlockBreak(), this);
		pm.registerEvents(new BlockPlace(), this);
		pm.registerEvents(new CommandUse(), this);
		pm.registerEvents(new ItemDrop(), this);
		pm.registerEvents(new PlayerQuit(), this);
		pm.registerEvents(new PlayerChat(), this);
		pm.registerEvents(new PlayerJoin(), this);
	//	pm.registerEvents(new EntityDamage(), this);
	}
	
	public void onDisable(){
		saveConfig();
	}
	
	public static Main getInst(){
		if(inst == null) return new Main();
		return inst;
	}

}
