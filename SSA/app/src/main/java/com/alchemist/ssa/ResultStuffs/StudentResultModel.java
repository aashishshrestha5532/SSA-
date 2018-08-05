package com.alchemist.ssa.ResultStuffs;

public class StudentResultModel {
    private String name;
    private int rollNo;
    private int position;
    private double marks;

    public StudentResultModel(String name, int position, int rollNo, double marks) {
        this.name = name;
        this.rollNo = rollNo;
        this.marks=marks;
        this.position=position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getRollNo() {
        return rollNo;
    }



    public int getPosition() {
        return position;
    }

    public double getMarks() {
        return marks;
    }
}
