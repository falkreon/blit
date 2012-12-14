package com.thoughtcomplex.blit;
public abstract class ControlDevice {

	public static void register() {};
	public String getName() { return "Null Device"; }
	public boolean isPressed(int key) { return false; }


}