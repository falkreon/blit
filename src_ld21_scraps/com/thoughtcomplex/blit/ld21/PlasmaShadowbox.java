package com.thoughtcomplex.blit.ld21;
import java.awt.*;

public class PlasmaShadowbox implements Shadowbox {

	int blocksWide = 60;
	int blocksHigh = 60;

	int numPhases = 3;
	
	double[] xphase = new double[numPhases];
	double[] xspeed = new double[numPhases];
	double[] xamp = new double[numPhases];
	double[] yphase = new double[numPhases];
	double[] yspeed = new double[numPhases];
	double[] yamp = new double[numPhases];
	
	public Color color1 = new Color(160,192,255);
	public Color color2 = new Color(10,50,70);

	public void init() {
		for(int i=0; i<numPhases; i++) {
			xphase[i] = Math.random()*6.28;
			xspeed[i] = Math.random()*0.01+0.001;
			if (Math.random()<0.5) xspeed[i] = -xspeed[i];
			xamp[i] = Math.random()*0.09+0.01;
			yphase[i] = Math.random()*6.28;
			yspeed[i] = Math.random()*0.01+0.001;
			yamp[i] = Math.random()*0.09+0.01;
			if (Math.random()<0.5) yspeed[i] = -yspeed[i];
		}
	}
	
	public void paint(Graphics g, int width, int height) {
		//find bar width
		int barWidth = (int)Math.ceil((double)width/(double)blocksWide);
		int barHeight = (int)Math.ceil((double)height/(double)blocksHigh);
	
		for(int y=0; y<blocksHigh; y++) {
			for(int x=0; x<blocksWide; x++) {
				long avgStrength = 0;
				for(int i=0; i<numPhases; i++) {
					long xPhaseStrength = (int)Math.abs(Math.sin((x*xamp[i])+xphase[i])*255);
					if (xPhaseStrength>255) xPhaseStrength=255;
					
					long yPhaseStrength = (int)Math.abs(Math.sin((y*yamp[i])+yphase[i])*255);
					if (yPhaseStrength>255) yPhaseStrength=255;
					
					long pointStrength = (xPhaseStrength+yPhaseStrength) >> 1;
					avgStrength+=pointStrength;
				}
				avgStrength = avgStrength / numPhases;
				if (avgStrength<0) avgStrength=0;
				if (avgStrength>255) avgStrength=255;
				
				int r1 = color1.getRed(); int r2 = color2.getRed();
				int rr = (int)weightedAverage(r1,r2,((double)avgStrength)/255.0);
				
				int g1 = color1.getGreen(); int g2 = color2.getGreen();
				int gg = (int)weightedAverage(g1,g2,((double)avgStrength)/255.0);
				
				int b1 = color1.getBlue(); int b2 = color2.getBlue();
				int bb = (int)weightedAverage(b1,b2,((double)avgStrength)/255.0);
				
				g.setColor(new Color(rr,gg,bb));
				g.fillRect(x*barWidth,y*barHeight,barWidth,barHeight);
			}
		}
		
		for(int i=0; i<numPhases; i++) {
			xphase[i]+=xspeed[i]; if (xphase[i]>6.28) xphase[i]-=6.28; if (xphase[i]<0) xphase[i]+=6.28;
			yphase[i]+=yspeed[i]; if (yphase[i]>6.28) yphase[i]-=6.28; if (yphase[i]<0) yphase[i]+=6.28;
			
			
			//drift
			xspeed[i]+=Math.random()*0.001-0.0005; if (xspeed[i]<-0.1) xspeed[i]=-0.1; if (xspeed[i]>0.1) xspeed[i] = 0.1;
			yspeed[i]+=Math.random()*0.001-0.0005; if (yspeed[i]<-0.1) yspeed[i]=-0.1; if (yspeed[i]>0.1) yspeed[i] = 0.1;
		}

	}
	
	double weightedAverage(double a, double b, double percent) {
		return (a*(1-percent))+b*percent;
	}
	
	public void shutdown() {}


}