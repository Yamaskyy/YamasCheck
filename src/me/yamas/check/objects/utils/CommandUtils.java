package me.yamas.check.objects.utils;

import java.util.ArrayList;
import java.util.List;

public class CommandUtils {
	
	private static List<String> commands = new ArrayList<String>();
	
	public static void addCommand(String command){
		commands.add(command);
	}
	
	public static void removeCommand(String command){
		commands.remove(command);
	}
	
	public static List<String> getCommands(){
		return new ArrayList<String>(commands);
	}

}
