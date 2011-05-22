package com.ravivyas.mStackEx.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ravivyas.mStackEx.configration.LoggingTag;
import com.ravivyas.mStackEx.objects.StatusObject;

public class StatusParser {

    public StatusObject parse( String data ) {

        StatusObject status = new StatusObject();
        try {
            JSONObject json = new JSONObject(data);
            JSONArray statistics = (JSONArray) json.get("statistics");
            json = statistics.getJSONObject(0);
            Log.d(LoggingTag.TAG, "Questions : " + json.getString("total_questions"));
            status.setTotal_questions(json.getString("total_questions"));
            status.setTotal_answers(json.getString("total_answers"));
            status.setTotal_users(json.getString("total_users"));
            status.setQuestions_per_minute(json.getString("questions_per_minute"));
            status.setAnswers_per_minute(json.getString("answers_per_minute"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return status;
    }
}
