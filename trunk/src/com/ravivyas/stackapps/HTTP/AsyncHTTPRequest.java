package com.ravivyas.stackapps.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;

import com.ravivyas.logger.Logger;

/**
 * Async task based HTTP request object . To make a http request , create a new
 * AsyncHttpRequest object , define the abstract methods and then call
 * createPostRequest or createGetRequest
 * 
 * AIU Specific : Make sure you check if the deivce has a data connection by
 * using com.aiu.utils.Status.isDataConnectionOn()
 * 
 * @author Ravi
 * 
 */
public abstract class AsyncHTTPRequest extends AsyncTask<Void, Integer, byte[]> {
	private String requestURL, postData;

	/*
	 * Used to keep track of users request type -1 : no request set 0 : get 1 :
	 * post
	 */
	private int requestType;
	private HttpResponse response;

	private int sizeRead;

	private int contentLength;

	private String userName;

	private String password;

	private static final int HTTP_RESPONSE_OK = 200;

	Timer timer;

	public AsyncHTTPRequest() {
		requestURL = null;
		postData = null;
		requestType = -1;
		contentLength = -1;
	}

	/*
	 * Not needed for CEC project , no get requests
	 */
	/*
	 * public void createGetRequest(String requestURL) { this.requestURL =
	 * requestURL; requestType = 0; // Set request type to get if (requestURL !=
	 * null) { Logger.d(this, "Async HTTP request executing"); this.execute(); }
	 * }
	 */

	/**
	 * Method for creating a post request
	 * 
	 * @param requestURL
	 *            URL for the post request
	 * @param postData
	 *            Data to be sent as a part of the post request
	 */
	public void createPostRequest(String requestURL, String postData) {
		this.requestURL = requestURL;
		this.postData = postData;
		requestType = 1; // Set request type to post
		if (requestURL != null && postData != null) {
			Logger.d(this, "Async HTTP post request executing");
			this.execute();
		}
	}

	public void createPostLoginRequest(String requestURL, String postData,
			String userName, String password) {
		this.requestURL = requestURL;
		this.postData = postData;
		this.userName = userName;
		this.password = password;
		requestType = 2; // Set request type to post , login
		if (requestURL != null && postData != null) {
			Logger.d(this, "Async HTTP post request executing");
			this.execute();
		}
	}

