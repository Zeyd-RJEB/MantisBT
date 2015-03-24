package tn.rnu.ensi.mantisbt.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tn.rnu.ensi.mantisbt.R;
import tn.rnu.ensi.mantisbt.services.EditProjectActivity;
import tn.rnu.ensi.mantisbt.services.NewProjectActivity;
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

public class ViewProjectsActivity extends ListActivity {
	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> projectsList;

	// url to get all projects list
	private static String url_all_projects = "http://10.0.2.2/android_connect/get_all_projects.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PROJECTS = "projects";
	private static final String TAG_PID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_DESCRIPTION = "description";

	// projects JSONArray
	JSONArray projects = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_projects);

		// Hashmap for ListView
		projectsList = new ArrayList<HashMap<String, String>>();

		// Loading projects in Background Thread
		new LoadAllProjects().execute();

		// Get listview
		ListView lv = getListView();

		// on selecting single product
		// launching Edit Product Screen
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String name = ((TextView) view.findViewById(R.id.name))
						.getText().toString();
				String description = ((TextView) view
						.findViewById(R.id.description)).getText().toString();
				Log.e("display project name", name);
				Log.e("display project description", description);
				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						EditProjectActivity.class);
				// sending name to next activity
				in.putExtra(TAG_NAME, name);
				in.putExtra(TAG_DESCRIPTION, description);
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
			// means user edited/deleted project
			// reload this screen again
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}

	}

	/**
	 * Background Async Task to Load all projects by making HTTP Request
	 * */
	class LoadAllProjects extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ViewProjectsActivity.this);
			pDialog.setMessage("Loading projects. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All projects from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_all_projects, "GET",
					params);

			// Check your log cat for JSON reponse
			Log.d("All Projects: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// projects found
					// Getting Array of Projects
					projects = json.getJSONArray(TAG_PROJECTS);

					// looping through All Products
					for (int i = 0; i < projects.length(); i++) {
						JSONObject c = projects.getJSONObject(i);

						// Storing each json item in variable
						String id = c.getString(TAG_PID);
						String name = c.getString(TAG_NAME);
						String description = c.getString(TAG_DESCRIPTION);

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_PID, id);
						map.put(TAG_NAME, name);
						map.put(TAG_DESCRIPTION, description);

						// adding HashList to ArrayList
						projectsList.add(map);
					}
				} else {
					// no projects found
					// Launch Add New project Activity
					Intent i = new Intent(getApplicationContext(),
							NewProjectActivity.class);
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
			// dismiss the dialog after getting all projects
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(
							ViewProjectsActivity.this, projectsList,
							R.layout.list_projects, new String[] {
									TAG_NAME, TAG_DESCRIPTION },
							new int[] {  R.id.name,R.id.description });
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}
}