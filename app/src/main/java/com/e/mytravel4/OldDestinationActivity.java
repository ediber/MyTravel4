package com.e.mytravel4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class OldDestinationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_destination);

        double latitude = getIntent().getDoubleExtra("latitude", -1);
        double longitude = getIntent().getDoubleExtra("longitude", -1);
    }
}
