package com.appfree.messengercute.adapter;


import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appfree.messengercute.R;


public class AdapterMenu extends ArrayAdapter {
	private Activity context=null;
	private int layout;
    private Typeface light;
	private String[] tendanhmuc;

	public AdapterMenu(Activity context, int layout, String[] tendanhmuc) {
		super(context, layout, tendanhmuc);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.layout=layout;
		this.tendanhmuc=tendanhmuc;
        light=Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Light.ttf");

	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LayoutInflater inflater = context.getLayoutInflater();
		convertView=inflater.inflate(R.layout.layout_list_danhmuc, null);
		TextView tv = (TextView) convertView.findViewById(R.id.tvTenDanhMuc);
        tv.setTypeface(light);
		//ImageView img=(ImageView)convertView.findViewById(R.id.imgAnhDanhMuc);
		tv.setText(tendanhmuc[position]);
		//img.setImageResource(anhdanhmuc[position]);
		return convertView;
	}

}
