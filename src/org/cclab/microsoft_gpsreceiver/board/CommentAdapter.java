package org.cclab.microsoft_gpsreceiver.board;

import java.util.ArrayList;

import org.cclab.microsoft_gpsreceiver.R;
import org.cclab.microsoft_gpsreceiver.Utility;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {

	ArrayList<Items.Base> items;
	Context context;
	int res_row_1;
	int res_row;
	LayoutInflater inflater;
	
	View view_row1;
	
	public CommentAdapter(Context context, int res_row_1, int res_row, ArrayList<Items.Base> items) {
		this.items = items;
		this.context = context;
		this.res_row_1 = res_row_1;
		this.res_row = res_row;
	
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	/*	
	private static class ViewHolder {
		
		boolean bHolder = false;
		public TextView tv_writer;
		public TextView tv_content;
		public TextView tv_date;
		
		
		boolean bHolderR1 = false;
		public TextView tv_writer_1;
		public TextView tv_content_1;
		public TextView tv_date_1;
		public TextView tv_contents_num_1;
		
		public void setViewHolder(TextView writer, TextView content, TextView date) {
			this.tv_writer = writer;
			this.tv_content = content;
			this.tv_date = date;
			
			bHolder = true;
		}
		
		public void setViewHolder_Row1(TextView writer, TextView content, TextView date,
				TextView contents_num) {
			this.tv_writer_1 = writer;
			this.tv_content_1 = content;
			this.tv_date_1 = date;
			this.tv_contents_num_1 = contents_num;
			
			bHolderR1 = true;
		}
	}
	*/
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// ViewHolder holder = null;
		
		TextView tv_writer;
		TextView tv_content;
		TextView tv_date;
		
		TextView tv_comments_num = null;
		
		
		if(position == 0) {
			convertView = inflater.inflate(res_row_1, parent, false);
			
			tv_writer = (TextView)convertView.findViewById(R.id.textview_cwriter1);
			tv_content = (TextView)convertView.findViewById(R.id.textview_ccontent1);
			tv_date = (TextView)convertView.findViewById(R.id.textview_cdate1);
			
			tv_comments_num = (TextView)convertView.findViewById(R.id.textview_ccomments_num1);
		}
		else {
			convertView = inflater.inflate(res_row, parent, false);
			
			tv_writer = (TextView)convertView.findViewById(R.id.textview_cwriter);
			tv_content = (TextView)convertView.findViewById(R.id.textview_ccontent);
			tv_date = (TextView)convertView.findViewById(R.id.textview_cdate);
		}
		
		String id = "";
		if(Utility.isAdmin(context))
			id = Utility.getStudentId(context);
		else
			id = Utility.getHashedStudentId(context).substring(0, 9);
		
		tv_writer.setText( items.get(position).writer.nickname + "(" + id + ")");
		tv_content.setText( items.get(position).content );
		tv_date.setText( items.get(position).date );
		
		if(position == 0)
			tv_comments_num.setText( ((Items.Board)items.get(position)).commentNum + "");
		
		
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

