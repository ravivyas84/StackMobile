package com.ravivyas.stackapps.HTTP.processor;

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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.ravivyas.stackapps.HTTP.requestobject.AsyncHTTPRequestObject;
import com.ravivyas.stackapps.configration.LoggingTag;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Async task based HTTP request object . To make a http request , create a new
 * AsyncHttpRequest object , define the abstract methods and then call
 * createPostRequest or createGetRequest
 * 
 * @author Ravi
 * 
 */
public abstract class AsyncHTTPProcessor extends AsyncTask<Void, Integer, byte[]> {

    private HttpResponse           response;
    private int                    sizeRead;
    private int                    contentLength;
    private static final int       HTTP_RESPONSE_OK = 200;

    private AsyncHTTPRequestObject request;
    
    private HttpClient client;
    private HttpPost postJob;
    private HttpGet getJob;

    public AsyncHTTPProcessor() {
        contentLength = -1;
    }

    AsyncHTTPProcessor( AsyncHTTPRequestObject request ) {
        this.request = request;
    }

    @Override
    protected final byte[] doInBackground( Void... params ) {
        if ( isCancelled() ) {
            return null;
        }
        Log.d(LoggingTag.TAG, "Async HTTP request started");
        if ( request.getRequestType() == -1 ) {
            return null;
        }
        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        client = new DefaultHttpClient(httpParameters);
        long startTime = System.currentTimeMillis();
        try {
            switch ( request.getRequestType() ){
                /*
                 * Not needed for CEC project , no get requests
                 */
                /*
                 * case 0: HttpGet getJob = new HttpGet(requestURL);
                 * Log.d(LoggingTag.TAG, "get url: " + requestURL); response =
                 * client.execute(getJob); break;
                 */
                case 1 :
                    if ( !isCancelled() ) {
                        Log.d(LoggingTag.TAG, "post data: " + request.getPostData());
                        Log.d(LoggingTag.TAG, "post url: " + request.getRequestURL());
                        postJob = new HttpPost(request.getRequestURL());
                        StringEntity se = new StringEntity(request.getPostData(), HTTP.UTF_8);
                        se.setContentType("application/json");
                        postJob.setEntity(se);
                        Log.d(LoggingTag.TAG, "Starting HTTP POST REQUEST");
                        response = client.execute(postJob);
                    }
                    break;
                case 2 :
                    if ( !isCancelled() ) {
                        Log.d(LoggingTag.TAG, "get url: " + request.getRequestURL());
                        getJob = new HttpGet(request.getRequestURL());
                        Log.d(LoggingTag.TAG, "Starting HTTP GET REQUEST");
                        response = client.execute(getJob);
                    }
                    break;
                default :
                    break;
            }
        } catch (ClientProtocolException e) {
            aborted(e);
            if ( !isCancelled() ) {
                e.printStackTrace();
                Log.d(LoggingTag.TAG, "Client Protocol Exception");
            }

            return null;
        } catch (IOException e) {
            aborted(e);
            if ( !isCancelled() ) {
                e.printStackTrace();
                Log.d(LoggingTag.TAG, "IOException Exception");
            }

            return null;
        }
        int responseCode = response.getStatusLine().getStatusCode();
        if ( !isCancelled() ) {
            Log.d(LoggingTag.TAG, "Response code: " + responseCode);
        }
        Header[] headers = response.getAllHeaders();
        if ( !isCancelled() ) {
            if ( headers != null ) {
                for ( Header header : headers ) {
                    Log.d(LoggingTag.TAG, "Header name :" + header.getName() + " : " + header.getValue());
                    if ( header.getName().equals("Content-Length") ) {
                        contentLength = Integer.parseInt(header.getValue());
                    }
                }
            }
            Log.d(LoggingTag.TAG, "Finished execution.Time taken:" + ((System.currentTimeMillis() - startTime)));
        }
        if ( responseCode == HTTP_RESPONSE_OK ) {
            if ( !isCancelled() ) {
                try {

                    return processEntity(response.getEntity());
                } catch (IllegalStateException e) {
                    aborted(e);
                    if ( !isCancelled() ) {
                        e.printStackTrace();
                        Log.d(LoggingTag.TAG, "Client Protocol Exception");
                    }

                    return null;
                } catch (IOException e) {
                    aborted(e);
                    if ( !isCancelled() ) {
                        e.printStackTrace();
                        Log.d(LoggingTag.TAG, "IOException Exception");
                    }

                    return null;
                }
            }
        } else {
            if ( !isCancelled() ) {
                Log.d(LoggingTag.TAG, "HTTP requestFailed = " + response.getStatusLine().getReasonPhrase());
            }
            requestFailed(response);
        }

        return null;
    }

    @Override
    protected void onCancelled() {

        super.onCancelled();
    }

    private byte[] processEntity( HttpEntity entity ) throws IllegalStateException , IOException {
        InputStream stream = entity.getContent();
        int size = 0;
        Log.d(LoggingTag.TAG, "Size of data: " + contentLength);
        sizeRead = 0;
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((size = stream.read(buffer)) != -1 && !isCancelled()) {
            baos.write(buffer, 0, size);
            sizeRead += size;
            int percent = (int) ((sizeRead * 100) / contentLength);
            if ( percent != 100 ) {
                requestStatus(percent);
            }
            buffer = new byte[bufferSize];
        }
        stream.close();
        requestStatus(100);
        if ( !isCancelled() ) { // && RuntimeCache.isRunning) {
            return baos.toByteArray();
        }
        return null;
    }

    /**
     * Abort current http request
     */
    public void abort() {
       if(postJob!= null){
           postJob.abort();
           Log.d(LoggingTag.TAG, "Post request aborted");
       }else{
           getJob.abort();
           Log.d(LoggingTag.TAG, "Get request aborted");
       }
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
    abstract protected void onPostExecute( byte[] result );

    /**
     * Callback which returns the exception object in case HTTP request is
     * aborted
     * 
     * @param e
     *            Exception due to which HTTP request was aborted
     */
    abstract public void aborted( Exception e );

    /**
     * Callback which returns the HTTP response object when HTTP response is not
     * 200
     * 
     * @param response
     *            Response object from the failed request
     */
    abstract public void requestFailed( HttpResponse response );

    /**
     * Callback which returns the status of the given update
     * 
     * @param status
     *            Status of the HTTP request
     */
    abstract public void requestStatus( int status );

}
