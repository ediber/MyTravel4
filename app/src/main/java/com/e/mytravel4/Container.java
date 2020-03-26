package com.e.mytravel4;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

public class Container {

    private final Realm realm;
    private RealmResults<Destination> destinations;

    public Container(Context context) {


        // Initialize Realm (just once per application)
        Realm.init(context);

// Get a Realm instance for this thread
        //   realm = Realm.getDefaultInstance();


        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        realm = Realm.getInstance(config);

        destinations = realm.where(Destination.class).findAll();
    }


    public void addOrUpdateDestination(Destination destination, List<Day> days) {
      /*  realm.beginTransaction();
        RealmList<Day> realmDays = new RealmList<>();
        for (Day day: days) {
            Day realmDay = realm.createObject(Day.class);
            realmDay.setDetails1(day.getDetails1());
            realmDay.setDetails2(day.getDetails2());
            realmDays.add(realmDay);
        }
        realm.commitTransaction();*/

        realm.beginTransaction();
        destination.replaceDays(days);
        realm.copyToRealm(destination);
        realm.commitTransaction();

        realm.beginTransaction();
        destinations = realm.where(Destination.class).findAll();
        realm.commitTransaction();
    }

    public ArrayList<Destination> getAllDestinations() {
        ArrayList<Destination> localDestinations = new ArrayList<>();
        for (Destination destination: destinations){
            localDestinations.add(destination);
        }
        return  localDestinations;
    }

    public Destination getDestination(double latitude, double longitude) {
        for (Destination destination: destinations){
            if(destination.getLatLng().latitude == latitude &&
               destination.getLatLng().longitude == longitude){
                return destination;
            }
        }

        return null;
    }
}
