package me.yamas.check.commands;

import me.yamas.check.Main;
import me.yamas.check.data.FileManager;
import me.yamas.check.objects.User;
import me.yamas.check.objects.utils.UserUtils;
import me.yamas.check.utils.Util;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SprawdzCmd implements CommandExecutor{
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			return false;
		}
		final Player player = (Player) sender;
		if(!player.hasPermission("yamascheck.cmd.sprawdz")){
			Util.sendMessage(player, "&cNie masz uprawnien do uzycia tej komendy &8(&7yamascheck.cmd.sprawdz&8)");
			return false;
		}
		FileConfiguration cfg = Main.getInst().getConfig();
		if(args.length == 0){ // -> /spr ss [nick]
			Util.sendMessage(player, "&7Dostepne poprawne uzycia: &3/sprawdz&7:");
			Util.sendMessage(player, " &8> &3ss &8[&3nick&8] - &7Sprawdza gracza");
			Util.sendMessage(player, " &8> &3czysty &8[&3nick&8] - &7Sprawdza gracza");
			Util.sendMessage(player, " &8> &3lps &8[&3nick&8] - &7Banuje gracza za logout podczas sprawdzania");
			Util.sendMessage(player, " &8> &3cheaty &8[&3nick&8] - &7Banuje gracza za cheaty");
			Util.sendMessage(player, " &8> &3bwsp &8[&3nick&8] - &7Banuje gracza za brak wspolpracy");
			Util.sendMessage(player, " &8> &3przyznanie &8[&3nick&8] - &7Banuje gracza na 5 dni za brak wspolpracy");
			return false;
		} else {
			if(args.length >= 2){
				final Player target = Bukkit.getPlayer(args[1]);
				if(target == null){
					Util.sendMessage(player, "&4Blad: &cPodany gracz jest offline!");
					return false;
				}
				final User u = User.get(target.getName());
				if(args[0].equalsIgnoreCase("ss")){
					if(u.isChecking()){
						Util.sendMessage(player, "&4Blad: &cGracz " + u.getName() + " jest juz sprawdzany!");
						return false;
					}
					u.setChecking(true);
					UserUtils.addUser(u);
					if(cfg.getBoolean("messages.titleandsubtitle")){
						ss(target, player, u, 1);
					}
					if(cfg.getBoolean("messages.actionbar")){
						ss(target, player, u, 2);
					}
					if(cfg.getBoolean("messages.chat")){
						ss(target, player, u, 3);
					}
					if(cfg.getBoolean("lobby.using")){
						Location lobbyLoc = new Location(Bukkit.getWorld(cfg.getString("lobby.world")),
								cfg.getDouble("lobby.x"),
								cfg.getDouble("lobby.y"),
								cfg.getDouble("lobby.z"));
						player.teleport(lobbyLoc);
						target.teleport(lobbyLoc);
					} else {
						if(!cfg.getBoolean("lobby.using")){
							player.teleport(target.getLocation());
						}
					}
					IChatBaseComponent actionbar = ChatSerializer.a("{\"text\":\"" + Util.fixColor(FileManager.getMessages().getString("actionbar.allowed-cmds")) + "\"}");
					final PacketPlayOutChat packetActionbar = new PacketPlayOutChat(actionbar, (byte) 2);
					Bukkit.getScheduler().runTaskLater(Main.getInst(), new Runnable() {
						
						@Override
						public void run() {
							Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getInst(), new Runnable() {
								
								@Override
								public void run() {
									((CraftPlayer)target).getHandle().playerConnection.sendPacket(packetActionbar);
									ss(target, player, u, 1);
								}
							}, 0L, 5*20);
							
						}
					}, 5*20);
				} else if(args[0].equalsIgnoreCase("czysty")){
					if(!u.isChecking()){
						Util.sendMessage(player, "&4Blad: &cGracz " + u.getName() + " nie jest sprawdzany!");
						return false;
					}
					u.setChecking(false);
					UserUtils.removeUser(u);
					if(cfg.getBoolean("messages.titleandsubtitle")){
						cz(target, player, u, 1);
					}
					if(cfg.getBoolean("messages.actionbar")){
						cz(target, player, u, 2);
					}
					if(cfg.getBoolean("messages.chat")){
						cz(target, player, u, 3);
					}
					Bukkit.getScheduler().cancelAllTasks();
					if(cfg.getBoolean("lobby.using")){
						Location lobbyLoc = new Location(Bukkit.getWorld(cfg.getString("lobbyback.world")),
								cfg.getDouble("lobbyback.x"),
								cfg.getDouble("lobbyback.y"),
								cfg.getDouble("lobbyback.z"));
						player.teleport(lobbyLoc);
						target.teleport(lobbyLoc);
					} else {
						if(!cfg.getBoolean("lobby.using")){
							player.teleport(target.getLocation());
						}
					}
				} else if(args[0].equalsIgnoreCase("lps")){
					if(!u.isChecking()){
						Util.sendMessage(player, "&4Blad: &cGracz " + u.getName() + " nie jest sprawdzany!");
						return false;
					}
					if(target.hasPermission("yamascheck.bypass")){
						Util.sendMessage(player, "&4Blad: &cGracz " + u.getName() + " jest nie karalny!");
						return false;
					}
					
					if(u.getName().equals(player.getName())){
						Util.sendMessage(player, "&4Blad: &cNie mozesz zbanowac samego siebie!");
						return false;
					}
					u.setChecking(false);
					Bukkit.getServer().dispatchCommand(player, "ban " + target.getName() + " logout podczas sprawdzania");
					Location lobbyLoc = new Location(Bukkit.getWorld(cfg.getString("lobbyback.world")),
							cfg.getDouble("lobbyback.x"),
							cfg.getDouble("lobbyback.y"),
							cfg.getDouble("lobbyback.z"));
					Bukkit.getScheduler().cancelAllTasks();;
					player.teleport(lobbyLoc);
				} else if(args[0].equalsIgnoreCase("cheaty")){
					if(!u.isChecking()){
						Util.sendMessage(player, "&4Blad: &cGracz " + u.getName() + " nie jest sprawdzany!");
						return false;
					}
					if(target.hasPermission("yamascheck.bypass")){
						Util.sendMessage(player, "&4Blad: &cGracz " + u.getName() + " jest nie karalny!");
						return false;
					}
					
					if(u.getName().equals(player.getName())){
						Util.sendMessage(player, "&4Blad: &cNie mozesz zbanowac samego siebie!");
						return false;
					}
					u.setChecking(false);
					Bukkit.getServer().dispatchCommand(player, "ban " + target.getName() + " Cheaty");
					Location lobbyLoc = new Location(Bukkit.getWorld(cfg.getString("lobbyback.world")),
							cfg.getDouble("lobbyback.x"),
							cfg.getDouble("lobbyback.y"),
							cfg.getDouble("lobbyback.z"));
					Bukkit.getScheduler().cancelAllTasks();
					player.teleport(lobbyLoc);
				} else if(args[0].equalsIgnoreCase("bwsp")){
					if(!u.isChecking()){
						Util.sendMessage(player, "&4Blad: &cGracz " + u.getName() + " nie jest sprawdzany!");
						return false;
					}
					if(target.hasPermission("yamascheck.bypass")){
						Util.sendMessage(player, "&4Blad: &cGracz " + u.getName() + " jest nie karalny!");
						return false;
					}
					
					if(u.getName().equals(player.getName())){
						Util.sendMessage(player, "&4Blad: &cNie mozesz zbanowac samego siebie!");
						return false;
					}
					u.setChecking(false);
					Bukkit.getServer().dispatchCommand(player, "ban " + target.getName() + " Brak wspolpracy");
					Location lobbyLoc = new Location(Bukkit.getWorld(cfg.getString("lobbyback.world")),
							cfg.getDouble("lobbyback.x"),
							cfg.getDouble("lobbyback.y"),
							cfg.getDouble("lobbyback.z"));
					Bukkit.getScheduler().cancelAllTasks();
					player.teleport(lobbyLoc);
				} else if(args[0].equalsIgnoreCase("przyznanie")){
					if(!u.isChecking()){
						Util.sendMessage(player, "&4Blad: &cGracz " + u.getName() + " nie jest sprawdzany!");
						return false;
					}
					if(target.hasPermission("yamascheck.bypass")){
						Util.sendMessage(player, "&4Blad: &cGracz " + u.getName() + " jest nie karalny!");
						return false;
					}
					
					if(u.getName().equals(player.getName())){
						Util.sendMessage(player, "&4Blad: &cNie mozesz zbanowac samego siebie!");
						return false;
					}
					u.setChecking(false);
					Bukkit.getServer().dispatchCommand(player, "tempban " + target.getName() + " 5 days Przyznanie do cheatow");
					Location lobbyLoc = new Location(Bukkit.getWorld(cfg.getString("lobbyback.world")),
							cfg.getDouble("lobbyback.x"),
							cfg.getDouble("lobbyback.y"),
							cfg.getDouble("lobbyback.z"));
					Bukkit.getScheduler().cancelAllTasks();
					player.teleport(lobbyLoc);
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
		return false;
	}

	public void ss(Player toSend, Player admin, User victim, int type){
		FileConfiguration cfg = Main.getInst().getConfig();
		FileConfiguration msg = FileManager.getMessages();
		switch (type) {
		case 1:
			IChatBaseComponent title = ChatSerializer.a("{\"text\":\"" + Util.setHEX(msg.getString("title.ss.message")
					.replace("{PLAYER}", victim.getName())
					.replace("{ADMIN}", admin.getName())
					.replace("{DATE}", Integer.toString(cfg.getInt("max-time-checking")))) + "\"}");
			IChatBaseComponent subtitle = ChatSerializer.a("{\"text\":\"" + Util.setHEX(msg.getString("subtitle.ss.message")
					.replace("{PLAYER}", victim.getName())
					.replace("{ADMIN}", admin.getName())
					.replace("{DATE}", Integer.toString(cfg.getInt("max-time-checking")))) + "\"}");
			PacketPlayOutTitle packetTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, title, cfg.getInt("title.time-on") * 20, cfg.getInt("title.time-is") * 20, cfg.getInt("title.time-off") * 20);
			PacketPlayOutTitle packetsubTitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitle, cfg.getInt("subtitle.time-on") * 20, cfg.getInt("subtitle.time-is") * 20, cfg.getInt("subtitle.time-off") * 20);
			((CraftPlayer)toSend).getHandle().playerConnection.sendPacket(packetTitle);
			((CraftPlayer)toSend).getHandle().playerConnection.sendPacket(packetsubTitle);
			break;
		case 2:
			IChatBaseComponent actionbar = ChatSerializer.a("{\"text\":\"" + Util.fixColor(msg.getString("actionbar.ss.message")) + "\"}");
			PacketPlayOutChat packetActionbar = new PacketPlayOutChat(actionbar, (byte) 2);
			((CraftPlayer)toSend).getHandle().playerConnection.sendPacket(packetActionbar);
			break;
		case 3:
			for(String string : msg.getStringList("chat.ss.message")){
				Util.broadcast(string
						.replace("{PLAYER}", victim.getName())
						.replace("{ADMIN}", admin.getName())
						.replace("{DATE}", Integer.toString(cfg.getInt("max-time-checking"))));
			}
			break;
		default:
			for(String string : msg.getStringList("chat.ss.message")){
				Util.broadcast(string
						.replace("{PLAYER}", victim.getName())
						.replace("{ADMIN}", admin.getName())
						.replace("{DATE}", Integer.toString(cfg.getInt("max-time-checking"))));
			}
			break;
		}
	}
	public void cz(Player toSend, Player admin, User victim, int type){
		FileConfiguration cfg = Main.getInst().getConfig();
		FileConfiguration msg = FileManager.getMessages();
		switch (type) {
		case 1:
			IChatBaseComponent title = ChatSerializer.a("{\"text\":\"" + Util.setHEX(msg.getString("title.cz.message")
					.replace("{PLAYER}", victim.getName())
					.replace("{ADMIN}", admin.getName())) + "\"}");
			IChatBaseComponent subtitle = ChatSerializer.a("{\"text\":\"" + Util.setHEX(msg.getString("subtitle.cz.message")
					.replace("{PLAYER}", victim.getName())
					.replace("{ADMIN}", admin.getName())) + "\"}");
			PacketPlayOutTitle packetTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, title, cfg.getInt("title.time-on"), cfg.getInt("title.time-is"), cfg.getInt("title.time-off"));
			PacketPlayOutTitle packetsubTitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitle, cfg.getInt("subtitle.time-on"), cfg.getInt("subtitle.time-is"), cfg.getInt("subtitle.time-off"));
			((CraftPlayer)toSend).getHandle().playerConnection.sendPacket(packetTitle);
			((CraftPlayer)toSend).getHandle().playerConnection.sendPacket(packetsubTitle);
			break;
		case 2:
			IChatBaseComponent actionbar = ChatSerializer.a("{\"text\":\"" + Util.setHEX(msg.getString("actionbar.cz.message")) + "\"}");
			PacketPlayOutChat packetActionbar = new PacketPlayOutChat(actionbar, (byte) 2);
			((CraftPlayer)toSend).getHandle().playerConnection.sendPacket(packetActionbar);
			break;
		case 3:
			for(String string : msg.getStringList("chat.cz.message")){
				Util.broadcast(string
						.replace("{PLAYER}", victim.getName())
						.replace("{ADMIN}", admin.getName()));
			}
			break;
		default:
			for(String string : msg.getStringList("chat.cz.message")){
				Util.broadcast(string
						.replace("{PLAYER}", victim.getName())
						.replace("{ADMIN}", admin.getName()));
			}
			break;
		}
	}
}
