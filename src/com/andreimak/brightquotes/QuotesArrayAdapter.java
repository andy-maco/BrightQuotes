package com.andreimak.brightquotes;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adapter for ListView in quotes list activity
 */
public class QuotesArrayAdapter extends ArrayAdapter<HashMap<String, String>> {
	
	private static final int ROW_LAYOUT_ID = R.layout.row_quote;

	private final Context context;
	private final List<HashMap<String, String>> values;

	public QuotesArrayAdapter(Context context,
			List<HashMap<String, String>> values) {
		super(context, ROW_LAYOUT_ID, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(ROW_LAYOUT_ID, parent, false);

		TextView tv1 = (TextView) rowView.findViewById(R.id.tvQuoteLine);
		TextView tv2 = (TextView) rowView.findViewById(R.id.tvQuoteTagLine);

		HashMap<String, String> currentValue = values.get(position);

		tv1.setText(currentValue.get(AuthorsListActivity.KEY_AUTHOR_NAME));
		tv2.setText(context.getString(R.string.authors_list_quotes_quantity)
				+ " "
				+ currentValue.get(AuthorsListActivity.KEY_QUOTES_QUANTITY));

		return rowView;
	}
}
