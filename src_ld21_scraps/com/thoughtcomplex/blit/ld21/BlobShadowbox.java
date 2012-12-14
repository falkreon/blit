package com.thoughtcomplex.blit.ld21;
import java.awt.*;

public class BlobShadowbox implements Shadowbox {

	int blocksWide = 80;
	int blocksHigh = 60;
	
	int numBlobs = 10;
	
	double[] blobX = new double[numBlobs];
	double[] blobY = new double[numBlobs];
	double[] blobvx = new double[numBlobs];
	double[] blobvy = new double[numBlobs];
	
	boolean iso = false;
	
	
	public Color color1 = new Color(70,10,30);
	public Color colora = color1;
	
	public Color color2 = new Color(192,140,182);
	public Color colorb = color2;

	public void init() {
		for(int i=0; i<numBlobs; i++) {
			blobX[i] = Math.random()*blocksWide;
			blobY[i] = Math.random()*blocksHigh;
			blobvx[i] = Math.random()*1 + 0.1; if (Math.random()<0.5) blobvx[i]= -blobvx[i];
			blobvy[i] = Math.random()*1 + 0.1; if (Math.random()<0.5) blobvy[i]= -blobvy[i];
		}
	}
	
	
	public void paint(Graphics g, int width, int height) {
		if (iso) {
			//calculate our poster colors ahead of time
			colora = weightedAverage(color1,color2,0.3);
			colorb = weightedAverage(color1,color2,0.6);
		}
	
		int blockWidth = (int)Math.ceil((double)width/(double)blocksWide);
		int blockHeight = (int)Math.ceil((double)height/(double)blocksHigh);
	
		for(int y=0; y<blocksHigh; y++) {
			for(int x=0; x<blocksWide; x++) {
				double r = 256;
				for(int i=0; i<numBlobs; i++) {
					double rlocal = (int)Math.sqrt(Math.abs(x-blobX[i])*Math.abs(x-blobX[i]) + Math.abs(y-blobY[i])*Math.abs(y-blobY[i]));
					if (rlocal<r) r=rlocal;
					//r-=(rlocal*0.5);
				}
				//r*=(numBlobs/4);
				r*=6;
				if (r>255) r=255; if (r<0) r=0;
				
				if (iso) {
					g.setColor(color1);
					if (r>220) g.setColor(colora);
					if (r>240) g.setColor(colorb);
					if (r>250) g.setColor(color2);
				
				
				} else {
					g.setColor(weightedAverage(color1,color2,1-(r/255)));
					//g.setColor(new Color((int)r,(int)r,(int)r));
				
				}
				
				g.fillRect(x*blockWidth,y*blockHeight,blockWidth,blockHeight);
			}
		}
	
		for(int i=0; i<numBlobs; i++) {
			//yay, blob physics!
			
			//blob gravity
			blobvy[i]+=0.027;

			//blob velocity
			blobX[i]+=blobvx[i];
			blobY[i]+=blobvy[i];
			
			//blob collisions (only with "walls", not each other)
			if (blobX[i]<-10) {
				blobX[i]=-10;
				blobvx[i] = Math.random()*1 + 0.1;
			}
			
			if (blobY[i]<-10) {
				blobY[i]=-10;
				blobvy[i] = Math.random()*1 + 0.1;
			}
			
			if (blobX[i]>=blocksWide+10) {
				blobX[i]=blocksWide+9;
				blobvx[i] = -( Math.random()*1 + 0.1);
			}
			
			if (blobY[i]>=blocksHigh+10) {
				blobY[i]=blocksHigh+9;
				blobvy[i] = -( Math.random()*1 + 0.1);
			}
		}
	
	
	}
	
	double weightedAverage(double a, double b, double percent) {
		return (a*(1-percent))+b*percent;
	}
	
	Color weightedAverage(Color a, Color b, double percent) {
		if (percent<0) return a; if (percent>=1) return b;
		int r1 = a.getRed(); int r2 = b.getRed();
		int rr = (int)weightedAverage(r1,r2,percent);
		
		int g1 = a.getGreen(); int g2 = b.getGreen();
		int gg = (int)weightedAverage(g1,g2,percent);
		
		int b1 = a.getBlue(); int b2 = b.getBlue();
		int bb = (int)weightedAverage(b1,b2,percent);
		
		return new Color(rr,gg,bb);
	}
	
	public void shutdown() {}



}