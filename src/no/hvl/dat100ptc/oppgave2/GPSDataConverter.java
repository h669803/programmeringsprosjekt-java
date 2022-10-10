package no.hvl.dat100ptc.oppgave2;

import java.lang.Math;
import no.hvl.dat100ptc.oppgave1.GPSPoint;

public class GPSDataConverter {
	
	private static int TIME_STARTINDEX = 11;
	private static int TIME_ENDINDEX = 19;

	public static int toSeconds(String timestr) {
		
		String[] timeArray = timestr.substring(TIME_STARTINDEX, TIME_ENDINDEX).split(":");
		int result = 0;
		
		for (int i = 0; i < 3; i++) {
			result += Integer.parseInt(timeArray[i]) * Math.pow(60, 2 - i);
		}
		return result;
		
	}

	public static GPSPoint convert(String timeStr, String latitudeStr, String longitudeStr, String elevationStr) {
		
		int time = toSeconds(timeStr);
		double latitude = Double.parseDouble(latitudeStr);
		double longitude = Double.parseDouble(longitudeStr);
		double elevation = Double.parseDouble(elevationStr);
		
		return new GPSPoint(time, latitude, longitude, elevation);
	    
	}
	
}
