package lu.svv.theodore.explain;

public class Cast {
	public static boolean toBoolean(double d) {
		return d != 0 ? true : false;
	}

	public static Double toDouble(boolean b) {
		return b? 1.0 : 0.0;
	}
}
