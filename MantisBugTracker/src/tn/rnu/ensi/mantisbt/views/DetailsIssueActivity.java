package tn.rnu.ensi.mantisbt.views;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tn.rnu.ensi.mantisbt.R;
import tn.rnu.ensi.mantisbt.services.EditIssueActivity;
import tn.rnu.ensi.mantisbt.webservices.JSONParser;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class DetailsIssueActivity extends Activity {

	TextView e_projectName;
	TextView e_assignTo;
	TextView e_reproducibility;
	TextView e_severity;
	TextView e_priority;
	TextView e_summary;
	ImageButton b_edit;
	ImageButton b_delete;
	String projectName;
	String assignTo;
	String reproducibility;
	String severity;
	String priority;
	String summary;
	String id;
	JSONObject issue;
	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	// single issue url
	private static final String url_issue_details = "http://10.0.2.2/android_connect/get_issue_details.php";

	// url to update issue
	// private static final String url_update_issue =
	// "http://10.0.2.2/android_connect/update_issue.php";

	// url to delete issue
	 private static final String url_delete_issue =	 "http://10.0.2.2/android_connect/delete_issue.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PROJECTNAME = "name";
	private static final String TAG_ASSIGN_TO = "username";
	private static final String TAG_REPRODUCIBILITY = "reproducibility";
	private static final String TAG_SEVERITY = "severity";
	private static final String TAG_PRIORITY = "priority";
	private static final String TAG_SUMMARY = "summary";
	private static final String TAG_ISSUE = "issue";
	private static final String TAG_ID = "id";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.issue_details);
		//getting issue details from intent
		 Intent intent = getIntent();
		 id = intent.getStringExtra(TAG_ID);
		Log.e(TAG_ID, id);
		b_edit = (ImageButton) findViewById(R.id.btnedit);
		b_delete = (ImageButton) findViewById(R.id.btndelete);
b_edit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent intent = new Intent(getApplicationContext(),
						EditIssueActivity.class);
				// sending pid to next activity
				intent.putExtra(TAG_ID, id);
				
				// starting new activity and expecting some response back
				startActivity(intent);
				}
});
		
b_delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent intent = new Intent(getApplicationContext(),
						ViewIssuesActivity.class);
				// sending pid to next activity
				intent.putExtra(TAG_ID, id);
				
				// starting new activity and expecting some response back
				startActivity(intent);
				}
});
		b_delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// Starting new intent
				Thread thread = new Thread() {
					public void run() {
						// do your Code Here
						int success;
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("id", id));
						
						JSONObject json = jsonParser.makeHttpRequest(url_delete_issue,
								"POST", params);
						Log.e("JSONObject json = jsonParser.makeHttpRequest", id);
						// check your log for json response
						// json success tag

						try {
							success = json.getInt(TAG_SUCCESS);
							Log.e("success", "" + success);
							// Building Parameters
							if (success == 1) {
								Intent intent = new Intent(getApplicationContext(),
										ViewIssuesActivity.class);
								// sending pid to next activity
								intent.putExtra(TAG_ID, id);
								
								// starting new activity and expecting some response back
								startActivity(intent);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				};
				thread.start();
				try {
					thread.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		 
		// Getting complete issue details in background thread
		Thread thread = new Thread() {
			public void run() {
				// do your Code Here
				int success;
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("id", id));
				// getting issue details by making HTTP request
				// Note that issue details url will use GET request
				JSONObject json = jsonParser.makeHttpRequest(url_issue_details,
						"GET", params);
				Log.e("JSONObject json = jsonParser.makeHttpRequest", id);
				// check your log for json response
				Log.d("Single issue Details", json.toString());
				// json success tag

				try {
					success = json.getInt(TAG_SUCCESS);
					Log.e("success", "" + success);
					// Building Parameters
					if (success == 1) {
						// successfully received issue details
						JSONArray issueObj = json.getJSONArray(TAG_ISSUE); // JSON
																			// Array

						// get first issue object from JSON Array
						issue = issueObj.getJSONObject(0);
						e_projectName = (TextView) findViewById(R.id.tvpn2);
						e_assignTo = (TextView) findViewById(R.id.tvassignto2);
						e_reproducibility = (TextView) findViewById(R.id.tvr2);
						e_severity = (TextView) findViewById(R.id.tvseverity2);
						e_priority = (TextView) findViewById(R.id.tvp2);
						e_summary = (TextView) findViewById(R.id.tvs2);

						e_projectName.setText(issue.getString(TAG_PROJECTNAME));
						e_assignTo.setText(issue.getString(TAG_ASSIGN_TO));
						e_reproducibility.setText(issue
								.getString(TAG_REPRODUCIBILITY));
						e_severity.setText(issue.getString(TAG_SEVERITY));
						e_priority.setText(issue.getString(TAG_PRIORITY));
						e_summary.setText(issue.getString(TAG_SUMMARY));

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		e_projectName = (TextView) findViewById(R.id.tvpn2);
		e_assignTo = (TextView) findViewById(R.id.tvassignto2);
		e_reproducibility = (TextView) findViewById(R.id.tvr2);
		e_severity = (TextView) findViewById(R.id.tvseverity2);
		e_priority = (TextView) findViewById(R.id.tvp2);
		e_summary = (TextView) findViewById(R.id.tvs2);
		try {
			e_projectName.setText(issue.getString(TAG_PROJECTNAME));
			e_assignTo.setText(issue.getString(TAG_ASSIGN_TO));
			e_reproducibility.setText(issue.getString(TAG_REPRODUCIBILITY));
			e_severity.setText(issue.getString(TAG_SEVERITY));
			e_priority.setText(issue.getString(TAG_PRIORITY));
			e_summary.setText(issue.getString(TAG_SUMMARY));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}