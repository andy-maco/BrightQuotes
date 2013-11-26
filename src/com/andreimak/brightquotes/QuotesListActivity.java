package com.andreimak.brightquotes;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.andreimak.brightquotes.adapters.QuotesArrayAdapter;
import com.andreimak.brightquotes.parsers.JSONParser;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Display list of quotes by selected author
 */
public class QuotesListActivity extends ActionBarActivity {

	protected final static String TAG_QUOTE = "tag quote";
	
	/* Constant keys for HashMap */
	public static final String KEY_QUOTE_TEXT = "quote_text";


	List<HashMap<String, String>> qList = new ArrayList<HashMap<String, String>>();

	private String mPickedAuthor;
	private String mPickedAuthorImage;
	
	/* Menu used for hardware button behavior onKeyUp */
	private Menu mainMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Read author from parent intent
		Intent mIntentStarted = getIntent();
		mPickedAuthor = mIntentStarted.getStringExtra(AuthorsListActivity.TAG_AUTHOR);
		mPickedAuthorImage = mIntentStarted.getStringExtra(AuthorsListActivity.TAG_AUTHOR_IMAGE);
		
		qList = getQuotes(AuthorsListActivity.JSON_URL, R.raw.bright_quotes, mPickedAuthor);
		
		final QuotesArrayAdapter mArrayAdapter = new QuotesArrayAdapter(this,
				qList);
		
		setContentView(R.layout.activity_quotes_list);
		
		TextView tv = (TextView)findViewById(R.id.tvAuthorInQuotesList);
		tv.setText(mPickedAuthor);
		
		ImageView iv = (ImageView)findViewById(R.id.ivAuthorQuotesListIcon);
		if (mPickedAuthorImage.equals("none")) {
			iv.setImageResource(R.drawable.avatar);
		} else {
			iv.setImageDrawable(getImageFromAsset(getApplicationContext(), mPickedAuthorImage));
		}
		
		ListView mListView = (ListView)findViewById(R.id.lvQuotes);
		// Set the ArrayAdapter as the ListView's adapter.
		mListView.setAdapter(mArrayAdapter);
		mListView.setTextFilterEnabled(true);
		
		/*
		mPickedAuthorArray = getResources().getIdentifier(mPickedAuthor.replaceAll("\\s+",""), "array", getPackageName());

		//Choose and load xml file for picked author
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
		
		*/

		/* --- Making list clickable --- */
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				String pickedQuote = mArrayAdapter.getItem(position).get(KEY_QUOTE_TEXT);	

				Intent mIntent = new Intent(getApplicationContext(),
						QuoteActivity.class);
				mIntent.putExtra(TAG_QUOTE, pickedQuote);
				mIntent.putExtra(AuthorsListActivity.TAG_AUTHOR, mPickedAuthor);
				mIntent.putExtra(AuthorsListActivity.TAG_AUTHOR_IMAGE, mPickedAuthorImage);
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
		getMenuInflater().inflate(R.menu.quotes_list_menu, menu);
		mainMenu = menu;
		return true;
	}
	
	/**
	 * Menu action bar handling
	 */
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
		case R.id.submenu_about:
			Intent mIntentAbout = new Intent(getApplicationContext(),
					AboutActivity.class);
			startActivity(mIntentAbout);
			return true;
		}
		return super.onOptionsItemSelected(item);
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
	private ArrayList<HashMap<String, String>> getQuotes(String url, int ResID, String pickedAuthor) {
		
		ArrayList<HashMap<String, String>> mQuotesList = new ArrayList<HashMap<String, String>>();

		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();

		// getting JSON string from URL
		JSONObject jObject = jParser.getJSONFromRaw(getApplicationContext(), ResID);

		JSONArray jArray = null;
		try {
			jArray = jObject.getJSONArray(AuthorsListActivity.JSON_AUTHORS_ARRAY);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String[] Authors = new String[jArray.length()];

		for (int i = 0; i < jArray.length(); i++) {
			try {

				JSONObject oneObject = jArray.getJSONObject(i);
				// Pulling items from the array
				Authors[i] = oneObject.getString(AuthorsListActivity.JSON_AUTHOR);

				if (Authors[i].equals(pickedAuthor)) {					
					JSONArray subArray = oneObject.getJSONArray(AuthorsListActivity.JSON_QUOTES);
					
					//Log.d("ARRAY", subArray + " length" + subArray.length());
					
					for (int j = 0; j < subArray.length(); j++) {
						
						//Log.d("ARRAY_", subArray.getString(j));
						
						// Not objects array - parse throw getString(j)
						mQuotesList.add(putData(subArray.getString(j)));
						
						/*
						 * if it was array of objects (i.e. have { at begin and } at end)
						 * 
						JSONObject jsonData = subArray.getJSONObject(j);
						mQuotesList.add(putData(jsonData.getString(AuthorsListActivity.JSON_QUOTES)));
						*
						*/

					}
					
				}
					
				//JSONArray subArray = oneObject.getJSONArray(AuthorsListActivity.JSON_QUOTES);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return mQuotesList;
	}

	/**
	 * Method save quote data into several HashMap
	 * 
	 * @param text
	 * @return
	 */
	private HashMap<String, String> putData(String text) {
		HashMap<String, String> item = new HashMap<String, String>();
		item.put(KEY_QUOTE_TEXT, text);
		return item;
	}
	
	/**
	 * Method load images from assets (input imageUrl like "folder/image.jpg")
	 * 
	 * http://xjaphx.wordpress.com/2011/10/02/store-and-use-files-in-assets/
	 * 
	 * @param context
	 * @param imageUrl
	 * @return
	 */
	private Drawable getImageFromAsset(Context context, String imageUrl) {
		//TODO: pull out into separate helper class
		Drawable mDrawable = null;
		try {
			AssetManager mAssetManager = context.getAssets();
			// get input stream
			InputStream is = mAssetManager.open(imageUrl);
			// load image as Drawable
			mDrawable = Drawable.createFromStream(is, null);
			is.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return mDrawable;
	}

}
