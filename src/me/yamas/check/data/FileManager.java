package me.yamas.check.data;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import me.yamas.check.Main;

public class FileManager {

	private static File mainDir = Main.getInst().getDataFolder();
	private static File cfg = new File(mainDir, "config.yml");
	private static File msgFile = new File(mainDir, "messages.yml");
	private static YamlConfiguration msg;
	
	public static void checkFiles(){
		if(!mainDir.exists()) mainDir.mkdir();
		if(!cfg.exists()) Main.getInst().saveDefaultConfig();
		if(!msgFile.exists()) Main.getInst().saveResource("messages.yml", true);
		msg = YamlConfiguration.loadConfiguration(msgFile);
	}
	
	public static YamlConfiguration getMessages(){
		return msg;
	}
}
