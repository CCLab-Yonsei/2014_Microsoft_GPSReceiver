package org.cclab.microsoft_gpsreceiver.board;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class CommentActivity extends ListActivity implements OnRefreshListener<ListView> {

	private final static String TAG = "Reading Activity";

	private ArrayList<Items.Base> commentList;
	
	private PullToRefreshListView ptrListView;
	private CommentAdapter adapter;
	
	private ProgressDialog progress;
	private boolean bFirstLoading = true;
	
	private Board board;
	
	private EditText et_nickname;
	private EditText et_content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_comment);
		
		Log.i(TAG, "OnCreate");
	
		commentList = new ArrayList<Items.Base>();
		
		Intent intent = getIntent();
		board = intent.getExtras().getParcelable("board");
		board.writer = intent.getExtras().getParcelable("writer");
		
		ptrListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list_comment);
		adapter = new CommentAdapter(this, R.layout.board_view_article_listview_row,
				R.layout.board_view_comment_listview_row,
				commentList);
		ptrListView.setOnRefreshListener(this);
		ptrListView.setAdapter(adapter);
		
		et_nickname = (EditText)findViewById(R.id.edittext_comment_nickname);
		et_content = (EditText)findViewById(R.id.edittext_comment_content);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		setRefreshing();
		
	}
	
	/**
	 * Communication tasks
	 *  
	 * @author gnoowik
	 *
	 */
	private class RefreshTaskOnCreate extends AsyncTask<Void, Void, Integer> {
		
		ArrayList<Items.Base> temp;
		
		@Override
		protected void onPreExecute() {
			
			temp = new ArrayList<Items.Base>();
			
			progress = new ProgressDialog(CommentActivity.this);
			progress.setTitle("");
			progress.setMessage(getResources().getString(R.string.board_loading));
			progress.show();

			super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			return getCommentListFromServer("http://165.132.120.151/comment_list.aspx?bid=" + board.no, temp);
		}
		
		@Override
		protected void onPostExecute(Integer param) {
			super.onPostExecute(param);		

			commentList.clear();
			commentList.add(board);
			commentList.addAll(temp);
			adapter.notifyDataSetChanged();
			
			progress.dismiss();
			toastErrorMessage(param);
			
		}
	}	
	private class RefreshTask extends AsyncTask<Void, Void, Integer> {
		
		ArrayList<Items.Base> temp;
		
		@Override
		protected void onPreExecute() {
			temp = new ArrayList<Items.Base>();
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(Void... params) {
			return getCommentListFromServer("http://165.132.120.151/comment_list.aspx?bid=" + board.no, temp);
			
		}
		
		@Override
		protected void onPostExecute(Integer param) {
			super.onPostExecute(param);
			
			commentList.clear();
			commentList.add(board);
			commentList.addAll(temp);
			
			adapter.notifyDataSetChanged();
			
			ptrListView.onRefreshComplete();
			toastErrorMessage(param);
			
		}
	}
	
	/**
	 * 
	 * @author gnoowik
	 *
	 */
	private class SendPostTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected void onPreExecute() {
			
			progress = new ProgressDialog(CommentActivity.this);
			
			progress.setMessage("로딩중입니다..");
			progress.setIndeterminate(true);
			progress.setCancelable(true);
			progress.show();
			
			super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(String... params) {
			
			try {
				
				// Set URL and connect
				URL url = new URL("http://165.132.120.151/comment_writer.aspx");
				HttpURLConnection http = (HttpURLConnection)url.openConnection();
				
				// Set Sending Mode
				http.setDefaultUseCaches(false);
				http.setDoInput(true);
				http.setDoOutput(true);
				http.setRequestMethod("POST");
				
				http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
				
				// Send to Server
				StringBuffer buffer = new StringBuffer();
				buffer.append("bid").append("=").append(params[0]).append("&");
				buffer.append("wid").append("=").append(params[1]).append("&");
				buffer.append("nickname").append("=").append(params[2]).append("&");
				buffer.append("content").append("=").append(params[3]);
				
				String str = buffer.toString();
				OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "utf-8");
				PrintWriter writer = new PrintWriter(outStream);
				writer.write(str);
				writer.flush();
		
				
				// Receive from Server
				http.getInputStream();
				/*
				InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "utf-8");
				BufferedReader reader = new BufferedReader(tmp);
				StringBuilder builder = new StringBuilder();
				
				while( (str=reader.readLine() ) != null ) {
					builder.append(str + "\n");
				}
				
				Log.i("Writing Activity", "DEBUG: " + builder.toString());
				*/
				http.disconnect();
				
				
			}
			catch(MalformedURLException e) {
				return -2;
			}				
			catch(IOException e) {
				Log.i("Comment Activity", e.toString());
				return -1;
			}
			
			
			return 0;
		}
		
		
		@Override
		protected void onPostExecute(Integer param) {
			progress.dismiss();
			
			if(param == 0) {
				Toast.makeText(CommentActivity.this, getResources().getString(R.string.comment_complete), Toast.LENGTH_SHORT).show();
				new RefreshTask().execute();
			}
			else if(param == -1) {
				Toast.makeText(CommentActivity.this, getResources().getString(R.string.board_error_io), Toast.LENGTH_SHORT).show();
			}
			else if(param == -2) {
				Toast.makeText(CommentActivity.this, getResources().getString(R.string.board_error_malformedurl),  Toast.LENGTH_SHORT).show();
			}
			
			et_nickname.setText("");
			et_content.setText("");
			
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(et_nickname.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);
			
			super.onPostExecute(param);
		}

		
	}

	// ============================================================
	// Other Work
	// ============================================================
	private int getCommentListFromServer(String address, ArrayList<Items.Base> temp) {
		
		URL url = null;
		try {
			
			url = new URL(address);
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document document = builder.parse(new InputSource(url.openStream()));
			Element element = document.getDocumentElement();
			
			
			for( Node aitem=element.getFirstChild(); aitem!=null; aitem=aitem.getNextSibling() ) {
	
				Items.Comment comment = new Items.Comment();
				Person writer = new Person();
				
				if(aitem.getNodeType() == Node.ELEMENT_NODE) {
					
					Element eleBoard = (Element)aitem;
										
					comment.date = eleBoard.getAttribute("date");
					
					for (Node _aitem=aitem.getFirstChild(); _aitem!=null; _aitem=_aitem.getNextSibling()) {
						if(_aitem.getNodeType() == Node.ELEMENT_NODE) {
							
							Element eleIn = (Element)_aitem;
							String tagName = eleIn.getTagName();
							
							if(tagName.equals("writer")) {
								// writer.sex = Integer.parseInt(eleIn.getAttribute("sex")) == 1 ? "남자" : "여자";
								// writer.name = eleIn.getAttribute("name");
								writer.id = eleIn.getAttribute("wid");
								writer.nickname = eleIn.getFirstChild().getTextContent();
								writer.hashedId = Utility.getHashedValue(CommentActivity.this, writer.id);
							}
							
							else if(tagName.equals("content"))
								comment.content = eleIn.getFirstChild().getTextContent();
							
						}
					} // inner for statement : _aitem
					
					comment.writer = writer;
					temp.add(comment);
					
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
				Toast.makeText(CommentActivity.this, getResources().getString(R.string.board_error_io), Toast.LENGTH_SHORT).show();
			}
			else if(state == -2) {
				Toast.makeText(CommentActivity.this, getResources().getString(R.string.board_error_malformedurl),  Toast.LENGTH_SHORT).show();
			}
			else if(state == -3) {
				Toast.makeText(CommentActivity.this, getResources().getString(R.string.board_error),  Toast.LENGTH_SHORT).show();
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
	
	
	// ============================================================
	// Listeners
	// ============================================================
	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		new RefreshTask().execute();
	}
	public void onCommentSend(View v) {
		
		String str_nickname = et_nickname.getText().toString();
		String str_content = et_content.getText().toString();
		
		if(str_nickname.trim().length() == 0 ||
				str_content.trim().length() == 0) {
			Toast.makeText(this, getResources().getString(R.string.writing_formcheck_empty), Toast.LENGTH_SHORT).show();
		}
		else if(str_nickname.contains("<") || str_nickname.contains(">") ||
				str_content.contains("<") || str_content.contains(">"))
			Toast.makeText(this, getResources().getString(R.string.writing_formcheck_script), Toast.LENGTH_SHORT).show();

		else {
			new SendPostTask().execute(
					String.valueOf(board.no), Utility.getStudentId(this), et_nickname.getText().toString(),
					et_content.getText().toString() );
			
		}
			
	}
}
