package com.e.mytravel4;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmList;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.e.mytravel4.Destination.NEW_DESTINATION;
import static com.e.mytravel4.Destination.OLD_DESTINATION;

public class NewDestinationActivity extends FragmentActivity {


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

        double latitude = getIntent().getDoubleExtra("latitude", -1);
        double longitude = getIntent().getDoubleExtra("longitude", -1);
        destinationType = getIntent().getStringExtra("destination_type");

        if (latitude != -1) {
            isFromMap = true;
        } else {
            isFromMap = false;
        }

        dayIndex = 0;

        if (isFromMap) {
            destination = DAO.getInstance(getApplicationContext()).getDestination(latitude, longitude);
            days = destination.getDays();
            destinationType = destination.getType();
            updateDestinationAndDate();
            updateCurrentDay(dayIndex, days);
            setHeaderVisibility(false);
        } else {
            days = new RealmList<Day>();
        }
        if (destinationType.equals(NEW_DESTINATION)) {
            title_text.setText("new destination");
        } else {
            title_text.setText("old destination");
        }
        enableDisableAddButton();
        realm = ((MyApplication) getApplication()).getRealm();

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Address> addresses = getLocationFromAddress(search_edit.getText().toString());
               /* SearchAdapter adapter = new SearchAdapter(getApplicationContext(), addresses, new SearchAdapter.AdapterListener() {
                    @Override
                    public void onItemClicked(Address location) {

                    }
                });

                recycler.setAdapter(adapter);
                recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));*/

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
                dayIndex = (dayIndex + 1) % (days.size());
                updateCurrentDay(dayIndex, days);
                enableDisableAddButton();
            }
        });

        prev_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDay();
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
                saveDay();
                cleanUI();
                dayIndex++;
                updateCurrentDay(dayIndex, days);
                enableDisableAddButton();
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
    }

    private void updateCurrentDay(int index, List<Day> days) {
        day_text.setText("day " + (index + 1));
        if (index < days.size()) { // existing day.  if not its a new day
            Day day = days.get(index);
            planing.setText(day.getDetails1());
            details.setText(day.getDetails2());
        }
    }

    private void saveDay() {
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher_background);
        Day day = new Day(planing.getText().toString(), details.getText().toString(), icon);

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

    private void updateDestinationAndDate() {
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
        String thoroughfare = location.getThoroughfare();
        if (thoroughfare == null) {
            thoroughfare = "";
        }

        String subThoroughfare = location.getSubThoroughfare();
        if (subThoroughfare == null) {
            subThoroughfare = "";
        }

        return location.getCountryName() + " " + location.getLocality() + " "
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


}
