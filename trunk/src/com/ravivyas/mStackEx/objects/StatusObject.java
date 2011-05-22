package com.ravivyas.mStackEx.objects;

/**
 * TODO Still incomplete
 * 
 * @author Ravi
 * 
 */
public class StatusObject {

    String total_questions;
    String total_answers;
    String total_users;
    String questions_per_minute;
    String answers_per_minute;

    public String getTotal_questions() {
        return total_questions;
    }

    public void setTotal_questions( String total_questions ) {
        this.total_questions = total_questions;
    }

    public String getTotal_answers() {
        return total_answers;
    }

    public void setTotal_answers( String total_answers ) {
        this.total_answers = total_answers;
    }

    public String getTotal_users() {
        return total_users;
    }

    public void setTotal_users( String total_users ) {
        this.total_users = total_users;
    }

    public String getQuestions_per_minute() {
        return questions_per_minute;
    }

    public void setQuestions_per_minute( String questions_per_minute ) {
        this.questions_per_minute = questions_per_minute;
    }

    public String getAnswers_per_minute() {
        return answers_per_minute;
    }

    public void setAnswers_per_minute( String answers_per_minute ) {
        this.answers_per_minute = answers_per_minute;
    }
}
