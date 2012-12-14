package com.thoughtcomplex.blit.ld21;
import java.awt.*;

public class StarfieldShadowbox implements Shadowbox {

	int maxStars = 80;

	int[] starX = new int[maxStars];
	int[] starY = new int[maxStars];
	int[] vx = new int[maxStars];
	int[] vy = new int[maxStars];
	
	
	
	Color[] starColor = new Color[maxStars];

	public void init() {
		for(int i=0; i<maxStars; i++) {
			starX[i] = -999;
			starY[i] = -999;
			starColor[i] = Color.WHITE;
		}
	}
	
	public void paint(Graphics g, int width, int height) {
		for(int i=0; i<maxStars; i++) {
			//INIT BASED ON WIDTH/HEIGHT
			if ((starX[i]==-999) | (starY[i]==-999)) initStar(i,width,height);
			
			//ACTUAL PAINT CODE
			int scale = width/128;
			g.setColor(starColor[i]);
			g.fillRect(starX[i],starY[i],scale,scale);
			
			
			//physics-motion
			starX[i]+=vx[i]; starY[i]+=vy[i];
			
			//physics-limits
			if (starX[i]<0) bootStar(i, width, height, width-1);
			if (starX[i]>width) bootStar(i, width, height, 0);
			if (starY[i]<0) { initStar(i,width,height); starY[i] = height-1; }
			if (starY[i]>height) { initStar(i, width, height); starY[i] = 0; }
			
			
			
		}
	
	
	}
	
	public void initStar(int num, int width, int height) {
		int xLoc = (int)(Math.random()*(width-1));
		bootStar(num,width,height,xLoc);
	}
	
	public void bootStar(int num, int width, int height, int x) {
		float maxSpeed = (int)(width/128);
		starX[num] = x;
		starY[num] = (int)(Math.random()*(height-10));
		
		vx[num] = (int) ( Math.random()*maxSpeed )+1;
		vy[num] = 0;
		
		float starIntensity = vx[num]/maxSpeed; if (starIntensity>1.0) starIntensity=1.0F;
		
		starColor[num] = new Color(starIntensity*0.6F,starIntensity*0.9F,starIntensity*1.0F);
	}
	
	
	
	public void shutdown() {}

}