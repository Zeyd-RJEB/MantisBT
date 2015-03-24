package tn.rnu.ensi.mantisbt.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LogoutActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent myIntent = new Intent(this, LoginActivity.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
		startActivity(myIntent);
		finish();
	}
}
