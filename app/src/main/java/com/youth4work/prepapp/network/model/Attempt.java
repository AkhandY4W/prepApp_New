package com.youth4work.prepapp.network.model;

import com.google.gson.annotations.SerializedName;

public class Attempt {

    @SerializedName("attemptedDate")
    private String attemptedDate;
    @SerializedName("correctAnswer")
    private String correctAnswer;
    @SerializedName("optionChoosen")
    private int optionChoosen;
    @SerializedName("question")
    private String question;
    @SerializedName("questionid")
    private int questionid;
    @SerializedName("userAnswer")
    private String userAnswer;
    @SerializedName("winOrLost")
    private boolean winOrLost;
    @SerializedName("questionImageUrl")
    private String qusImageUrl;
    @SerializedName("correctAnswerImageUrl")
    private String ansImageUrl;
    @SerializedName("userAnswerImageUrl")
    private String userselectedImageUrl;

    public String getQusImageUrl() {
        return qusImageUrl;
    }

    public void setQusImageUrl(String qusImageUrl) {
        this.qusImageUrl = qusImageUrl;
    }

    public String getAnsImageUrl() {
        return ansImageUrl;
    }

    public void setAnsImageUrl(String ansImageUrl) {
        this.ansImageUrl = ansImageUrl;
    }

    public String getUserselectedImageUrl() {
        return userselectedImageUrl;
    }

    public void setUserselectedImageUrl(String userselectedImageUrl) {
        this.userselectedImageUrl = userselectedImageUrl;
    }

    public String getAttemptedDate() {
        return attemptedDate;
    }

    public void setAttemptedDate(String attemptedDate) {
        this.attemptedDate = attemptedDate;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getOptionChoosen() {
        return optionChoosen;
    }

    public void setOptionChoosen(int optionChoosen) {
        this.optionChoosen = optionChoosen;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public boolean isWinOrLost() {
        return winOrLost;
    }

    public void setWinOrLost(boolean winOrLost) {
        this.winOrLost = winOrLost;
    }
}
