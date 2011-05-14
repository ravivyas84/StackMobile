package com.ravivyas.stackapps.HTTP.requestobject;

import java.util.HashMap;

/**
 * 
 * @author <A HREF="mailto:ravivyas84@gmail.com">Ravi Vyas </A>
 * 
 */
public class AsyncHTTPRequestObject{

    public final int                REQUEST_TYPE_POST = 1;
    public final int                REQUEST_TYPE_GET  = 2;

    private String                  requestURL;
    private String                  postData;

    private int                     requestType;
    private int                     connectionTimeout;
    private int                     soTimeout;

    private HashMap<String, String> header;
    
    
    public AsyncHTTPRequestObject() {
        requestType = -1;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * Set the connection timeout for this object.
     * 
     * @param connectionTimeout
     *            Connection timeout in milliseconds
     */
    public void setConnectionTimeout( int connectionTimeout ) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    /**
     * Set socket timeout for this object. in milliseconds
     * 
     * @param soTimeout
     *            Socket timeout in milliseconds.
     */
    public void setSoTimeout( int soTimeout ) {
        this.soTimeout = soTimeout;
    }

    /**
     * Set request type of the current object
     * 
     * @param requestType
     *            </br> AsyncHTTPRequestObject.REQUEST_TYPE_POST or
     *            AsyncHTTPRequestObject.REQUEST_TYPE_GET
     * 
     */
    public void setRequestType( int requestType ) {
        this.requestType = requestType;
    }

    /**
     * Set Request URL
     * 
     * @param requestURL
     */
    public void setRequestUrl( String requestURL ) {
        this.requestURL = requestURL;
    }

    /**
     * Method for creating a post request
     * 
     * @param requestURL
     *            URL for the post request
     * @param postData
     *            Data to be sent as a part of the post request
     */
    public void setPostData( String postData ) {
        this.postData = postData;
    }

    /**
     * Add headers for this HTTP request
     * 
     * @param name
     *            Name of the header to be added
     * @param value
     *            Value of the header to be added
     */
    public void addHeaderElement( String name , String value ) {
        header.put(name, value);
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String getPostData() {
        return postData;
    }

    /**
     * Returns request type
     * 
     * @return 1 = REQUEST_TYPE_POST<br/>
     *         2 = REQUEST_TYPE_GET
     */
    public int getRequestType() {
        return requestType;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }
}
