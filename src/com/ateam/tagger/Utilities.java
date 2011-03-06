package com.ateam.tagger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Utilities {
	public final static String TAG = "Utilities";

	public static String getDeviceId(Context context) {
		// This can sometimes throw a NullPointerException. Probably has
		// something to do with
		// the emulator not having any telephony services.
		try {
			TelephonyManager phone = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			return phone.getDeviceId();
		} catch (NullPointerException e) {
			return "FAKE_DEVICE_ID";
		}
	}

	public static boolean sendHttpPostMessage(Bundle message) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(message.getString("url"));
		List<NameValuePair> postDataList = new LinkedList<NameValuePair>();
		Boolean success = true;
		for (String key : message.getBundle("postdata").keySet()) {
			postDataList.add(new BasicNameValuePair(key, message.getBundle("postdata").getString(key)));
		}
		try {
			post.setEntity(new UrlEncodedFormEntity(postDataList));
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "Could not encode data", e);
		}

		try {
			HttpResponse response = client.execute(post);
			int status = response.getStatusLine().getStatusCode();
			Log.i(TAG, String.format("Received HTTP status code: %d", status));
			success = (response.getStatusLine().getStatusCode() == 200);
		} catch (ClientProtocolException e) {
			success = false;
			Log.e(TAG, "Message not sent", e);
		} catch (IOException e) {
			success = false;
			Log.e(TAG, "Message not sent", e);
		}

		return success;
	}
}
