package me.yamas.check.listeners;

import me.yamas.check.Main;
import me.yamas.check.data.FileManager;
import me.yamas.check.objects.User;
import me.yamas.check.utils.Util;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener{
	
	IChatBaseComponent title = ChatSerializer.a("{\"text\":\"" + Util.setHEX(FileManager.getMessages().getString("title.ss.message")) + ":\"}");
	IChatBaseComponent subtitle = ChatSerializer.a("{\"text\":\"" + Util.setHEX(FileManager.getMessages().getString("subtitle.cant-break")) + "\"}");
	IChatBaseComponent actionbar = ChatSerializer.a("{\"text\":\"" + Util.fixColor(FileManager.getMessages().getString("actionbar.allowed-cmds")) + "\"}");
	PacketPlayOutTitle packetTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, title, Main.getInst().getConfig().getInt("title.time-on") * 20, Main.getInst().getConfig().getInt("title.time-is") * 20, Main.getInst().getConfig().getInt("title.time-off") * 20);
	PacketPlayOutTitle packetsubTitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitle, Main.getInst().getConfig().getInt("subtitle.time-on") * 20, Main.getInst().getConfig().getInt("subtitle.time-is") * 20, Main.getInst().getConfig().getInt("subtitle.time-off") * 20);
	PacketPlayOutChat packetActionbar = new PacketPlayOutChat(actionbar, (byte) 2);
	
	@EventHandler
	public void onBreak(final BlockBreakEvent e){
		User u = User.get(e.getPlayer().getName());
		if(u.isChecking()){
			if(!e.getPlayer().hasPermission("yamascheck.bypass")){

				((CraftPlayer)e.getPlayer()).getHandle().playerConnection.sendPacket(packetTitle);
				((CraftPlayer)e.getPlayer()).getHandle().playerConnection.sendPacket(packetsubTitle);
				Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInst(), new Runnable() {
					
					@Override
					public void run() {
						((CraftPlayer)e.getPlayer()).getHandle().playerConnection.sendPacket(packetActionbar);
					}
				}, 0L, 5*20);
				e.setCancelled(true);
				
			}
		}
	}
}
