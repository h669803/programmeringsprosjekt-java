package no.hvl.dat100ptc.oppgave6;

import javax.swing.JOptionPane;

import easygraphics.*;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave3.GPSUtils;
import no.hvl.dat100ptc.oppgave4.GPSComputer;

public class CycleComputer extends EasyGraphics {

	private static int SPACE = 10;
	private static int MARGIN = 20;
	
	private static int ROUTEMAPXSIZE = 1000; 
	private static int ROUTEMAPYSIZE = 400;
	private static int HEIGHTSIZE = 300;

	private GPSComputer gpscomp;
	private GPSPoint[] gpspoints;
	private double[] longitudes;
	private double[] latitudes;
	
	private int N = 0;

	public CycleComputer() {

		String filename = JOptionPane.showInputDialog("GPS data filnavn: ");

		gpscomp = new GPSComputer(filename);
		gpspoints = gpscomp.getGPSPoints();

	}

	public static void main(String[] args) {
		launch(args);
	}

	public void run() {

		N = gpspoints.length; // number of gps points
		latitudes = GPSUtils.getLatitudes(gpspoints);
		longitudes = GPSUtils.getLongitudes(gpspoints);

		makeWindow("Cycle Computer", 
			2 * MARGIN + ROUTEMAPXSIZE,
			2 * MARGIN + ROUTEMAPYSIZE + HEIGHTSIZE + SPACE);
		
		setColor(27, 27, 27);
		setFont("Consolas", 16);
		fillRectangle(0, 0, 2 * MARGIN + ROUTEMAPXSIZE, 2 * MARGIN + ROUTEMAPYSIZE + HEIGHTSIZE + SPACE);
		setStatText(0, 0, 0, 0, 0, 0);
		setColor(255, 255, 0);
		bikeRoute(MARGIN + HEIGHTSIZE, MARGIN + ROUTEMAPYSIZE + HEIGHTSIZE + SPACE);

	}
	
	private int textIndex;

	public void setStatText(int time, double distance, double elevation, double maxSpeed, double avgSpeed, double energy) {
		
		String[] stats = gpscomp.getFormatedStatistics(time, distance, elevation, maxSpeed, avgSpeed, energy);
		setColor(255, 255, 255);
		int y = MARGIN;
		
		// Skjuler den gamle teksten
		if (textIndex > 0) for (int i = 0; i < stats.length; i++) {
			setVisible(textIndex - i, false);
		}
		for (int i = 0; i < stats.length; i++) {
			textIndex = drawString(stats[i], MARGIN, y += 20);
		}
		
	}
	
	public void bikeRoute(int heightbase, int mapbase) {

		float timeScale = Float.parseFloat(getText("Tidsskalering"));
		double scale = Math.min(ystep(), xstep()),
		weight = gpscomp.getWeight(),
		minlat = GPSUtils.findMin(latitudes),
		minlong = GPSUtils.findMin(longitudes),
		totalElevation = 0,
		totalDistance = 0,
		speedSum = 0,
		maxSpeed = 0,
		totalEnergy = 0;
				
		int totalTime = 0;
		int x = 0, y = 0;
		
		double[] climbs = gpscomp.climbs();
		
		for (int i = 0; i < N; i++) {
			
			int newx = MARGIN + (int)Math.round((longitudes[i] - minlong) * scale);
			int newy = mapbase - (int)Math.round((latitudes[i] - minlat) * scale);
			double elevation = gpspoints[i].getElevation();
			
			if (i > 0) {
				int deltaTime = gpspoints[i].getTime() - gpspoints[i - 1].getTime();
				double distance = GPSUtils.distance(gpspoints[i - 1], gpspoints[i]);
				double speed = GPSUtils.speed(gpspoints[i - 1], gpspoints[i]);
				double energy = gpscomp.kcal(weight, deltaTime, speed);
				
				pause(Math.round(deltaTime / timeScale * 1000));
				setStatText(
					totalTime += deltaTime,
					totalDistance += distance / 1000,
					totalElevation += Math.max(0, elevation - gpspoints[i - 1].getElevation()),
					maxSpeed = Math.max(maxSpeed, speed),
					(speedSum += speed) / i,
					totalEnergy += energy
				);
				
				setColor(
					Math.max(0, Math.min((int)Math.round(climbs[i - 1] * 2000 + 255), 255)),
					Math.max(0, Math.min((int)Math.round(climbs[i - 1] * -2000 + 255), 255)),
					0
				);
				
				drawLine(x, y, newx, newy);
			}
			
			fillCircle(x = newx, y = newy, 4);
			
			int rectHeight = (int)Math.round(Math.max(elevation, 0));
			fillRectangle(MARGIN + i * 2, heightbase - rectHeight, 2, rectHeight);
		}

	}

	public double xstep() {

		double[] minmax = GPSUtils.findMinMax(longitudes);

		return ROUTEMAPXSIZE / (minmax[1] - minmax[0]); 
		
	}

	public double ystep() {
	
		double[] minmax = GPSUtils.findMinMax(latitudes);
		
		return ROUTEMAPYSIZE / (minmax[1] - minmax[0]);
		
	}

}
