package com.ateam.tagger;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;

// See http://developer.android.com/reference/android/app/IntentService.html
// and http://developer.android.com/guide/topics/fundamentals/services.html
public class CommunicationService extends IntentService {
	private NotificationManager mNM;
	
	public CommunicationService() {
		super("MothershipCommunicationThread");
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}
	
	@Override
	public void onDestroy() {
		// TODO: Might be a good idea to cancel any active notifications
		super.onDestroy();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (Utilities.sendHttpPostMessage(intent.getBundleExtra("message"))) {
			synchronized(this) {
				postFailiureNotification();
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

	private void postFailiureNotification() {
		// TODO Post a notification that says that there is limited connectivity etc.
		
	}
}
