package com.e.mytravel4;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    private Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Realm (just once per application)
        Realm.init(getApplicationContext());

// Get a Realm instance for this thread
        //   realm = Realm.getDefaultInstance();


        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        realm = Realm.getInstance(config);
    }

    public Realm getRealm() {
        return realm;
    }
}
