package com.thoughtcomplex.blit;
import java.awt.*;
import java.awt.image.*;

public class ResourceBroker {

	protected static Component observer = new javax.swing.JButton("WHY JAVA, WHY?");

	public static Image loadImage(String imageName) {
		MediaTracker mt = new MediaTracker(observer);
		Image ret = Toolkit.getDefaultToolkit().getImage(imageName);
		mt.addImage(ret,0);
		try { mt.waitForAll(); } catch (Exception e) {}
		return ret;
	}
	
	public static Image getFlipped(Image im) {
		BufferedImage ret = new BufferedImage(im.getWidth(observer),im.getHeight(observer),BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = ret.getGraphics();
		g.drawImage(im,im.getWidth(observer),0,-im.getWidth(observer),im.getHeight(observer),observer);
		g.dispose();
		return ret;
	}
	
	public static Image[] diceImage(Image im, int tileWidth, int tileHeight) {
		int tilesWide = im.getWidth(observer)/tileWidth;
		int tilesHigh = im.getHeight(observer)/tileHeight;
		int curTileNum = 0;
		Image[] result = new Image[tilesWide*tilesHigh];
		for(int y=0; y<tilesHigh; y++) {
			for(int x=0; x<tilesWide; x++) {
				BufferedImage curTile = new BufferedImage(tileWidth,tileHeight,BufferedImage.TYPE_4BYTE_ABGR);
				Graphics g = curTile.getGraphics();
				g.drawImage(im,-x*tileWidth,-y*tileHeight,im.getWidth(observer),im.getHeight(observer),observer);
				g.dispose();
				result[curTileNum]=curTile;
				curTileNum++;
			}
		}
		return result;
	}
	
}