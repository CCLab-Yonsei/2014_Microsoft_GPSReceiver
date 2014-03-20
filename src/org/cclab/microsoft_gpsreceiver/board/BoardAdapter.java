package org.cclab.microsoft_gpsreceiver.board;

import java.util.ArrayList;
import java.util.List;

import org.cclab.microsoft_gpsreceiver.R;
import org.cclab.microsoft_gpsreceiver.board.Items.Board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/*
* Adapter for board item
*/
public class BoardAdapter extends ArrayAdapter<Items.Board> {

	int res;
	Context ctx;
	ArrayList<Board> items;
	
	LayoutInflater inflater;
	
	
	public BoardAdapter(Context context, int resource, ArrayList<Board> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		res = resource;
		ctx = context;
		
		items = objects;
		
		inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	private static class ViewHolder {
		
		public final TextView writer;
		public final TextView title;
		public final TextView contents;
		
		public ViewHolder(TextView writer, TextView title, TextView contents) {
			this.writer = writer;
			this.title = title;
			this.contents = contents;
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if(convertView == null) {
			convertView = inflater.inflate(res, parent, false);
			
			holder = new ViewHolder(
					(TextView)convertView.findViewById(R.id.writer),
					(TextView)convertView.findViewById(R.id.title),
					(TextView)convertView.findViewById(R.id.contents)
					);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.writer.setText( items.get(position).writer );
		holder.title.setText( items.get(position).title );
		holder.contents.setText( items.get(position).contents );
		
		return convertView;
		
	}
	
}