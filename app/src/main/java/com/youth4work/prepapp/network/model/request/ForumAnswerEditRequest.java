package com.youth4work.prepapp.network.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Samar on 5/17/2016.
 */
public class ForumAnswerEditRequest {
    /*
    {
     "AnswerId": 649,
     "Answer": "bachelor degree",
     "AnswerByUserId":7
    }
     */
    public ForumAnswerEditRequest(int AnswerId,String Answer, Long AnswerByUserId) {
        this.AnswerId = AnswerId;
        this.Answer = Answer;
        this.AnswerByUserId=AnswerByUserId;
    }
    @SerializedName("AnswerId")
    private int AnswerId;
    @SerializedName("Answer")
    private String Answer;
    @SerializedName("AnswerByUserId")
    private Long AnswerByUserId;

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public int getAnswerId() {
        return AnswerId;
    }

    public void setAnswerId(int answerId) {
        AnswerId = answerId;
    }

    public Long getAnswerByUserId() {
        return AnswerByUserId;
    }

    public void setAnswerByUserId(Long answerByUserId) {
        AnswerByUserId = answerByUserId;
    }


}
