package com.thoughtcomplex.blit.ld21;

import java.io.*;
import javazoom.jl.player.*;
import javazoom.jl.decoder.JavaLayerException;

public class MusicPlayer implements Runnable {

	static String filename = "";
	static Player curPlayer = null;
	static boolean keepPlaying = true;
	static Thread playerThread = null;
	
	
	public MusicPlayer() {
	}

	public static void startPlaying(String new_filename) {
		keepPlaying = true;
		System.out.println("StartPlaying "+new_filename);
		filename = new_filename;
		if (playerThread!=null) {
			stopPlaying();
			playerThread.interrupt();
			try { playerThread.join(); } catch (InterruptedException e) {}
		}
		playerThread = new Thread(new MusicPlayer());
		playerThread.start();
	}
	
	public void run() {
		System.out.println("Run "+filename);
		try {
			while(keepPlaying) {
				curPlayer = new Player(new FileInputStream(filename));
				curPlayer.play();
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public static void stopPlaying() {
		System.out.println("StopPlaying "+filename);
		keepPlaying = false;
		if (curPlayer!=null) {
			curPlayer.close();
			curPlayer = null;
		}
	}

}