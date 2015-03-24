package tn.rnu.ensi.mantisbt.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tn.rnu.ensi.mantisbt.R;
import tn.rnu.ensi.mantisbt.entities.Project;
import tn.rnu.ensi.mantisbt.webservices.JSONParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class EditProjectActivity extends Activity implements
		OnItemSelectedListener {

	EditText ename;
	String status="development";
	EditText edescrption;
	Spinner spinner;
	ImageButton bedit;
	ImageButton bdelete;
	Project project = new Project();
	

	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	// single project url
	private static final String url_project_details = "http://10.0.2.2/android_connect/get_project_details.php";

	// url to update project
	private static final String url_update_project = "http://10.0.2.2/android_connect/update_project.php";

	// url to delete project
	private static final String url_delete_project = "http://10.0.2.2/android_connect/delete_project.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PROJECT = "project";
	private static final String TAG_NAME = "name";
	private static final String TAG_STATUS = "status";
	private static final String TAG_DESCRIPTION = "description";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_project);

		//EditText
		
		ename=(EditText)findViewById(R.id.etprojectname);
		edescrption=(EditText)findViewById(R.id.etdescription);
		
		//  buttons
		bedit = (ImageButton) findViewById(R.id.btnedit);
		bdelete = (ImageButton) findViewById(R.id.btndelete);
		final Spinner spinner = (Spinner) findViewById(R.id.spinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.status_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		 

		// getting project details from intent
		Intent i = getIntent();

		// getting project name from intent
	project.setName(i.getStringExtra(TAG_NAME));
		project.setDescription(i.getStringExtra(TAG_DESCRIPTION));
		ename.setText(project.getName());
		ename.setKeyListener(null);
		edescrption.setText(project.getDescription());
		
		// Getting complete project details in background thread
		 //new GetProjectDetails().execute();

		// edit button click event
		bedit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// starting background task to update project
				 new EditProjectDetails().execute();
			}
		});

		// Delete button click event
		bdelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// deleting project in background thread
				 new DeleteProject().execute();
			}
		});

	}

	/**
	 * Background Async Task to Get complete project details
	 * */
	class GetProjectDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditProjectActivity.this);
			pDialog.setMessage("Loading project details. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Getting project details in background thread
		 * */
		protected String doInBackground(String... params) {

			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					// Check for success tag
					int success;
					try {
						// Building Parameters
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("name", project.getName()));

						// getting project details by making HTTP request
						// Note that issue details url will use GET request
						JSONObject json = jsonParser.makeHttpRequest(
								url_project_details, "GET", params);

						// check your log for json response
						Log.d("Single Project Details", json.toString());

						// json success tag
						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							// successfully received project details
							JSONArray projectObj = json
									.getJSONArray(TAG_PROJECT); // JSON Array

							// get first project object from JSON Array
							JSONObject product = projectObj.getJSONObject(0);

							// project with this name found
							// Edit Text
							ename = (EditText) findViewById(R.id.etprojectname);
							edescrption = (EditText) findViewById(R.id.etdescription);

							// display product data in EditText
							ename.setText(product.getString(TAG_NAME));
							edescrption.setText(product
									.getString(TAG_DESCRIPTION));

						} else {
							// project with name not found
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once got all details
			pDialog.dismiss();
		}
	}

	/**
	 * Background Async Task to Save product Details
	 * */
	class EditProjectDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditProjectActivity.this);
			pDialog.setMessage("Saving project ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Saving project
		 * */
		protected String doInBackground(String... args) {

			// getting updated data from EditTexts
			String name = ename.getText().toString();
			String description = edescrption.getText().toString();
			Log.e("naaaame", name);
			Log.e("desscccc", description);
			Log.e("statuuuuus", status);

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_NAME, name));
			params.add(new BasicNameValuePair(TAG_STATUS, status));
			params.add(new BasicNameValuePair(TAG_DESCRIPTION, description));

			// sending modified data through http request
			// Notice that update project url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_update_project,
					"POST", params);

			// check json success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully updated
					
					Intent i = getIntent(); 
					// send result code 100 to notify about project update
					setResult(100, i);
					finish();
				} else {
					// failed to update project
					
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
			// dismiss the dialog once project uupdated
			pDialog.dismiss();
		}
	}

	/*****************************************************************
	 * Background Async Task to Delete Project
	 * */
	class DeleteProject extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditProjectActivity.this);
			pDialog.setMessage("Deleting Project...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Deleting product
		 * */
		protected String doInBackground(String... args) {

			// Check for success tag
			int success;
			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("name", project.getName()));

				// getting product details by making HTTP request
				JSONObject json = jsonParser.makeHttpRequest(
						url_delete_project, "POST", params);

				// check your log for json response
				Log.d("Delete Project", json.toString());

				// json success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					// project successfully deleted
					// notify previous activity by sending code 100
					Intent i = getIntent();
					// send result code 100 to notify about product deletion
					setResult(100, i);
					finish();
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
			// dismiss the dialog once project deleted
			pDialog.dismiss();

		}

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		status = arg0.getItemAtPosition(arg2).toString();
		Toast.makeText(arg0.getContext(), status, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}
}
