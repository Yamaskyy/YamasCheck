package me.yamas.check.commands;

import me.yamas.check.Main;
import me.yamas.check.objects.User;
import me.yamas.check.utils.Util;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class YamasCheckCmd implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			return false;
		}
		Player player = (Player) sender;
		FileConfiguration cfg = Main.getInst().getConfig();
		if(args.length == 0){
			if(player.hasPermission("yamascheck.admin")){
				Util.sendMessage(player, "&7Dostepne poprawne uzycia: &3/yamascheck&7:");
			//	Util.sendMessage(player, " &8> &3reload - &7Przeladowywuje plugin");
				Util.sendMessage(player, " &8> &3setlobby - &7Ustawia miejsce sprawdzarki");
				Util.sendMessage(player, " &8> &3setlobbyback - &7Ustawia miejsce powrotu z sprawdzarki");
				Util.sendMessage(player, " &8> &3lobbyusing &8[&3on&8|&3off&8] - &7Wlacza/wylacza teleportacje do sprawdzarki");
				Util.sendMessage(player, " &8> &3lobbybackusing &8[&3on&8|&3off&8] - &7Wlacza/wylacza teleportacje powrotna &4[TODO]");
				return false;
			} else {
				Util.sendMessage(player, "&8### &4YAMASCHECK &8###");
				Util.sendMessage(player, " &8> &aversion: &c" + Main.getInst().getDescription().getVersion());
				Util.sendMessage(player, " &8> &aby: &cYamasky");
				Util.sendMessage(player, " &8> &afor: &cRCDROP");
				Util.sendMessage(player, "&8### &4YAMASCHECK &8###");
				return false;
			}
		} else {
			if(args.length == 1){
				if(args[0].equalsIgnoreCase("setlobby")){
					if(!player.hasPermission("yamascheck.admin")){
						Util.sendMessage(player, "&cNie masz uprawnien do uzycia tej komendy &8(&7yamascheck.admin&8)");
						return false;
					}
					User u = new User(player);
					Location location = player.getLocation();
					IChatBaseComponent title = ChatSerializer.a("{\"text\":\"§4YamasCheck\"}");
					IChatBaseComponent subtitle = ChatSerializer.a("{\"text\":\"§7Sprawdzarka zostala ustawiona!\"}");
					PacketPlayOutTitle packetTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, title, cfg.getInt("title.time-on") * 20, cfg.getInt("title.time-is") * 20, cfg.getInt("title.time-off") * 20);
					PacketPlayOutTitle packetsubTitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitle, cfg.getInt("subtitle.time-on") * 20, cfg.getInt("subtitle.time-is") * 20, cfg.getInt("subtitle.time-off") * 20);
					u.setUsingLobby(true);
				//	cfg.set("lobby.using", u.getUsingLobby() ? true : false);
					cfg.set("lobby.world", location.getWorld().getName());
					cfg.set("lobby.x", location.getX());
					cfg.set("lobby.y", location.getY());
					cfg.set("lobby.z", location.getZ());
					cfg.set("lobby.yaw", location.getYaw());
					cfg.set("lobby.pitch", location.getPitch());
					Main.getInst().saveConfig();
					((CraftPlayer)player).getHandle().playerConnection.sendPacket(packetTitle);
					((CraftPlayer)player).getHandle().playerConnection.sendPacket(packetsubTitle);
				} else if(args[0].equalsIgnoreCase("setlobbyback")){
					if(!player.hasPermission("yamascheck.admin")){
						Util.sendMessage(player, "&cNie masz uprawnien do uzycia tej komendy &8(&7yamascheck.admin&8)");
						return false;
					}
					User u = new User(player);
					Location location = player.getLocation();
					IChatBaseComponent title = ChatSerializer.a("{\"text\":\"§4YamasCheck\"}");
					IChatBaseComponent subtitle = ChatSerializer.a("{\"text\":\"§7Miejsce powrotu zostalo ustawione!\"}");
					PacketPlayOutTitle packetTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, title, cfg.getInt("title.time-on") * 20, cfg.getInt("title.time-is") * 20, cfg.getInt("title.time-off") * 20);
					PacketPlayOutTitle packetsubTitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitle, cfg.getInt("subtitle.time-on") * 20, cfg.getInt("subtitle.time-is") * 20, cfg.getInt("subtitle.time-off") * 20);
					u.setUsingLobby(true);
				//	cfg.set("lobbyback.using", u.getUsingLobby() ? true : false);
					cfg.set("lobbyback.world", location.getWorld().getName());
					cfg.set("lobbyback.x", location.getX());
					cfg.set("lobbyback.y", location.getY());
					cfg.set("lobbyback.z", location.getZ());
					cfg.set("lobbyback.yaw", location.getYaw());
					cfg.set("lobbyback.pitch", location.getPitch());
					Main.getInst().saveConfig();
					((CraftPlayer)player).getHandle().playerConnection.sendPacket(packetTitle);
					((CraftPlayer)player).getHandle().playerConnection.sendPacket(packetsubTitle);
				}
			} else {
				if(args.length == 2){
					if(args[0].equalsIgnoreCase("lobbyusing")){
						if(!player.hasPermission("yamascheck.admin")){
							Util.sendMessage(player, "&cNie masz uprawnien do uzycia tej komendy &8(&7yamascheck.admin&8)");
							return false;
						}
						User u = new User(player);
						
						if(args[1].equalsIgnoreCase("on")){

							u.setUsingLobby(true);
							Util.sendMessage(player, "&7Pomyslnie &4wlaczyles &7teleportacje do sprawdzarki");
							return false;
						}
						
						if(args[1].equalsIgnoreCase("off")){

							u.setUsingLobby(false);
							Util.sendMessage(player, "&7Pomyslnie &4wylaczyles &7teleportacje do sprawdzarki");
							return false;
						}
						
						
						
					} else if(args[0].equalsIgnoreCase("lobbybackusing")){
						if(!player.hasPermission("yamascheck.admin")){
							Util.sendMessage(player, "&cNie masz uprawnien do uzycia tej komendy &8(&7yamascheck.admin&8)");
							return false;
						}
						User u = new User(player);
						
						if(args[1].equalsIgnoreCase("on")){

							u.setUsingLobbyBack(true);
							Util.sendMessage(player, "&7Pomyslnie &4wlaczyles &7teleportacje powrotna do ustawionego miejsca");
							return false;
						}
						
						if(args[1].equalsIgnoreCase("off")){

							u.setUsingLobbyBack(false);
							Util.sendMessage(player, "&7Pomyslnie &4wylaczyles &7teleportacje powrotna do ustawionego miejsca");
							return false;
						}
					}
				} else {
					Util.sendMessage(player, "&7Dostepne poprawne uzycia: &3/sprawdz&7:");
					Util.sendMessage(player, " &8> &3ss &8[&3nick&8] - &7Sprawdza gracza");
					Util.sendMessage(player, " &8> &3czysty &8[&3nick&8] - &7Sprawdza gracza");
					Util.sendMessage(player, " &8> &3lps &8[&3nick&8] - &7Banuje gracza za logout podczas sprawdzania");
					Util.sendMessage(player, " &8> &3cheaty &8[&3nick&8] - &7Banuje gracza za cheaty");
					Util.sendMessage(player, " &8> &3bwsp &8[&3nick&8] - &7Banuje gracza za brak wspolpracy");
					Util.sendMessage(player, " &8> &3przyznanie &8[&3nick&8] - &7Banuje gracza na 5 dni za brak wspolpracy");
					return false;
				}
			}
		}
		return false;
	}
}
