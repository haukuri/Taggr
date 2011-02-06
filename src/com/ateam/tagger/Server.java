package com.ateam.tagger;

import android.location.Location;

public class Server {
	public static String authenticateUser(String userid) throws Exception {
		if (userid.compareTo("1337") == 0) {
			return "this is an auth token";
		} else {
			throw new Exception("Invalid user");
		}
	}

	public static boolean sendAgentLocation(Location aLocation, String authToken) {
		// TODO Auto-generated method stub
		return true;
	}
}
