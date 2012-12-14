package com.thoughtcomplex.blit.ld21;
public class Camera extends Mob {

	public boolean xScroll = false;
	public boolean yScroll = false;

	/**
	*	Follow: Causes this Camera to try to center the given Mob on the screen
	*	@curRoom		The Room currently being displayed and simulated
	*	@curPlayer		The Mob representing the player or Mob to be followed, which is presumed to be inside the room.
	*	@screenWidth	The current width of the screen or window
	*	@screenHeight	The current height of the screen or window
	*	@pixelSize		The game internally operates at a very low resolution. This is how many real pixels it takes to make an internal pixel.
	*	@tileSize		The size of a tile in internal pixels. This should actually be a constant 8 but included here for idiot-proofing.
	**/
	public void follow(Room curRoom, Mob curPlayer, int screenWidth, int screenHeight, int pixelSize, int tileSize) {
		int roomPixelWidth = curRoom.width*tileSize*pixelSize;
	
		if (roomPixelWidth<screenWidth) {
			//center the room on the screen, because it's too small to scroll.
			this.x = -(screenWidth-roomPixelWidth)/2;
		} else {
			//center the player, but bump into the sides if we hit them
			this.x = (curPlayer.x*pixelSize) - (screenWidth/2); xScroll=true;
			if (this.x<0) this.x=0;
			if (this.x>=roomPixelWidth-screenWidth) this.x=roomPixelWidth-screenWidth-1;
		}
		
		int roomPixelHeight = curRoom.height*tileSize*pixelSize;
	
		if (roomPixelHeight<screenHeight) {
			//center the room on the screen, because it's too small to scroll.
			this.y = -(screenHeight-roomPixelHeight)/2;
		} else {
			//center the player, but bump into the sides if we hit them
			this.y = curPlayer.y*pixelSize - (screenHeight/2); yScroll=true;
			if (this.y<0) this.y=0;
			if (this.y>=roomPixelHeight-screenHeight) this.y=roomPixelHeight-screenHeight;
		}
	}
}