package tn.rnu.ensi.mantisbt.views;

import tn.rnu.ensi.mantisbt.R;
import tn.rnu.ensi.mantisbt.controllers.LogoutActivity;
import tn.rnu.ensi.mantisbt.services.MainScreenActivity;
import tn.rnu.ensi.mantisbt.services.NewProjectActivity;
import tn.rnu.ensi.mantisbt.services.ReportIssueActivity;
import tn.rnu.ensi.mantisbt.utilities.ImageAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class GridViewActivity extends Activity {

	GridView gridView;

	static final String[] Actions = new String[] { "Report Issue",
			"View Issues", "New Project", "View Projects", "Logout",
			"Contact Us", "About", "Manage" };

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.actionslist);
		System.out.println("Cbon");
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String username = extras.getString("username");
			TextView textView;
			textView = (TextView) findViewById(R.id.tvhello);
			if (textView != null) {
				textView.setTextSize(8);
				textView.setVisibility(View.VISIBLE);
				textView.setText(username);
				textView.setTextColor(0x112233);
			}
			gridView = (GridView) findViewById(R.id.gridView1);

			gridView.setAdapter(new ImageAdapter(this, Actions));

			gridView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					Toast.makeText(
							getApplicationContext(),
							((TextView) v.findViewById(R.id.grid_item_label))
									.getText(), Toast.LENGTH_SHORT).show();
					if (((TextView) v.findViewById(R.id.grid_item_label))
							.getText().equals("Report Issue")) {
						Intent myIntent = new Intent(getApplicationContext(),
								ReportIssueActivity.class);
						myIntent.putExtra("key", "msg");
						startActivity(myIntent);
					} else if (((TextView) v.findViewById(R.id.grid_item_label))
							.getText().equals("View Issues")) {
						Intent myIntent = new Intent(getApplicationContext(),
								ViewIssuesActivity.class);
						myIntent.putExtra("key", "msg");
						startActivity(myIntent);
					} else if (((TextView) v.findViewById(R.id.grid_item_label))
							.getText().equals("New Project")) {
						Intent myIntent = new Intent(getApplicationContext(),
								NewProjectActivity.class);
						myIntent.putExtra("key", "msg");
						startActivity(myIntent);
					} else if (((TextView) v.findViewById(R.id.grid_item_label))
							.getText().equals("About")) {
						Intent myIntent = new Intent(getApplicationContext(),
								AboutActivity.class);
						myIntent.putExtra("key", "msg");
						startActivity(myIntent);
					} else if (((TextView) v.findViewById(R.id.grid_item_label))
							.getText().equals("Logout")) {
						Intent myIntent = new Intent(getApplicationContext(),
								LogoutActivity.class);
						myIntent.putExtra("key", "msg");
						startActivity(myIntent);
					} else if (((TextView) v.findViewById(R.id.grid_item_label))
							.getText().equals("View Projects")) {
						Intent myIntent = new Intent(getApplicationContext(),
								ViewProjectsActivity.class);
						myIntent.putExtra("key", "msg");
						startActivity(myIntent);
					}	else if (((TextView) v.findViewById(R.id.grid_item_label))
								.getText().equals("Contact Us")) {
							Intent myIntent = new Intent(getApplicationContext(),
									GooglePlusActivity.class);
							myIntent.putExtra("key", "msg");
							startActivity(myIntent);
					} else if (((TextView) v.findViewById(R.id.grid_item_label))
							.getText().equals("Manage")) {
						Intent myIntent = new Intent(getApplicationContext(),
								MainScreenActivity.class);
						startActivity(myIntent);
						// }
						// } else {
						//
					}

				}
			});
			
		}
	}
}
