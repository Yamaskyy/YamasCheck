package me.yamas.check.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Util {

	public static String setHEX(String arg0){
		return ChatColor.translateAlternateColorCodes('&', arg0);
	}
	
	public static String fixColor(String arg0){
		return arg0.replace('&', '§');
	}
	
	public static boolean sendMessage(CommandSender sender, String string){
		if((string != null) || (string != " ")){
			sender.sendMessage(setHEX(string));
		}
		return false;
	}
	
	public static boolean broadcast(String string){
		if((string != null) || (string != " ")){
			Bukkit.getServer().broadcastMessage(setHEX(string));
		}
		return false;
	}
}
