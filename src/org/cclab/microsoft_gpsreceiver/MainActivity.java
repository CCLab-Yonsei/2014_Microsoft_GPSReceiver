package org.cclab.microsoft_gpsreceiver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
	}
	
	public void onLogging(View v) {
		ToggleButton button_startstop = (ToggleButton)findViewById(R.id.togglebutton_startstop);
		TextView tv = (TextView)findViewById(R.id.text_loggingstatus);
		
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
