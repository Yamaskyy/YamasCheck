package me.yamas.check.listeners;

import me.yamas.check.Main;
import me.yamas.check.objects.User;
import me.yamas.check.utils.Util;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamage implements Listener{
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e){
		Player victim = (Player) e.getEntity();
		Player damager = (Player) e.getDamager();
		IChatBaseComponent titletodamager = ChatSerializer.a("{\"text\":\"" + Util.setHEX("&4Osoba jest sprawdzana") + "\"}");
		IChatBaseComponent subtitletodamager = ChatSerializer.a("{\"text\":\"" + Util.setHEX("&7Gracz " + victim.getName() + " jest sprawdzany!") + "\"}");
		PacketPlayOutTitle packetTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, titletodamager, Main.getInst().getConfig().getInt("title.time-on") * 20, Main.getInst().getConfig().getInt("title.time-is") * 20, Main.getInst().getConfig().getInt("title.time-off") * 20);
		PacketPlayOutTitle packetsubTitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitletodamager, Main.getInst().getConfig().getInt("subtitle.time-on") * 20, Main.getInst().getConfig().getInt("subtitle.time-is") * 20, Main.getInst().getConfig().getInt("subtitle.time-off") * 20);
		if(victim instanceof Player){
			if(damager instanceof Player){
				User user = User.get(victim.getName());
				if(user.isChecking()){
					e.setCancelled(true);
					((CraftPlayer)damager).getHandle().playerConnection.sendPacket(packetTitle);
					((CraftPlayer)damager).getHandle().playerConnection.sendPacket(packetsubTitle);
				}
			}
		}
	}
}
