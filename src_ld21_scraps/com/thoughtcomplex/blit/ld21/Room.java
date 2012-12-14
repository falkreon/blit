package com.thoughtcomplex.blit.ld21;
import java.util.Vector;

public class Room {

	int width = 20;
	int height = 20;
	public int[][] terrain = new int[width][height];
	public int[][] charge = new int[width][height];
	public Vector<Door> exits = new Vector<Door>();
	public MessageQueue openScript = new MessageQueue();
	public Vector<Mob> monsters = new Vector<Mob>();
	public Vector<Mob> trashMonsters = new Vector<Mob>();

	public Room(int qwidth, int qheight) {
		width=qwidth; height=qheight;
		terrain = new int[width][height];
		charge = new int[width][height];
		setTileWalls(1);
	}

	
	public void setTileID(int x, int y, int val) {
		if (x<0) return;
		if (y<0) return;
		if (x>=width) return;
		if (y>=height) return;
		//if (val>=terrainColors.length) return;
		
		terrain[x][y] = val;
	}
	
	public int getTileID(int x, int y) {
		if (x<0) return 0;
		if (y<0) return 0;
		if (x>=width) return 0;
		if (y>=height) return 0;
		
		return terrain[x][y];
	}
	
	public boolean getCharge(int x, int y) {
		if (x<0) return false;
		if (y<0) return false;
		if (x>=width) return false;
		if (y>=height) return false;
		
		return ( (charge[x][y] & 1) == 1 );
	}
	
	public void energize(int x, int y) {
		if (x<0) return;
		if (y<0) return;
		if (x>=width) return;
		if (y>=height) return;
		
		charge[x][y] = charge[x][y] | 1; //set charge bit
	}
	
	public void clearCharges() {
		for(int y = 0; y<height; y++) {
			for(int x = 0; x<width; x++) {
				charge[x][y] = charge[x][y] & 0xFFFFFE; //assume 24 bits are in use, should be enough
			}
		}
	}
	
	public int getHeight() { return height; }
	public int getWidth() { return width; }
	
	
	
	public void setTileRect(int qx, int qy, int wid, int hit, int val) {
		for(int y = 0; y<hit; y++) {
			for(int x = 0; x<wid; x++) {
				setTileID(x+qx,y+qy,val);
			}
		}
	}
	
	public void setTileWalls(int val) {
		for(int y=0; y<height; y++) {
			setTileID(0,y,val);
			setTileID(width-1,y,val);
		}
		
		for(int x=0; x<width; x++) {
			setTileID(x,0,val);
			setTileID(x,height-1,val);
		}
	}
	
	public void buildLine(int x1, int y1, int x2, int y2, int val) {
		double rise = y2-y1;
		double run = x2-x1;
		double increments = Math.max(Math.abs(rise),Math.abs(run));
		if (increments==0) increments=1.0;
		double risePerTick = rise/increments;
		double runPerTick = run/increments;
		//System.out.println(runPerTick);
		double x = x1; double y = y1;
		for (int i=0; i<(int)increments; i++) {
			setTileID((int)x,(int)y,val);
			x+=runPerTick;
			y+=risePerTick;
		}
	}
	
	
	public void buildLadder(int qx, int qy, int hit) {
		setTileRect(qx,qy,1,hit,-6);
	}
	
	public void checkConnections() {
		for(int y=0; y<height; y++) {
			for(int x=0; x<width; x++) {
				int curTile = getTileID(x,y);
				if (curTile==7) setOffElectricalChain(x,y);
			}
		}
	}
	
	public boolean canMoveLeft(Mob m) {
		if (m.x<=0) return false;
		if ( ((int)m.x) % 8!=0) return true;
		
		int tilesHigh = (int)Math.ceil(m.height/8.0); if (tilesHigh<1) tilesHigh=1;
		if ((int)m.y%8!=0) tilesHigh++;
		for(int i=0; i<tilesHigh; i++) {
			int id = getTileID((int)(m.x/8)-1,(int)(m.y/8.0)+i);
			if (id>0) return false;
		}
		return true;
	}
	
	public boolean canMoveRight(Mob m) {
		if (m.x+m.width-1>=(getWidth()*8)-1) return false;
		if ((int)(m.x+m.width-1)%8!=8-1) return true;
		
		int tilesHigh = (int)Math.ceil(m.height/8.0); if (tilesHigh<1) tilesHigh=1;
		if ((int)m.y%8!=0) tilesHigh++;
		for(int i=0; i<tilesHigh; i++) {
			int id = getTileID((int)((m.x+m.width-1)/8)+1,(int)(m.y/8)+i);
			if (id>0) return false;
		}
		return true;
	}
	
	public boolean canMoveUp(Mob m) {
		if (m.y<=0) return false;
		if ( ((int)m.y) % 8!=0) return true;
		
		int tilesWide = (int)Math.ceil(m.width/8.0); if (tilesWide<1) tilesWide=1;
		if (m.x%8!=0) tilesWide++;
		
		for(int i=0; i<tilesWide; i++) {
			int id = getTileID((int)(m.x/8)+i,(int)(m.y/8)-1);
			if (id>0) return false;
		}
		return true;
	}
	
	public boolean canMoveDown(Mob m) {
		if (m.y+m.height-1>(getHeight()*8)-1) return false;
		if ((int)m.y%8!=0) return true;
		
		int tilesWide = (int)Math.ceil(m.width/8.0); if (tilesWide<1) tilesWide=1;
		if ((int)m.x%8!=0) tilesWide++;
		for(int i=0; i<tilesWide; i++) {
			int id = getTileID((int)(m.x/8)+i,(int)((m.y+m.height-1)/8)+1);
			if (id>0) return false;
		}
		return true;
	}
	
	public void removeItem(int id) {
		for(int y=0; y<height; y++) {
			for(int x=0; x<width; x++) {
				int curTile = getTileID(x,y);
				if (curTile==id) setTileID(x,y,0);
			}
		}
	}
	
	public void removeGuns() {
		removeItem(-13);
	}
	
	public void removeJumpBoost() {
		removeItem(-15);
	}
	
	public void removeJetpack() {
		removeItem(-16);
	}
	
	public void setOffElectricalChain(int sx, int sy) {
		if (getCharge(sx,sy)) return;
		else {
			energize(sx,sy);
			if (isWire(sx-1,sy)) setOffElectricalChain(sx-1,sy);
			if (isWire(sx+1,sy)) setOffElectricalChain(sx+1,sy);
			if (isWire(sx,sy-1)) setOffElectricalChain(sx,sy-1);
			if (isWire(sx,sy+1)) setOffElectricalChain(sx,sy+1);
		}
	}
	
	public boolean isWire(int qx, int qy) {
		int blockID = getTileID(qx,qy);
		return isWire(blockID);
	}
	
	public boolean isWire(int blockID) {
		if ((blockID>=8) | (blockID<12)) return true;
		else return false;
	}
}