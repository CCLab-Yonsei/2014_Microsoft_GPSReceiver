package org.cclab.microsoft_gpsreceiver;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class SendPost extends AsyncTask<NameValuePair, Void, String> {

	private final String serverUri = "http://165.132.120.151";
	
	@Override
	protected String doInBackground(NameValuePair... params) {
		
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for(int i=0; i<params.length; i++) {
			pairs.add(params[i]);
		}
		
		HttpClient client = new DefaultHttpClient();
		
		HttpParams httpParams = client.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000); // connection timeout = 5s
		HttpConnectionParams.setSoTimeout(httpParams, 5000); // socket timeout = 5s
		
		HttpPost httpPost = new HttpPost(serverUri);
		
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs);
			httpPost.setEntity(entity);
			HttpResponse response = client.execute(httpPost);
			
			return EntityUtils.getContentCharSet(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// tasks to be executed after everything is done
	}

}
