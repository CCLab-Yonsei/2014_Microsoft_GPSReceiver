package org.cclab.microsoft_gpsreceiver.widget;

import org.cclab.microsoft_gpsreceiver.GpsService;
import org.cclab.microsoft_gpsreceiver.R;
import org.cclab.microsoft_gpsreceiver.Utility;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;


public class Widget extends AppWidgetProvider {
	
	public static final String intentCurrentStateLoggingOn = "org.cclab.microsoft_gpsreceiver.widget.action.WIDGET_LOGGING_ON";
	public static final String intentCurrentStateLoggingOff = "org.cclab.microsoft_gpsreceiver.widget.action.WIDGET_LOGGING_OFF";
	
	private final String actionButtonClick = "org.cclab.microsoft_gpsreceiver.widget.action.WIDGET_BUTTON_CLICK";
	
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		Log.i("Widget", "onUpdate");
		
		for(int i=0; i < appWidgetIds.length; i++) {
			int appWidgetId = appWidgetIds[i];
			
			// 
			Intent onclickIntent = new Intent(actionButtonClick);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, onclickIntent, 0);
			
			RemoteViews widgetLayoutView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			widgetLayoutView.setOnClickPendingIntent(R.id.widget_textview, pendingIntent);
			
			// 
			// check if GPS logging service is running 
			boolean isServiceRunning = Utility.isServiceRunning(context, GpsService.class.getName());
			
			RemoteViews remoteWidgetLayoutView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			if(isServiceRunning) {
				remoteWidgetLayoutView.setTextViewText(R.id.widget_textview, context.getResources().getString(R.string.widget_off));
			}
			else {
				remoteWidgetLayoutView.setTextViewText(R.id.widget_textview, context.getResources().getString(R.string.widget_on));
			}
			
			//
			appWidgetManager.updateAppWidget(appWidgetId, widgetLayoutView);
		}
	}
	 
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		
		Log.i("Widget", "onReceive");
		Log.i("Widget", "Action: " + intent.getAction());
		
		RemoteViews remoteWidgetLayoutView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		ComponentName watchWidget = new ComponentName(context, Widget.class);
		
		// check if GPS logging service is running 
		boolean isServiceRunning = Utility.isServiceRunning(context, GpsService.class.getName());
		if(intent.getAction().equals(actionButtonClick)) {
			if(isServiceRunning) {
				context.stopService(new Intent(context, GpsService.class));
				remoteWidgetLayoutView.setTextViewText(R.id.widget_textview, context.getResources().getString(R.string.widget_off));
			}
			else {
				context.startService(new Intent(context, GpsService.class));
				remoteWidgetLayoutView.setTextViewText(R.id.widget_textview, context.getResources().getString(R.string.widget_on));
			}
		}
		// from start logging from MainActivity
		else if(intent.getAction().equals(intentCurrentStateLoggingOn)) {
			Log.i("Widget", "On");
			remoteWidgetLayoutView.setTextViewText(R.id.widget_textview, context.getResources().getString(R.string.widget_on));
		}
		// from finish logging from MainActivity
		else if(intent.getAction().equals(intentCurrentStateLoggingOff)) {
			Log.i("Widget", "Off");
			remoteWidgetLayoutView.setTextViewText(R.id.widget_textview, context.getResources().getString(R.string.widget_off));
		}
		
		// update
		(AppWidgetManager.getInstance(context)).updateAppWidget(watchWidget, remoteWidgetLayoutView);
	 }
	 
	 @Override
	 public void onDeleted(Context context, int[] appWidgetIds) {
		 super.onDeleted(context, appWidgetIds);
	 }
	 
	 @Override
	 public void onDisabled(Context context) {
		 super.onDisabled(context);
	 }
	 
}