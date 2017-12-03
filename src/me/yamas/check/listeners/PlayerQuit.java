package me.yamas.check.listeners;

import me.yamas.check.data.FileManager;
import me.yamas.check.objects.User;
import me.yamas.check.utils.Util;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerQuit implements Listener{
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		Player player = e.getPlayer();
		User u = new User(player);
		YamlConfiguration msg = FileManager.getMessages();
		User user = User.get(u.getName());
		if(user.isChecking()){
			player.getInventory().clear();
			player.getInventory().setHelmet(new ItemStack(Material.AIR));
			player.getInventory().setChestplate(new ItemStack(Material.AIR));
			player.getInventory().setLeggings(new ItemStack(Material.AIR));
			player.getInventory().setBoots(new ItemStack(Material.AIR));
			user.setChecking(false);
			player.setHealth(0L);
			Util.broadcast(msg.getString("quit-is-checking").replace("{PLAYER}", u.getName()));
		}
	}

}
