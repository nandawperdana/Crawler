package com.nandanu.crawler;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nandanu.crawler.controller.AppController;
import com.nandanu.crawler.controller.CustomListAdapter;
import com.nandanu.crawler.controller.UrlSetter;
import com.nandanu.crawler.model.Barang;
import com.nandanu.crawler.model.Constants;

public class HomeFragment extends Fragment {
	// Log tag
	private static final String TAG = MainActivity.class.getSimpleName();

	// Barang json
	private static UrlSetter urlSetter = new UrlSetter();

	private final String url = urlSetter.getUrl() + ":8081/bukalapak";
	private ProgressDialog pDialog;
	private List<Barang> listBarang = new ArrayList<Barang>();
	private ListView listView;
	private CustomListAdapter adapter;

	public HomeFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);
		listView = (ListView) rootView.findViewById(R.id.listViewHome);
		adapter = new CustomListAdapter(getActivity(), listBarang);
		listView.setAdapter(adapter);

		pDialog = new ProgressDialog(getActivity());
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");
		pDialog.show();

		// changing action bar color
		getActivity().getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#eb3266")));

		// Creating volley request obj
		JsonObjectRequest barangReq = new JsonObjectRequest(Method.GET, url,
				null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						try {
							JSONArray barangJSON = response
									.getJSONArray("data");
							JSONObject dataJSON = barangJSON.getJSONObject(0);
							JSONArray obj1 = dataJSON.getJSONArray("discount");

							// Parsing json
							for (int i = 0; i < obj1.length(); i++) {
								JSONObject obj = obj1.getJSONObject(i);
								Barang barang = new Barang();
								barang.setTitle(obj
										.getString(Constants.TAG_TITLE));
								barang.setLink(obj
										.getString(Constants.TAG_LINK));
								barang.setCategory(obj
										.getString(Constants.TAG_CAT));
								barang.setImage(obj
										.getString(Constants.TAG_IMG));
								barang.setPrice("Rp "
										+ obj.getString(Constants.TAG_PRICE)
										+ ",-");
								barang.setCrawl("Bukalapak");

								// adding barang to movies array
								listBarang.add(barang);
							}
							hidePDialog();
						} catch (JSONException e) {
							e.printStackTrace();
						}
						// notifying list adapter about data changes
						// so that it renders the list view with updated data
						adapter.notifyDataSetChanged();
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error?: " + error.getMessage());
						hidePDialog();
					}
				});
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(barangReq);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String uri = "http://www.bukalapak.com"
						+ listBarang.get(position).getLink();
				Toast.makeText(getActivity(), uri, Toast.LENGTH_LONG).show();
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(uri));
				startActivity(browserIntent);
			}
		});
		return rootView;
	}

	private void hidePDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		hidePDialog();
	}

}
