package org.cclab.microsoft_gpsreceiver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

public class SendPost extends AsyncTask<String, Void, String> {

	private final String urlServer = "http://165.132.120.151/msproject/gettxt";

	@Override
	protected String doInBackground(String... params) {
		
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		
		final String pathToOurFile = params[0];
		final String lineEnd = "\r\n";
		final String twoHyphens = "--";
		final String boundary = "*****";
		
		int bytesRead;
		int bytesAvailable;
		int bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		
		int responseCode = -1;
		
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile));
			
			URL url = new URL(urlServer);
			connection = (HttpURLConnection)url.openConnection();
			
			// allow inputs & outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			
			// enable POST method
			connection.setRequestMethod("POST");
			
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			
			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);;
			outputStream.writeBytes("Content-Disposition: form-data; name=\"file1\";filename=\"" + pathToOurFile +"\"" + lineEnd);
			outputStream.writeBytes(lineEnd);
			
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			
			// read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			
			while (bytesRead > 0)
			{
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// Responses from the server (code and message)
			final int serverResponseCode = connection.getResponseCode();
			final String serverResponseMessage = connection.getResponseMessage();

			responseCode = serverResponseCode;
			
			Log.i("GpsService", "ResponseCode: " + serverResponseCode);
			Log.i("GpsService", "ResponseMessage: " + serverResponseMessage);
			
			fileInputStream.close();
			outputStream.flush();
			outputStream.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return responseCode + "";
	}

	@Override
	protected void onPostExecute(String result) {
		// tasks to be executed after everything is done
	}

}