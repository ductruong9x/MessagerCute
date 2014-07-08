package com.appfree.messengercute.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appfree.messengercute.R;
import com.appfree.messengercute.model.TinNhan;

import java.util.ArrayList;


public class AdapterMessager extends ArrayAdapter<TinNhan> {
	public Activity context = null;
	public int layout;
	public ArrayList<TinNhan> myList = null;

	public AdapterMessager(Activity context, int layout,
                           ArrayList<TinNhan> myList) {
		super(context,layout,myList);
		this.context = context;
		this.layout = layout;
		this.myList = myList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = context.getLayoutInflater();
		convertView = inflater.inflate(layout, null);
		TextView tvChiTietTinNhan = (TextView) convertView
				.findViewById(R.id.tvChiTietTinNhan);
		tvChiTietTinNhan.setText(myList.get(position).getNoidung());
		return convertView;
	}

}
