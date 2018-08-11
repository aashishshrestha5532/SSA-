package com.alchemist.ssa.ResultStuffs;

/**
 * Created by Ashish Alton on 8/7/2018.
 */

public class StudentMarkModel {

    private String sub_id,subject;
    private int marks,status;

    public StudentMarkModel(String subject,int marks,int status){
        this.marks=marks;
        this.status=status;
        this.subject=subject;
    }

    public int getMarks() {
        return marks;
    }

    public int getStatus() {
        return status;
    }

    public String getSub_id() {
        return sub_id;
    }

    public String getSubject() {
        return subject;
    }
}
