package com.alchemist.ssa.EventStuffs;

import android.graphics.Bitmap;

/**
 * Created by Ashish Alton on 7/5/2018.
 */

public class EventModel {

    private String event_title,event_description,event_date;
    private Bitmap event_image;


    public EventModel(String event_title,String event_date,String event_description,Bitmap event_image){
        this.event_title=event_title;
        this.event_description=event_description;
        this.event_date=event_date;
        this.event_image=event_image;
    }

    public Bitmap getEvent_image() {
        return event_image;
    }

    public String getEvent_date() {
        return event_date;
    }

    public String getEvent_description() {
        return event_description;
    }

    public String getEvent_title() {
        return event_title;
    }
}
