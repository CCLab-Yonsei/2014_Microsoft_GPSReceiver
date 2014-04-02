package org.cclab.microsoft_gpsreceiver;


import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

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
		Date date = new Date(timestamp);
		Format sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		
		return latitude + "\t" + longitude + "\t" + sdf.format(date) + "\t" + nSatellite + "\t" + hdop + "\n";
	}
	
	public double getLat() { return latitude; }
	public double getLng() { return longitude; }
	public long getTimestamp() { return timestamp; }
	public int getNStatellite() { return nSatellite; }
	public double getHdop() { return hdop; }

}
