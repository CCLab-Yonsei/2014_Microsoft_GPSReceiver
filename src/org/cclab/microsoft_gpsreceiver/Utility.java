package org.cclab.microsoft_gpsreceiver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
	
	/**
	 * Get student ID 
	 * 
	 * @author ipuris
	 * @param context
	 * @return
	 */
	public static String getStudentId(Context context) {
		SharedPreferences settings = context.getSharedPreferences(Constants.PREFS, 0);
		return settings.getString(Constants.PREFS_USERID, "");
	}
	
	/**
	 * Get hash value of student ID 
	 * 
	 * @author ipuris
	 * @param context
	 * @return
	 */
	public static String getHashedStudentId(Context context) {
		SharedPreferences settings = context.getSharedPreferences(Constants.PREFS, 0);
		final String userid = settings.getString(Constants.PREFS_USERID, "");
		
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-1");
			byte[] useridBytes = userid.getBytes();
			byte[] salt = "A Context-aware Path Recommendation System using Crowdsourcing".getBytes();
			digest.update(useridBytes, 0, useridBytes.length);
			digest.update(salt, 0, salt.length);
			byte[] result = digest.digest();
			
			return bytesToHex(result);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Get hash value of student ID 
	 * 
	 * @author ipuris
	 * @param context
	 * @return
	 */
	public static String getHashedValue(Context context, final String value) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-1");
			byte[] useridBytes = value.getBytes();
			byte[] salt = "A Context-aware Path Recommendation System using Crowdsourcing".getBytes();
			digest.update(useridBytes, 0, useridBytes.length);
			digest.update(salt, 0, salt.length);
			byte[] result = digest.digest();
			
			return bytesToHex(result);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex( byte[] bytes )
	{
	    char[] hexChars = new char[ bytes.length * 2 ];
	    for( int j = 0; j < bytes.length; j++ )
	    {
	        int v = bytes[ j ] & 0xFF;
	        hexChars[ j * 2 ] = hexArray[ v >>> 4 ];
	        hexChars[ j * 2 + 1 ] = hexArray[ v & 0x0F ];
	    }
	    return new String( hexChars );
	}
	
	/**
	 * Check whether the user is an administrator or not  
	 * 
	 * @author ipuris
	 * @param context
	 * @return
	 */
	public static boolean isAdmin(Context context) {
		SharedPreferences settings = context.getSharedPreferences(Constants.PREFS, 0);
		final String userid = settings.getString(Constants.PREFS_USERID, "");
		
		String[] administrators = {"2009311758", "2011311701", "2013311487", "2013321252"};
		
		for(String administrator : administrators) {
			if(administrator.equals(userid)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Check whether netwokr is available
	 * 
	 * @author gnoowik
	 * @param context
	 * @return true/false
	 */
	public static boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
