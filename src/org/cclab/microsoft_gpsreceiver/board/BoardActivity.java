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
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class BoardActivity extends ListActivity implements OnRefreshListener<ListView>, OnItemClickListener, OnScrollListener {

	private static final String TAG = "Board Activity";
	
	private PullToRefreshListView ptrListView;
	private ListView listView;
	private BoardAdapter adapter;
	
	private ArrayList<Board> boardList;
	
	// Loading progress dialog
	ProgressDialog progress;
	boolean bFirstLoading = true;
	
	/////////////////////////////////
	// Variable for Loadmore
	////////////////////////////////
	
	// footer view
	private View mFooterView;

	private boolean mIsLoadingMore = false;
	private int mCurrentScrollState;
	private boolean bIsThereItemToLoad = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);
		
		ptrListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list_board);
		listView = ptrListView.getRefreshableView();
		
		// Set listeners
		ptrListView.setOnRefreshListener(this);
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(this);
		// ptrListView.setOnLastItemVisibleListener(lastListener);
		
		boardList = new ArrayList<Board>();	
		adapter = new BoardAdapter(this, R.layout.board_list_listview_row, boardList);
		ptrListView.setAdapter(adapter);
		
		// registerForContextMenu(listView);
		// Set footer view
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFooterView = (View)inflater.inflate(R.layout.board_footer_loadmore, null);
		mFooterView.setVisibility(View.GONE);
		listView.addFooterView(mFooterView);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		setRefreshing();
		super.onStart();

	}
	
	
	// ============================================================
	// Listeners method
	// ============================================================
	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		
		new RefreshTask().execute();
	}
	public void onWrite(View v) {
		Intent intent = new Intent(BoardActivity.this, WritingActivity.class);
		intent.putExtra("MODE", BoardParams.MODE_WRITE);	// MODE_WRITE
		startActivity(intent);
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(BoardActivity.this, CommentActivity.class);
		
		intent.putExtra("board", boardList.get(position-1));
		intent.putExtra("writer", boardList.get(position-1).writer);
		startActivity(intent);
	}


	// ============================================================
	// Communicate Tasks
	// ============================================================
	private class RefreshTaskOnCreate extends AsyncTask<Void, Void, Integer> {
		
		ArrayList<Items.Board> temp;
		
		@Override
		protected void onPreExecute() {
			
			temp = new ArrayList<Items.Board>();
			
			progress = new ProgressDialog(BoardActivity.this);
			progress.setTitle("");
			progress.setMessage("로딩중...");
			progress.show();
				
			
			super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			// return getBoardListFromServer("http://165.132.120.151/board_list.aspx?mode=r&last=" +
			//		getLastBoardNo() );
			return getBoardListFromServer("http://165.132.120.151/board_list.aspx?mode=r&last=-1"
					, temp);
		}
		
		@Override
		protected void onPostExecute(Integer param) {
			
			postWorkInCommon(param, temp, true);
			progress.dismiss();
			
			super.onPostExecute(param);
		}
	}	
	private class RefreshTask extends AsyncTask<Void, Void, Integer> {
		
		ArrayList<Items.Board> temp;
		
		@Override
		protected void onPreExecute() {
			temp = new ArrayList<Items.Board>();
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// return getBoardListFromServer("http://165.132.120.151/board_list.aspx?mode=r&last="+
			//		getLastBoardNo() );
			return getBoardListFromServer("http://165.132.120.151/board_list.aspx?mode=r&last=" +
					getLastItemNo(), temp);
			
		}
		
		@Override
		protected void onPostExecute(Integer param) {
			postWorkInCommon(param, temp, true);
			ptrListView.onRefreshComplete();
			
			super.onPostExecute(param);
		}
	
	}
	private class LoadMoreTask extends AsyncTask<Void, Void, Integer> {
		
		ArrayList<Items.Board> temp;
		
		@Override
		protected void onPreExecute() {
			
			temp = new ArrayList<Items.Board>();
			mFooterView.setVisibility(View.VISIBLE);
				
			super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return getBoardListFromServer("http://165.132.120.151/board_list.aspx?mode=lm&last=" +
					getLastItemNo(), temp);
		}
		
		@Override
		protected void onPostExecute(Integer param) {

			postWorkInCommon(param, temp, false);
			onLoadingMoreComplete();
			mFooterView.setVisibility(View.GONE);
			
			super.onPostExecute(param);
		}
	}	

	// ============================================================
	// Other Work
	// ============================================================
	private int getBoardListFromServer(String address, ArrayList<Items.Board> temp) {
		
		int total = 0;
		URL url = null;
		try {
			
			url = new URL(address);
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document document = builder.parse(new InputSource(url.openStream()));
			Element element = document.getDocumentElement();
			
			total = Integer.parseInt(element.getAttribute("total"));

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
								writer.id = eleIn.getAttribute("wid");
								writer.nickname = eleIn.getFirstChild().getTextContent();
								writer.hashedId = Utility.getHashedValue(BoardActivity.this, writer.id);
							}
							/*
							else if(tagName.equals("title"))
								board.title = eleIn.getFirstChild().getTextContent();
								*/
							else if(tagName.equals("content"))
								board.content = eleIn.getFirstChild().getTextContent();
							
						}
					} // inner for statement : _aitem
					
					board.writer = writer;
					temp.add(board);
					
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
		
		return total;
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
	private void setFirstRefreshing() {
		new RefreshTaskOnCreate().execute();
		bFirstLoading = false;
	}
	private void setRefreshing() {
		if(bFirstLoading) {
			setFirstRefreshing();
		}
		else {
			ptrListView.setRefreshing();
		}
	}
	// Toast errmsg and calculate remained item num in database
	private void postWorkInCommon(int param, ArrayList<Items.Board> temp, boolean listClear) {
		if(listClear)
			boardList.clear();
		for(int i=0; i<temp.size(); i++)
			boardList.add(temp.get(i));
		
		if(boardList.size() < param)
			bIsThereItemToLoad = true;
		else {
			bIsThereItemToLoad = false;
			listView.removeFooterView(mFooterView);
		}
		
		adapter.notifyDataSetChanged();
		toastErrorMessage(param);
	}
	private int getLastItemNo() {
		if(boardList.size() > 0)
			return boardList.get(boardList.size()-1).no;
		else return 0;
	}
	

	// ============================================================
	// Implement Loadmore
	// ============================================================
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		//bug fix: listview was not clickable after scroll
		
		if ( scrollState == OnScrollListener.SCROLL_STATE_IDLE )
    	{
      		view.invalidateViews();
    	}
		        	
		mCurrentScrollState = scrollState;


	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
		if (visibleItemCount == totalItemCount) {
			// mProgressBarLoadMore.setVisibility(View.GONE);
			return;
		}

		boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

		if (!mIsLoadingMore && loadMore
				&& mCurrentScrollState != SCROLL_STATE_IDLE && bIsThereItemToLoad) {
			// mProgressBarLoadMore.setVisibility(View.VISIBLE);
			Log.i(TAG, "LoadMore!");
			onLoadMore();
		}

	}
	
	// Called when if there are items to load more end of listview
	private void onLoadMore() {
		mIsLoadingMore = true;
		new LoadMoreTask().execute();
	}
	private void onLoadingMoreComplete() {
		mIsLoadingMore = false;
	}

}

