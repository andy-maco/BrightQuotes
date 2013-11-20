package com.andreimak.brightquotes;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter for ListView in authors list activity
 * 
 * http://www.vogella.com/articles/AndroidListView/article.html
 * http://stackoverflow.com/questions/17775077/the-constructor-arrayadapterarraylisthashmapstring-stringcontext-int-a
 */
public class AuthorsArrayAdapter extends ArrayAdapter<HashMap<String, String>> {

	private final Context context;
	private final List<HashMap<String, String>> values;

	public AuthorsArrayAdapter(Context context,
			List<HashMap<String, String>> values) {
		super(context, R.layout.row_author, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row_author, parent, false);
		
		TextView textView1 = (TextView) rowView.findViewById(R.id.tvFirstLine);
		TextView textView2 = (TextView) rowView.findViewById(R.id.tvSecondLine);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.ivIcon);

		HashMap<String, String> currentValue = values.get(position);

		textView1.setText(currentValue.get(AuthorsListActivity.KEY_AUTHOR_NAME));
		textView2.setText("Quotes: " + currentValue.get(AuthorsListActivity.KEY_QUOTES_QUANTITY));
		
		Log.d("IMAGE", currentValue.get(AuthorsListActivity.KEY_AUTHOR_IMAGE));
		
		if (currentValue.get(AuthorsListActivity.KEY_AUTHOR_IMAGE).equals("none")) {
			imageView.setImageResource(R.drawable.avatar);
		} else {
			imageView.setImageDrawable(getImageFromAsset(context,
					currentValue.get(AuthorsListActivity.KEY_AUTHOR_IMAGE)));
		}

		return rowView;
	}

	/**
	 * Method load images from assets (input imageUrl like "image.jpg")
	 * 
	 * http://xjaphx.wordpress.com/2011/10/02/store-and-use-files-in-assets/
	 * 
	 * @param context
	 * @param imageUrl
	 * @return
	 */
	private Drawable getImageFromAsset(Context context, String imageUrl) {
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
