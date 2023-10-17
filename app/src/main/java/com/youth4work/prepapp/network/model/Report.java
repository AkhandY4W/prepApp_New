package com.youth4work.prepapp.network.model;

import java.util.List;

public class Report {
    private List<SubjectStats> subjectStatses;
    private UserStats userStats;

    public Report(List<SubjectStats> subjectStatses, UserStats userStats) {

        this.subjectStatses = subjectStatses;
        this.userStats = userStats;
    }

    public List<SubjectStats> getSubjectStatses() {
        return subjectStatses;
    }

    public void setSubjectStatses(List<SubjectStats> subjectStatses) {
        this.subjectStatses = subjectStatses;
    }

    public UserStats getUserStats() {
        return userStats;
    }

    public void setUserStats(UserStats userStats) {
        this.userStats = userStats;
    }
}
