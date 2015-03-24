package tn.rnu.ensi.mantisbt.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tn.rnu.ensi.mantisbt.R;
import tn.rnu.ensi.mantisbt.controllers.LogoutActivity;
import tn.rnu.ensi.mantisbt.entities.Issue;
import tn.rnu.ensi.mantisbt.entities.Project;
import tn.rnu.ensi.mantisbt.entities.User;
import tn.rnu.ensi.mantisbt.mailconfig.BackgroundMail;
import tn.rnu.ensi.mantisbt.views.ViewIssuesActivity;
import tn.rnu.ensi.mantisbt.webservices.JSONParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class ReportIssueActivity extends Activity implements
		OnItemSelectedListener, OnClickListener {
	// Progress Dialog
	private ProgressDialog pDialog;
	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();
	ArrayList<String> usersList;
	Map<String, String> emailMap = new HashMap<String, String>();
	ArrayList<String> projectNameList;
	User user = new User();
	Project project = new Project();

	// url to get all projects list
	private static String url_projects = "http://10.0.2.2/android_connect/get_all_projects.php";
	// url to get all users list
	private static String url_users = "http://10.0.2.2/android_connect/users.php";

	Spinner spinner;
	Spinner projectNameSpinner;
	Spinner reproducibilitySpinner;
	Spinner severitySpinner;
	Spinner prioritySpinner;
	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_USERS = "users";
	// private static final String TAG_ID = "id";
	private static final String TAG_USERNAME = "username";
	// url to create new issue
	private static String url_create_issue = "http://10.0.2.2/android_connect/create_issue.php";
	// users JSON array
	JSONArray users = null;
	// projects JSON array
	JSONArray projects = null;
	Issue issue = new Issue();
	ImageButton add;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bugreporting);
		add = (ImageButton) findViewById(R.id.bta);
		add.setOnClickListener(this);
		// Loading users and projects in Background Thread
		new LoadAllUsers().execute();
		new LoadAllProjects().execute();
		// Spinner element
		projectNameSpinner = (Spinner) findViewById(R.id.projectnamespinner);
		spinner = (Spinner) findViewById(R.id.spinner);
		reproducibilitySpinner = (Spinner) findViewById(R.id.reproducibilityspinner);
		severitySpinner = (Spinner) findViewById(R.id.severityspinner);
		prioritySpinner = (Spinner) findViewById(R.id.priorityspinner);
		// Spinner click listener
		spinner.setOnItemSelectedListener(this);
		projectNameSpinner.setOnItemSelectedListener(this);
		reproducibilitySpinner.setOnItemSelectedListener(this);
		severitySpinner.setOnItemSelectedListener(this);
		prioritySpinner.setOnItemSelectedListener(this);

		ArrayAdapter<CharSequence> reproducibilityAdapter = ArrayAdapter
				.createFromResource(this, R.array.reproducibility_array,
						android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		reproducibilityAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		// Apply the adapter to the spinner
		reproducibilitySpinner.setAdapter(reproducibilityAdapter);

		ArrayAdapter<CharSequence> severityAdapter = ArrayAdapter
				.createFromResource(this, R.array.severity_array,
						android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		severityAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		// Apply the adapter to the spinner
		severitySpinner.setAdapter(severityAdapter);

		ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter
				.createFromResource(this, R.array.priority_array,
						android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		priorityAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		// Apply the adapter to the spinner
		prioritySpinner.setAdapter(priorityAdapter);

		// Spinner Drop down elements
		usersList = new ArrayList<String>();
		usersList.add("administrator");
		projectNameList = new ArrayList<String>();
		projectNameList.add("choose project");

		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, usersList);

		// Creating adapter for projects spinner
		ArrayAdapter<String> projectsAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, projectNameList);

		// Drop down layout style - list view with radio button
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		// Drop down layout style - list view with radio button
		projectsAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);

		// attaching data adapter to spinner
		spinner.setAdapter(dataAdapter);

		// attaching data adapter to spinner
		projectNameSpinner.setAdapter(projectsAdapter);
	}

	/**
	 * Background Async Task to Load all users by making HTTP Request
	 * */
	class LoadAllUsers extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			// super.onPreExecute();
			// pDialog = new ProgressDialog(ReportIssueActivity.this);
			// pDialog.setMessage("Loading Users. Please wait...");
			// pDialog.setIndeterminate(false);
			// pDialog.setCancelable(false);
			// pDialog.show();
			//
			// new Thread(new Runnable() {
			// @Override
			// public void run() {
			// try {
			//
			// Thread.sleep(0);
			//
			// pDialog.dismiss();
			//
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// }
			// }).start();
		}

		/**
		 * getting All users from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_users, "GET", params);

			// Check your log cat for JSON reponse
			Log.d("All users: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);
				Log.i("success", "" + success);
				if (success == 1) {
					// users found
					// Getting Array of users
					users = json.getJSONArray(TAG_USERS);
					usersList.remove("administrator");
					// looping through All users
					for (int i = 0; i < users.length(); i++) {
						JSONObject c = users.getJSONObject(i);

						// Storing each json item in variable
						// String id = c.getString(TAG_ID);
						String name = c.getString(TAG_USERNAME);
						String email = c.getString("email");
						emailMap.put(name, email);
						usersList.add(name);

					}
				} else {
					// no users found
					// Launch Add New issue Activity
					Intent i = new Intent(getApplicationContext(),
							LogoutActivity.class);
					// Closing all previous activities
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
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
			pDialog = new ProgressDialog(ReportIssueActivity.this);
			pDialog.setMessage("Loading Projects. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {

						Thread.sleep(2000);

						pDialog.dismiss();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}

		/**
		 * getting All projects from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_projects, "GET",
					params);

			// Check your log cat for JSON response
			Log.d("All projects: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);
				Log.i("success", "" + success);
				if (success == 1) {
					// projects found
					// Getting Array of Projects
					projects = json.getJSONArray("projects");
					// usersList.remove("administrator");
					// looping through All projects
					for (int i = 0; i < projects.length(); i++) {
						JSONObject c = projects.getJSONObject(i);

						// Storing each json item in variable
						// String id = c.getString(TAG_ID);
						String name = c.getString("name");

						projectNameList.add(name);

					}
				} else {
					// no projects found
					// Launch Add New issue Activity
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
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// On selecting a spinner item
		switch (parent.getId()) {
		case R.id.projectnamespinner:
			project.setName(parent.getItemAtPosition(position).toString());
			issue.setProject(project);
			break;
		case R.id.spinner:
			user.setUserName(parent.getItemAtPosition(position).toString());
			user.setEmail(emailMap.get(parent.getItemAtPosition(position).toString()));
			issue.setAssignTo(user);

			break;
		case R.id.reproducibilityspinner:
			issue.setReproducibility(parent.getItemAtPosition(position).toString());
			break;
		case R.id.severityspinner:
			issue.setSeverity(parent.getItemAtPosition(position).toString());
			break;
		case R.id.priorityspinner:
			issue.setPriority(parent.getItemAtPosition(position).toString());
			break;
		}

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	class CreateIssue extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ReportIssueActivity.this);
			pDialog.setMessage("Creating Issue..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating issue
		 * */
		protected String doInBackground(String... args) {

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("projectName", issue.getProject().getName()));
			params.add(new BasicNameValuePair("assignTo", issue.getAssignTo().getUserName()));
			params.add(new BasicNameValuePair("reproducibility",issue.getReproducibility()));
			params.add(new BasicNameValuePair("severity", issue.getSeverity()));
			params.add(new BasicNameValuePair("priority", issue.getPriority()));
			params.add(new BasicNameValuePair("summary", issue.getSummary()));
			params.add(new BasicNameValuePair("description", issue.getDescription()));
			// getting JSON Object
			// Note that create issue url accepts POST method
			JSONObject js = jParser.makeHttpRequest(url_create_issue, "POST",
					params);

			// check log cat for response
			Log.d("Create Response", js.toString());

			// check for success tag
			try {
				int success = js.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created project
					Intent i = new Intent(getApplicationContext(),
							ViewIssuesActivity.class);
					startActivity(i);

					// closing this screen
					finish();
				} else {
					// failed to create issue
				}
			} catch (JSONException e) {
				Log.i("errrrrrr","errrrrrr");
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

	@Override
	public void onClick(View arg0) {
		issue.setSummary(((EditText) findViewById(R.id.ets)).getText().toString());
		issue.setDescription(((EditText) findViewById(R.id.etd)).getText().toString());
		new CreateIssue().execute();
		new Thread() {
			public void run() {
		BackgroundMail bm = new BackgroundMail(
				getApplicationContext());
		bm.setGmailUserName("zeydrjeb@gmail.com");
		bm.setGmailPassword("260031237md");
		bm.setMailTo(issue.getAssignTo().getEmail());
		bm.setFormSubject("Bug Message Details");
		bm.setFormBody("Dear "+issue.getAssignTo().getUserName()+"\n\nThe following issue has been Created,\nProject: " +issue.getProject().getName()+"\n Assigned To: " +issue.getAssignTo().getUserName()+"\n Reproducibility: " +issue.getReproducibility()+"\nSeverity: " +issue.getSeverity()+"\nPriority: " +issue.getPriority()+"\nSummary: " +issue.getSummary());
		bm.send();}}.start();
	}
}
