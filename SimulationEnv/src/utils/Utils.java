package utils;

public class Utils {
	public static boolean compareDoubles(double a, double b, double epsilon) {
	    return Math.abs(a - b) < epsilon;
	}
}
