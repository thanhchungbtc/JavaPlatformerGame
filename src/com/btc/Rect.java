package com.btc;


public class Rect {
	public double x;
	public double y;
	public double width;
	public double height;
	public Rect(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public Vector2D bottomLeft() {
		return new Vector2D(this.x, this.y + this.height);
	}
	
	 public boolean intersects(Rect r) {
		 double tw = this.width;
		 double th = this.height;
		 double rw = r.width;
		 double rh = r.height;
	        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
	            return false;
	        }
	     double tx = this.x;
	     double ty = this.y;
	     double rx = r.x;
	     double ry = r.y;
	        rw += rx;
	        rh += ry;
	        tw += tx;
	        th += ty;
	        //      overflow || intersect
	        return ((rw < rx || rw > tx) &&
	                (rh < ry || rh > ty) &&
	                (tw < tx || tw > rx) &&
	                (th < ty || th > ry));
	    }
	
	public Rect intersection(Rect r) {
        double tx1 = this.x;
        double ty1 = this.y;
        double rx1 = r.x;
        double ry1 = r.y;
        double tx2 = tx1; tx2 += this.width;
        double ty2 = ty1; ty2 += this.height;
        double rx2 = rx1; rx2 += r.width;
        double ry2 = ry1; ry2 += r.height;
        if (tx1 < rx1) tx1 = rx1;
        if (ty1 < ry1) ty1 = ry1;
        if (tx2 > rx2) tx2 = rx2;
        if (ty2 > ry2) ty2 = ry2;
        tx2 -= tx1;
        ty2 -= ty1;
        // tx2,ty2 will never overflow (they will never be
        // larger than the smallest of the two source w,h)
        // they might underflow, though...
        if (tx2 < Double.MIN_VALUE) tx2 = Double.MIN_VALUE;
        if (ty2 < Double.MIN_VALUE) ty2 = Double.MIN_VALUE;
        return new Rect(tx1, ty1, tx2, ty2);
    }
}
