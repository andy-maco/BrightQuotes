package com.andreimak.brightquotes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

/**
 * Read JSON from URL or local assets folder
 * 
 * http://stackoverflow.com/questions/9605913/how-to-parse-json-in-android
 * http://stackoverflow.com/questions/13814503/reading-a-json-file-in-android
 */
public class JSONParser {
	
	static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {}

    /**
     * Read JSON from URL
     * 
     * @param url
     * @return
     */
    public JSONObject getJSONFromUrl(String url) {

        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (ClientProtocolException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
        	// json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception ex) {
            Log.e("Buffer Error", "Error converting result " + ex.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException ex) {
            Log.e("JSON Parser", "Error parsing data " + ex.toString());
        }

        // return JSON String
        return jObj;

    }
	
    
    /**
     * Read JSON from local asset
     * 
     * url - for local "file_name.json"
     * 
     * @param context
     * @param url
     * @return
     */
    public JSONObject getJSONFromAsset(Context context,String url) {
        try {
        	AssetManager mAssetManager = context.getAssets();
            InputStream is = mAssetManager.open(url);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
        	Log.e("JSON Parser", "Error parsing data " + ex.toString());
        }
        
     // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException ex) {
            Log.e("JSON Parser", "Error parsing data " + ex.toString());
        }

        // return JSON String
        return jObj;
    }
 
    
	/**
	 * Read JSON from raw resources
	 * 
	 * http://stackoverflow.com/questions/6349759/using-json-file-in-android-app-resources
	 * 
	 * @param context
	 * @param url
	 * @return
	 */
    public JSONObject getJSONFromRaw(Context context,int ResID) {
        try {
            InputStream is = context.getResources().openRawResource(ResID);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
        	Log.e("JSON Parser", "Error parsing data " + ex.toString());
        }
        
     // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException ex) {
            Log.e("JSON Parser", "Error parsing data " + ex.toString());
        }

        // return JSON String
        return jObj;
    }
}
