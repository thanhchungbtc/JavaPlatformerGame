package com.btc.helper;

import com.btc.Rect;

public class CollisionsHelper {
	public static boolean RectIntersectsRect(Rect rect1, Rect rect2) {
		return rect1.intersects(rect2);
	}
	
	public static Rect RectIntersection(Rect rect1, Rect rect2) {		
		return rect1.intersection(rect2);
	}
	
	 
}
