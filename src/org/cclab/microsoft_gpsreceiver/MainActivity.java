package org.cclab.microsoft_gpsreceiver;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	
	private ToggleButton button_startstop;
	private TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		button_startstop = (ToggleButton)findViewById(R.id.togglebutton_startstop);
		tv = (TextView)findViewById(R.id.text_loggingstatus);
		
		// TODO is it needed?
		// if GpsService is already running, turn on the toggle button 
		final boolean serviceRunning = isServiceRunning(GpsService.class.getName());
		if(serviceRunning) {
			button_startstop.setChecked(true);
			tv.setText(R.string.text_loggingstatus_on);
		}
		else {
			button_startstop.setChecked(false);
			tv.setText(R.string.text_loggingstatus_off);
		}
		
	}
	
	public void onLogging(View v) {
		// check whether GPS is enabled or not
		final LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			final String question = getResources().getString(R.string.alert_enable_gps_question); 
			final String yes = getResources().getString(R.string.alert_enable_gps_yes);
			final String no = getResources().getString(R.string.alert_enable_gps_no);
			
			builder.setMessage(question).setCancelable(false).setPositiveButton(yes, new DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog, final int id) {
					startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
				}}).setNegativeButton(no, new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int id) {
						dialog.cancel();
					}}
				);
			final AlertDialog alert = builder.create();
		    alert.show();
		    
		    button_startstop.setChecked(false);
		}
		else {
			if(button_startstop.isChecked()) {
				tv.setText(R.string.text_loggingstatus_on);
				startService(new Intent(this, GpsService.class));
			}
			else {
				tv.setText(R.string.text_loggingstatus_off);
				stopService(new Intent(this, GpsService.class));
			}
		}
	}
	
	/**
	 * Check whether a service is running or not
	 * 
	 * @author ipuris
	 * @param serviceName Strongly recommended to use 'MyService.class.getName()'.
	 * @return
	 */
	private boolean isServiceRunning(final String serviceName) {
		ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		for(RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if(serviceName.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
