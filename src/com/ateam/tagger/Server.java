package com.ateam.tagger;

public class Server {
	public static String authenticateUser(String userid) throws Exception {
		if (userid == "1337") {
			return "this is an auth token";
		} else {
			throw new Exception("Invalid user");
		}
	}
}
