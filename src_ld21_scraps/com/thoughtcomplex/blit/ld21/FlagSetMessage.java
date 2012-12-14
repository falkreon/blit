package com.thoughtcomplex.blit.ld21;
import com.thoughtcomplex.blit.GameWindow;

public class FlagSetMessage extends Message {
	String key = "";
	boolean value = true;

	public FlagSetMessage(String flagName) {
		super("Set "+flagName);
		key = flagName; value = true;
	}
	
	public FlagSetMessage(String flagName, boolean newValue) {
		super("Set "+flagName+" to "+newValue);
		
		key=flagName;
		value = newValue;
	}
	
	public void execute(Mob player, Room rm, GameWindow gw) {
		GameFlags.putFlag(key,value);
	}

}