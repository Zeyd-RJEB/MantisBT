package tn.rnu.ensi.mantisbt.services;

import tn.rnu.ensi.mantisbt.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class ManageActivity extends Activity implements OnClickListener {
	EditText sms;
	ImageButton send;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage);

		send = (ImageButton) findViewById(R.id.ibsms);
		send.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		sms = (EditText) findViewById(R.id.etsms);
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sms.getText()
				.toString() + 41115665)));
	}
}
