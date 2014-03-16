package org.cclab.microsoft_gpsreceiver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;


public class Widget extends AppWidgetProvider {
	
	 private Context context;
	 private String GPS_START = "org.cclab.microsoft_gpsreceiver.widget.action.GPSSTART";
	 private String GPS_STOP = "org.cclab.microsoft_gpsreceiver.widget.action.GPSSTOP";
			 

	@Override
	 public void onEnabled(Context context) {
		 super.onEnabled(context);
	 }
	 
	 @Override
	 public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		 Log.i("gpswidget", "updated");
		 this.context = context;
		 super.onUpdate(context, appWidgetManager, appWidgetIds);
		 
		 for(int i=0; i < appWidgetIds.length; i++) {
			 int appWidgetId = appWidgetIds[i];
			 RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			 
			 Intent StartIntent = new Intent(GPS_START);
			 Intent StopIntent = new Intent(GPS_STOP);
			 //StopIntent.setAction(GPS_STOP);
			 
			 PendingIntent startPendingIntent = PendingIntent.getBroadcast(context, 0, StartIntent, 0);
			 PendingIntent stopPendingIntent = PendingIntent.getBroadcast(context, 0, StopIntent, 0);
			 views.setOnClickPendingIntent(R.id.widget_startbutton, startPendingIntent);
			 views.setOnClickPendingIntent(R.id.widget_stopbutton, stopPendingIntent);
			 
			 appWidgetManager.updateAppWidget(appWidgetId, views);
		 }
	 }
	 
	 @Override
	 public void onReceive(Context context, Intent intent) {
		 Log.i("gpswidget", "receive");
		 
		 if(intent.getAction().equals(GPS_START)) {		 
			 Log.i("gpswidget", "start");
			 context.startService(new Intent(context, GpsService.class));
		 }
		 else if(intent.getAction().equals(GPS_STOP)) {
			 Log.i("gpswidget", "stop");
			 context.stopService(new Intent(context, GpsService.class));
		 }
		 
		 super.onReceive(context, intent);
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