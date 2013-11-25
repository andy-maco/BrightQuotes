package com.andreimak.brightquotes;

import java.io.IOException;
import java.io.InputStream;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Display single quote selected from quotes list activity 
 */
public class QuoteActivity extends ActionBarActivity {
	
	private String mPickedQuote;
	private String mPickedAuthor;
	private String mPickedAuthorImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quote);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Intent mIntentStarted = getIntent();
		mPickedQuote = mIntentStarted.getStringExtra(QuotesListActivity.TAG_QUOTE);
		mPickedAuthor = mIntentStarted.getStringExtra(AuthorsListActivity.TAG_AUTHOR);
		mPickedAuthorImage = mIntentStarted.getStringExtra(AuthorsListActivity.TAG_AUTHOR_IMAGE);
		
		TextView mTextViewQouteText = (TextView) findViewById(R.id.tvQuoteText);
		mTextViewQouteText.setText(mPickedQuote);
		
		TextView tv = (TextView)findViewById(R.id.tvAuthorQuoteName);
		String emDash = Html.fromHtml("&mdash;").toString();
		tv.setText(emDash + " " + mPickedAuthor);
		
		ImageView iv = (ImageView)findViewById(R.id.ivAuthorQuoteIcon);
		if (mPickedAuthorImage.equals("none")) {
			iv.setImageResource(R.drawable.avatar);
		} else {
			iv.setImageDrawable(getImageFromAsset(getApplicationContext(), mPickedAuthorImage));
		}
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
		getMenuInflater().inflate(R.menu.quote, menu);
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
