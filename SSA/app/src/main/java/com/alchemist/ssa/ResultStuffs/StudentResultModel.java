package com.alchemist.ssa.ResultStuffs;

public class StudentResultModel {
    private String name;
    private int rollNo;
    private int position,id;
    private String marks;

    public StudentResultModel(int id,String name, int position, int rollNo, String marks) {
        this.name = name;
        this.rollNo = rollNo;
        this.marks=marks;
        this.position=position;
        this.id=id;
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

    public String getMarks() {
        return marks;
    }

    public int getId() {
        return id;
    }
}
