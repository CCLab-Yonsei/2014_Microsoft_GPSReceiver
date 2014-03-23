package org.cclab.microsoft_gpsreceiver;

import java.util.concurrent.ExecutionException;

import org.cclab.microsoft_gpsreceiver.network.SendGet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	
	private ToggleButton buttonStartstop;
	private TextView tvGpsStatus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// initialize member variables
		buttonStartstop = (ToggleButton)findViewById(R.id.mainactivity_togglebutton_startstop);
		tvGpsStatus = (TextView)findViewById(R.id.mainactivity_textview_gpsstatus);
		
		// get default values from shared preferences
		SharedPreferences settings = getSharedPreferences(Constants.PREFS, 0);
		boolean initialized = settings.getBoolean(Constants.PREFS_INITIALIZED, false);
		if(!initialized) {
			Intent intent = new Intent(this, RegistrationActivity.class);
			startActivityForResult(intent, Constants.REQCODE_USER_REGISTRATION);
		}
		
		// set student ID
		TextView tvStudentId = (TextView)findViewById(R.id.mainactivity_textview_studentid);
		tvStudentId.setText(getResources().getString(R.string.main_textview_contributor) + "   " + Utility.getStudentId(this));
		
	}
	
	@Override 
	protected void onResume() {
		super.onResume();
		
		Log.i("Main Activity", "onResume()");
		// if GpsService is already running, turn on the toggle button 
		final boolean serviceRunning = Utility.isServiceRunning(getApplicationContext(), GpsService.class.getName());
		if(serviceRunning) {
			buttonStartstop.setChecked(true);
			tvGpsStatus.setText(R.string.main_textview_loggingstatus_on);
		}
		else {
			buttonStartstop.setChecked(false);
			tvGpsStatus.setText(R.string.main_textview_loggingstatus_off);
		}
		
		// TODO if GPS turns off? 

		// udpate contribution values
		updateContribution();

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == Constants.REQCODE_USER_REGISTRATION) {
			if(resultCode == Activity.RESULT_OK) {
				Log.i("MainActivity", "Student ID " + Utility.getStudentId(this) + " is successfully stored");
				Toast.makeText(this, getResources().getString(R.string.main_toast_studentid_confirmed), Toast.LENGTH_SHORT).show();
				
				TextView tvStudentId = (TextView)findViewById(R.id.mainactivity_textview_studentid);
				tvStudentId.setText(getResources().getString(R.string.main_textview_contributor) + "   " + Utility.getStudentId(this));
			}
			else if(resultCode == Activity.RESULT_CANCELED) {
				Log.i("MainActivity", "Student ID is NOT stored");
				System.exit(0);
			}
		}
	}
	
	/**
	 * Logging toggle button onClick listener
	 * 
	 * @author ipuris
	 * @param v
	 */
	public void onLoggingButtonTouchListener(View v) {
		
		// check whether GPS is enabled or not
		final LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && buttonStartstop.isChecked()) {
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
		    
		    buttonStartstop.setChecked(false);
		}
		else {
			if(buttonStartstop.isChecked()) {
				tvGpsStatus.setText(R.string.main_textview_loggingstatus_on);
				startService(new Intent(this, GpsService.class));
				
				Intent intent = new Intent(Constants.INTENT_WIDGET_CURRENTSTATE_LOGGING_ON);
				sendBroadcast(intent);
			}
			else {
				tvGpsStatus.setText(R.string.main_textview_loggingstatus_off);
				stopService(new Intent(this, GpsService.class));
				
				Intent intent = new Intent(Constants.INTENT_WIDGET_CURRENTSTATE_LOGGING_OFF);
				sendBroadcast(intent);
				
			}
		}
	}
	
	/**
	 * Board button onClick listener
	 * 
	 * @param v
	 */
	public void onBoardClickListener(View v) {
		Intent intent = new Intent(MainActivity.this, org.cclab.microsoft_gpsreceiver.board.BoardActivity.class);
		startActivity(intent);
	}
	
	/**
	 * Board button onClick listener
	 * 
	 * @param v
	 */
	public void onRankClickListener(View v) {
		Intent intent = new Intent(MainActivity.this, org.cclab.microsoft_gpsreceiver.RankActivity.class);
		startActivity(intent);
	}

	/**
	 * Board button onClick listener
	 * 
	 * @param v
	 */
	public void onAboutClickListener(View v) {
		Intent intent = new Intent(MainActivity.this, org.cclab.microsoft_gpsreceiver.AboutActivity.class);
		startActivity(intent);
	}

	/**
	 * 
	 * 
	 * @param v
	 */
	public void onThanksClickListener(View v) {
		updateContribution();
		
		Toast.makeText(this, getResources().getString(R.string.main_toast_update_contribution), Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Update contribution values
	 *  
	 * @author ipuris
	 */
	private void updateContribution() {
		
		// get total contribution
		int totalContribution = -1;
		int currentContribution = -1;
		try {
			String totalContributionStr = new SendGet().execute("http://165.132.120.151/row_count.aspx").get();
			String currentContributionStr = new SendGet().execute("http://165.132.120.151/get_stdid.aspx?studentid=" + Utility.getStudentId(this)).get();
			totalContribution = Integer.valueOf(totalContributionStr.substring(0, totalContributionStr.indexOf('\n')).trim());
			currentContribution = Integer.valueOf(currentContributionStr.substring(0, currentContributionStr.indexOf('\n')).trim());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		// update 
		final float ratio = (float) ((float)currentContribution / (float)totalContribution * 100.0);
		
		TextView tvContribution = (TextView)findViewById(R.id.mainactivity_textview_contribution);
		tvContribution.setText(getResources().getString(R.string.main_textview_contribution) + "   " + currentContribution + " / " + totalContribution + " (" + String.format("%.2f", ratio) + "%)");
	}
}
