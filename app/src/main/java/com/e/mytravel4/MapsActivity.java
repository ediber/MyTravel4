package com.e.mytravel4;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static com.e.mytravel4.Destination.NEW_DESTINATION;
import static com.e.mytravel4.Destination.OLD_DESTINATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private View addDestination;
    // private boolean mapReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        addDestination = findViewById(R.id.add_destination);// יצירת לחצן של הוסםת יעד

        addDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//פעולה בעת הלחיצה
                new AlertDialog.Builder(MapsActivity.this)// הופעת ההודעה האם לבחור ביעד שטיילתי או מתוכנן
                        .setTitle("add destination")
                        .setMessage("do you want to add a new or an old destination ?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("new", new DialogInterface.OnClickListener() {//לחצן היעד החדש
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MapsActivity.this, NewDestinationActivity.class); //נפתח מסך של יעד חדש
                                intent.putExtra("destination_type", NEW_DESTINATION);
                                startActivity(intent);

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("old", new DialogInterface.OnClickListener() {// לחצן היעד הישן
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MapsActivity.this, NewDestinationActivity.class); //נפתח מסך של יעד חדש
                                intent.putExtra("destination_type", OLD_DESTINATION);
                                startActivity(intent);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

      /*  // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
*/
        showDestinations();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mMap != null) {
            showDestinations();
        }
    }

    private void showDestinations() {
        List<Destination> destinations = DAO.getInstance(getApplicationContext()).getAllDestinations();
        for (Destination destination : destinations) {
            LatLng latLng = destination.getLatLng();

            Drawable drawable = getResources().getDrawable(R.drawable.green_icon);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//            bitmap.setHeight(5);

            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);

            String type = destination.getType();


            MarkerOptions marker;
            if (type.equals(NEW_DESTINATION)) {
                marker = new MarkerOptions().position(latLng).
                        title(destination.getAddress(getApplicationContext()).getLocality() + " " + type)
                        .icon(icon);
            } else {
                marker = new MarkerOptions().position(latLng).
                        title(destination.getAddress(getApplicationContext()).getLocality() + " " + type);
            }

            mMap.addMarker(marker);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    LatLng position = marker.getPosition();
                    //marker.getTag()

                    Intent intent = new Intent(MapsActivity.this, NewDestinationActivity.class); //נפתח מסך של יעד חדש
                    intent.putExtra("latitude", position.latitude);
                    intent.putExtra("longitude", position.longitude);
                    startActivity(intent);
                    return false;
                }
            });

        }
    }
}
