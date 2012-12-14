package com.thoughtcomplex.blit.ld21;
import java.awt.Color;

public class Bullet {
	public boolean solid = true;
	public boolean playerOwned = true;
	public double x = 0;
	public double y = 0;
	public double vx = 0;
	public double vy = 0;
	public Color color = Color.YELLOW;
	public int size = 2;
	public int lifetime = -1;
	
	public Bullet() {}
	
	public Bullet(double qx, double qy, double qvx, double qvy) {
		x=qx; y=qy; vx=qvx; vy=qvy;
	}
	
}