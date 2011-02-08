package com.ateam.tagger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.location.Location;
import android.util.Log;

public class Server {
	private static String TAG = "Server";
	
	public static String authenticateUser(String userid) throws Exception {
		if (userid.compareTo("1337") == 0) {
			return "this is an auth token";
		} else {
			throw new Exception("Invalid user");
		}
	}

	public static boolean sendAgentLocation(Location aLocation, String agentid, String deviceid) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://srtagger.appspot.com/agentlocation");
		try {
			// Add data to post
			List<NameValuePair> pairs = new ArrayList<NameValuePair>(5);
			pairs.add(new BasicNameValuePair("agentid", agentid));
			pairs.add(new BasicNameValuePair("deviceid", deviceid));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ", Locale.getDefault());			
			pairs.add(new BasicNameValuePair("time", format.format(new Date())));
			pairs.add(new BasicNameValuePair("longitude", String.format("%f", aLocation.getLongitude())));
			pairs.add(new BasicNameValuePair("latitude", String.format("%f", aLocation.getLatitude())));
			post.setEntity(new UrlEncodedFormEntity(pairs));
			
			// Send POST request
			HttpResponse response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();
			Log.d(TAG, String.format("Got HTTP response: %d", code));
			return code == 200;
		} catch (Exception e) {
			Log.e(TAG, "Failed to send location", e);
			return false;
		}
	}
	
	// TODO: Maybe we should send the timestamp on the Location fix to allow the server to estimate the accuracy
	//       of the tag
	public static boolean sendPatientLoaction(Location pLocation,
			String qrcode, String agentid, String deviceid) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://srtagger.appspot.com/patientlocation");
		try {
			// Add data to post
			List<NameValuePair> pairs = new ArrayList<NameValuePair>(6);
			pairs.add(new BasicNameValuePair("agentid", agentid));
			pairs.add(new BasicNameValuePair("deviceid", deviceid));
			pairs.add(new BasicNameValuePair("qrcode", qrcode));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ", Locale.getDefault());			
			pairs.add(new BasicNameValuePair("time", format.format(new Date())));
			pairs.add(new BasicNameValuePair("longitude", String.format("%f", pLocation.getLongitude())));
			pairs.add(new BasicNameValuePair("latitude", String.format("%f", pLocation.getLatitude())));
			post.setEntity(new UrlEncodedFormEntity(pairs));
			
			// Send POST request
			HttpResponse response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();
			Log.d(TAG, String.format("Got HTTP response: %d", code));
			return code == 200;
		} catch (Exception e) {
			Log.e(TAG, "Failed to send patient location", e);
			return false;
		}
	}
}
