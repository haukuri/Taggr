package com.ateam.tagger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogIn extends Activity {
	EditText useridEntry;
	Button loginButton;

	// Constants
	@SuppressWarnings("unused")
	private static String TAG = "LogIn";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		useridEntry = (EditText) findViewById(R.id.useridEntry);
		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(logInOnClickListener);
	}

	private OnClickListener logInOnClickListener = new OnClickListener() {
		public void onClick(View v) {
			String userid = useridEntry.getText().toString();
			if (Utilities.validateUser(userid)) {
				Intent intent = new Intent();
				intent.putExtra("userid", userid);
				intent.setAction("android.intent.action.TAGGER");
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(), "Invalid user!", Toast.LENGTH_SHORT).show();
			}
		}
	};
}
