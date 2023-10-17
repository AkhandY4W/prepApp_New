package com.youth4work.prepapp.infrastructure;


import androidx.annotation.NonNull;

public class QuestionManager {
    @NonNull
    private static QuestionManager ourInstance = new QuestionManager();

    private QuestionManager() {
    }

    @NonNull
    public static QuestionManager getInstance() {
        return ourInstance;
    }

}
