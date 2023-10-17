package com.youth4work.prepapp.network.model;

import java.util.List;

public class AutoCompleteData {
    private List<String> colleges;
    private List<String> degrees;
    private List<String> specializations;
    private List<String> cities;

    public AutoCompleteData(List<String> colleges, List<String> degrees, List<String> specializations, List<String> cities) {

        this.colleges = colleges;
        this.degrees = degrees;
        this.specializations = specializations;
        this.cities = cities;
    }

    public List<String> getColleges() {
        return colleges;
    }

    public void setColleges(List<String> colleges) {
        this.colleges = colleges;
    }

    public List<String> getDegrees() {
        return degrees;
    }

    public void setDegrees(List<String> degrees) {
        this.degrees = degrees;
    }

    public List<String> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<String> specializations) {
        this.specializations = specializations;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }
}
