package com.youth4work.prepapp.network.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Samar on 5/18/2016.
 */
public class VoteResponse {

    @SerializedName("voteForumAnswerResult")
    private boolean VoteForumAnswerResult;

    public boolean isVoteForumAnswerResult() {
        return VoteForumAnswerResult;
    }

    public void setVoteForumAnswerResult(boolean voteForumAnswerResult) {
        VoteForumAnswerResult = voteForumAnswerResult;
    }
}
