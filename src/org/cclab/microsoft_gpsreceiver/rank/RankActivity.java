package org.cclab.microsoft_gpsreceiver.rank;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.cclab.microsoft_gpsreceiver.R;
import org.cclab.microsoft_gpsreceiver.R.layout;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class RankActivity extends Activity {

	private static final String mRankAddress = "http://165.132.120.151/rank.aspx";
	private int mTotal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rank);
		
		new getRankListFromServer().execute();
		
	}
	
	private class getRankListFromServer extends AsyncTask<Void, Void, ArrayList<ArrayList<String>>> {

		@Override
		protected ArrayList<ArrayList<String>> doInBackground(Void... params) {
			// TODO Auto-generated method stub	
			
			ArrayList<ArrayList<String>> RankList = new ArrayList<ArrayList<String>>();
					
			try {
				URL rank_url = new URL(mRankAddress);
											
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				
				Document document = builder.parse(new InputSource(rank_url.openStream()));
				Element element = document.getDocumentElement();
				
				mTotal = Integer.parseInt(element.getAttribute("total"));
				
				Log.i("rank", "Total Value = " + String.valueOf(mTotal));
								
				int rank = 0;
				
				for(Node aitem=element.getFirstChild(); aitem!=null; aitem=aitem.getNextSibling()) {
					
					if(aitem.getNodeType() == Node.ELEMENT_NODE) {
						rank++;
						
						ArrayList<String> RankRaw = new ArrayList<String>();
						
						Log.i("rank", "rank = " + String.valueOf(rank));
						Log.i("rank", "id = " + ((Element)aitem).getAttribute("id"));
						
						
						RankRaw.add(String.valueOf(rank));
						RankRaw.add( ((Element)aitem).getAttribute("id") );
	
						int gps_cnt = Integer.parseInt(aitem.getFirstChild().getTextContent());
						Log.i("rank", "gps_cnt = " + String.valueOf(gps_cnt));
						
						RankRaw.add(String.valueOf(gps_cnt));
						RankRaw.add(String.valueOf(gps_cnt/mTotal * 100));
						Log.i("rank", "percentage = " + String.valueOf(gps_cnt/mTotal * 100));
						RankList.add(RankRaw);			
					}
				}
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return RankList;
		}
		
		@Override
		protected void onPostExecute(ArrayList<ArrayList<String>> RankList) {
			
			LinearLayout rankVerticalLinear = (LinearLayout) findViewById(R.id.rankactivity_linearlayout_ranklist);
			
			for(ArrayList<String> rankRaw : RankList) {
				
				LinearLayout rankListRaw = new LinearLayout(RankActivity.this);
				TextView tv_rank = new TextView(RankActivity.this);
				TextView tv_contributor_id = new TextView(RankActivity.this);
				TextView tv_gps_cnt = new TextView(RankActivity.this);
				TextView tv_percentage = new TextView(RankActivity.this);
				
				tv_rank.setText(rankRaw.get(0));
				tv_contributor_id.setText(rankRaw.get(1));
				tv_gps_cnt.setText(rankRaw.get(2));
				tv_percentage.setText(rankRaw.get(3));
				
				rankListRaw.addView(tv_rank);
				rankListRaw.addView(tv_contributor_id);
				rankListRaw.addView(tv_gps_cnt);
				rankListRaw.addView(tv_percentage);
				
				rankVerticalLinear.addView(rankListRaw);
				
			}
			
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}

}
