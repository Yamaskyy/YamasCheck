package me.yamas.check.objects.utils;

import java.util.ArrayList;
import java.util.List;

import me.yamas.check.objects.User;

public class UserUtils {
	
	private static List<User> users = new ArrayList<User>();
	
	public static void addUser(User user){
		users.add(user);
	}
	
	public static void removeUser(User user){
		users.remove(user);
	}
	
	public static List<User> getUsers(){
		return new ArrayList<User>(users);
	}

}
