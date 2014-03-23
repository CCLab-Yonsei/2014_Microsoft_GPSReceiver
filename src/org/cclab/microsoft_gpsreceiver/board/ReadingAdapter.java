package org.cclab.microsoft_gpsreceiver.board;

import java.util.ArrayList;
import java.util.List;

import org.cclab.microsoft_gpsreceiver.board.Items.Base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

public class ReadingAdapter extends ArrayAdapter<Items.Base> {

	ArrayList<Items.Base> items; 
	
	public ReadingAdapter(Context context, int resource, List<Base> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}

	
	

}
