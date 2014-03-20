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
	
	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		
		Toast.makeText(this, getResources().getString(R.string.main_toast_studentid_needed), Toast.LENGTH_SHORT).show();
	}
	
	public void onConfirmListener(View v) {
		
		EditText editText = (EditText)findViewById(R.id.editText_userid);
		final String studentId = editText.getText().toString();
		
		if(studentId.length() > 0) {
			SharedPreferences settings = getSharedPreferences(Constants.PREFS, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(Constants.PREFS_USERID, studentId);
			editor.putBoolean(Constants.PREFS_INITIALIZED, true);
			
			editor.commit();
			
			setResult(Activity.RESULT_OK);
			finish();
		}
		else {
			Toast.makeText(this, getResources().getString(R.string.main_toast_studentid_needed), Toast.LENGTH_SHORT).show();
		}
		
	}
}
