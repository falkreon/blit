package com.thoughtcomplex.blit;
import com.thoughtcomplex.blit.ld21.GameWindow;

public class Game {
	public static void main(String [] args) {
		GameWindow mainWindow = new GameWindow();
		GameControl.registerKeyboard(mainWindow);
		GameDisplay.setView(new GameView());
		mainWindow.setVisible(true);
	}



}