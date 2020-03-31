package com.e.mytravel4;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmList;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.e.mytravel4.Destination.NEW_DESTINATION;
import static com.e.mytravel4.Destination.OLD_DESTINATION;

public class NewDestinationActivity extends FragmentActivity {


    private static final int RESULT_LOAD_IMG = 1;
    private EditText search_edit;
    private RecyclerView recycler;
    private TextView search_text;
    private TextView date_text;
    private View add_day;
    private View next_day;
    private View prev_day;
    private View save_button;
    private View cancel_button;
    private Date date;
    private Address address;
    private Destination destination;
    private TextView day_text;
    private EditText planing;
    private EditText details;

    private boolean isFromMap;
    private String destinationType;

    private int dayIndex;
    private List<Day> days;
    private Realm realm;
    private View search_button;
    private View date_button;
    private TextView title_text;
    private ImageView image;
    private View image_button;
    private Bitmap bitmap;
    private View parent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_destination);

        search_edit = findViewById(R.id.new_dest_search_edit);
        search_text = findViewById(R.id.new_dest_search_text);
        date_text = findViewById(R.id.new_dest_date_text);
        search_button = findViewById(R.id.new_dest_search_button);
        date_button = findViewById(R.id.new_dest_date_button);

        add_day = findViewById(R.id.new_dest_add_day);
        next_day = findViewById(R.id.new_dest_next_day);
        prev_day = findViewById(R.id.new_dest_prev_day);
        save_button = findViewById(R.id.new_dest_save);
        cancel_button = findViewById(R.id.new_dest_cancel);

        day_text = findViewById(R.id.new_dest_day);
        planing = findViewById(R.id.new_dest_planing);
        details = findViewById(R.id.new_dest_details);
        title_text = findViewById(R.id.new_dest_title);

        image = findViewById(R.id.new_dest_image);
        image_button = findViewById(R.id.new_dest_image_button);

        parent = findViewById(R.id.new_dest_parent);

        double latitude = getIntent().getDoubleExtra("latitude", -1);
        double longitude = getIntent().getDoubleExtra("longitude", -1);
        destinationType = getIntent().getStringExtra("destination_type");

        if (latitude != -1) {
            isFromMap = true;
        } else {
            isFromMap = false;
        }

        dayIndex = 0;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background); //adding default value

        if (isFromMap) {
            destination = DAO.getInstance(getApplicationContext()).getDestination(latitude, longitude);
            days = destination.getDays();
            destinationType = destination.getType();
            updateDestinationDetails();
            updateCurrentDay(dayIndex, days);
            setHeaderVisibility(false);
        } else {
            days = new RealmList<Day>();
        }
        if (destinationType.equals(NEW_DESTINATION)) {
            title_text.setText("new destination");
            parent.setBackgroundColor(0x808BDC39);
        } else {
            title_text.setText("old destination");
            parent.setBackgroundColor(0x80EC2E2E);
        }
        enableDisableAddButton();
        realm = ((MyApplication) getApplication()).getRealm();

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Address> addresses = getLocationFromAddress(search_edit.getText().toString());

                String show = "";
                if (addresses != null && addresses.size() > 0) {
                    address = addresses.get(0);
                    show = parseAddress(address);
                }

                search_edit.setText("");
                search_text.setText(show);
            }
        });

        date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        NewDestinationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, month + 1);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        date = cal.getTime();
                        //     date = new Date(year - 1900, month, dayOfMonth);

                        String dateStr = dayOfMonth + "/" + (month + 1) + "/" + year;
                        date_text.setText(dateStr);
                    }
                }, 2020, 1, 1);

                datePickerDialog.show();
            }
        });

        image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDay();
                // destination.replaceDays(days, realm);
                DAO.getInstance(getApplicationContext()).addOrUpdateDestination(destination, days);
                onBackPressed();
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        next_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDay();
                cleanUI();
                dayIndex = (dayIndex + 1) % (days.size());
                updateCurrentDay(dayIndex, days);
                enableDisableAddButton();
            }
        });

        prev_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDay();
                cleanUI();
                if (dayIndex == 0) {
                    dayIndex = days.size() - 1;
                } else {
                    dayIndex = (dayIndex - 1);
                }
                updateCurrentDay(dayIndex, days);
                enableDisableAddButton();
            }
        });

        add_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address != null) {
                    saveDay();
                    cleanUI();
                    dayIndex++;
                    updateCurrentDay(dayIndex, days);
                    enableDisableAddButton();
                } else {
                    Toast.makeText(getApplicationContext(), "please choose location", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void enableDisableAddButton() {
        if (dayIndex < days.size() - 1) {
            add_day.setEnabled(false);
        } else {
            add_day.setEnabled(true);
        }

    }

    private void setHeaderVisibility(boolean b) {
        if (b == false) {
            search_edit.setVisibility(View.INVISIBLE);
            search_button.setVisibility(View.INVISIBLE);
            date_button.setVisibility(View.INVISIBLE);
        }

    }

    private void cleanUI() {
        planing.setText("");
        details.setText("");
        //    image.setBackgroundResource(R.drawable.ic_launcher_background);

        int w = 50, h = 50;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(w, h, conf); // this creates a MUTABLE bitmap
        image.setImageBitmap(bmp);

        bitmap = bmp;

//        image.setImageDrawable(null);
//        image.setImageBitmap(null);
    }

    private void updateCurrentDay(int index, List<Day> days) {
        day_text.setText("day " + (index + 1));
        if (index < days.size()) { // existing day.  if not its a new day
            Day day = days.get(index);
            planing.setText(day.getDetails1());
            details.setText(day.getDetails2());
            bitmap = day.getImage();
            image.setImageBitmap(bitmap);
        } else {
            // bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background); //adding default value
        }
    }

    private void saveDay() {

        Day day = new Day(planing.getText().toString(), details.getText().toString(), bitmap);

        realm.beginTransaction();
        if (dayIndex < days.size()) { // day already exist in days and needs to be updated
            days.set(dayIndex, day);
        } else { // its a new day
            days.add(day);
        }
        realm.commitTransaction();

        if (destination == null) {
    /*        realm.beginTransaction();

            destination = realm.createObject(Destination.class,  ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE)+"");
            destination.setAddress(address);
            destination.setType(NEW_DESTINATION);
            destination.setDate(date);

            realm.commitTransaction();
*/

            if (destinationType.equals(NEW_DESTINATION)) {
                destination = new Destination(address, NEW_DESTINATION, date);
            } else {
                destination = new Destination(address, OLD_DESTINATION, date);
            }
        }
    }

    private void updateDestinationDetails() {
        String show = parseAddress(destination.getAddress(getApplicationContext()));
        search_text.setText(show);

        date = destination.getDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String dateStr = cal.get(Calendar.DAY_OF_MONTH) + "/" +
                (cal.get(Calendar.MONTH)) + "/" + (cal.get(Calendar.YEAR));
        date_text.setText(dateStr);
    }

    private String parseAddress(Address location) {
        String locality = location.getLocality();
        if (locality == null) {
            locality = "";
        }

        String thoroughfare = location.getThoroughfare();
        if (thoroughfare == null) {
            thoroughfare = "";
        }

        String subThoroughfare = location.getSubThoroughfare();
        if (subThoroughfare == null) {
            subThoroughfare = "";
        }

        return location.getCountryName() + " " + locality + " "
                + thoroughfare + " " + subThoroughfare;
    }


    public List<Address> getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            // LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            return address;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                bitmap = BitmapFactory.decodeStream(imageStream);
                image.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                // Toast.makeText(PostImage.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            // Toast.makeText(PostImage.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
}
