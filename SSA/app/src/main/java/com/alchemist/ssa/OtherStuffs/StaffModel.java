package com.alchemist.ssa.OtherStuffs;

/**
 * Created by Ashish Alton on 8/6/2018.
 */

public class StaffModel {

    private String teacherName,level;
    private long phone;


    public StaffModel(String teacherName,long phone,String level){
        this.teacherName=teacherName;
        this.phone=phone;
        this.level=level;
    }

    public long getPhone() {
        return phone;
    }

    public String getLevel() {
        return level;
    }

    public String getTeacherName() {
        return teacherName;
    }
}
