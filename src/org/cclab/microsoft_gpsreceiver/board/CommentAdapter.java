package org.cclab.microsoft_gpsreceiver.board;

import java.util.ArrayList;

import org.cclab.microsoft_gpsreceiver.R;
import org.cclab.microsoft_gpsreceiver.Utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {

	ArrayList<Items.Base> items;
	Context context;
	int res_article;
	int res_comment;
	LayoutInflater inflater;
	
	View view_row1;
	
	public CommentAdapter(Context context, int res_article, int res_comment, ArrayList<Items.Base> items) {
		this.items = items;
		this.context = context;
		this.res_article = res_article;
		this.res_comment = res_comment;
	
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// ViewHolder holder = null;
		
		TextView tv_writer;
		TextView tv_content;
		TextView tv_date;
		TextView tv_comments_num = null;
		
		
		if(position == 0) {
			convertView = inflater.inflate(res_article, parent, false);
			
			tv_writer = (TextView)convertView.findViewById(R.id.board_view_article_textview_writer);
			tv_content = (TextView)convertView.findViewById(R.id.board_view_article_textview_contents);
			tv_date = (TextView)convertView.findViewById(R.id.board_view_article_textview_date);
			tv_comments_num = (TextView)convertView.findViewById(R.id.board_view_article_textview_comments_num);
		}
		else {
			convertView = inflater.inflate(res_comment, parent, false);
			
			tv_writer = (TextView)convertView.findViewById(R.id.board_view_comment_textview_writer);
			tv_content = (TextView)convertView.findViewById(R.id.board_view_comment_textview_content);
			tv_date = (TextView)convertView.findViewById(R.id.board_view_comment_textview_date);
		}
		
		String id = Utility.getHashedStudentId(context).substring(0, 9);
		
		tv_writer.setText( items.get(position).writer.nickname + " (" + id + ")");
		tv_content.setText( items.get(position).content );
		tv_date.setText( items.get(position).date );
		
		if(position == 0) {
			tv_comments_num.setText( ((Items.Board)items.get(position)).commentNum + "");
		}
		
		return convertView;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}

