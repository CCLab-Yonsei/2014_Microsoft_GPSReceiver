package org.cclab.microsoft_gpsreceiver.board;

import java.util.ArrayList;
import java.util.List;

import org.cclab.microsoft_gpsreceiver.R;
import org.cclab.microsoft_gpsreceiver.Utility;
import org.cclab.microsoft_gpsreceiver.board.Items.Board;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/*
* Adapter for board item
* Normal user adapter
*/
public class BoardAdapter extends ArrayAdapter<Items.Board> {

	int res;
	Context ctx;
	ArrayList<Board> items;
	
	LayoutInflater inflater;
	boolean bAdmin;
	
	public BoardAdapter(Context context, int resource, ArrayList<Board> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		res = resource;
		ctx = context;
		
		items = objects;
		bAdmin = Utility.isAdmin(context);
		
		inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	private static class ViewHolder {
		
		public final TextView tv_writer;
		// public final TextView tv_title;
		public final TextView tv_content;
		public final TextView tv_date;
		public final TextView tv_comments_num;
		
		public ViewHolder(TextView writer, TextView content, TextView date, TextView comments_num) {
			this.tv_writer = writer;
			// this.tv_title = title;
			this.tv_content = content;
			this.tv_date = date;
			this.tv_comments_num = comments_num;
			
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if(convertView == null) {
			convertView = inflater.inflate(res, parent, false);
			
			holder = new ViewHolder(
					(TextView)convertView.findViewById(R.id.textview_bwriter),
				//	(TextView)convertView.findViewById(R.id.textview_btitle),
					(TextView)convertView.findViewById(R.id.textview_bcontent),
					(TextView)convertView.findViewById(R.id.textview_bdate),
					(TextView)convertView.findViewById(R.id.textview_bcomments_num)
				
					);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		Board item = items.get(position);
		
		if(item.kind == 1) {
			convertView.setBackgroundColor(Color.rgb(200, 200, 200));
		}
		else convertView.setBackgroundColor(Color.WHITE);
		
		if(bAdmin)
			holder.tv_writer.setText( item.writer.nickname + "(" + item.writer.id + ")" );
		else
			holder.tv_writer.setText( item.writer.nickname + "(" + item.writer.hashedId.substring(0, 9) + ")" );
		
		// holder.tv_title.setText( item.title + "(" + item.commentNum + ")" );
		holder.tv_content.setText( item.content );
		holder.tv_date.setText( item.date );
		holder.tv_comments_num.setText( item.commentNum + "" );
		
		return convertView;
		
	}
	
}
	
	