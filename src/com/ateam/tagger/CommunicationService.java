package com.ateam.tagger;

import android.app.IntentService;
import android.content.Intent;

// See http://developer.android.com/reference/android/app/IntentService.html
// and http://developer.android.com/guide/topics/fundamentals/services.html
public class CommunicationService extends IntentService {

	public CommunicationService() {
		super("MothershipCommunicationThread");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (Utilities.sendHttpPostMessage(intent.getBundleExtra("message"))) {
			synchronized(this) {
				postFailNotification();
				Intent retryIntent = new Intent(this, CommunicationService.class);
				retryIntent.putExtra("message", intent.getBundleExtra("message"));
				startService(retryIntent);
			}
		} else {
			synchronized (this) {
				cancelFailureNotification();
			}
		}
		
	}

	private void cancelFailureNotification() {
		// TODO Cancel any failure notifications that are currently active
		
	}

	private void postFailNotification() {
		// TODO Post a notification that says that there is limited connectivity etc.
		
	}
}
