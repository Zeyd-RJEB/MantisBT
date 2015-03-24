package tn.rnu.ensi.mantisbt.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import tn.rnu.ensi.mantisbt.R;
import tn.rnu.ensi.mantisbt.entities.User;
import tn.rnu.ensi.mantisbt.views.GridViewActivity;
import tn.rnu.ensi.mantisbt.webservices.JSONParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class SignUpActivity extends Activity {

	// Progress Dialog
		private ProgressDialog pDialog;

		JSONParser jsonParser = new JSONParser();
		EditText username;
		EditText password;
		EditText email;
		User user = new User();
		

		private static String url_add_user = "http://10.0.2.2/android_connect/add_user.php";

		// JSON Node names
		private static final String TAG_SUCCESS = "success";

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.signup);
//			Bundle extras = getIntent().getExtras();
//			if (extras != null) {
//				if(extras.getString("error").equals("error")){
//				final TextView textView;
//				textView = (TextView) findViewById(R.id.tverror);
//				textView.setText(R.string.please_check);
//				textView.setVisibility(View.VISIBLE);
//				textView.postDelayed(new Runnable() {
//					public void run() {
//					    textView.setVisibility(View.INVISIBLE);
//					}
//					}, 5000);
//			}}

			// Edit Text
			username = (EditText) findViewById(R.id.etun);
			password = (EditText) findViewById(R.id.etpw);
			email = (EditText) findViewById(R.id.ete);
			// signup ImageButton
			ImageButton btnSignUp = (ImageButton) findViewById(R.id.btnsu);

			// imageButton click event
			btnSignUp.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					// Add user in background thread
					new AddUser().execute();
				}
			});
		}

		/**
		 * Background Async Task to add user
		 * */
		class AddUser extends AsyncTask<String, String, String> {

			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(SignUpActivity.this);
				pDialog.setMessage("Adding User..");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}

			/**
			 * Creating user
			 * */
			protected String doInBackground(String... args) {
				user.setUserName(username.getText().toString());
				user.setPassWord(password.getText().toString());
				user.setEmail(email.getText().toString());

				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", user.getUserName()));
				params.add(new BasicNameValuePair("password", user.getPassWord()));
				params.add(new BasicNameValuePair("email", user.getEmail()));

				// getting JSON Object
				// Note that create product url accepts POST method
				JSONObject json = jsonParser.makeHttpRequest(url_add_user,
						"POST", params);

				// check log cat for response
				Log.d("Create Response", json.toString());

				// check for success tag
				try {
					int success = json.getInt(TAG_SUCCESS);

					if (success == 1) {
						// successfully added user
						Log.i("success tag", "successfully checked user");

						Intent i = new Intent(getApplicationContext(),
								GridViewActivity.class);
						i.putExtra("username", user.getUserName());
						startActivity(i);

						// closing this screen
						finish();
					} else {
						// fail
						Log.i("success tag", "fail");
						Intent i = new Intent(getApplicationContext(),
								SignUpActivity.class);
						i.putExtra("error", "error");
						startActivity(i);

					}
				} catch (JSONException e) {
					Log.i("Exception", "Exception");
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


		
	}