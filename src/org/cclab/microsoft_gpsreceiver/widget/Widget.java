package org.cclab.microsoft_gpsreceiver.widget;

import org.cclab.microsoft_gpsreceiver.Constants;
import org.cclab.microsoft_gpsreceiver.GpsService;
import org.cclab.microsoft_gpsreceiver.R;
import org.cclab.microsoft_gpsreceiver.Utility;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Environment;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


public class Widget extends AppWidgetProvider {
	
	private static final String TAG = "MS Widget";
	
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		
		Log.i(TAG, "=====onEnabled=====");
		// Logging to logfile
		Utility.log(Environment.getExternalStorageDirectory().getPath() + "/data/" + context.getPackageName(),
				"[Widget] onEnabled");
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		Log.i(TAG, "onUpdate()");
		// Logging to logfile
		Utility.log(Environment.getExternalStorageDirectory().getPath() + "/data/" + context.getPackageName(),
				"[Widget] onUpdate");
		
		for(int i=0; i < appWidgetIds.length; i++) {
			int appWidgetId = appWidgetIds[i];
			
			// 
			Intent onclickIntent = new Intent(Constants.INTENT_WIDGET_ACTION_BUTTON_CLICK);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, onclickIntent, PendingIntent.FLAG_NO_CREATE);
			
			RemoteViews widgetLayoutView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			widgetLayoutView.setOnClickPendingIntent(R.id.widget_imgbtn, pendingIntent);
			
			// 
			// check if GPS logging service is running 
			boolean isServiceRunning = Utility.isServiceRunning(context, GpsService.class.getName());
			
			if(isServiceRunning) {
				Log.i(TAG, "onUpdate, isServiceRunning == true");
				
				widgetLayoutView.setTextViewText(R.id.widget_textview, context.getResources().getString(R.string.widget_on));
				widgetLayoutView.setImageViewResource(R.id.widget_imgbtn, R.drawable.ic_action_location_off_dark);
			}
			else {
				
				Log.i(TAG, "onUpdate, isServiceRunning == false");
				
				widgetLayoutView.setTextViewText(R.id.widget_textview, context.getResources().getString(R.string.widget_off));
				widgetLayoutView.setImageViewResource(R.id.widget_imgbtn, R.drawable.ic_action_location_found_dark);
			}
			
			Log.i(TAG, "onUpdate, Last Line");
			appWidgetManager.updateAppWidget(appWidgetId, widgetLayoutView);
		} // For statement End
	}
	 
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		
		// Logging to logfile
		Utility.log(Environment.getExternalStorageDirectory().getPath() + "/data/" + context.getPackageName(),
				"[Widget] onReceive Action(" + intent.getAction() + ")");
		
		Log.i(TAG, "onReceive()" + intent.getAction());
		RemoteViews remoteWidgetLayoutView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		ComponentName watchWidget = new ComponentName(context, Widget.class);
		
		// check if GPS logging service is running 
		boolean isServiceRunning = Utility.isServiceRunning(context, GpsService.class.getName());
		
		if(intent.getAction().equals(Constants.INTENT_WIDGET_ACTION_BUTTON_CLICK)) {
			Log.i(TAG, "onReceive, Click Event Occur");
			
			if(isServiceRunning) {
				
				if(Utility.isNetworkAvailable(context)) {
					Log.i(TAG, "onReceive, GPS Provider enabled, isServiceRunning == true");
					context.stopService(new Intent(context, GpsService.class));
		
					// Logging to logfile
					Utility.log(Environment.getExternalStorageDirectory().getPath() + "/data/" + context.getPackageName(),
							"[Service] Stopped by widgets");
					
					remoteWidgetLayoutView.setTextViewText(R.id.widget_textview, context.getResources().getString(R.string.widget_off));
					remoteWidgetLayoutView.setTextColor(R.id.widget_textview, Color.LTGRAY);
					remoteWidgetLayoutView.setImageViewResource(R.id.widget_imgbtn, R.drawable.ic_action_location_off_dark);
				}
				else {	// If networks is available, ignore widget click event.
					Toast.makeText(context, context.getResources().getString(R.string.main_toast_request_network), Toast.LENGTH_LONG).show();
				}
			}
			else {

				// check whether GPS is enabled or not
				final LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
				if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					Log.i(TAG, "onReceive, GPS Provider disabled");
					Toast.makeText(context, R.string.main_alert_enable_gps_question, Toast.LENGTH_SHORT).show();
					
					Intent settingsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(settingsIntent);
				}
				else {
					context.startService(new Intent(context, GpsService.class));
					
					// Logging to logfile
					Utility.log(Environment.getExternalStorageDirectory().getPath() + "/data/" + context.getPackageName(),
							"[Service] Started by widgets");
					
					remoteWidgetLayoutView.setTextViewText(R.id.widget_textview, context.getResources().getString(R.string.widget_on));
					remoteWidgetLayoutView.setTextColor(R.id.widget_textview, Color.WHITE);
					remoteWidgetLayoutView.setImageViewResource(R.id.widget_imgbtn, R.drawable.ic_action_location_found_dark);
				}
			}
		
		}
		// from start logging from MainActivity
		else if(intent.getAction().equals(Constants.INTENT_WIDGET_CURRENTSTATE_LOGGING_ON)) {
			Log.i(TAG, "onReceive, LOGGING ON");
			remoteWidgetLayoutView.setTextViewText(R.id.widget_textview, context.getResources().getString(R.string.widget_on));
			remoteWidgetLayoutView.setTextColor(R.id.widget_textview, Color.WHITE);
			remoteWidgetLayoutView.setImageViewResource(R.id.widget_imgbtn, R.drawable.ic_action_location_found_dark);
		}
		
		// from finish logging from MainActivity
		else if(intent.getAction().equals(Constants.INTENT_WIDGET_CURRENTSTATE_LOGGING_OFF)) {
			Log.i(TAG, "onReceive, LOGGING OFF");
			remoteWidgetLayoutView.setTextViewText(R.id.widget_textview, context.getResources().getString(R.string.widget_off));
			remoteWidgetLayoutView.setTextColor(R.id.widget_textview, Color.LTGRAY);
			remoteWidgetLayoutView.setImageViewResource(R.id.widget_imgbtn, R.drawable.ic_action_location_off_dark);
		}
		
		// update
		Log.i(TAG, "onReceive, Last Line");
		(AppWidgetManager.getInstance(context)).updateAppWidget(watchWidget, remoteWidgetLayoutView);
	 }
	 
	 @Override
	 public void onDeleted(Context context, int[] appWidgetIds) {
		 super.onDeleted(context, appWidgetIds);
		 Log.i(TAG, "=====onDeleted=====");
			// Logging to logfile
			Utility.log(Environment.getExternalStorageDirectory().getPath() + "/data/" + context.getPackageName(),
					"[Widget] onDeleted");
	 }
	 
	 @Override
	 public void onDisabled(Context context) {
		 super.onDisabled(context);
		 Log.i(TAG, "=====onDisabled=====");
			// Logging to logfile
			Utility.log(Environment.getExternalStorageDirectory().getPath() + "/data/" + context.getPackageName(),
					"[Widget] onDisabled");
	 }
	 
}