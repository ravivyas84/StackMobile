package com.ravivyas.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.os.Environment;

public class ExceptionLogger implements UncaughtExceptionHandler {
	File sdCard;
	private UncaughtExceptionHandler defaultUEH;

	public ExceptionLogger() {
		this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
		sdCard = Environment.getExternalStorageDirectory();
	}

	public void uncaughtException(Thread t, Throwable e) {
		String timestamp = (String) String.valueOf(System.currentTimeMillis());
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		e.printStackTrace(printWriter);
		String stacktrace = result.toString();
		printWriter.close();
		String filename = timestamp + ".stacktrace";

		if (LogConfiguration.LOCAL_PATH != null) {
			writeToFile(stacktrace, filename);
		}

		defaultUEH.uncaughtException(t, e);
	}

	private void writeToFile(String stacktrace, String filename) {
		try {
			BufferedWriter bos = new BufferedWriter(new FileWriter(
					sdCard.getAbsolutePath() + "/" + LogConfiguration.LOCAL_PATH
							+ "/" + filename));
			bos.write(stacktrace);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}