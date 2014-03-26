package org.cclab.microsoft_gpsreceiver.rank;

import java.util.ArrayList;

import org.cclab.microsoft_gpsreceiver.R;
import org.cclab.microsoft_gpsreceiver.Utility;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RankAdapter extends ArrayAdapter<RankItem> {

	ArrayList<RankItem> items;
	Context context;
	int res;
	
	LayoutInflater inflater;
	
	
	public RankAdapter(Context context, int resource, ArrayList<RankItem> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		res = resource;
		items = objects;
		
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	private static class ViewHolder {
		
		TextView tv_rank;
		TextView tv_contributor;
		TextView tv_measure;
		
		public ViewHolder(TextView tv_rank, TextView tv_contributor, TextView tv_measure) {
			this.tv_rank = tv_rank;
			this.tv_contributor = tv_contributor;
			this.tv_measure = tv_measure;
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		
		if(convertView == null) {
			convertView = inflater.inflate(res, parent, false);
			
			holder = new ViewHolder(
					(TextView)convertView.findViewById(R.id.textview_rank),
					(TextView)convertView.findViewById(R.id.textview_rank_contributor),
					(TextView)convertView.findViewById(R.id.textview_rank_measure)
					);
			
			convertView.setTag(holder);
			
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		if(position == 0)
			convertView.setBackgroundColor(Color.rgb(229, 157, 96));
		else if(position == 1)
			convertView.setBackgroundColor(Color.rgb(240, 240, 240));
		else if(position == 3) {
			
		}
		else convertView.setBackgroundColor(Color.rgb(255, 255, 255));
		
		RankItem item = items.get(position);
		holder.tv_rank.setText( item.rank + "");
		if( Utility.getStudentId(context).equals(item.contributor) ) {
			holder.tv_contributor.setText( item.contributor );
			// TODO : size up font!!
		}
		else
			holder.tv_contributor.setText( item.contributor.substring(0, item.contributor.length()-1) + "***");
		
		holder.tv_measure.setText( item.gpsCount + "(" + String.format("%2.2f", item.percentage) + "%)" );
		
		return convertView;
	}

}
