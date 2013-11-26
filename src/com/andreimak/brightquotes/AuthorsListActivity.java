package com.andreimak.brightquotes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.andreimak.brightquotes.adapters.AuthorsArrayAdapter;
import com.andreimak.brightquotes.parsers.JSONParser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * Display list of authors
 */
public class AuthorsListActivity extends ActionBarActivity {

	/* Constant keys for intents */
	protected static final String TAG_AUTHOR = "com.andreimak.brightquotes.TAG_AUTHOR";
	protected static final String TAG_AUTHOR_IMAGE = "com.andreimak.brightquotes.TAG_AUTHOR_IMAGE";

	protected static final String JSON_URL = "bright_quotes.json";
	protected static final String JSON_AUTHORS_ARRAY = "authors";
	protected static final String JSON_AUTHOR = "authorName";
	protected static final String JSON_IMAGE = "image";
	protected static final String JSON_QUOTES = "quotes";

	/* Constant keys for HashMap */
	public static final String KEY_AUTHOR_NAME = "author_name";
	public static final String KEY_AUTHOR_IMAGE = "author_image";
	public static final String KEY_QUOTES_QUANTITY = "quotes_quantity";

	List<HashMap<String, String>> qAuthors = new ArrayList<HashMap<String, String>>();
	
	/* Menu used for hardware button behavior onKeyUp */
	private Menu mainMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Read data from local assets
		qAuthors = getAuthors(JSON_URL);

		final AuthorsArrayAdapter mArrayAdapter = new AuthorsArrayAdapter(this,
				qAuthors);

		setContentView(R.layout.activity_authors_list);
		ListView mListView = (ListView) findViewById(R.id.lvAuthors);

		// Set the ArrayAdapter as the ListView's adapter.
		mListView.setAdapter(mArrayAdapter);
		mListView.setTextFilterEnabled(true);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				String mPickedAuthor = mArrayAdapter.getItem(position).get(
						KEY_AUTHOR_NAME);
				String mPickedAuthorImage = mArrayAdapter.getItem(position)
						.get(KEY_AUTHOR_IMAGE);

				Intent mIntent = new Intent(getApplicationContext(),
						QuotesListActivity.class);
				mIntent.putExtra(TAG_AUTHOR, mPickedAuthor);
				mIntent.putExtra(TAG_AUTHOR_IMAGE, mPickedAuthorImage);
				startActivity(mIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.authors_list_menu, menu);
		mainMenu = menu;
		return true;
	}
	
	/**
	 * Menu action bar handling
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.submenu_about:
			Intent mIntentAbout = new Intent(getApplicationContext(),
					AboutActivity.class);
			startActivity(mIntentAbout);
			return true;
		default:
			return false;
		}
	}

	/**
	 * Menu hardware button open action bar menu
	 * 
	 * http://stackoverflow.com/questions/12277262/
	 * opening-submenu-in-action-bar-on-hardware-menu-button-click
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_MENU:

				mainMenu.performIdentifierAction(R.id.menu_more, 0);
				// Log.d("Menu", "menu button pressed");
				return true;
			}

		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * Method for read JSON and save into List of HashMaps
	 * 
	 * @param url
	 * @return
	 */
	private ArrayList<HashMap<String, String>> getAuthors(String url) {

		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();

		// getting JSON string from URL
		JSONObject jObject = jParser.getJSONFromRaw(getApplicationContext(),
				R.raw.bright_quotes);

		JSONArray jArray = null;
		try {
			jArray = jObject.getJSONArray(JSON_AUTHORS_ARRAY);
		} catch (JSONException ex) {
			ex.printStackTrace();
		}

		String[] Authors = new String[jArray.length()];
		ArrayList<HashMap<String, String>> mAuthorsList = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < jArray.length(); i++) {
			try {

				JSONObject oneObject = jArray.getJSONObject(i);
				// Pulling items from the array
				Authors[i] = oneObject.getString(JSON_AUTHOR);

				JSONArray subArray = oneObject.getJSONArray(JSON_QUOTES);

				mAuthorsList
						.add(putData(oneObject.getString(JSON_AUTHOR),
								oneObject.getString(JSON_IMAGE),
								"" + subArray.length()));

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return mAuthorsList;
	}

	/**
	 * Method save author data into 3 HashMap like {"author_name",
	 * "W.Shakespeare"} etc
	 * 
	 * @param name
	 * @param image
	 * @param quantity
	 * @return
	 */
	private HashMap<String, String> putData(String name, String image,
			String quantity) {
		HashMap<String, String> item = new HashMap<String, String>();
		item.put(KEY_AUTHOR_NAME, name);
		item.put(KEY_AUTHOR_IMAGE, image);
		item.put(KEY_QUOTES_QUANTITY, quantity);
		return item;
	}
}
