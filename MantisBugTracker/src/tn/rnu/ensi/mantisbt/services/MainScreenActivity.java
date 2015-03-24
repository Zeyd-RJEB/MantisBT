package tn.rnu.ensi.mantisbt.services;

import tn.rnu.ensi.mantisbt.R;
import tn.rnu.ensi.mantisbt.views.ViewProjectsActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainScreenActivity extends Activity {

	Button btnView;
	Button btnNew;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);

		// Buttons
		btnView = (Button) findViewById(R.id.btnView);
		btnNew = (Button) findViewById(R.id.btnCreate);

		// view products click event
		btnView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching All products Activity
				Intent i = new Intent(getApplicationContext(),
						ViewProjectsActivity.class);
				startActivity(i);

			}
		});

		// view products click event
		btnNew.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching create new activity
				
				Intent i = new Intent(getApplicationContext(),
						ReportIssueActivity.class);
				startActivity(i);

			}
		});
	}
}
