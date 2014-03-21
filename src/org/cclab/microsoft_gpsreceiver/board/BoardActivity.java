package org.cclab.microsoft_gpsreceiver.board;

/* Main activity of board */

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.cclab.microsoft_gpsreceiver.R;
import org.cclab.microsoft_gpsreceiver.Utility;
import org.cclab.microsoft_gpsreceiver.board.Items.Board;
import org.cclab.microsoft_gpsreceiver.board.Items.Person;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class BoardActivity extends ListActivity {

	private static final String TAG = "Board Activity";
	
	private PullToRefreshListView ptrListView;
	private ListView listView;
	private BoardAdapter adapter;
	
	private ArrayList<Board> boardList;
	
	// For First Load
	ProgressDialog progress;
	boolean firstLoad = true;
	
	// For Load more
	View footer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);
		
		ptrListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		listView = ptrListView.getRefreshableView();
		
		// Set listeners
		ptrListView.setOnRefreshListener(refreshListener);
		ptrListView.setOnLastItemVisibleListener(lastListener);
		
		boardList = new ArrayList<Board>();
		// Set example data to lists
		// addExampleBorads();
		
		adapter = new BoardAdapter(this, R.layout.listview_board_row, boardList);
		ptrListView.setAdapter(adapter);
		
		// Set footer
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footer = inflater.inflate(R.layout.listview_board_footer, null, false);
		listView.addFooterView(footer);
		footer.setVisibility(View.GONE);
		
		// First Refresh
		new RefreshTaskOnCreate().execute();
		firstLoad = false;
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
		if(firstLoad == false)
			ptrListView.setRefreshing();

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.board, menu);
				
		return true;
	}
	
	
	// ============================================================
	// Listeners for Listview
	// ============================================================
	
	OnRefreshListener<ListView> refreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			Log.i("BoardACtivity", "OnRefreshListener");
			boardList.clear();
			new RefreshTask().execute();
		}
		
	};
	OnLastItemVisibleListener lastListener = new OnLastItemVisibleListener() {

		@Override
		public void onLastItemVisible() {
			// TODO Auto-generated method stub
			new LoadmoreTask().execute();
		}
		
	};

	// ============================================================
	// Communicate Tasks
	// ============================================================
	private class RefreshTaskOnCreate extends AsyncTask<Void, Void, Integer> {
		
		@Override
		protected void onPreExecute() {
			if(firstLoad) {
				progress = new ProgressDialog(BoardActivity.this);
				progress.setTitle("");
				progress.setMessage("로딩중...");
				progress.show();
				
			}
			super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			return getBoardListFromServer("http://165.132.120.151/board_list.aspx?mode=r&last=" +
					getLastBoardNo() );
		}
		
		@Override
		protected void onPostExecute(Integer param) {
			adapter.notifyDataSetChanged();
			progress.dismiss();
			
			toastErrorMessage(param);
			
			super.onPostExecute(param);
		}
	}	
	private class RefreshTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return getBoardListFromServer("http://165.132.120.151/board_list.aspx?mode=r&last="+
					getLastBoardNo() );
			
		}
		
		@Override
		protected void onPostExecute(Integer param) {
			adapter.notifyDataSetChanged();
			ptrListView.onRefreshComplete();
			
			toastErrorMessage(param);
			
			super.onPostExecute(param);
		}
	
	}
	private class LoadmoreTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected void onPreExecute() {
			footer.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return getBoardListFromServer("http://165.132.120.151/board_list.aspx?mode=lm&last="+
					getLastBoardNo());
			
			
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			toastErrorMessage(result);
			footer.setVisibility(View.GONE);
			super.onPostExecute(result);
		}
		
	}
	
	// ============================================================
	// OnButtonClick
	// ============================================================
	public void onWrite(View v) {
		Intent intent = new Intent(BoardActivity.this, WriteActivity.class);
		intent.putExtra("MODE", BoardParams.MODE_WRITE);	// MODE_WRITE
		startActivity(intent);
		
	}
	
	
	// ============================================================
	// Other Work
	// ============================================================
	private int getBoardListFromServer(String address) {
		
		URL url = null;
		try {
			
			url = new URL(address);
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document document = builder.parse(new InputSource(url.openStream()));
			Element element = document.getDocumentElement();
			
			
			
			
			for( Node aitem=element.getFirstChild(); aitem!=null; aitem=aitem.getNextSibling() ) {

				Items.Board board = new Items.Board();
				Person writer = writer = new Person();
				
				if(aitem.getNodeType() == Node.ELEMENT_NODE) {
					// notice or normal
					Element eleBoard = (Element)aitem;
					
					// Set kind
					if(eleBoard.getTagName().equals("notice"))
						board.kind = 1;
					else board.kind = 3;
					
					board.date = eleBoard.getAttribute("date");
					board.no = Integer.parseInt(eleBoard.getAttribute("no"));
					board.commentNum = Integer.parseInt(eleBoard.getAttribute("comments"));
					
					
					for (Node _aitem=aitem.getFirstChild(); _aitem!=null; _aitem=_aitem.getNextSibling()) {
						if(_aitem.getNodeType() == Node.ELEMENT_NODE) {
							
							Element eleIn = (Element)_aitem;
							String tagName = eleIn.getTagName();
							
							if(tagName.equals("writer")) {
								writer.sex = Integer.parseInt(eleIn.getAttribute("sex")) == 1 ? "남자" : "여자";
								writer.name = eleIn.getAttribute("name");
								writer.id = eleIn.getFirstChild().getTextContent();
								writer.hashedId = Utility.getHashedValue(BoardActivity.this, writer.id);
							}
							else if(tagName.equals("title"))
								board.title = eleIn.getFirstChild().getTextContent();
							else if(tagName.equals("content"))
								board.content = eleIn.getFirstChild().getTextContent();
							
						}
					} // inner for statement : _aitem
					
					board.writer = writer;
					boardList.add(board);
					
				}
				
			}

		} catch(MalformedURLException e) {
			return -2;
		} catch(IOException e) {
			return -1;
		} catch (Exception e) {
			Log.i(TAG, e.toString());
			return -3;
			
		} finally {
			
		}
		
		return 0;
	}
	private void toastErrorMessage(int state) {
		
		if(state == -1) {
			Toast.makeText(BoardActivity.this, "IOException 네트워크 상태가 좋지 않습니다.", Toast.LENGTH_SHORT).show();
		}
		else if(state == -2) {
			Toast.makeText(BoardActivity.this, "MalforemdURLException 서버가 응답하지 않습니다.",  Toast.LENGTH_SHORT).show();
		}
		else if(state == -3) {
			Toast.makeText(BoardActivity.this, "Exception 알 수 없는 오류.",  Toast.LENGTH_SHORT).show();
		}
	}
	private int getLastBoardNo() {
		
		int no = 0;
		int size = boardList.size();
		if(size != 0) {
			no = boardList.get(size-1).no;
		}
		
		return no;
	}

}

