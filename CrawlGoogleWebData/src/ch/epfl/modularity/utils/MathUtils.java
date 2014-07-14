package ch.epfl.modularity.utils;

import java.awt.Point;
import java.util.Arrays;


public class MathUtils {
	// public static double angleBetween3Point(Point p1, Point p2, Point p3) {
	// double a1 = Math.atan2(p2.x - p1.x, p2.y - p1.y);
	// double a2 = Math.atan2(p3.x - p2.x, p3.y - p2.y);
	// return a1 + a2;
	// }

	public static double angleBetween3Point(Point p1, Point p2, Point p3) {
		double a = distance(p1, p3);
		double b = distance(p1, p2);
		double c = distance(p2, p3);
		if (b == 0 || c == 0) {
			return 0;
		}

		return Math.acos((b * b + c * c - a * a) / (2 * b * c));
	}

	public static double[] anglesBetween3Point(Point p1, Point p2, Point p3) {
		double a = distance(p1, p3);
		double b = distance(p1, p2);
		double c = distance(p2, p3);

		double aa = (b == 0 || c == 0) ? 0 : Math.acos((b * b + c * c - a * a) / (2 * b * c));
		double ab = (a == 0 || c == 0) ? 0 : Math.acos((a * a + c * c - b * b) / (2 * a * c));
		double ac = (b == 0 || a == 0) ? 0 : Math.acos((b * b + a * a - c * c) / (2 * b * a));

		return new double[] { ac, aa, ab, b, a, c };
	}

	public static double distance(Point p1, Point p2) {
		return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
	}

	// public static boolean isOrdered(Point p0, Point p1, Point p2){
	// if(p0.x <= p1.x && p1.x <= p2.x && p0.y <= p1.y && p1.y <= p2.y || p0.x >= p1.x && p1.x >= p2.x && p0.y >= p1.y && p1.y >= p2.y){
	// return true;
	// }
	// else if(){
	// return -1;
	// }
	// }

	public static double distancePoint2Line(Point p0, Point p1, Point m) {
		// double nx = (double) (p1.y - p0.y) / (double) (p0.x - p1.x);
		// double c = nx * p0.x + p0.y;
		// double d = (nx * m.x + m.y + c) / Math.sqrt(nx * nx + 1);
		// return Math.abs(d);

		double c = distance(p1, p0);
		if (c == 0) {
			return distance(p1, m);
		}

		int ax = m.x - p0.x;
		int ay = m.y - p0.y;
		int bx = p1.x - m.x;
		int by = p1.y - m.y;

		int d = ax * by - bx * ay;

		return (double) Math.abs(d / 2 / c);
	}

	public static double distancePoint2Segment(Point p0, Point p1, Point m) {
		double[] as = anglesBetween3Point(p0, m, p1);

		if (as[0] >= Math.PI / 2) {
			return as[3];
		} else if (as[2] >= Math.PI / 2) {
			return as[5];
		} else {
			return distancePoint2Line(p0, p1, m);
		}
	}
	
	public static int roundUp(float f){
		int ff = (int)f;
		if(f - ff > 0){
			ff+= 1;
		}
		
		return ff;
	}
	
	public static int roundDown(float f){
		return (int) f;
	}

	// public static double distancePoint2Line2(Point p0, Point p1, Point m){
	// double b = distance(p1, m);
	// double c = distance(p0, m);
	//
	// if(b == 0 || c == 0){
	// return 0;
	// }
	//
	// return Math.sqrt(1.0/(1/(b*b) + 1/(c * c)));
	// }

	public static void main(String[] args) {
		Point p1 = new Point(0, 0);
		Point p2 = new Point(5, 6);
		Point p3 = new Point(10, 10);

		System.out.println(angleBetween3Point(p1, p2, p3) * 180 / Math.PI);
		System.out.println(Arrays.toString(anglesBetween3Point(p1, p2, p3)));
		System.out.println(distancePoint2Line(p1, p3, p2));
		// System.out.println(distancePoint2Line2(p1, p3, p2));
	}
}
