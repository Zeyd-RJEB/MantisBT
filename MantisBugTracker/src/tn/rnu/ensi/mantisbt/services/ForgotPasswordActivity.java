package tn.rnu.ensi.mantisbt.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tn.rnu.ensi.mantisbt.controllers.LoginActivity;
import tn.rnu.ensi.mantisbt.entities.User;
import tn.rnu.ensi.mantisbt.mailconfig.BackgroundMail;
import tn.rnu.ensi.mantisbt.webservices.JSONParser;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ForgotPasswordActivity extends Activity{

	// Progress Dialog
		//private ProgressDialog pDialog;

		JSONParser jsonParser = new JSONParser();
		JSONObject user;
		User User = new User();

		private static String url_get_user = "http://10.0.2.2/android_connect/get_user.php";

		// JSON Node names
		private static final String TAG_SUCCESS = "success";

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				 User.setUserName(extras.getString("username"));
				
					// check user in background thread
				// Building Parameters
				new Thread() {
					public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", User.getUserName()));
	

				// getting JSON Object
				JSONObject json = jsonParser.makeHttpRequest(url_get_user,
						"POST", params);

				// check log cat for response
				Log.d("Create Response", json.toString());

				// check for success tag
				try {
					int success = json.getInt(TAG_SUCCESS);

					if (success == 1) {
						// successfully checked user
						Log.i("success tag", "successfully retrieved user");
						JSONArray userObj = json.getJSONArray("user");
						user = userObj.getJSONObject(0);
						User.setPassWord(user.getString("password"));
						User.setEmail(user.getString("email"));
						
						new Thread() {
							public void run() {
						BackgroundMail bm = new BackgroundMail(
								getApplicationContext());
						bm.setGmailUserName("zeydrjeb@gmail.com");
						bm.setGmailPassword("260031237md");
						bm.setMailTo(User.getEmail());
						bm.setFormSubject("Password Message Sent");
						bm.setFormBody("Dear "+User.getUserName()+" \n\nIf you supplied the correct password for your account, we will now have sent a confirmation message to that e-mail address. \nOnce the message has been received you can retrieve your password \nPASSWORD : "+User.getPassWord());
						bm.send();}}.start();
						
						
						
						
						//Toast.makeText(getApplicationContext(), "A mail was sent Please chek your MailBox", 
							//	   Toast.LENGTH_LONG).show();
						// closing this screen
						finish();
					} else {
						// fail
						Log.i("success tag", "fail");
						//Toast.makeText(getApplicationContext(), "This name does not match in Mantis Data Base", 
							//	   Toast.LENGTH_LONG).show();
						Intent i = new Intent(getApplicationContext(),
								LoginActivity.class);
						startActivity(i);

					}
				} catch (JSONException e) {
					Log.i("Exception", "Exception");
					e.printStackTrace();
				}
					}}.start();}}

		
			}