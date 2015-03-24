package tn.rnu.ensi.mantisbt.utilities;

import tn.rnu.ensi.mantisbt.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
	private Context context;
	private final String[] actionValues;

	public ImageAdapter(Context context, String[] actionValues) {
		this.context = context;
		this.actionValues = actionValues;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		if (convertView == null) {

			gridView = new View(context);

			// get layout
			gridView = inflater.inflate(R.layout.action, null);

			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			textView.setText(actionValues[position]);

			// set image based on selected text
			ImageView imageView = (ImageView) gridView
					.findViewById(R.id.grid_item_image);

			String action = actionValues[position];

			if (action.equals("Report Issue")) {
				imageView.setImageResource(R.drawable.newissue);
			} else if (action.equals("View Issues")) {
				imageView.setImageResource(R.drawable.viewissue);
			} else if (action.equals("New Project")) {
				imageView.setImageResource(R.drawable.newfolder);
			} else if (action.equals("View Projects")) {
				imageView.setImageResource(R.drawable.viewprojects);
			} else if (action.equals("Logout")) {
				imageView.setImageResource(R.drawable.logout);
			} else if (action.equals("Contact Us")) {
				imageView.setImageResource(R.drawable.gp);
			} else if (action.equals("About")) {
				imageView.setImageResource(R.drawable.i);
			} else if (action.equals("Manage")) {
				imageView.setImageResource(R.drawable.manage);
			}
		} else {
			gridView = (View) convertView;
		}

		return gridView;
	}

	@Override
	public int getCount() {
		return actionValues.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
