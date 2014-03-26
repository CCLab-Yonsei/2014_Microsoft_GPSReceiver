package org.cclab.microsoft_gpsreceiver.board;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.cclab.microsoft_gpsreceiver.R;
import org.cclab.microsoft_gpsreceiver.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class WritingActivity extends Activity {

	EditText et_title;
	EditText et_nickname;
	EditText et_content;

	// Only visible 'write' mode
	CheckBox cb_addnotice;
	
	int mode;
	
	ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_writing);

		Intent intent = getIntent();
		mode = intent.getIntExtra("MODE", -1);
		
		if(mode == -1) {
			finish();
		}
		
		// Initialize Components
		// et_title = (EditText)findViewById(R.id.edittext_title);
		et_nickname = (EditText)findViewById(R.id.board_writing_edittext_nickname);
		et_content = (EditText)findViewById(R.id.board_writing_edittext_content);
		cb_addnotice = (CheckBox)findViewById(R.id.board_writing_checkbox_addnotice);
		
		init();
		
	}
	
	
	private void init() {
		
		if(mode == BoardParams.MODE_WRITE) {
			
			if(Utility.isAdmin(this))
				cb_addnotice.setVisibility(View.VISIBLE);
		}
		
		else if(mode == BoardParams.MODE_MODIFY) {
			
		}
	}
	
	
	public void onSend(View v) {
		
		int state = 0;
		
		String id = Utility.getStudentId(this);
		// String title = et_title.getText().toString();
		String content = et_content.getText().toString();
		String nickname = et_nickname.getText().toString();
		String kind;
		
		if(Utility.isAdmin(this)) 
			kind = String.valueOf( cb_addnotice.isChecked() ? 1 : 3 );
		else {
			kind = "3";
		}
		
		// if(title.length() == 0)
		//	title = "제목 없음";
		if(content.length() == 0)
			state = -1; 
		if(nickname.length() == 0)
			state = -2;
		
		if(state == 0) {
			new SendPostTask().execute(id, content, kind, nickname);
		}
		else Toast.makeText(this, "닉네임과 글내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
	
		Log.i("Wrting Activity", kind);
	}
	
	private class SendPostTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected void onPreExecute() {
			
			dialog = new ProgressDialog(WritingActivity.this);
			dialog.setTitle("잠시만 기다려주세요");
			dialog.setMessage("잠깐 기다리라고..");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(String... params) {
			
			
			
			try {
				
				// Set URL and connect
				URL url = new URL("http://165.132.120.151/board_writer.aspx");
				HttpURLConnection http = (HttpURLConnection)url.openConnection();
				
				// Set Sending Mode
				http.setDefaultUseCaches(false);
				http.setDoInput(true);
				http.setDoOutput(true);
				http.setRequestMethod("POST");
				
				http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
				
				// Send to Server
				StringBuffer buffer = new StringBuffer();
				buffer.append("wid").append("=").append(params[0]).append("&");
				buffer.append("content").append("=").append(params[1]).append("&");
				buffer.append("kind").append("=").append(params[2]).append("&");
				buffer.append("nickname").append("=").append(params[3]);
				
				String str = buffer.toString();
				OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "utf-8");
				PrintWriter writer = new PrintWriter(outStream);
				writer.write(str);
				writer.flush();
		
				
				// Receive from Server
				http.getInputStream();
				/*
				InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "utf-8");
				BufferedReader reader = new BufferedReader(tmp);
				StringBuilder builder = new StringBuilder();
				
				while( (str=reader.readLine() ) != null ) {
					builder.append(str + "\n");
				}
				
				Log.i("Writing Activity", "DEBUG: " + builder.toString());
				*/
				http.disconnect();
				
				
			}
			catch(MalformedURLException e) {
				return -2;
			}				
			catch(IOException e) {
				Log.i("Writing Activity", e.toString());
				return -1;
			}
			
			
			return 0;
		}
		
		
		@Override
		protected void onPostExecute(Integer param) {
			dialog.dismiss();
			
			if(param == 0) {
				Toast.makeText(WritingActivity.this, "글 작성이 완료되었습니다.", Toast.LENGTH_SHORT).show();
				finish();
			}
			else if(param == -1) {
				Toast.makeText(WritingActivity.this, "네트워크 상태가 좋지 않습니다.", Toast.LENGTH_SHORT).show();
			}
			else if(param == -2) {
				Toast.makeText(WritingActivity.this, "서버가 응답하지 않습니다.",  Toast.LENGTH_SHORT).show();
			}
			
			super.onPostExecute(param);
		}

		
	}
	
	
}
