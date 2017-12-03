package me.yamas.check.listeners;

import me.yamas.check.data.FileManager;
import me.yamas.check.objects.User;
import me.yamas.check.utils.Util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener{
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		Player player = e.getPlayer();
		YamlConfiguration msg = FileManager.getMessages();
		User u = new User(player);
		User user = User.get(u.getName());
		if(user.isChecking()){
			e.setFormat(Util.setHEX(msg.getString("format-chat")
					.replace("{PLAYER}", u.getName())
					.replace("{MESSAGE}", e.getMessage())));
		}
	}
}
