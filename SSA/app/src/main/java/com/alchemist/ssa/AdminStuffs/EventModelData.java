package com.alchemist.ssa.AdminStuffs;

import android.graphics.Bitmap;

/**
 * Created by Ashish Alton on 8/11/2018.
 */

public class EventModelData {
    private int event_id;
    private String eventName,eventType,eventDesc,eventDate,eventTime;
    private Bitmap event_image;


    public EventModelData(int event_id,String eventName,String eventType,String eventDesc,String eventDate,String eventTime,Bitmap event_image){
        this.event_id=event_id;
        this.eventName=eventName;
        this.eventDesc=eventDesc;
        this.eventDate=eventDate;
        this.eventTime=eventTime;
        this.event_image=event_image;
        this.eventType=eventType;
    }

    public int getEvent_id() {
        return event_id;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getEventType() {
        return eventType;
    }
    public Bitmap getEvent_image() {
        return event_image;
    }
}
