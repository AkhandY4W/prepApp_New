package com.youth4work.prepapp.network.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Samar on 5/17/2016.
 */
public class ForumAnswerRequest {

    /*
    {
      "ForumId": 497,
      "Answer": "any bachelor degree",
      "AnswerByUserId":72
    }
     */

    public ForumAnswerRequest(int ForumId,String Answer, Long AnswerByUserId) {
        this.ForumId = ForumId;
        this.Answer = Answer;
        this.AnswerByUserId=AnswerByUserId;
    }
    @SerializedName("ForumId")
    private int ForumId;
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

    public int getForumId() {
        return ForumId;
    }

    public void setForumId(int forumId) {
        ForumId = forumId;
    }

    public Long getAnswerByUserId() {
        return AnswerByUserId;
    }

    public void setAnswerByUserId(Long answerByUserId) {
        AnswerByUserId = answerByUserId;
    }


}
