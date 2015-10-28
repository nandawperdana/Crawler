package com.nandanu.crawler.controller;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.nandanu.crawler.R;
import com.nandanu.crawler.model.Barang;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Barang> listBarang;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListAdapter(Activity activity, List<Barang> listBarang) {
		this.activity = activity;
		this.listBarang = listBarang;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listBarang.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listBarang.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.row_home, null);
		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		NetworkImageView thumbNail = (NetworkImageView) convertView
				.findViewById(R.id.thumbnail);
		TextView namaBarang = (TextView) convertView
				.findViewById(R.id.textViewBarangNama);
		TextView hargaBarang = (TextView) convertView
				.findViewById(R.id.textViewBarangHarga);
		TextView katBarang = (TextView) convertView
				.findViewById(R.id.textViewBarangKategori);
		TextView crawl = (TextView) convertView
				.findViewById(R.id.textViewCrawlFrom);

		// getting barang data for the row
		Barang brg = listBarang.get(position);

		// thumbnail image
		thumbNail.setImageUrl(brg.getImage(), imageLoader);

		// nama
		namaBarang.setText(brg.getTitle());

		// harga
		hargaBarang.setText("" + brg.getPrice());

		// genre
		katBarang.setText("Kategori " + brg.getCategory());

		// crawl
		crawl.setText("" + brg.getCrawl());

		return convertView;
	}

}
