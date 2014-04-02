package org.cclab.microsoft_gpsreceiver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import org.cclab.microsoft_gpsreceiver.network.SendPost;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class GpsService extends Service {

	private LocationManager locationManager;
	private LocationListener locationListener;
	
	private double hdop;
	private int nSatellite;

	private ArrayList<GpsData> dataset;
	private GpsStatus.NmeaListener nmeaListener;
	
	private Vibrator vibrator;
	private long[] vibPattern = {100, 500, 100, 500};
	private boolean bGpsTurnOff = false;
	
	@Override
	public void onCreate() {
		super.onCreate();

		// initialize member variables
		nmeaListener = new GpsStatus.NmeaListener() {
			
			@Override
			public void onNmeaReceived(long timestamp, String nmea) {
				String[] element = nmea.split(",");
				try {
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
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		
		dataset = new ArrayList<GpsData>();
		
		
		// request location updates 
		final long timeInterval = 5 * 1000;
		// TODO how about minDistance 1~2m? 
		try {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, timeInterval, 0, locationListener);
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		locationManager.addNmeaListener(nmeaListener);
		
		// success message
		Toast.makeText(this,  getResources().getString(R.string.service_start), Toast.LENGTH_SHORT).show();
		
		// Logging to logfile
		Utility.log(Environment.getExternalStorageDirectory().getPath() + "/data/" + getPackageName(),
				"[Service] Service created");
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
		
		// test input
		// dataset.add(new GpsData(1.0, 2.0, 3, 4, 5));
		if(dataset.size() > 0) { 
			// save data to file
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());
			
			final String filepath = directory + "/" + 
					Utility.getStudentId(this) + "_" + // user id 
					cal.get(Calendar.YEAR) + 
					Utility.getTwoDigitNumber(cal.get(Calendar.MONTH) + 1) +  
					Utility.getTwoDigitNumber(cal.get(Calendar.DATE)) + "_" + 
					Utility.getTwoDigitNumber(cal.get(Calendar.HOUR_OF_DAY)) +  
					Utility.getTwoDigitNumber(cal.get(Calendar.MINUTE)) + 
					Utility.getTwoDigitNumber(cal.get(Calendar.SECOND)) + ".txt";
			
			File file = new File(filepath);
			
			FileWriter fw = null;
			BufferedWriter bw = null;
			
			try {
				file.createNewFile();
				fw = new FileWriter(file);
				bw = new BufferedWriter(fw);
				
				// Log.i("GpsService", "DatasetSize: " + dataset.size());
				Toast.makeText(getApplicationContext(), dataset.size() + getResources().getString(R.string.service_numberofpoints_message), Toast.LENGTH_SHORT).show();
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
			
			// send file to server
			try {
				final int responseCode = new SendPost().execute(filepath).get();
				if(responseCode == 200) {
					
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

			// update contribution
			SharedPreferences settings = getSharedPreferences(Constants.PREFS, 0);
			int currentContribution = settings.getInt(Constants.PREFS_CONTRIBUTION, 0);
			
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt(Constants.PREFS_CONTRIBUTION, currentContribution + dataset.size());
			editor.commit();
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
		
		// Logging to logfile
		Utility.log(Environment.getExternalStorageDirectory().getPath() + "/data/" + getPackageName(),
				"[Service] Service destroyed");
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}
	
	private class MyLocationListener implements LocationListener {
		
		@Override
		public void onLocationChanged(Location location) {
			GpsData data = new GpsData(location.getLatitude(), location.getLongitude(), location.getTime(), nSatellite, hdop);
			dataset.add(data);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch(status) {
			case LocationProvider.AVAILABLE:
				break;
			case LocationProvider.OUT_OF_SERVICE:
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				break;
			}
		}

		@Override
		public void onProviderEnabled(String provider) {
			Log.i("Gps Service", "onProviderEnabled()");

			if(bGpsTurnOff) {
				Toast.makeText(GpsService.this, getResources().getString(R.string.service_thanks), Toast.LENGTH_SHORT).show();
				bGpsTurnOff = false;
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			Log.i("Gps Service", "onProviderDisabled()");
			
			vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(vibPattern, -1);
			
			bGpsTurnOff = true;
			Toast.makeText(GpsService.this, getResources().getString(R.string.service_warning_gps_off), Toast.LENGTH_LONG).show();
		}

	}

}
