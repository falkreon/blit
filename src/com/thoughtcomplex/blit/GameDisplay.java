package com.thoughtcomplex.blit;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class GameDisplay {
	/*
		GameDisplay: Visual Framework for Real-Time Interactive Simulations
		
		Indirection Layers: GameWindow (instance) -> GameDisplay (static) -> GameView (instance)
	
		GameWindow is a lightweight control which automatically engages a render loop and sends events to GameDisplay.
		GameDisplay (this class) is among other things, an abstraction layer which directs frame events to the current GameView. It also manages scaling, centering and
		clipping so that your visuals free-scale as the window resizes. The logical display will have the same aspect ratio as the physical display, but it will always have a fixed height. Vertical screen real-estate is much more valuable, and GUI elements can easily swing left or right to accommodate the space.
	*/

	public static GameView currentView = null;
	private static int physicalWidth = 1024;
	private static int physicalHeight = 768;
	private static int physicalXOffset = 0;
	private static int physicalYOffset = 0;
	private static int logicalWidth = 2;
	private static int logicalHeight = 2;
	private static double pixelRatio = 1.0;
	private static AffineTransform viewTransform = null;
	
	public static void updateSize(int wid, int hit, int xofs, int yofs) {
		if ((physicalWidth!=wid) | (physicalHeight!=hit)) {
			physicalWidth = wid; physicalHeight = hit;
			physicalXOffset = xofs; physicalYOffset = yofs;
			updateMetrics();
		} else {
			physicalWidth = wid; physicalHeight = hit;
			physicalXOffset = xofs; physicalYOffset = yofs;
		}
	}
	
	public static void updateMetrics() {
		if (currentView!=null) {
			//This is an easy calculation. The hard question is: How many *whole* pixels per pixel will take up the whole display height?
			logicalHeight = currentView.getLogicalHeight();
			pixelRatio = (double)physicalHeight/(double)logicalHeight;
			//Snap-to here so that we get a nice round ratio if we can.
			if ((pixelRatio<2.01) & (pixelRatio>2.0)) pixelRatio = 2.0;
			if ((pixelRatio<1.51) & (pixelRatio>1.5)) pixelRatio = 1.5;
			if ((pixelRatio<1.01) & (pixelRatio>1.0)) pixelRatio = 1.0;
			if (pixelRatio==0) pixelRatio=1;
			logicalWidth = (int)(physicalWidth/pixelRatio);
			currentView.setLogicalWidth(logicalWidth);
		} else {
			pixelRatio = 1.0;
		}
		
		viewTransform = AffineTransform.getScaleInstance(pixelRatio,pixelRatio);
		//find out how much leftover we have on the sides
		int widthLeftover = physicalWidth - (int)(logicalWidth*pixelRatio);
		int heightLeftover = physicalHeight - (int)(logicalHeight*pixelRatio);
		viewTransform.translate((int)(widthLeftover/2),(int)(heightLeftover/2));
	}
	
	public static void paint(Graphics2D g) {
		//setup the transform and clear the pane
		AffineTransform oldTransform = g.getTransform();
		g.translate(physicalXOffset,physicalYOffset);
		
		g.setColor(Color.BLACK);
		g.fillRect(0,0,physicalWidth,physicalHeight);
		
		g.transform(viewTransform);
	
		if (currentView!=null) {
			currentView.paint(g);
		} else {
			g.setColor(Color.BLUE);
			g.fillRect(0,0,physicalWidth,physicalHeight);
		}
		g.setTransform(oldTransform);
		g.setColor(Color.GREEN);
		g.drawString("Physical Dimensions: "+physicalWidth+"x"+physicalHeight+" / "+pixelRatio+" offset by "+physicalXOffset+"x"+physicalYOffset,100,50);
	}
	
	public static void setView(GameView newView) {
		if (currentView!=null) {
			//TODO: send a message to help tear down the current view
		}
		
		currentView = newView;
		updateMetrics();
	}

}