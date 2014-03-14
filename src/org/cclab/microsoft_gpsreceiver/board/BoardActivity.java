package org.cclab.microsoft_gpsreceiver.board;

/* Board main activity */

import org.cclab.microsoft_gpsreceiver.R;
import org.cclab.microsoft_gpsreceiver.R.layout;
import org.cclab.microsoft_gpsreceiver.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BoardActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.board, menu);
				
		return true;
	}

}
