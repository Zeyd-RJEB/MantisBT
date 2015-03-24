package tn.rnu.ensi.mantisbt.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tn.rnu.ensi.mantisbt.R;
import tn.rnu.ensi.mantisbt.services.ReportIssueActivity;
import tn.rnu.ensi.mantisbt.webservices.JSONParser;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ViewIssuesActivity extends ListActivity {
	// Progress Dialog
	private ProgressDialog pDialog;
	String summary;
	String id;
	
	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> issuesList;

	// url to get all issues list
	private static String url_all_issues = "http://10.0.2.2/android_connect/get_all_issues.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ISSUES = "issues";
	private static final String TAG_ID = "id";
	private static final String TAG_SUMMARY = "summary";

	// products JSONArray
	JSONArray issues = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_issues);

		// Hashmap for ListView
		issuesList = new ArrayList<HashMap<String, String>>();

		// Loading products in Background Thread
		new LoadAllIssues().execute();

		// Get listview
		ListView lv = getListView();

		// on selecting single issue
		// launching Edit issue Screen
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String iid = ((TextView) view.findViewById(R.id.id)).getText()
						.toString();

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						DetailsIssueActivity.class);
				// sending pid to next activity
				in.putExtra(TAG_ID, iid);
				
				// starting new activity and expecting some response back
				startActivityForResult(in, 100);
			}
		});

	}

	// Response from Edit Product Activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if result code 100
		if (resultCode == 100) {
			// if result code 100 is received 
			// means user edited/deleted issue
			// reload this screen again
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}

	}

	/**
	 * Background Async Task to Load all product by making HTTP Request
	 * */
	class LoadAllIssues extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ViewIssuesActivity.this);
			pDialog.setMessage("Loading issues. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All issues from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_all_issues, "GET", params);
			
			// Check your log cat for JSON reponse
			Log.d("All Issues: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// projects found
					// Getting Array of Products
					issues = json.getJSONArray(TAG_ISSUES);

					// looping through All Products
					for (int i = 0; i < issues.length(); i++) {
						JSONObject c = issues.getJSONObject(i);

						// Storing each json item in variable
						id = c.getString(TAG_ID);
						summary = c.getString(TAG_SUMMARY);
						

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_ID, id);
						map.put(TAG_SUMMARY, summary);
						Log.e(TAG_SUMMARY,summary);
						

						// adding HashList to ArrayList
						issuesList.add(map);
					}
				} else {
					// no projects found
					// Launch Add New Issue Activity
					Intent i = new Intent(getApplicationContext(),
							ReportIssueActivity.class);
					// Closing all previous activities
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
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
			// dismiss the dialog after getting all issues
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(
							ViewIssuesActivity.this, issuesList,
							R.layout.list_issues, new String[] { TAG_ID,
									 TAG_SUMMARY},
							new int[] { R.id.id, R.id.summary});
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}
}
