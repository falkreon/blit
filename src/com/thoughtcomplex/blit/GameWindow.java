package com.thoughtcomplex.blit;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameWindow extends JFrame implements WindowListener {
	public GameWindow() {
		this.setSize(new Dimension(1024,768));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addWindowListener(this);
		//this.setUndecorated(true);
	}
	
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	
	public void windowActivated(WindowEvent e) {
	}
	public void windowDeactivated(WindowEvent e) {}
	
	public void windowOpened(WindowEvent e) {
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
	}
	
	public void windowClosing(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	
	
	public void paint(Graphics q) {
		updateDisplay();
		this.repaint();
	}
	
	public void updateDisplay() {
		Component content = this.getContentPane();
		GameDisplay.updateSize(content.getWidth(),content.getHeight(), getInsets().left, getInsets().top);
		this.setMinimumSize(new Dimension(480+getInsets().left+getInsets().right,480+getInsets().top+getInsets().bottom));
	
		if (this.getBufferStrategy()==null) this.createBufferStrategy(2);
		Graphics g = this.getBufferStrategy().getDrawGraphics();
		if (g instanceof Graphics2D) {
			GameDisplay.paint((Graphics2D)g);
		}
		this.getBufferStrategy().show();
	}

}