package com.nandanu.crawler;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.nandanu.crawler.controller.JSONParser;
import com.nandanu.crawler.controller.UrlSetter;
import com.nandanu.crawler.model.Barang;
import com.nandanu.crawler.model.Constants;

public class SearchFragment extends Fragment {
	// Log tag
	private static final String TAG = MainActivity.class.getSimpleName();

	// widgets
	private EditText editTextNamaBrg;
	private Spinner spKategori;
	private ProgressDialog pDialog;
	private Button buttonSearch;

	// var
	private JSONParser jParser = new JSONParser();
	public static final List<Barang> listSearchResult = new ArrayList<Barang>();
	private static UrlSetter urlSetter = new UrlSetter();
	private String urlSearch = urlSetter.getUrl()
			+ "/ta_crawler/php/search.php";
	private String kategori = "", keyword = "", sortby = "";
	private int page = 1;
	private String[] sort, kat;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search, container,
				false);
		editTextNamaBrg = (EditText) rootView
				.findViewById(R.id.editTextSearchNamaBrg);
		spKategori = (Spinner) rootView
				.findViewById(R.id.spinnerSearchKategori);
		spKategori.setAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, Constants.kategori));
		buttonSearch = (Button) rootView.findViewById(R.id.buttonSearch);

		pDialog = new ProgressDialog(getActivity());
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");

		// changing action bar color
		getActivity().getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#eb3266")));

		buttonSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				keyword = editTextNamaBrg.getText().toString();
				kategori = spKategori.getSelectedItem().toString();
				if (kategori.contains(" ")) {
					if (kategori.contains("Semua Kategori")) {
						kategori = kategori.toLowerCase();
						kategori = kategori.replaceAll(" ", "-");
					} else
						kategori = kategori.replaceAll(" ", "%20");
				}
				sortby = "terbaru";

				// request
				listSearchResult.clear();
				new LoadSearchResult().execute();
			}
		});
		return rootView;
	}

	private void showpDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hidepDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
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

			urlSearch += "?keyword=" + keyword + "&kategori=" + kategori
					+ "&sort=" + sortby + "&page=" + String.valueOf(page);

			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(urlSearch, "GET");

			Log.d(TAG, "URL search: " + urlSearch);
			Log.d(TAG, "json: " + json.toString());
			Log.d(TAG, "kat: " + kategori);
			Log.d(TAG, "keyword: " + keyword);
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
					listSearchResult.add(barang);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			hidepDialog();
			// updating UI from Background Thread
			Intent i = new Intent(getActivity(), ResultSearchActivity.class);
			i.putExtra("keyword", keyword);
			i.putExtra("kategori", kategori);

			getActivity().startActivity(i);
		}
	}
}
