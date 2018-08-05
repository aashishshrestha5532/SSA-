package com.alchemist.ssa.ScheduleStuffs;

public class ScheduleGridModel2 {
    private String subject,teacherName;
    private String className;
    private String time;
    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public ScheduleGridModel2(String className, String subject, String time,String teacherName){
        this.className = className;
        this.subject = subject;
        this.time = time;
        this.teacherName=teacherName;



    }
}
