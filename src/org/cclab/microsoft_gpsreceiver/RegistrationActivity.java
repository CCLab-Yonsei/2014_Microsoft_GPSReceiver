package org.cclab.microsoft_gpsreceiver;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void onConfirmListener(View v) {
		
		boolean valid = true;
		
		EditText editText = (EditText)findViewById(R.id.editText_userid);
		final String studentId = editText.getText().toString();
		
		// validation check
		try {
			if(studentId.length() != 10) {
				valid = false;
			}
			
			final int studentIdInt = Integer.valueOf(studentId);
			if(studentIdInt < 2000000000 && studentIdInt > 2015000000) {
				valid = false;
			}	
		}
		catch(Exception e) {
			valid = false;
			e.printStackTrace();
		}
		
		if(studentId != null && valid) {
			SharedPreferences settings = getSharedPreferences(Constants.PREFS, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(Constants.PREFS_USERID, studentId);
			editor.putBoolean(Constants.PREFS_INITIALIZED, true);
			
			editor.commit();
			
			setResult(Activity.RESULT_OK);
			finish();
		}
		else {
			Toast.makeText(this, getResources().getString(R.string.main_toast_studentid_notconfirmed), Toast.LENGTH_SHORT).show();
		}
		
	}
}
