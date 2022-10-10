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
		return this.gpspoints;
	}
	
	// beregn total distances (i meter)
	public double totalDistance() {

		double distance = 0;
		
		for (int i = 1; i < gpspoints.length; i++) {
			distance += GPSUtils.distance(gpspoints[i - 1], gpspoints[i]);
		}
		
		return distance;

	}

	// beregn totale høydemeter (i meter)
	public double totalElevation() {

		double elevation = 0;
		
		for (int i = 1; i < gpspoints.length; i++)
			elevation += Math.max(0, gpspoints[i].getElevation() - gpspoints[i - 1].getElevation());

		return elevation;

	}

	// beregn total tiden for hele turen (i sekunder)
	public int totalTime() {
		
		// antar punktene er sortert etter tid
		return gpspoints[gpspoints.length - 1].getTime() - gpspoints[0].getTime();

	}
		
	// beregn gjennomsnitshastighets mellom hver av gps punktene

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

	// beregn kcal gitt weight og tid der kjøres med en gitt hastighet
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
	
	private static double WEIGHT = 80.0;
	private static String[] labels = new String[] {
		"Total distance", "Total elevation", "Max speed", "Average speed", "Energy"
	};
	private static String[] units = new String[] {
		"km", "m", "km/t", "km/t", "kcal"	
	};
	
	private double[] getStatistics() {
		return new double[] {
			totalDistance() / 1000,
			totalElevation(),
			maxSpeed(),
			averageSpeed(),
			totalKcal(WEIGHT)
		};
	}
	
	public String[] getFormatedStatistics() {
		
		double[] statistics = getStatistics();
		String[] result = new String[6];
		result[0] = "Total time      :" + GPSUtils.formatTime(totalTime());
		for (int i = 0; i < 5; i++) {
			result[i + 1] = String.format(Locale.US, "%-16s:%10.2f %s", labels[i], statistics[i], units[i]);
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
