package org.cclab.microsoft_gpsreceiver.board;

/* Main activity of board */

import org.cclab.microsoft_gpsreceiver.R;
import org.cclab.microsoft_gpsreceiver.R.layout;
import org.cclab.microsoft_gpsreceiver.R.menu;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BoardActivity extends ListActivity {

	private PullToRefreshListView listView;
	private BoardAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);
		
		listView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		
		// Set listeners
		listView.setOnRefreshListener(refreshListener);
		listView.setOnLastItemVisibleListener(lastListener);
		
		adapter = new BoardAdapter()
		
		
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
			
		}
		
	};
	
	OnLastItemVisibleListener lastListener = new OnLastItemVisibleListener() {

		@Override
		public void onLastItemVisible() {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	private class GetBoardsTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
	/*
	* Adapter for board item
	*/
	private class BoardAdapter extends ArrayAdapter {

		public BoardAdapter(Context context, int resource) {
			super(context, resource);
			// TODO Auto-generated constructor stub
		}
		
	}

}

