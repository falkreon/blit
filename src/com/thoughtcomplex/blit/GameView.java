package com.thoughtcomplex.blit;
import java.awt.*;
import javax.swing.*;

public class GameView {


	/* logicalWidth and logicalHeight: Logical screen dimensions are a negotiation between GameDisplay and GameView. GameView has the final say on height,
	and gameDisplay takes that information and informs the View how wide the screen must be to accomodate that without black bars on the sides. */
	protected int logicalWidth = 640;
	protected int logicalHeight = 480;

	//therefore we provide an accessor for logicalHeight, but a mutator for logicalWidth.
	public int getLogicalHeight() { return logicalHeight; }
	public void setLogicalWidth(int newWidth) { logicalWidth = newWidth; }

	
	public int tempAnim = 0;
	public int lock = 0;
	
	
	public void paint(Graphics2D g) {
		g.setColor(new Color(128,64,255));
		g.fillRect(0,0,logicalWidth,logicalHeight);
		g.setColor(Color.BLUE);
		g.fillRect(8,8,logicalWidth-16,logicalHeight-16);
		g.setColor(Color.WHITE);
		g.drawString("LOGICAL SIZE: "+logicalWidth+"x"+logicalHeight,100,100);
		
		Image spritesheet = ResourceBroker.loadImage("player.png");
		Image[] playerTiles = ResourceBroker.diceImage(spritesheet,64,64);
		g.drawImage(playerTiles[tempAnim+4],128,128, new JButton());
		
		lock++;
		if (lock>10) {
			lock=0; tempAnim++; if (tempAnim>=8) tempAnim=0;
		}
	}


}