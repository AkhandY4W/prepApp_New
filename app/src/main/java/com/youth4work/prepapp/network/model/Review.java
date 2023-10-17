package com.youth4work.prepapp.network.model;

import java.util.List;

public class Review {
    private TestResult testResult;
    private UserStats userStats;
    private List<Attempt> attempts;
    public Review(TestResult testResult, UserStats userStats) {
        this.testResult = testResult;
        this.userStats = userStats;
    }
    public Review(List<Attempt> attempts, UserStats userStats) {
        this.attempts = attempts;
        this.userStats = userStats;
    }
    public Review(List<Attempt> attempts) {
        this.attempts = attempts;
    }
    public List<Attempt> getAttempts()
    {
        return attempts;
    }
    public void setAttempts(List<Attempt> attempts)
    {
        this.attempts= attempts;
    }
    public UserStats getUserStats() {
        return userStats;
    }

    public void setUserStats(UserStats userStats) {
        this.userStats = userStats;
    }

    public TestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(TestResult testResult) {
        this.testResult = testResult;
    }
}
