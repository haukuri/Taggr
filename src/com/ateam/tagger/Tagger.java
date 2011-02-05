package com.ateam.tagger;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class Tagger extends Activity {
	// Widgets
	Location mLocation;
	TextView latitudeLabel;
	TextView longitudeLabel;
	TextView useridLabel;
	TextView barcodeLabel;
	Button scanButton;
	
	// Constants
	static final String TAG = "Tagger";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        latitudeLabel = (TextView)findViewById(R.id.latitudeLabel);
        longitudeLabel = (TextView)findViewById(R.id.longitudeLabel);
        useridLabel = (TextView)findViewById(R.id.useridLabel);
        barcodeLabel = (TextView)findViewById(R.id.barcodeLabel);
        scanButton = (Button)findViewById(R.id.scanButton);
        
        // A LocationListener is an observer class that gets information from the Android location framework.
        // The LocationManager is a facade for the location framework
        // http://developer.android.com/guide/topics/location/obtaining-user-location.html */
        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
        	public void onLocationChanged(Location location) {
        		Log.i(TAG, "Recieved a LocationChanged signal");
        		mLocation = location;
        		latitudeLabel.setText(String.format("%g", mLocation.getLatitude()));
        		longitudeLabel.setText(String.format("%g", mLocation.getLongitude()));
        		sendLocation();
        	}
        	
        	// TODO: Update location precision information
        	public void onStatusChanged(String provider, int status, Bundle extras) {
        		Log.i(TAG, String.format("Recieved a StatusChanged signal: Provider %s, status %d", provider, status));	
        	}
        	
        	// TODO: Update status icon?
        	public void onProviderEnabled(String provider) {
        		Log.i(TAG, String.format("Recieved a ProviderEnabled signal: Provider %s", provider));
        	}
        	
        	// TODO: Update status icon and possibly warn user if GPS is not working
        	public void onProviderDisabled(String provider) {
        		Log.i(TAG, String.format("Recieved a ProviderDisabled signal: Provider %s", provider));
        	}
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
    
	// Send location to central server if more than MAX_LOCATION_AGE seconds have passed
	// since the last update. This should probably be done asynchronously (e.g. in a thread)
    // The information sent should be the longitude, latitude, accuracy and user id
	protected void sendLocation() {
		Log.i(TAG, "Sending location");
	}
}