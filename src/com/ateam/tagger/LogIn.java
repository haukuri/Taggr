package com.ateam.tagger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LogIn extends Activity {
	EditText useridEntry;
	Button loginButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.login);
	    	    
	    useridEntry = (EditText)findViewById(R.id.useridEntry);
	    loginButton = (Button)findViewById(R.id.loginButton);
	    loginButton.setOnClickListener(logInOnClickListener);
	    
	
	    // TODO Auto-generated method stub
	    
	}
	
	private OnClickListener logInOnClickListener = new OnClickListener() {
		public void onClick(View v) {
			String userid = useridEntry.getText().toString();
			try {
				String authToken = Server.authenticateUser(userid);
				Intent intent = new Intent();
				intent.putExtra("AUTH_TOKEN", authToken);
				intent.setClass(v.getContext(), Tagger.class);			
				startActivity(intent);
			} catch (Exception e) {
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				builder.setMessage(String.format("Unable to authenticate: %s", e.getMessage()))
				       .setPositiveButton("OK", new DialogInterface.OnClickListener () {
				    	   public void onClick(DialogInterface dialog, int id) {
				    		   dialog.cancel();
				    	   }
				       });
				builder.show();
			}
		}
	};
}
