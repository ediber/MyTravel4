package com.e.mytravel4;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

public class Destination {
    private String id;
    private LatLng position;
    private List<Day> days;
    private String type; // previous or current destination
    private Date date;

    public Destination(String id, LatLng position, List<Day> days, String type, Date date) {
        this.id = id;
        this.position = position;
        this.days = days;
        this.type = type;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
