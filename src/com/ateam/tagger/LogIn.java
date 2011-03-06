package com.ateam.tagger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LogIn extends Activity {
	EditText useridEntry;
	Button loginButton;

	// Constants
	private static String TAG = "LogIn";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		useridEntry = (EditText) findViewById(R.id.useridEntry);
		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(logInOnClickListener);

		// TODO Auto-generated method stub

	}

	private OnClickListener logInOnClickListener = new OnClickListener() {
		public void onClick(View v) {
			String userid = useridEntry.getText().toString();
			try {
				String authToken = Utilities.authenticateUser(userid);
				Log.d(TAG, String.format("Got authentication token: %s",
						authToken));
				Intent intent = new Intent();
				intent.putExtra("auth-token", authToken);
				intent.putExtra("userid", userid);
				intent.setAction("android.intent.action.TAGGER");
				startActivity(intent);
			} catch (Exception e) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						v.getContext());
				builder.setMessage(
						String.format("Unable to authenticate: %s",
								e.getMessage())).setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
				builder.show();
			}
		}
	};
}
