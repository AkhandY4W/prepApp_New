package com.youth4work.prepapp.network.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Question {
    @SerializedName("correctOptionID")
    protected String CorrectOptionID;
    @SerializedName("difficulty")
    private String Difficulty;
    @SerializedName("difficultyID")
    private int DifficultyID;
    @SerializedName("question")
    private String Question;
    @SerializedName("questionImgUrl")
    private String QuestionImgUrl;
    @SerializedName("ratingActivity")
    private int Rating;
    @SerializedName("contributer")
    private String contributer;
    @SerializedName("contributerimageurl")
    private String contributerimageurl;
    @SerializedName("id")
    private int id;
    @SerializedName("testID")
    private int testID;
    @SerializedName("time2solve")
    private int time2solve;

    @SerializedName("options")
    private List<Options> Options;

    public String getCorrectOptionID() {
        return CorrectOptionID;
    }

    public void setCorrectOptionID(String CorrectOptionID) {
        this.CorrectOptionID = CorrectOptionID;
    }

    public String getDifficulty() {
        return Difficulty;
    }

    public void setDifficulty(String Difficulty) {
        this.Difficulty = Difficulty;
    }

    public int getDifficultyID() {
        return DifficultyID;
    }

    public void setDifficultyID(int DifficultyID) {
        this.DifficultyID = DifficultyID;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String Question) {
        this.Question = Question;
    }

    public String getQuestionImgUrl() {
        return QuestionImgUrl;
    }

    public void setQuestionImgUrl(String QuestionImgUrl) {
        this.QuestionImgUrl = QuestionImgUrl;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int Rating) {
        this.Rating = Rating;
    }

    public String getContributer() {
        return contributer;
    }

    public void setContributer(String contributer) {
        this.contributer = contributer;
    }

    public String getContributerimageurl() {
        return contributerimageurl;
    }

    public void setContributerimageurl(String contributerimageurl) {
        this.contributerimageurl = contributerimageurl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTestID() {
        return testID;
    }

    public void setTestID(int testID) {
        this.testID = testID;
    }

    public int getTime2solve() {
        return time2solve;
    }

    public void setTime2solve(int time2solve) {
        this.time2solve = time2solve;
    }

    public List<Options> getOptions() {
        return Options;
    }

    public void setOptions(List<Options> Options) {
        this.Options = Options;
        for (Question.Options option : this.Options) {
            if (option.getOptionID().equalsIgnoreCase(CorrectOptionID)) {
                option.setAnswer(true);
            }
        }
    }

    public boolean isCorrectOrWrong() {
        boolean correctOrWrong = false;
        for (int i = 0; i < this.getOptions().size(); i++) {
            if (this.getOptions().get(i).isSelected() && this.getOptions().get(i).isAnswer()) {
                correctOrWrong = true;
            }
        }
        return correctOrWrong;
    }

    @Nullable
    public String getSelectedAnswerId() {
        for (int i = 0; i < this.getOptions().size(); i++) {
            if (this.getOptions().get(i).isSelected()) {
                return this.getOptions().get(i).getOptionID();
            }
        }
        return null;
    }

    public void setOptionSelected(int position) {
        for (int i = 0; i < this.getOptions().size(); i++) {
            this.getOptions().get(i).setSelected(false);
        }

        this.getOptions().get(position).setSelected(true);
    }

    public static class Options {
        @SerializedName("option")
        private String Option;
        @SerializedName("optionID")
        private String OptionID;
        @SerializedName("optionImgUrl")
        private String OptionImgUrl;
        private boolean answer = false;
        private boolean selected = false;

        public String getOption() {
            return Option;
        }

        public void setOption(String Option) {
            this.Option = Option;
        }

        public String getOptionID() {
            return OptionID;
        }

        public void setOptionID(String OptionID) {
            this.OptionID = OptionID;
        }

        public String getOptionImgUrl() {
            return OptionImgUrl;
        }

        public void setOptionImgUrl(String OptionImgUrl) {
            this.OptionImgUrl = OptionImgUrl;
        }

        public boolean isAnswer() {
            return answer;
        }

        public void setAnswer(boolean answer) {
            this.answer = answer;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public boolean isSelectedWrong() {
            return false;
        }
    }
}
