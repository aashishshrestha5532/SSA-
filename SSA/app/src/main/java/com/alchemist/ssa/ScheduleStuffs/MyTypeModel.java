package com.alchemist.ssa.ScheduleStuffs;

import java.util.List;

public class MyTypeModel  {

    private List<ScheduleGridModel2> lists;
    private String name;


    public MyTypeModel(List<ScheduleGridModel2> lists,String name){
        this.lists=lists;
        this.name=name;
    }

    public List<ScheduleGridModel2> getLists() {
        return lists;
    }

    public String getName() {
        return name;
    }
}
