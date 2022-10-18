package no.hvl.dat100ptc.oppgave4;

import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave2.GPSData;
import no.hvl.dat100ptc.oppgave2.GPSDataFileReader;
import no.hvl.dat100ptc.oppgave3.GPSUtils;
import java.util.Locale;

public class GPSComputer {
	
	private GPSPoint[] gpspoints;
	
	public GPSComputer(String filename) {

		GPSData gpsdata = GPSDataFileReader.readGPSFile(filename);
		gpspoints = gpsdata.getGPSPoints();

	}

	public GPSComputer(GPSPoint[] gpspoints) {
		this.gpspoints = gpspoints;
	}
	
	public GPSPoint[] getGPSPoints() {
		return gpspoints;
	}
	
	public double totalDistance() {

		double distance = 0;
		
		for (int i = 1; i < gpspoints.length; i++)
			distance += GPSUtils.distance(gpspoints[i - 1], gpspoints[i]);
		
		return distance;

	}

	public double totalElevation() {

		double elevation = 0;
		
		for (int i = 1; i < gpspoints.length; i++)
			elevation += Math.max(0, gpspoints[i].getElevation() - gpspoints[i - 1].getElevation());

		return elevation;

	}

	public int totalTime() {
		
		// antar punktene er sortert etter tid
		return gpspoints[gpspoints.length - 1].getTime() - gpspoints[0].getTime();

	}

	public double[] speeds() {
		
		int l = gpspoints.length - 1;
		double[] speeds = new double[l];
		
		for (int i = 0; i < l; i++)
			speeds[i] = GPSUtils.speed(gpspoints[i], gpspoints[i + 1]);
		
		return speeds;
		
	}
	
	public double maxSpeed() {
		
		return GPSUtils.findMax(speeds());
		
	}

	public double averageSpeed() {

		return totalDistance() / totalTime() * 3.6;
		
	}
	
	// Oppgave 6a
	public double[] climbs() {
		
		int l = gpspoints.length - 1;
		double[] climbs = new double[l];
		
		for (int i = 0; i < l; i++) {
			double deltaElev = gpspoints[i + 1].getElevation() - gpspoints[i].getElevation();
			climbs[i] = deltaElev / GPSUtils.distance(gpspoints[i], gpspoints[i + 1]);
		}
		
		return climbs;
		
	}
	
	public double maxClimb() {
		
		return GPSUtils.findMax(climbs());
		
	}

	public double kcal(double weight, int secs, double speed) {

		double met = 16;		
		double speedmph = speed / 1.609344;
		if (speedmph < 10) met = 4;
		else if (speedmph < 12) met = 6;
		else if (speedmph < 14) met = 8;
		else if (speedmph < 16) met = 10;
		else if (speedmph < 20) met = 12;

		return met * weight * secs / 3600;

	}

	public static double MS = 2.236936;
	
	public double totalKcal(double weight) {

		double totalkcal = 0;
		
		for (int i = 1; i < gpspoints.length; i++) {
			double speed = GPSUtils.speed(gpspoints[i - 1], gpspoints[i]);
			int secs = gpspoints[i].getTime() - gpspoints[i - 1].getTime();
			totalkcal += kcal(weight, secs, speed);
		}

		return totalkcal;
		
	}
	
	private static double WEIGHT = 80;
	private static String[] labels = {
		"Total distance", "Total elevation", "Max speed", "Average speed", "Energy"
	};
	private static String[] units = {
		"km", "m", "km/t", "km/t", "kcal"	
	};
	
	public String[] getFormatedStatistics() {
		
		double[] statistics = {
			totalDistance() / 1000,
			totalElevation(),
			maxSpeed(),
			averageSpeed(),
			totalKcal(WEIGHT)
		};
		String[] result = new String[6];
		result[0] = "Total time      :" + GPSUtils.formatTime(totalTime());
		for (int i = 0; i < 5; i++) {
			result[i + 1] = String.format(Locale.US, "%-16s:%10.2f %s", labels[i], statistics[i], units[i]);
		}
		return result;
		
	}
	
	public double getWeight() {
		return WEIGHT;
	}
	
	public String formatStat(String label, double stat, String unit) {
		return String.format(Locale.US, "%-16s:%10.2f %s", label, stat, unit);
	}
	
	public String[] getFormatedStatistics(int time, double distance, double elevation, double maxSpeed, double avgSpeed, double energy) {
		
		double[] statistics = {distance, elevation, maxSpeed, avgSpeed, energy};
		String[] result = new String[6];
		result[0] = "Total time      :" + GPSUtils.formatTime(time);
		for (int i = 0; i < 5; i++) {
			result[i + 1] = formatStat(labels[i], statistics[i], units[i]);
		}
		return result;
		
	}
	
	public void displayStatistics() {

		System.out.println("==============================================");
		
		for (String stat : getFormatedStatistics())
			System.out.println(stat);
			
		System.out.println("==============================================");
		
	}

}
