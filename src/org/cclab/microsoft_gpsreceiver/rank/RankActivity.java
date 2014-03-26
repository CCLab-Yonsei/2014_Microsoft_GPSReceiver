package org.cclab.microsoft_gpsreceiver.rank;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.cclab.microsoft_gpsreceiver.R;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RankActivity extends Activity {

	private static final String RANK_ADDRESS = "http://165.132.120.151/rank.aspx";
	private int mTotal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rank);
		
		new getRankListFromServer().execute();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	
	private class getRankListFromServer extends AsyncTask<Void, Void, ArrayList<ArrayList<String>>> {

		@Override
		protected ArrayList<ArrayList<String>> doInBackground(Void... params) {
			
			ArrayList<ArrayList<String>> rankList = new ArrayList<ArrayList<String>>();
					
			try {
				URL rank_url = new URL(RANK_ADDRESS);
											
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				
				Document document = builder.parse(new InputSource(rank_url.openStream()));
				Element element = document.getDocumentElement();
				
				mTotal = Integer.parseInt(element.getAttribute("total"));
				
//				Log.i("rank", "Total Value = " + String.valueOf(mTotal));
								
				int rank = 0;
				
				for(Node aitem=element.getFirstChild(); aitem!=null; aitem=aitem.getNextSibling()) {
					
					if(aitem.getNodeType() == Node.ELEMENT_NODE) {
						rank++;
						
						ArrayList<String> rankRaw = new ArrayList<String>();
						
//						Log.i("rank", "rank = " + String.valueOf(rank));
//						Log.i("rank", "id = " + ((Element)aitem).getAttribute("id"));
						rankRaw.add(String.valueOf(rank));
						rankRaw.add(((Element)aitem).getAttribute("id"));
	
						int gpsCount = Integer.parseInt(aitem.getFirstChild().getTextContent());
//						Log.i("rank", "gps_cnt = " + String.valueOf(gps_cnt));
						
						rankRaw.add(String.valueOf(gpsCount));
						rankRaw.add(String.valueOf(gpsCount/mTotal * 100));
//						Log.i("rank", "percentage = " + String.valueOf(gps_cnt/mTotal * 100));
						rankList.add(rankRaw);
					}
				}
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return rankList;
		}
		
		@Override
		protected void onPostExecute(ArrayList<ArrayList<String>> rankList) {
			
			LinearLayout rankVerticalLinear = (LinearLayout) findViewById(R.id.rankactivity_linearlayout_ranklist);
			
			for(ArrayList<String> rankRaw : rankList) {
				
				final String rank = rankRaw.get(0); 
				final String contributor = rankRaw.get(1);
				final String gpsCount = rankRaw.get(2);
				final String percentage = rankRaw.get(3);
				
				LinearLayout rankListRaw = new LinearLayout(RankActivity.this);
				TextView tvRank = new TextView(RankActivity.this);
				TextView tvContributorId = new TextView(RankActivity.this);
				TextView tvGpsCount = new TextView(RankActivity.this);
				TextView tvPercentage = new TextView(RankActivity.this);
				
				tvRank.setText(rank);
				tvContributorId.setText(contributor);
				tvGpsCount.setText(gpsCount);
				tvPercentage.setText(percentage);
				
				rankListRaw.addView(tvRank);
				rankListRaw.addView(tvContributorId);
				rankListRaw.addView(tvGpsCount);
				rankListRaw.addView(tvPercentage);
				
				rankVerticalLinear.addView(rankListRaw);
				
			}
			
		}
	}
	
}
