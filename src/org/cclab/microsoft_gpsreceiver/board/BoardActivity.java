package org.cclab.microsoft_gpsreceiver.board;

/* Main activity of board */

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.cclab.microsoft_gpsreceiver.R;
import org.cclab.microsoft_gpsreceiver.R.layout;
import org.cclab.microsoft_gpsreceiver.R.menu;
import org.cclab.microsoft_gpsreceiver.board.Items.Board;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class BoardActivity extends ListActivity {

	private PullToRefreshListView ptrListView;
	private ListView listView;
	private BoardAdapter adapter;
	
	private ArrayList<Board> boards;
	
	Button writeButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);
		
		ptrListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		listView = ptrListView.getRefreshableView();
		
		// Set listeners
		ptrListView.setOnRefreshListener(refreshListener);
		ptrListView.setOnLastItemVisibleListener(lastListener);
		
		boards = new ArrayList<Board>();
		// Set example data to lists
		// addExampleBorads();

		// adapter = new BoardAdapter()
		
		adapter = new BoardAdapter(this, R.layout.board_row, boards);
		ptrListView.setAdapter(adapter);
		
		writeButton = (Button)findViewById(R.id.button_write);

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.board, menu);
				
		return true;
	}
	
	
	/*
	 * Listeners for listview
	 */
	OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			new RefreshTask().execute("http://165.132.120.151/boardtest.aspx");
		}
		
	};
	
	OnLastItemVisibleListener lastListener = new OnLastItemVisibleListener() {

		@Override
		public void onLastItemVisible() {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	private class RefreshTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			URL url = null;
			try {
				
				url = new URL(params[0]);
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				
				Document document = builder.parse(new InputSource(url.openStream()));
				Element element = document.getDocumentElement();
				
				/*
				 * xml format
				 * <?xml version ... ?>
				 * <msbbs>
				 * <bitem date="" no="" comments="15" kind="1 or 2">
				 * 	<writer sex="1" grant="">id</writer>
				 * 	<title> title text </title>
				 * 	<content> contents text </content>
				 * </bitem>
				 * <bitem>
				 * 	...
				 * </bitem>
				 * </msbbs>
				 */
				
				NodeList items = element.getElementsByTagName("bitem");
				int len = items.getLength();
				for(int i=0; i<len; i++) {
					int kind = Integer.parseInt( ((Element)items.item(i)).getAttribute("no") );
					
					// String title = ((Element)(items.item(i))).getTextContent();
					Log.i("Refrsh Task", "" +kind);
					
					//String contents = bitem.getLastChild().getNodeValue();
					//String writer = ((Element)bitem).getAttribute("seq");
					
					/*
					boards.add( new Items.Board(
							title, contents, writer
							));
					*/
				}
				
				
				
			} catch(MalformedURLException e) {
				// URL is wrong
				Log.i("Refresh Task", "MalForemdURLException");
			} catch(IOException e) {
				// Opening connection is wrong
				Log.i("Refresh Task", "Opening connection is wrong");
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				
			}
			
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Void... params) {
			
		}
		
		@Override
		protected void onPostExecute(Void param) {
			adapter.notifyDataSetChanged();
			
			ptrListView.onRefreshComplete();
			super.onPostExecute(param);
		}

	
		
	}
	
	public void onWrite(View v) {
		Intent intent = new Intent(BoardActivity.this, DetailActivity.class);
		startActivity(intent);
	}

	

}

