package com.alchemist.ssa.AttendanceStuffs;



public class AttendanceGridModel {

    private String name,rollNo;
    private int studentStatus=-1;
    private int totalAttendance,presentAttendance;
    private String remarks,performance;

    public AttendanceGridModel(String name, String rollNo,int totalAttendance,int presentAttendance,String remarks,String performance) {
        this.name = name;
        this.rollNo = rollNo;
        this.totalAttendance=totalAttendance;
        this.presentAttendance=presentAttendance;
        this.remarks=remarks;
        this.performance=performance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public int getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(int studentStatus) {
        this.studentStatus = studentStatus;
    }

    public int getPresentAttendance() {
        return presentAttendance;
    }

    public void setPresentAttendance(int presentAttendance) {
        this.presentAttendance = presentAttendance;
    }

    public int getTotalAttendance() {
        return totalAttendance;
    }

    public void setTotalAttendance(int totalAttendance) {
        this.totalAttendance = totalAttendance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public String getPerformance() {
        return performance;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }
}
