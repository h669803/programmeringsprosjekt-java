package no.hvl.dat100ptc.oppgave5;

import javax.swing.JOptionPane;

import easygraphics.EasyGraphics;
import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave3.GPSUtils;
import no.hvl.dat100ptc.oppgave4.GPSComputer;

public class ShowRoute extends EasyGraphics {

	private static int MARGIN = 20;
	private static int MAPXSIZE = 1600;
	private static int MAPYSIZE = 800;

	private GPSPoint[] gpspoints;
	private GPSComputer gpscomputer;
	private double[] longitudes;
	private double[] latitudes;
	
	public ShowRoute() {

		String filename = JOptionPane.showInputDialog("GPS data filnavn: ");
		gpscomputer = new GPSComputer(filename);

		gpspoints = gpscomputer.getGPSPoints();
		latitudes = GPSUtils.getLatitudes(gpspoints);
		longitudes = GPSUtils.getLongitudes(gpspoints);

	}

	public static void main(String[] args) {
		launch(args);
	}

	public void run() {

		makeWindow("Route", MAPXSIZE + 2 * MARGIN, MAPYSIZE + 2 * MARGIN);

		showRouteMap(MARGIN + MAPYSIZE);
		
		showStatistics();
	}

	// antall x-pixels per lengdegrad
	public double xstep() {

		double[] minmax = GPSUtils.findMinMax(longitudes);

		return MAPXSIZE / (minmax[1] - minmax[0]); 
		
	}

	// antall y-pixels per breddegrad
	public double ystep() {
	
		double[] minmax = GPSUtils.findMinMax(latitudes);
		println(minmax[0] + ", " + minmax[1]);
		return MAPYSIZE / (minmax[1] - minmax[0]);
		
	}

	public void showRouteMap(int ybase) {

		double scale = Math.min(ystep(), xstep()),
		minlat = GPSUtils.findMin(latitudes),
		minlong = GPSUtils.findMin(longitudes);
		
		int x = 0, y = 0;
		
		setColor(0, 255, 0);
		for (int i = 0; i < gpspoints.length; i++) {
			
			int newx = MARGIN + (int)Math.round((longitudes[i] - minlong) * scale);
			int newy = ybase - (int)Math.round((latitudes[i] - minlat) * scale);
			
			if (i > 0) 
				drawLine(x, y, newx, newy);
			
			fillCircle(x = newx, y = newy, 4);
			
		}
		
	}
	
	public void showStatistics() {

		String[] text = gpscomputer.getFormatedStatistics();

		setColor(0, 0, 0);
		setFont("Consolas", 16);
		int y = MARGIN;
		
		for (String str : text) {
			drawString(str, MARGIN, y += 20);
		}
		
	}

}
