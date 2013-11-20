package com.andreimak.brightquotes;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Display list of quotes by selected author
 */
public class QuotesListActivity extends ActionBarActivity {

	protected final static String TAG_QUOTE = "tag quote";

	private String mPickedAuthor;
	private int mPickedAuthorArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* --- Read author from parent intent --- */
		Intent mIntentStarted = getIntent();
		mPickedAuthor = mIntentStarted.getStringExtra(AuthorsListActivity.TAG_AUTHOR);
		
		mPickedAuthorArray = getResources().getIdentifier(mPickedAuthor.replaceAll("\\s+",""), "array", getPackageName());

		/* --- Choose and load xml file for picked author --- */
		Resources mResources = getResources();
		String[] quoteList = mResources.getStringArray(mPickedAuthorArray);

		// Making final to so that we can reference it inside the listener
		final ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(
				this, R.layout.list_row_quote, R.id.tvQuote, quoteList);

		setContentView(R.layout.activity_quotes_list);
		
		// Find the ListView resource.
		ListView mListView = (ListView)findViewById(R.id.lvQuotes);
				
		// Set the ArrayAdapter as the ListView's adapter.
		mListView.setAdapter(mArrayAdapter);
				
		mListView.setTextFilterEnabled(true);

		/* --- Making list clickable --- */
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				String pickedQuote = mArrayAdapter.getItem(position);

				Intent mIntent = new Intent(getApplicationContext(),
						QuoteActivity.class);
				mIntent.putExtra(TAG_QUOTE, pickedQuote);
				startActivity(mIntent);

			}
		});

		// Show the Up button in the action bar.
		setupActionBar();

	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quotes_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
