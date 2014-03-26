package org.cclab.microsoft_gpsreceiver.board;

import java.util.ArrayList;

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

/**
 * Adapter for board item
 * Normal user adapter
 *  
 * @author Han Park
 *
 */
public class BoardAdapter extends ArrayAdapter<Items.Board> {

	int res;
	Context ctx;
	ArrayList<Board> items;
	
	LayoutInflater inflater;
	boolean bAdmin;
	
	public BoardAdapter(Context context, int resource, ArrayList<Board> objects) {
		super(context, resource, objects);

		res = resource;
		ctx = context;
		
		items = objects;
		bAdmin = Utility.isAdmin(context);
		
		inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	private static class ViewHolder {
		
		public final TextView tv_writer;
		public final TextView tv_content;
		public final TextView tv_date;
		public final TextView tv_comments_num;
		
		public ViewHolder(TextView writer, TextView content, TextView date, TextView comments_num) {
			this.tv_writer = writer;
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
					(TextView)convertView.findViewById(R.id.board_list_textview_writer),
					(TextView)convertView.findViewById(R.id.board_list_textview_content),
					(TextView)convertView.findViewById(R.id.board_list_textview_date),
					(TextView)convertView.findViewById(R.id.board_list_textview_comments_num)
				
					);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		Board item = items.get(position);
		
		if(item.kind == 1) {
			convertView.setBackgroundColor(Color.parseColor("#eeeeee"));
		}
		else convertView.setBackgroundColor(Color.WHITE);
		
		holder.tv_writer.setText( item.writer.nickname + " (" + item.writer.hashedId.substring(0, 9) + ")" );
		holder.tv_content.setText( item.content );
		holder.tv_date.setText( item.date );
		holder.tv_comments_num.setText( item.commentNum + "" );
		
		return convertView;
		
	}
	
}
	
	