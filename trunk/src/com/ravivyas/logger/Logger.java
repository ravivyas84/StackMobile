package com.ravivyas.logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.os.Environment;
import android.util.Log;

public class Logger {
	static FileOutputStream f;
	
	public static String logFileName = null;
	
	// private static RBLogger instance;
	private Logger() {
		if (LogConfiguration.WRITE_LOG_TO_FILE) {
			f = null;
		}
	}

	/*
	 * public static RBLogger getInstance() { if (instance == null) { instance =
	 * new RBLogger(); } return instance; }
	 */

	public static void writeLogToFile(Object o, String msg) {

		writeToFile(System.currentTimeMillis() + "::::"
				+ o.getClass().getSimpleName() + " :::: " + msg + "\n");

	}

	private static void writeLogToFile(String msg) {
		writeToFile(System.currentTimeMillis() + "::::" + msg + "\n");
		
	}
	
	private static void writeToFile(String message) {
		if (f == null) {
			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File(sdCard.getAbsolutePath() + "/"
					+ LogConfiguration.LOCAL_PATH + "/");
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					Log.d(LogConfiguration.TAG, "Error writing to file , Cound not create");
				}
			} else {
				Log.d(LogConfiguration.TAG, "Dir already exists");
			}
			if(Logger.logFileName == null){
				Logger.logFileName = String.valueOf(System.currentTimeMillis());
				Log.d(LogConfiguration.TAG, "--------------------------New Log File Due To Error------------------------------");
			}
			File file = new File(dir, Logger.logFileName);
			Log.d(LogConfiguration.TAG, "Filename : " + file.getAbsolutePath());
			try {
				f = new FileOutputStream(file, true);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d(LogConfiguration.TAG, "Error writing to file , not found");
				return;
			}
		}

		try {
			f.write(message.getBytes());
			// Log.d(TAG, "Witten to file : " + filename);
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(LogConfiguration.TAG, "Error writing to file");
		}
	}

	public static void debug(Object o, String msg) {
		if (LogConfiguration.LOG_LEVEL <= Log.DEBUG){
			Log.d(LogConfiguration.TAG, o.getClass().getSimpleName() + ": " + msg);
			if (LogConfiguration.WRITE_LOG_TO_FILE) {
				writeLogToFile(o, "debug :" + msg);
			}
		}

	}

	public static void error(Object o, String msg) {
		if (LogConfiguration.LOG_LEVEL <= Log.ERROR){
			Log.e(LogConfiguration.TAG, o.getClass().getSimpleName() + ": " + msg);
			if (LogConfiguration.WRITE_LOG_TO_FILE) {
				writeLogToFile(o, "error :" + msg);
			}
		}
	}

	public static void e(Object o, String msg) {
		if (LogConfiguration.LOG_LEVEL <= Log.ERROR){
			Log.e(LogConfiguration.TAG, o.getClass().getSimpleName() + ": " + msg);
			if (LogConfiguration.WRITE_LOG_TO_FILE) {
				writeLogToFile(o, "e :" + msg);
			}
		}
	}

	public static void w(Object o, String msg) {
		if (LogConfiguration.LOG_LEVEL <= Log.WARN){
			Log.w(LogConfiguration.TAG, o.getClass().getSimpleName() + ": " + msg);
			if (LogConfiguration.WRITE_LOG_TO_FILE) {
				writeLogToFile(o, "w :" + msg);
			}
		}
	}

	public static void i(Object o, String msg) {
		if (LogConfiguration.LOG_LEVEL <= Log.INFO){
			Log.i(LogConfiguration.TAG, o.getClass().getSimpleName() + ": " + msg);
			if (LogConfiguration.WRITE_LOG_TO_FILE) {
				writeLogToFile(o, "i :" + msg);
			}
		}
	}

	public static void d(Object o, String msg) {
		if (LogConfiguration.LOG_LEVEL <= Log.DEBUG){
			Log.d(LogConfiguration.TAG, o.getClass().getSimpleName() + ": " + msg);
			if (LogConfiguration.WRITE_LOG_TO_FILE) {
				writeLogToFile(o, "d :" + msg);
			}
		}
	}

	public static void d(Object o, String msg, Exception e) {
		if (LogConfiguration.LOG_LEVEL <= Log.DEBUG){
			Log.d(LogConfiguration.TAG, o.getClass().getSimpleName() + ": " + msg, e);
			if (LogConfiguration.WRITE_LOG_TO_FILE) {
				writeLogToFile(o, "d, e :" + msg);
			}
		}
	}

	public static void d(String className, String msg) {
		if (LogConfiguration.LOG_LEVEL <= Log.DEBUG){
			Log.d(LogConfiguration.TAG, className + msg);
			if (LogConfiguration.WRITE_LOG_TO_FILE) {
				writeLogToFile("d : " + className + " , " + msg);
			}
			
		}
	}

}
