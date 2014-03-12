package org.cclab.microsoft_gpsreceiver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

public class GpsService extends Service {

	private LocationManager locationManager;
	private LocationListener locationListener;
	
	private double hdop;
	private int nSatellite;

	private ArrayList<GpsData> dataset;
	private GpsStatus.NmeaListener nmeaListener;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// initialize member variables
		nmeaListener = new GpsStatus.NmeaListener() {
			
			@Override
			public void onNmeaReceived(long timestamp, String nmea) {
				String[] element = nmea.split(",");
				try {
					// TODO Re-check condition
					if(element[0].equals("$GPGGA") && Integer.parseInt(element[6]) != 0) {
						hdop = Double.parseDouble(element[8]);
						nSatellite = Integer.parseInt(element[7]);
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		};
		locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.addNmeaListener(nmeaListener);
		locationListener = new MyLocationListener();
		
		dataset = new ArrayList<GpsData>();
		
		
		// request location updates 
		long timeInterval = 5 * 1000;
		// TODO how about minDistance 1~2m? 
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, timeInterval, 0, locationListener);
		
		// success message
		Toast.makeText(this,  getResources().getString(R.string.service_start), Toast.LENGTH_LONG).show();
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		boolean success = true;
		
		// create directory
		File directory = new File(Environment.getExternalStorageDirectory().getPath() + "/data/" + getPackageName());
		
		if(!directory.isDirectory()) {
			directory.mkdirs();
		}
		
		// save data to file
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		File file = new File(directory + "/" + cal.get(Calendar.YEAR) + "_" + (cal.get(Calendar.MONTH) + 1) + "_" + cal.get(Calendar.DATE) + "_" + 
				cal.get(Calendar.HOUR_OF_DAY) +	"_" + cal.get(Calendar.MINUTE) + "_" + cal.get(Calendar.SECOND) + ".txt");
		
		FileWriter fw = null;
		BufferedWriter bw = null;
		
		try {
			file.createNewFile();
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			
			for(int i = 0; i < dataset.size(); i++) {
				bw.write(dataset.get(i).toString());
			}
			
			bw.close();
			fw.close();
		}
		catch(IOException e) {
			e.printStackTrace();
			success = false;
		}
		
		// remove listeners
		locationManager.removeUpdates(locationListener);
		locationManager.removeNmeaListener(nmeaListener);
		
		// success message
		if(success) {
			Toast.makeText(this,  getResources().getString(R.string.service_stop), Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(this,  getResources().getString(R.string.service_error), Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private class MyLocationListener implements LocationListener {
		
		@Override
		public void onLocationChanged(Location location) {
			
			GpsData data = new GpsData(location.getLatitude(), location.getLongitude(), location.getTime(), nSatellite, hdop);
			dataset.add(data);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

	}

}
