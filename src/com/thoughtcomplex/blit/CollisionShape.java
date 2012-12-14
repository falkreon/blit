package com.thoughtcomplex.blit;
public class CollisionShape {
	double x = 0;
	double y = 0;
	double width = 0;
	double height = 0;

	public boolean contains(double _x, double _y) {
		if(_x<this.x) return false;
		if(_y<this.y) return false;
		if(_x>=this.x+this.width) return false;
		if(_y>=this.y+this.height) return false;
		return true;
	}
}