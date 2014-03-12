package org.cclab.microsoft_gpsreceiver;

public class GpsData {
	private double latitude;
	private double longitude;
	private long timestamp;
	private int nSatellite;
	private double hdop;
	
	public GpsData(double latitude, double longitude, long timestamp, int nSatellite, double hdop) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.timestamp = timestamp;
		this.nSatellite = nSatellite;
		this.hdop = hdop;
	}
	
	@Override
	public String toString() {
		return latitude + "\t" + longitude + "\t" + timestamp + "\t" + nSatellite + "\t" + hdop + "\n";
	}

}
