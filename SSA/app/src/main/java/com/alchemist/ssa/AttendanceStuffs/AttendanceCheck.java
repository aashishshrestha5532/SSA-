package com.alchemist.ssa.AttendanceStuffs;

/**
 * Created by Ashish Alton on 8/1/2018.
 */

public class AttendanceCheck {

    private boolean flag;
    private int roll;


    public AttendanceCheck(boolean flag,int roll){
        this.flag=flag;
        this.roll=roll;
    }

    public boolean isFlag() {
        return flag;
    }

    public int getRoll() {
        return roll;
    }
}
