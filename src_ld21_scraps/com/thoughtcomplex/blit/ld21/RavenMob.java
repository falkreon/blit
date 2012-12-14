package com.thoughtcomplex.blit.ld21;

import java.awt.Color;
import java.awt.Image;

public class RavenMob extends Mob {
	int jumpCharge = 5;
	boolean isJumping = false;
	int timer = (int)(Math.random()*300)+300;
	boolean isCharging = false;
	Image leftImage = ResourceBroker.loadImage("hvitravnur.png");
	Image rightImage = ResourceBroker.getFlipped(leftImage);
	
	public RavenMob() {
		this.width=16; this.height=16;
		this.maxHealth=2; this.health=2;
		this.jumpCharge = (int)(Math.random()*5);
		curImage = rightImage;
	}
	
	public void ai(Room curRoom, Mob player) {
		super.ai(curRoom,player);
		
		if (isCharging & (player.health>0)) {
		
			if (!isJumping) {
				if (player.x<this.x) vx=-1;
				if (player.x>this.x) vx=+1;
			}
			
		} else {
			if (Math.random()<0.01) {
				if (Math.random()<0.5) vx=-0.8; else vx=0.8;
			}
		}
		
		timer--; if (timer<=0) {
			timer = (int)(Math.random()*300)+100;
			if (isCharging) timer+=200;
			isCharging=!isCharging;
			jumpCharge=0; isJumping=false;
			if (Math.random()<0.5) vx=-0.8; else vx=0.8;
		}
		
		//both charge and retreat involve lots of jumping
		if (isJumping) {
			this.vy = -3;
			jumpCharge--;
			if (jumpCharge<=0) { isJumping=false; }
		} else {
			if (!curRoom.canMoveDown(this)) {
				jumpCharge = Math.min(5,jumpCharge+1);
				
				if ((Math.random()<0.04) & (jumpCharge>=5)) {
					isJumping=true;
				}
			}
		}
		
		if (vx>0) curImage=rightImage;
		if (vx<0) curImage=leftImage;
	}
	
	public void onCollide(Mob m) {
		if (m.isPlayer) m.onDamage(1);
	}
	
	public void onDamage(int numDamage) {
		super.onDamage(numDamage);
		vx=0;
	}
	
	public void onDie(Room curRoom, Mob player, GameWindow gw) {
		super.onDie(curRoom,player,gw);
		gw.explode((int)x,(int)y,40,2,64,Color.RED,Color.GRAY);
	}
}