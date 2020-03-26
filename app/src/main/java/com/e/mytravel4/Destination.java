package com.e.mytravel4;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Destination extends RealmObject {

    public static String NEW_DESTINATION = "new destination";
    public static String OLD_DESTINATION = "old destination";

   // @PrimaryKey
    private long id;
   // private Address address;
    private RealmList<Day> days;
    private String type; // previous or current destination
    private Date date;
    private double latitude;
    private double longitude;

    public Destination() {

    }

    public Destination(Address address, String type, Date date) {

        id = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);

    //    this.address = address;
        latitude = address.getLatitude();
        longitude = address.getLongitude();

        this.days = new RealmList<Day>();
        this.type = type;
        this.date = date;
    }



    public long getId() {
        return id;
    }


    public LatLng getLatLng() {
        LatLng latLng = new LatLng(latitude, longitude);
        return latLng;
    }


    public List<Day> getDays() {
        return days;
    }

 /*   public void setDays(List<Day> days) {
        this.days = days;
    }*/

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

    public void addDay(Day day) {
        days.add(day);
    }



    public Address getAddress(Context context){
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {

        }

        return addresses.get(0);
    }

    public void replaceDays(List<Day> days) {
        this.days = (RealmList<Day>) days;
    }

    public void setAddress(Address address) {
        latitude = address.getLatitude();
        longitude = address.getLongitude();
    }
}
