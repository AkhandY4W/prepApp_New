package com.youth4work.prepapp.network.model.request;

import com.google.gson.annotations.SerializedName;

public class PushAnswer {
    @SerializedName("AnswerID")
    private int AnswerID;
    @SerializedName("result")
    private int result;
    @SerializedName("questionid")
     private int questionid;
    @SerializedName("answerby")
    private Long answerby;
    @SerializedName("defaulttime")
    private int defaulttime;
    @SerializedName("timetaken")
    private int timetaken;

    public PushAnswer(int  selectedAnswerId, int mWinOrLose, int questionId, Long userId, int time2solve, int mTimeTaken) {
        this.AnswerID = selectedAnswerId;
        this.result = mWinOrLose;
        this.questionid = questionId;
        this.answerby = userId;
        this.defaulttime = time2solve;
        this.timetaken = mTimeTaken;
    }

    public int getAnswerID() {
        return AnswerID;
    }

    public void setAnswerID(int answerID) {
        AnswerID = answerID;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    public Long getAnswerby() {
        return answerby;
    }

    public void setAnswerby(Long answerby) {
        this.answerby = answerby;
    }

    public int getDefaulttime() {
        return defaulttime;
    }

    public void setDefaulttime(int defaulttime) {
        this.defaulttime = defaulttime;
    }

    public int getTimetaken() {
        return timetaken;
    }

    public void setTimetaken(int timetaken) {
        this.timetaken = timetaken;
    }
}
