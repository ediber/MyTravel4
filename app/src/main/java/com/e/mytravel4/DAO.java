package com.e.mytravel4;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class DAO {
    // static variable single_instance of type Singleton
    private static DAO single_instance = null;

    private  Context context;
    private Container container;

    // private constructor restricted to this class itself
    private DAO(Context context) {
        container = new Container(context);
        this.context = context;
    }


    // static method to create instance of Singleton class
    public static DAO getInstance(Context context)
    {
        if (single_instance == null)
            single_instance = new DAO(context);

        return single_instance;
    }

    public void addOrUpdateDestination(Destination destination, List<Day> days) {
        container.addOrUpdateDestination(destination, days);
    }

    public ArrayList<Destination> getAllDestinations() {
        return container.getAllDestinations();
    }

    public Destination getDestination(double latitude, double longitude) {
        return container.getDestination(latitude, longitude);
    }
}
