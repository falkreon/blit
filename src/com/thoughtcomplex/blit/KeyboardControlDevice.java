package com.thoughtcomplex.blit;
import java.awt.*;
import java.awt.event.*;

public class KeyboardControlDevice extends ControlDevice implements KeyListener {

	private static boolean[] keys = new boolean[400]; //400 keys should be enough for anyon- err nevermind.

	private static KeyboardControlDevice singleton = null;
	
	protected static KeyboardControlDevice getSingleton() {
		if (singleton==null) singleton = new KeyboardControlDevice();
		return singleton;
	}
	
	public String getName() { return "Keyboard"; }
	public boolean isPressed(int key) {
		if (key>=keys.length) return false;
		if (key<0) return false;
		return keys[key];
	}

	public static void registerKeyboard(Component c) {
		c.addKeyListener(getSingleton());
	}
	
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode<0) return;
		if (keyCode>=keys.length) return;
		keys[keyCode]=true;
	}
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode<0) return;
		if (keyCode>=keys.length) return;
		keys[keyCode]=false;
	}
	
	public void keyTyped(KeyEvent e) {}

}