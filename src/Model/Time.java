package Model;

/**
 * Time management class to define time units
 * @author fcalvier
 *
 */
public class Time {
	static final double second = 1;
	static final double minute = 60;
	static final double hour = 3600;
	static final double day = 86400;
	static final double week = 604800;
	static final double month = 18144000;
	
	static double defaultTimeUnit(){
		return hour;
	}
}
