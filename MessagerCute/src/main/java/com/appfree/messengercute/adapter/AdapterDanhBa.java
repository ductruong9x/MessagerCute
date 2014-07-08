package com.appfree.messengercute.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appfree.messengercute.R;
import com.appfree.messengercute.model.DanhBaDetail;

import java.util.ArrayList;


public class AdapterDanhBa extends ArrayAdapter<DanhBaDetail> {
	public Activity context = null;
	public int layout;
	ArrayList<DanhBaDetail> myList = null;

	public AdapterDanhBa(Activity context, int layout,
			ArrayList<DanhBaDetail> myList) {
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
		TextView tvTenDanhBa = (TextView) convertView
				.findViewById(R.id.tvTenDanhBa);
		TextView tvSDT = (TextView) convertView.findViewById(R.id.tvSDT);
		tvTenDanhBa.setText(myList.get(position).getName());
		tvSDT.setText(myList.get(position).getPhone());
		return convertView;
	}

}