	@Override
	protected final byte[] doInBackground(Void... params) {
		if (isCancelled()) {
			return null;
		}
		Logger.d(this, "Async HTTP request started");
		if (requestType == -1) {
			return null;
		}
		final HttpClient client = new DefaultHttpClient();
		timer = new Timer();
		timer.schedule(timerTask, 60000);
		long startTime = System.currentTimeMillis();

		try {
			switch (requestType) {
			/*
			 * Not needed for CEC project , no get requests
			 */
			/*
			 * case 0: HttpGet getJob = new HttpGet(requestURL); Logger.d(this,
			 * "get url: " + requestURL); response = client.execute(getJob);
			 * break;
			 */
			case 1:
				if (!isCancelled()) {
					Logger.d(this, "post data: " + postData);
					Logger.d(this, "post url: " + requestURL);
					HttpPost postJob = new HttpPost(requestURL);
					StringEntity se = new StringEntity(postData, HTTP.UTF_8);
					se.setContentType("application/json");
					postJob.setEntity(se);
					Logger.d(this, "Starting HTTP POST REQUEST");
					response = client.execute(postJob);
				}
				break;
			case 2:
				if (!isCancelled()) {
					Logger.d(this, "post data: " + postData);
					Logger.d(this, "post url: " + requestURL);
					HttpPost postJob = new HttpPost(requestURL);
					StringEntity se = new StringEntity(postData, HTTP.UTF_8);
					se.setContentType("application/json");
					postJob.setEntity(se);
					Logger.d(this, "Starting HTTP POST REQUEST");

					postJob.setHeader(CECHttpPostParameterKeys.USERNAME_STRING,
							userName);
					postJob.setHeader(CECHttpPostParameterKeys.PASSWORD_STRING,
							password);

					response = client.execute(postJob);
				}
				break;

			default:
				break;
			}

		} catch (ClientProtocolException e) {
			aborted(e);
			if (!isCancelled()) {
				e.printStackTrace();
				Logger.e(this, "Client Protocol Exception");
			}
			timer.cancel();
			return null;
		} catch (IOException e) {
			aborted(e);
			if (!isCancelled()) {
				e.printStackTrace();
				Logger.e(this, "IOException Exception");
			}
			timer.cancel();
			return null;
		}
		int responseCode = response.getStatusLine().getStatusCode();
		if (!isCancelled()) {
			Logger.d(this, "Response code: " + responseCode);
		}
		Header[] headers = response.getAllHeaders();
		// Logger.i(this,headers.toString());
		if (!isCancelled()) {
			if (headers != null) {
				for (Header header : headers) {
					Logger.d(this, "Header name :" + header.getName() + " : "
							+ header.getValue());
					if (header.getName().equals("Content-Length")) {
						contentLength = Integer.parseInt(header.getValue());
					}
				}
			}
			Logger.d(
					this,
					"Finished execution.Time taken:"
							+ ((System.currentTimeMillis() - startTime)));
		}

		if (responseCode == HTTP_RESPONSE_OK) {
			if (!isCancelled()) {
				try {
					timer.cancel();
					return processEntity(response.getEntity());
				} catch (IllegalStateException e) {
					aborted(e);
					if (!isCancelled()) {
						e.printStackTrace();
						Logger.e(this, "Client Protocol Exception");
					}
					timer.cancel();
					return null;
				} catch (IOException e) {
					aborted(e);
					if (!isCancelled()) {
						e.printStackTrace();
						Logger.e(this, "IOException Exception");
					}
					timer.cancel();
					return null;
				}
			}
		} else {
			if (!isCancelled()) {
				Logger.d(this, "HTTP requestFailed = "
						+ response.getStatusLine().getReasonPhrase());
			}
			requestFailed(response);
		}
		timer.cancel();
		return null;
	}

	@Override
	protected void onCancelled() {
		timer.cancel();
		super.onCancelled();
	}

	private byte[] processEntity(HttpEntity entity)
			throws IllegalStateException, IOException {
		InputStream stream = entity.getContent();
		int size = 0;
		Logger.d(this, "Size of data: " + contentLength);
		sizeRead = 0;
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((size = stream.read(buffer)) != -1 && !isCancelled()) {
			baos.write(buffer, 0, size);
			sizeRead += size;
			int percent = (int) ((sizeRead * 100) / contentLength);
			if (percent != 100) {
				requestStatus(percent);
			}
			buffer = new byte[bufferSize];
		}
		stream.close();
		requestStatus(100);
		if (!isCancelled()) { // && RuntimeCache.isRunning) {
			return baos.toByteArray();
		}
		return null;
	}

	/**
	 * Callback which returns the data retrived from the server in case of a
	 * HTTP 200 response
	 * 
	 * @param result
	 *            Data returned from server , null if there was a problem
	 * 
	 */
	@Override
	abstract protected void onPostExecute(byte[] result);

	/**
	 * Callback which returns the exception object in case HTTP request is
	 * aborted
	 * 
	 * @param e
	 *            Exception due to which HTTP request was aborted
	 */
	abstract public void aborted(Exception e);

	/**
	 * Callback which returns the HTTP response object when HTTP response is not
	 * 200
	 * 
	 * @param response
	 *            Response object from the failed request
	 */
	abstract public void requestFailed(HttpResponse response);

	/**
	 * Callback which returns the status of the given update
	 * 
	 * @param status
	 *            Status of the HTTP request
	 */
	abstract public void requestStatus(int status);

	TimerTask timerTask = new TimerTask() {

		@Override
		public void run() {
			timeoutAsyncTask();

		}
	};

	protected void timeoutAsyncTask() {
		Exception e = new ConnectTimeoutException();
		aborted(e);
		this.cancel(true);

	}

}
