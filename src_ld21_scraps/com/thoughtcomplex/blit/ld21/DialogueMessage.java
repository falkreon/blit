package com.thoughtcomplex.blit.ld21;

import java.awt.*;

public class DialogueMessage extends Message {
	
	Image faceImage = null;
	
	public DialogueMessage(String text, Image newFaceImage) {
		super(text);
		messageString = text;
		faceImage=newFaceImage;
		delay = 300;
	}
	
	public void execute(Mob player, Room rm, GameWindow gw) {
		gw.showMessage(this);
	}


}