package com.ravivyas.stackapps.configration;

public class ApiConfigration {

	private final static String API_KEY = "cW4njnpKWEmi1b4r6u2mfA";

	private final static String API_BASE_URL = "http://api.stackoverflow.com/";

	private final static String API_VERSION_1 = "1.0";

	public static String getApiVersion1() {
		return API_VERSION_1;
	}

	public static String getApiVersion2() {
		return API_VERSION_1_1;
	}

	private final static String API_VERSION_1_1 = "1.1";

	public static String getApiKey() {
		return API_KEY;
	}

}
