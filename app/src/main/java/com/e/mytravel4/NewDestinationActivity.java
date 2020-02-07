package com.e.mytravel4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class NewDestinationActivity extends FragmentActivity implements OnMapReadyCallback {

    private View searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_destination);

        searchView = findViewById(R.id.search);

        findViewById(R.id.new_dest_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //        search();
            }
        });

    }

/*    private void search() {
     *//*   // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        // Create a RectangularBounds object.
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596));*//*
        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
            //    .setLocationBias(bounds)
                //.setLocationRestriction(bounds)
                .setOrigin(new LatLng(-33.8749937,151.2041382))
                .setCountries("AU", "NZ")
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                Log.i(TAG, prediction.getPlaceId());
                Log.i(TAG, prediction.getPrimaryText(null).toString());
            }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
            }
        });
    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


}
