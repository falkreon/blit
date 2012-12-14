package com.thoughtcomplex.blit.ld21;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javazoom.jl.player.*;
import javazoom.jl.decoder.JavaLayerException;

public class test {
	public static void main(String[] args) {
		try {
			Player p = new Player(new FileInputStream("title.mp3"));
			p.play();
			System.out.println("Played.");
		} catch (Exception e) { e.printStackTrace(); }
	}

}