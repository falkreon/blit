package com.thoughtcomplex.blit.ld21;
import java.util.Hashtable;

public class GameFlags {

	private static Hashtable<String,Boolean> values = new Hashtable<String,Boolean>();

	private GameFlags() {}
	
	public static boolean getFlag(String key) {
		Boolean b = values.get(key);
		if (b==null) return false;
		return b;
	}
	
	public static void putFlag(String key, boolean value) {
		values.put(key,value);
		save();
	}
	
	public static void setFlag(String key) {
		values.put(key,true);
		save();
	}
	
	public static void save() {
		//DO NOT SAVE RIGHT NOW
	}
	
}