package no.hvl.dat100ptc.oppgave2;

import no.hvl.dat100ptc.oppgave1.GPSPoint;

public class GPSData {

	private GPSPoint[] gpspoints;
	protected int antall = 0;

	public GPSData(int n) {

		gpspoints = new GPSPoint[n];
		antall = 0;
	
	}

	public GPSPoint[] getGPSPoints() {
		return gpspoints;
	}
	
	protected boolean insertGPS(GPSPoint gpspoint) {
		
		if (antall < gpspoints.length) {
			gpspoints[antall++] = gpspoint;
			return true;
		}
		return false;

	}

	public boolean insert(String time, String latitude, String longitude, String elevation) {

		return insertGPS(GPSDataConverter.convert(time, latitude, longitude, elevation));
		
	}

	public void print() {

		System.out.println("====== Konvertert GPS Data - START ======");
		
		for (GPSPoint point : gpspoints) {
			System.out.print(point.toString());
		}
		
		System.out.println("====== Konvertert GPS Data - SLUTT ======");

	}
}
