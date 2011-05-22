package com.ravivyas.mStackEx.ui;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ravivyas.mStackEx.R;
import com.ravivyas.mStackEx.HTTP.processor.AsyncHTTPProcessor;
import com.ravivyas.mStackEx.HTTP.requestobject.AsyncHTTPRequestObject;
import com.ravivyas.mStackEx.configration.LoggingTag;
import com.ravivyas.mStackEx.configration.Values;
import com.ravivyas.mStackEx.objects.StatusObject;
import com.ravivyas.mStackEx.parsers.StatusParser;

public class LandingActivity extends Activity {

    ProgressBar statusProgress;
    /** Called when the activity is first created. */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        AsyncHTTPRequestObject statusRequest = new AsyncHTTPRequestObject();
        statusRequest.setRequestType(Values.REQUEST_TYPE_GET);
        statusRequest.setRequestUrl(getString(R.string.apiBaseUrl) + getString(R.string.apiStats));
        statusProgress= (ProgressBar) findViewById(R.id.statusProgress);
        
        AsyncHTTPProcessor processor = new AsyncHTTPProcessor(statusRequest) {

            @Override
            public void requestStatus( int status ) {
                Log.d(LoggingTag.TAG, "Status : " + status);

            }

            @Override
            public void requestFailed( HttpResponse response ) {
                // TODO Auto-generated method stub

            }

            @Override
            protected void onPostExecute( byte[] result ) {
                // TODO Auto-generated method stub
                try {
                    String data = new String(result, "UTF-8");
                    Log.d(LoggingTag.TAG, "Resut : " + data);
                    StatusParser parser = new StatusParser();
                    StatusObject status = parser.parse(data);
                    updateUI(status);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void aborted( Exception e ) {
                // TODO Auto-generated method stub

            }
        };
        processor.execute();

    }

    private void updateUI( StatusObject status ) {
        // TODO Auto-generated method stub
        TextView questionsTextView = (TextView) findViewById(R.id.questionsValue);
        TextView answersTextView = (TextView) findViewById(R.id.answersValue);
        TextView usersTextView = (TextView) findViewById(R.id.usersValue);
        TextView qpmTextView = (TextView) findViewById(R.id.qpmValue);
        TextView apmTextView = (TextView) findViewById(R.id.apmValue);

        questionsTextView.setText(status.getTotal_questions());
        answersTextView.setText(status.getTotal_answers());
        usersTextView.setText(status.getTotal_users());
        qpmTextView.setText(status.getQuestions_per_minute());
        apmTextView.setText(status.getAnswers_per_minute());
        statusProgress.setVisibility(View.GONE);
        findViewById(R.id.relativeLayout1).setVisibility(View.VISIBLE);

    }

    // @Override
    // public boolean onCreateOptionsMenu( Menu menu ) {
    // MenuInflater inflater = getMenuInflater();
    // inflater.inflate(R.menu.menu_landing, menu);
    // return true;
    // }
    //
    // @Override
    // public boolean onOptionsItemSelected( MenuItem item ) {
    // // Handle item selection
    // switch ( item.getItemId() ){
    // default :
    // return super.onOptionsItemSelected(item);
    // }
    // }

}