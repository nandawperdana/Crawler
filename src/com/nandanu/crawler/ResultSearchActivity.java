package com.nandanu.crawler;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.nandanu.crawler.controller.CustomListAdapter;
import com.nandanu.crawler.controller.JSONParser;
import com.nandanu.crawler.controller.UrlSetter;
import com.nandanu.crawler.model.Barang;
import com.nandanu.crawler.model.Constants;

public class ResultSearchActivity extends Activity {

	// Log tag
	private static final String TAG = MainActivity.class.getSimpleName();

	// JSON
	private static UrlSetter urlSetter = new UrlSetter();
	private JSONParser jParser = new JSONParser();

	// widgets
	private ListView listViewResult;
	private CustomListAdapter adapter;
	private ProgressDialog pDialog;

	// var
	private List<Barang> listBarang = new ArrayList<Barang>();

	private String urlSearch = urlSetter.getUrl()
			+ "/ta_crawler/php/search.php";
	private String kategori = "", keyword = "", sortby = "";
	private int currentPage_ = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_search);
		// changing action bar color
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#eb3266")));

		Intent i = getIntent();
		keyword = i.getStringExtra("keyword");
		kategori = i.getStringExtra("kategori");
		sortby = "terbaru";
		listBarang = SearchFragment.listSearchResult;

		listViewResult = (ListView) findViewById(R.id.listViewResultSearch);

		// LoadMore button
		Button btnLoadMore = new Button(ResultSearchActivity.this);
		btnLoadMore.setBackgroundResource(R.drawable.button_background);
		btnLoadMore.setText("Load More");
		btnLoadMore.setTextColor(Color.WHITE);

		// Adding Load More button to lisview at bottom
		listViewResult.addFooterView(btnLoadMore);

		adapter = new CustomListAdapter(ResultSearchActivity.this, listBarang);
		listViewResult.setAdapter(adapter);

		pDialog = new ProgressDialog(ResultSearchActivity.this);
		pDialog.setMessage("Loading...");

		/**
		 * Listening to Load More button click event
		 * */
		btnLoadMore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Starting a new async task
				currentPage_ += 1;
				new LoadSearchResult().execute();
			}
		});

		listViewResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String uri = "http://www." + listBarang.get(position).getCrawl() + listBarang.get(position).getLink();
				Toast.makeText(ResultSearchActivity.this, uri,
						Toast.LENGTH_LONG).show();
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(uri));
				startActivity(browserIntent);
			}
		});
	}

	private void showpDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hidepDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result_search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.menu_terbaru) {
			sortby = "terbaru";
			Toast.makeText(ResultSearchActivity.this, "Terbaru ",
					Toast.LENGTH_LONG).show();
			listBarang.clear();
			currentPage_ = 1;
			// adapter.notifyDataSetChanged();
			new LoadSearchResult().execute();
			return true;
		}
		if (id == R.id.menu_termurah) {
			sortby = "termurah";
			Toast.makeText(ResultSearchActivity.this, "Termurah ",
					Toast.LENGTH_LONG).show();
			listBarang.clear();
			currentPage_ = 1;
			// adapter.notifyDataSetChanged();
			new LoadSearchResult().execute();
			return true;
		}
		if (id == R.id.menu_termahal) {
			sortby = "termahal";
			Toast.makeText(ResultSearchActivity.this, "Termahal ",
					Toast.LENGTH_LONG).show();
			listBarang.clear();
			currentPage_ = 1;
			// adapter.notifyDataSetChanged();
			new LoadSearchResult().execute();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Background Async Task to Load result by making HTTP Request
	 * */
	class LoadSearchResult extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showpDialog();
		}

		/**
		 * getting result from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// params.add(new BasicNameValuePair("keyword", keyword));
			// params.add(new BasicNameValuePair("kategori", kategori));
			// params.add(new BasicNameValuePair("sort", sortby));
			// params.add(new BasicNameValuePair("page", String.valueOf(page)));
			if (kategori.contains(" ")) {
				kategori = kategori.replaceAll(" ", "%20");
			}

			urlSearch += "?keyword=" + keyword + "&kategori=" + kategori
					+ "&sort=" + sortby + "&page="
					+ String.valueOf(currentPage_);

			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(urlSearch, "GET");

			Log.d(TAG, "URL search: " + urlSearch);
			Log.d(TAG, "json: " + json.toString());
			try {
				JSONArray dataJSON = json.getJSONArray("data");
				// looping through All result
				for (int i = 0; i < dataJSON.length(); i++) {
					JSONObject data = (JSONObject) dataJSON.get(i);
					Barang barang = new Barang();
					barang.setTitle(data.getString(Constants.TAG_TITLE));
					barang.setLink(data.getString(Constants.TAG_LINK));
					barang.setCategory(data.getString(Constants.TAG_CAT));
					barang.setImage(data.getString(Constants.TAG_IMG));
					barang.setPrice(data.getString(Constants.TAG_PRICE));
					barang.setCrawl(data.getString(Constants.TAG_SITE));

					// adding barang to movies array
					listBarang.add(barang);
				}

				// notifying list adapter about data changes
				// so that it renders the list view with updated
				// data
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// adapter = new CustomListAdapter(ResultSearchActivity.this,
			// listBarang);
			// listViewResult.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			hidepDialog();
		}
	}
}