package org.cclab.microsoft_gpsreceiver;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class Utility {
	
	/**
	 * return two digit number (ex: 3 -> 03)
	 * 
	 * @author ipuris
	 * @param number
	 * @return
	 */
	public static String getTwoDigitNumber(final int number) {
		return (number < 10 && number > 0) ? "0" + Integer.toString(number) : Integer.toString(number);
	}
	
	/**
	 * Check whether a service is running or not
	 * 
	 * @author ipuris
	 * @param serviceName Strongly recommended to use 'MyService.class.getName()'.
	 * @return
	 */
	public static boolean isServiceRunning(final Context context, final String serviceName) {
		ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		for(RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if(serviceName.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
