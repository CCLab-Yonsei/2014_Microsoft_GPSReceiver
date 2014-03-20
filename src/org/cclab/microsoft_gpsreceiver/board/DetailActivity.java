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

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class DetailActivity extends Activity {

	EditText et_title;
	EditText et_content;
	
	ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		et_title = (EditText)findViewById(R.id.edittext_title);
		et_content = (EditText)findViewById(R.id.edittext_content);
	}
	
	public void onSend(View v) {
		
		String title = et_title.getText().toString();
		String content = et_content.getText().toString();
		
		if(title.length() == 0)
			title = "제목 없음";
		
		new SendPostTask().execute(title, content);
	
	}
	
	private class SendPostTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			
			dialog = new ProgressDialog(DetailActivity.this);
			dialog.setTitle("잠시만 기다려주세요");
			dialog.setMessage("잠깐 기다리라고..");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(String... params) {
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
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
				buffer.append("writer").append("=").append("2013311487").append("&");
				buffer.append("title").append("=").append(params[0]).append("&");
				buffer.append("content").append("=").append(params[1]).append("&");
				buffer.append("kind").append("=").append("1");
				
				String str = buffer.toString();
				OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "utf-8");
				PrintWriter writer = new PrintWriter(outStream);
				writer.write(str);
				writer.flush();
		
				
				// Receive from Server
				Log.d("SENDPOST", "Receive From Server");
				InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "utf-8");
				BufferedReader reader = new BufferedReader(tmp);
				StringBuilder builder = new StringBuilder();
				
				while( (str=reader.readLine() ) != null ) {
					builder.append(str + "\n");
				}
				
				http.disconnect();
				
				
			}
			catch(MalformedURLException e) {
				Log.d("SendPostTask", "URL Error!");
			}				
			catch(IOException e) {
				Log.d("SendPostTask", "Cannot connect to server");
			}
			
			
			return null;
		}
		
		
		@Override
		protected void onPostExecute(Void param) {
			dialog.dismiss();
			super.onPostExecute(param);
		}

		
	}
	
	
}
