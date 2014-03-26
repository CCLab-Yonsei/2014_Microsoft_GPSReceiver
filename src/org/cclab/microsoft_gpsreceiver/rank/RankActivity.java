package org.cclab.microsoft_gpsreceiver.rank;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.cclab.microsoft_gpsreceiver.R;
import org.cclab.microsoft_gpsreceiver.board.BoardActivity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RankActivity extends ListActivity {

	private static final String RANK_ADDRESS = "http://165.132.120.151/rank.aspx";
	private int mTotal;
	private ProgressDialog mProgress;
	
	private ArrayList<RankItem> rankList;
	private RankAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rank);
		
		rankList = new ArrayList<RankItem>();
		adapter = new RankAdapter(this, R.layout.rank_list_listview_row, rankList);
		setListAdapter(adapter);
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		
		new getRankListFromServer().execute();
		
	}
	
	private class getRankListFromServer extends AsyncTask<Void, Void, Integer> {

		ArrayList<RankItem> temp = new ArrayList<RankItem>();
		
		@Override
		protected void onPreExecute() {
			mProgress = new ProgressDialog(RankActivity.this);
			mProgress.setTitle("");
			mProgress.setMessage("로딩중...");
			mProgress.show();
			
			rankList.clear();
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			
			
					
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
						
						RankItem aRank = new RankItem();
						
//						Log.i("rank", "rank = " + String.valueOf(rank));
//						Log.i("rank", "id = " + ((Element)aitem).getAttribute("id"));
						aRank.rank = rank;
						aRank.contributor = ((Element)aitem).getAttribute("id");
	
						int gpsCount = Integer.parseInt(aitem.getFirstChild().getTextContent());
						aRank.gpsCount = gpsCount;
//						Log.i("rank", "gps_cnt = " + String.valueOf(gps_cnt));
						aRank.percentage = (double)gpsCount/(double)mTotal * 100;
//						Log.i("rank", "percentage = " + String.valueOf(gps_cnt/mTotal * 100));
						temp.add(aRank);
					}
				}
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return -1;
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
				return -2;
			} catch (SAXException e) {
				e.printStackTrace();
				return -3;
			} catch (IOException e) {
				e.printStackTrace();
				return -4;
			}
			
			return 0;
			
		}
		
		@Override
		protected void onPostExecute(Integer param) {
			
			rankList.addAll(temp);
			adapter.notifyDataSetChanged();
			mProgress.dismiss();
			
			if(param == -4)
				Toast.makeText(RankActivity.this, "서버에 연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
			else if(param != 0) Toast.makeText(RankActivity.this, param + "알 수 없는 에러 발생. 게시판에 신고해주세요.", Toast.LENGTH_SHORT).show();
			
			
			
			
		}
	}
	
}
