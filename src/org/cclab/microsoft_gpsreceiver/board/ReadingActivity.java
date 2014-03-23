package org.cclab.microsoft_gpsreceiver.board;

import java.util.ArrayList;

import org.cclab.microsoft_gpsreceiver.R;
import org.cclab.microsoft_gpsreceiver.board.Items.Board;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class ReadingActivity extends ListActivity {

	private final static String TAG = "Reading Activity";

	private ArrayList<Items.Base> commentList;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reading);

		Log.i(TAG, "OnCreate");
	
		commentList = new ArrayList<Items.Base>();
		
		Intent intent = getIntent();
		Board board = intent.getExtras().getParcelable("board");
		board.writer = intent.getExtras().getParcelable("writer");
		
		
	}
	
	
	private class GetCommentTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
		}
		
	}
	
}
