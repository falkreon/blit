package com.thoughtcomplex.blit;

public class ActivatorEntry {
	public String activator = "null";
	public String device = "Keyboard"; 
	public int key = 1;
	
	public ActivatorEntry(String newActivator, String newDevice, int newKey) {
		activator=newActivator;
		device=newDevice;
		key=newKey;
	}
	
	public boolean equals(ActivatorEntry a) {
		if (!this.activator.equalsIgnoreCase(a.activator)) return false;
		if (!this.device.equalsIgnoreCase(a.device)) return false;
		if (key!=a.key) return false;
		
		return true;
	}
	
}