package com.ravivyas.logger;

public class LogConfiguration {

	static final String TAG = "CECLOG";
	
	// VERBOSE = 2;
	// DEBUG = 3;
	// INFO = 4;
	// WARN = 5;
	// ERROR = 6;
	// ASSERT = 7;
	public static final int LOG_LEVEL = 2;
	
	/*
	 * if set writes log to SDcard, please make sure Write permission is enabled
	 */
	public static boolean WRITE_LOG_TO_FILE = true;

	/*
	 * folder where logs will be stored
	 */
	public static String LOCAL_PATH = "aiuLogs";
	
}
