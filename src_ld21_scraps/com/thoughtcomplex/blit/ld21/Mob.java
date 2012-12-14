package com.thoughtcomplex.blit.ld21;

import java.awt.*;
import javax.swing.*;
import javax.sound.sampled.Clip;

public class Mob {

	public double x = 0.0;
	public double y = 0.0;
	public double vx = 0.0;
	public double vy = 0.0;
	public int width = 8;
	public int height = 16;
	
	public int health = 6;
	public int maxHealth = 6;
	public int invincibility = 0;
	public boolean isPlayer = false;
	public Clip hurtSound = ResourceBroker.loadSample("hurt.wav");
	
	public Color c = Color.BLUE;
	Image curImage = null;
	JButton LAME = new JButton("WHY SO CRUEL, AWT?");

	public void paint(Graphics g, int blockSize, Mob camera) {
		if (curImage!=null) {
			//g.setColor(c);
			//g.fillRect(((int)x)*blockSize, ((int)y)*blockSize, blockSize*width, blockSize*height );
			g.drawImage( curImage, ((int)x)*blockSize-(int)camera.x, ((int)y)*blockSize-(int)camera.y, blockSize*width, blockSize*height , LAME);
		} else {
			g.setColor(c);
			g.fillRect(((int)x)*blockSize-(int)camera.x, ((int)y)*blockSize-(int)camera.y, blockSize*width, blockSize*height );
		}
	}
	
	public void ai(Room curRoom, Mob player) {
		//this is the player. do nothing.
		if (invincibility>0) invincibility--;
	}
	
	public boolean contains(int qx, int qy) {
		if (qx<x) return false;
		if (qy<y) return false;
		if (qx>=x+width) return false;
		if (qy>=y+width) return false;
		return true;
	}
	
	public boolean collides(Mob m) {
		//rough, fast, short-circuit collision detect
		if (contains((int)m.x,(int)m.y)) return true;
		if (contains((int)m.x+m.width-1,(int)m.y)) return true;
		if (contains((int)m.x,(int)m.y+m.height-1)) return true;
		if (contains((int)m.x+m.width-1,(int)m.y+m.height-1)) return true;
		
		if (m.contains((int)x,(int)y)) return true;
		if (m.contains((int)x+width-1,(int)y)) return true;
		if (m.contains((int)x,(int)y+height-1)) return true;
		if (m.contains((int)x+width-1,(int)y+height-1)) return true;
		
		return false;
	}
	
	public void onCollide(Mob m) {
	}
	
	public void onDie(Room curRoom, Mob player, GameWindow gw) {
		gw.explode((int)x,(int)y,20,2,64,Color.RED,Color.YELLOW);
	}
	
	public void onDamage(int numDamage) {
		if (invincibility==-1) return; //indefinite invincibility.
		if (invincibility>0) return; //temporary invincibility.
		
		
		if (health>0) {
			health-= numDamage;
			invincibility=10; //I don't know. Maybe?
			ResourceBroker.playSound(hurtSound);
		}
	}
}