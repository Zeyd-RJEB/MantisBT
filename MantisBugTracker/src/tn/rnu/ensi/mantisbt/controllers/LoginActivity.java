package tn.rnu.ensi.mantisbt.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import tn.rnu.ensi.mantisbt.R;
import tn.rnu.ensi.mantisbt.entities.User;
import tn.rnu.ensi.mantisbt.services.ForgotPasswordActivity;
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

public class LoginActivity extends Activity {

	// Progress Dialog
	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();
	EditText username;
	EditText password;
	TextView forgot;
	TextView signUp;
	User user = new User();
	

	private static String url_check_user = "http://10.0.2.2/android_connect/check_user.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if(extras.getString("error").equals("error")){
			final TextView textView;
			textView = (TextView) findViewById(R.id.tverror);
			textView.setText(R.string.please_check);
			textView.setVisibility(View.VISIBLE);
			textView.postDelayed(new Runnable() {
				public void run() {
				    textView.setVisibility(View.INVISIBLE);
				}
				}, 5000);
		}}

		// Edit Text
		username = (EditText) findViewById(R.id.etusername);
		password = (EditText) findViewById(R.id.etpassword);
		forgot = (TextView) findViewById(R.id.tvforgotpw);
		forgot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				user.setUserName(username.getText().toString());
				Log.e("Onclickkkk", user.getUserName());
				Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
				
				intent.putExtra("username", user.getUserName());
				startActivity(intent);
			}
		});
		signUp = (TextView) findViewById(R.id.tvsignup);
		signUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				user.setUserName(username.getText().toString());
				Log.e("Onclickkkk", user.getUserName());
				Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
				intent.putExtra("username", user.getUserName());
				startActivity(intent);
			}
		});

		// Login ImageButton
		ImageButton btnLogin = (ImageButton) findViewById(R.id.btnlogin);

		// imageButton click event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// check user in background thread
				new CheckUser().execute();
			}
		});
	}

	/**
	 * Background Async Task to check user
	 * */
	class CheckUser extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Checking User..");
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

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", user.getUserName()));
			params.add(new BasicNameValuePair("password", user.getPassWord()));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_check_user,
					"POST", params);

			// check log cat for response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully checked user
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
							LoginActivity.class);
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
