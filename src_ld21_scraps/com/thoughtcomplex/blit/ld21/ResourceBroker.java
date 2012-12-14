package com.thoughtcomplex.blit.ld21;
import java.awt.*;
import java.awt.image.*;
import javax.swing.JButton;
import java.io.File;
import javax.sound.sampled.*;

public class ResourceBroker {
	public static JButton observer = new JButton("DUMB THINGS JAVA REQUIRES");
	//public static Image player = null;
	public static Font dialogueFont = new Font("Courier",Font.BOLD,24);
	
	
	public static void load() {
		
	
	
	}
	
	public static Image loadImage(String imageName) {
		MediaTracker mt = new MediaTracker(observer);
		Image ret = Toolkit.getDefaultToolkit().getImage(imageName);
		mt.addImage(ret,0);
		try { mt.waitForAll(); } catch (Exception e) {}
		return ret;
	}
	
	public static Clip loadSample(String sampleName) {
		try {
			AudioInputStream in = AudioSystem.getAudioInputStream(new File(sampleName));
			Clip c = AudioSystem.getClip();
			c.open(in);
			return c;
		} catch (Exception e) { return null; }
	}
	
	public static Image getFlipped(Image im) {
		BufferedImage ret = new BufferedImage(im.getWidth(observer),im.getHeight(observer),BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = ret.getGraphics();
		g.drawImage(im,im.getWidth(observer),0,-im.getWidth(observer),im.getHeight(observer),observer);
		g.dispose();
		return ret;
	}

	public static void playSound(Clip c) {
		if (c==null) return;
		c.stop();
		c.setFramePosition(0);
		c.start();
	}
	
}