package com.alchemist.ssa.ScheduleStuffs;

import java.util.ArrayList;

public class ScheduleGridModel {
    private String day, periodName;
    private int time;


    public ScheduleGridModel(String day, String periodName, int time) {
        this.day = day;
        this.periodName = periodName;
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getPeroidName() {
        return periodName;
    }

    public void setPeroidName(String peroidName) {
        this.periodName = peroidName;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
