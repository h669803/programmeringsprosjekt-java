package no.hvl.dat100ptc.oppgave3;

import java.lang.Math;
import java.util.Locale;
import no.hvl.dat100ptc.oppgave1.GPSPoint;

public class GPSUtils {

	public static double findMax(double[] da) {

		double max = da[0];
		
		for (double d : da) {
			if (d > max) max = d;
		}
		
		return max;
	}

	public static double findMin(double[] da) {

		double min = da[0];
		
		for (double d : da) {
			if (d < min) min = d;
		}
		
		return min;

	}
	
	public static double[] findMinMax(double[] da) {
		double min = da[0], max = min;
		
		for (double d : da) {
			if (d > max) max = d;
			if (d < min) min = d;
		}
		
		return new double[] {min, max};
	}

	public static double[] getLatitudes(GPSPoint[] gpspoints) {
		
		int l = gpspoints.length;
		double[] latitudes = new double[l];
		
		for (int i = 0; i < l; i++) 
			latitudes[i] = gpspoints[i].getLatitude();

		return latitudes;
		
	}

	public static double[] getLongitudes(GPSPoint[] gpspoints) {

		int l = gpspoints.length;
		double[] longitudes = new double[l];
		
		for (int i = 0; i < l; i++) 
			longitudes[i] = gpspoints[i].getLongitude();

		return longitudes;

	}

	private static int R = 6371000; // jordens radius

	public static double distance(GPSPoint gpspoint1, GPSPoint gpspoint2) {

		double latitude1 = gpspoint1.getLatitude() * Math.PI / 180,
		latitude2 = gpspoint2.getLatitude() * Math.PI / 180,
		deltaLat = latitude2 - latitude1,
		deltaLong = (gpspoint2.getLongitude() - gpspoint1.getLongitude()) * Math.PI / 180,
		a = Math.pow(Math.sin(deltaLat / 2), 2) + Math.cos(latitude1) * Math.cos(latitude2) * Math.pow(Math.sin(deltaLong / 2), 2),
		c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		
		return R * c;

	}

	public static double speed(GPSPoint gpspoint1, GPSPoint gpspoint2) {

		return Math.abs(distance(gpspoint1, gpspoint2) / (gpspoint2.getTime() - gpspoint1.getTime()) * 3.6);

	}

	public static String formatTime(int secs) {
		
		return String.format("%10s", String.format("%02d:%02d:%02d", secs / 3600, secs % 3600 / 60, secs % 60));

	}

	public static String formatDouble(double d) {
		
		return String.format(Locale.US, "%10.2f", d);
		
	}
}
