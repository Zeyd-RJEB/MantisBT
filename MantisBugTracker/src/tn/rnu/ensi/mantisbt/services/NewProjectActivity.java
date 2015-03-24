package tn.rnu.ensi.mantisbt.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import tn.rnu.ensi.mantisbt.R;
import tn.rnu.ensi.mantisbt.views.ViewProjectsActivity;
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

public class NewProjectActivity extends Activity implements
		OnItemSelectedListener {

	String projectName;
	String status;
	String description;
	
	Spinner spinner;
	
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	

	// url to create new project
	private static String url_create_project = "http://10.0.2.2/android_connect/create_project.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addproject);

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

		
		ImageButton add = (ImageButton) findViewById(R.id.btnaddproject);

		add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				status = String.valueOf(spinner.getSelectedItem());
				Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
				projectName = ((EditText) findViewById(R.id.etprojectname)).getText().toString();
				description = ((EditText) findViewById(R.id.etdescription)).getText().toString();
				new CreateProject().execute();
				
			}
		});
	}
	
	class CreateProject extends AsyncTask<String, String, String> { 
		
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
//			super.onPreExecute();
//			pDialog = new ProgressDialog(NewProjectActivity.this);
//			pDialog.setMessage("Creating Project..");
//			pDialog.setIndeterminate(false);
//			pDialog.setCancelable(true);
//			pDialog.show();
		}

		/**
		 * Creating project
		 * */
		protected String doInBackground(String... args) {
			

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("projectName", projectName));
			params.add(new BasicNameValuePair("status", status));
			params.add(new BasicNameValuePair("description", description));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_create_project,
					"POST", params);
			
			// check log cat fro response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created project
					Intent i = new Intent(getApplicationContext(), ViewProjectsActivity.class);
					startActivity(i);
					
					// closing this screen
					finish();
				} else {
					// failed to create project
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
			// dismiss the dialog once done
			pDialog.dismiss();
		}

	}

//	public void insert() {
//		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//		nameValuePairs.add(new BasicNameValuePair("projectName", projectName));
//		nameValuePairs.add(new BasicNameValuePair("status", status));
//		nameValuePairs.add(new BasicNameValuePair("description", description));
//
//		try {
//			HttpClient httpclient = new DefaultHttpClient();
//			System.out.println("Cbon");
//			HttpPost httppost = new HttpPost("http://10.0.2.2/android_connect/check_user.php");
//			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//			HttpResponse response = httpclient.execute(httppost);
//			HttpEntity entity = response.getEntity();
//			is = entity.getContent();
//			Log.e("pass 1", "connection success ");
//		} catch (Exception e) {
//			Log.e("Fail 1", e.toString());
//			Toast.makeText(getApplicationContext(), "Invalid IP Address",
//					Toast.LENGTH_LONG).show();
//		}
//
//		try {
//			BufferedReader reader = new BufferedReader(new InputStreamReader(
//					is, "iso-8859-1"), 8);
//			StringBuilder sb = new StringBuilder();
//			while ((line = reader.readLine()) != null) {
//				sb.append(line + "\n");
//			}
//			is.close();
//			result = sb.toString();
//			Log.e("pass 2", "connection success ");
//		} catch (Exception e) {
//			Log.e("Fail 2", e.toString());
//		}
//
//		try {
//			JSONObject json_data = new JSONObject(result);
//			code = (json_data.getInt("code"));
//
//			if (code == 1) {
//				Toast.makeText(getBaseContext(), "Inserted Successfully",
//						Toast.LENGTH_SHORT).show();
//			} else {
//				Toast.makeText(getBaseContext(), "Sorry, Try Again",
//						Toast.LENGTH_LONG).show();
//			}
//		} catch (Exception e) {
//			Log.e("Fail 3", e.toString());
//		}
//	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		status = arg0.getItemAtPosition(arg2).toString();
		Toast.makeText(arg0.getContext(), status, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		status = new String("development");

	}

}