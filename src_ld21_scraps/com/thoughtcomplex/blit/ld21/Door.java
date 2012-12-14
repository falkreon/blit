package com.thoughtcomplex.blit.ld21;
public class Door {
	public int x = 0;
	public int y = 0;
	public String targetRoom = "";
	public int targetX = 0; //0 = the same. -1 = snap left, 1 = snapRight
	public int targetY = 0;
	public boolean facingRight = true;
	public boolean infected=false;
	public boolean locked=false;
	public boolean flashing = false;
	public int resistance = 8;
	
	public Door(int qx, int qy, String qtargetRoom) {
		x=qx; y=qy; targetRoom=qtargetRoom;
		if (x>2) targetX=-1;
		if (x<=2) targetX=1;
		targetY = 0;
	}
	
	public boolean contains(int qx, int qy) {
		if ((qx<x*8) | (qy<y*8)) return false;
		if (qx>=x*8+16) return false;
		if (qy>=y*8+24) return false;
		return true;
	}

}