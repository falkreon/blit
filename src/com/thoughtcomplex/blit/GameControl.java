package com.thoughtcomplex.blit;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Vector;

public class GameControl {
	/*
		This class represents keybords, touchpads, joysticks and/or gamepads, or some other device that can generate inputs which are either "on" or "off. It also
		represents a mapping from these buttons or keys to logical activators such as "jump" or "right". This is a many-to-one mapping, so that more than one key
		could map to the same activator; for instance, both VK_D and VK_RIGHT could be mapped to the activator "right".
	
		In order to function, this class needs two things up front: The host frame (or JFrame) of the application must add the GameControl singleton with
		addKeyListener, and mappings must be loaded into the class either manually in the program code or from a key mapping file.
	
		After setup is done, the class can be statically queried for the state of a key for convenience.
	*/
	
	private static Vector<ActivatorEntry> activatorEntries = new Vector<ActivatorEntry>();
	private static Vector<ControlDevice> devices = new Vector<ControlDevice>(); //TODO: Populate this list.
	
	private static GameControl singleton = null;
	
	private GameControl() {
		//hard-code some activatorEntries for convenience.
		
		//arrow keys
		activatorEntries.add(new ActivatorEntry("Left","Keyboard",KeyEvent.VK_LEFT));
		activatorEntries.add(new ActivatorEntry("Right","Keyboard",KeyEvent.VK_RIGHT));
		activatorEntries.add(new ActivatorEntry("Up","Keyboard",KeyEvent.VK_UP));
		activatorEntries.add(new ActivatorEntry("Down","Keyboard",KeyEvent.VK_DOWN));
		
		//WASD keys
		activatorEntries.add(new ActivatorEntry("Left","Keyboard",KeyEvent.VK_A));
		activatorEntries.add(new ActivatorEntry("Right","Keyboard",KeyEvent.VK_D));
		activatorEntries.add(new ActivatorEntry("Up","Keyboard",KeyEvent.VK_W));
		activatorEntries.add(new ActivatorEntry("Down","Keyboard",KeyEvent.VK_S));
	}
	
	public static GameControl getSingleton() {
		if (singleton==null) singleton = new GameControl();
		return singleton;
	}
	
	public boolean getActivatorInternal(String activatorName) {
		for(ActivatorEntry a : activatorEntries) {
			if (a.activator.equalsIgnoreCase(activatorName)) {
				//find out what device this activator goes to
				for(ControlDevice b : devices) {
					if (a.device.equalsIgnoreCase(b.getName())) {
						if (b.isPressed(a.key)) return true; //if false keep checking
					}
				}
			}
			
		}
		
		return false;
	}
	
	public static boolean getActivator(String activatorName) {
		return getSingleton().getActivatorInternal(activatorName);
	}
	
	public void mapActivator(String activatorName, String device, int key) {
		activatorEntries.add(new ActivatorEntry(activatorName, device, key));
	}
	
	public static void registerKeyboard(Component c) {
		KeyboardControlDevice.registerKeyboard(c);
		devices.add(KeyboardControlDevice.getSingleton());
	}
}