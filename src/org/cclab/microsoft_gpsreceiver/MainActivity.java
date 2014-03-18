package org.cclab.microsoft_gpsreceiver;

import org.cclab.microsoft_gpsreceiver.widget.Widget;

import android.app.Activity;
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
	private TextView tv_gpsstatus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		button_startstop = (ToggleButton)findViewById(R.id.togglebutton_startstop);
		tv_gpsstatus = (TextView)findViewById(R.id.text_gpsstatus);
	}
	
	@Override 
	protected void onResume() {
		super.onResume();
		
		// if GpsService is already running, turn on the toggle button 
		final boolean serviceRunning = Utility.isServiceRunning(getApplicationContext(), GpsService.class.getName());
		if(serviceRunning) {
			button_startstop.setChecked(true);
			tv_gpsstatus.setText(R.string.main_text_loggingstatus_on);
		}
		else {
			button_startstop.setChecked(false);
			tv_gpsstatus.setText(R.string.main_text_loggingstatus_off);
		}
		
		// TODO if GPS turns off? 
	}
	
	/**
	 * Logging toggle button onClick listener
	 * 
	 * @author ipuris
	 * @param v
	 */
	public void onLoggingButtonTouch(View v) {
		// check whether GPS is enabled or not
		final LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			final String question = getResources().getString(R.string.main_alert_enable_gps_question); 
			final String yes = getResources().getString(R.string.main_alert_enable_gps_yes);
			final String no = getResources().getString(R.string.main_alert_enable_gps_no);
			
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
				tv_gpsstatus.setText(R.string.main_text_loggingstatus_on);
				startService(new Intent(this, GpsService.class));
				
				Intent intent = new Intent(Widget.intentCurrentStateLoggingOn);
				sendBroadcast(intent);
			}
			else {
				tv_gpsstatus.setText(R.string.main_text_loggingstatus_off);
				stopService(new Intent(this, GpsService.class));
				
				Intent intent = new Intent(Widget.intentCurrentStateLoggingOff);
				sendBroadcast(intent);
			}
		}
	}
	
	/**
	 * Board button onClick listener
	 * 
	 * @param v
	 */
	public void onBoard(View v) {
		Intent intent = new Intent(MainActivity.this, org.cclab.microsoft_gpsreceiver.board.BoardActivity.class);
		startActivity(intent);
	}
	
}
