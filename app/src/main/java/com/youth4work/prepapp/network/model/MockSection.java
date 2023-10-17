package com.youth4work.prepapp.network.model;

public class MockSection implements Comparable<MockSection> {
    String SectionName;
    int SectionId;
    int serialNo;

    public MockSection(String sectionName, int sectionId, int serialNo){
        SectionName=sectionName;
        SectionId=sectionId;
        this.serialNo=serialNo;
    }
    public String getSectionName() {
        return SectionName;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public void setSectionName(String sectionName) {
        SectionName = sectionName;
    }

    public int getSectionId() {
        return SectionId;
    }

    public void setSectionId(int sectionId) {
        SectionId = sectionId;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MockSection) {
            return ((MockSection) obj).SectionId == SectionId;
        }
        return false;
    }
    @Override
    public int hashCode() {
        return this.SectionId;
    }


    @Override
    public int compareTo(MockSection u) {
        if (getSerialNo() == 0 || u.getSerialNo() == 0) {
            return 0;
        }
        return getSerialNo()-u.getSerialNo();
    }
}
