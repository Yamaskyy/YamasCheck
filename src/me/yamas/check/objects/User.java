package me.yamas.check.objects;

import java.util.UUID;

import me.yamas.check.objects.utils.UserUtils;

import org.bukkit.entity.Player;

public class User {
	
	private String name;
	private UUID uuid;
	private boolean isChecking;
	private boolean usingLobby;
	private boolean usingLobbyBack;
	
	public User(String name){
		this.name = name;
	}
	
	public User(Player player){
		this.name = player.getName();
		this.uuid = player.getUniqueId();
	}

	public String getName() {
		return name;
	}

	public UUID getUUID() {
		return uuid;
	}
	
	public boolean isChecking() {
		return isChecking;
	}
	
	public boolean getUsingLobby(){
		return usingLobby;
	}
	
	public boolean getUsingLobbyBack(){
		return usingLobbyBack;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}
	
	public void setChecking(boolean isChecking) {
		this.isChecking = isChecking;
	}
	
	public void setUsingLobby(boolean usingLobby) {
		this.usingLobby = usingLobby;
	}
	
	public void setUsingLobbyBack(boolean usingLobbyBack) {
		this.usingLobbyBack = usingLobbyBack;
	}

	public static User get(String name){
		for(User u : UserUtils.getUsers()){
			if(u.getName().equalsIgnoreCase(name)) return u;
		}
		return new User(name);
	}
}
