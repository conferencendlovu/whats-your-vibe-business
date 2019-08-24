package za.co.whatsyourvibe.business.activities.event;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import za.co.whatsyourvibe.business.R;

public class EventLocationActivity extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "EventLocationActivity";

    private TextInputLayout mEventLocation, mEventTime, mEventDate;
    private double dblLatitude, dblLongitude;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_location);

        Toolbar toolbar = findViewById(R.id.event_location_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Event Location");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEventLocation = findViewById(R.id.event_location_tilEventLocation);
        mEventDate = findViewById(R.id.event_location_tilEventDate);

        mEventTime = findViewById(R.id.event_location_tilEventTime);


        Button btnNext = findViewById(R.id.event_location_btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });

        initGooglePlaces();
    }

    private void checkFields() {

        if (TextUtils.isEmpty(mEventLocation.getEditText().getText())){
            mEventLocation.setError("Please enter your event location using the search box above!");
            return;
        }

        if (TextUtils.isEmpty(mEventTime.getEditText().getText())){
            mEventTime.setError("Event time is missing!");
            return;
        }

        if (TextUtils.isEmpty(mEventDate.getEditText().getText())){
            mEventDate.setError("Event date is missing!");
            return;
        }


        String date = mEventDate.getEditText().getText().toString().trim();
        String time = mEventTime.getEditText().getText().toString().trim();
        String location = mEventLocation.getEditText().getText().toString().trim();


        EventCategory.myEvent.setDate(date);
        EventCategory.myEvent.setTime(time);
        EventCategory.myEvent.setLocation(location);
        EventCategory.myEvent.setLatitude(dblLatitude);
        EventCategory.myEvent.setLongitude(dblLongitude);

        Intent i = new Intent(EventLocationActivity.this, EventRestrictionsActivity.class);
        startActivity(i);
    }

    private void initGooglePlaces() {

        String apiKey = "AIzaSyBTBAbiOwIwBZN3QJyR8ujV_Hh72QwbdaA";

        // Initialize Places.
        Places.initialize(getApplicationContext(), apiKey);

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        final AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                                                                   getSupportFragmentManager().findFragmentById(R.id.google_places);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.NAME,
                Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mEventLocation.getEditText().setText(place.getAddress());
                if (place.getLatLng() !=null){
                    dblLatitude = place.getLatLng().latitude;
                    dblLongitude = place.getLatLng().longitude;
                }else {
                    dblLatitude = -25.906850;
                    dblLongitude = 28.012100;
                }

            }


            @Override
            public void onError(Status status) {
                Log.e(TAG, "An error occurred: " + status.getStatusMessage());
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR,year);

        c.set(Calendar.MONTH,month);

        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        String date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        mEventDate.getEditText().setText(date);

    }



    private void setEventDate() {

        DialogFragment eventDate = new za.co.whatsyourvibe.business.utils.DatePicker();

        eventDate.show(getSupportFragmentManager(),"EVENT_DATE");
    }


    private void setEventTime() {

        DialogFragment eventTime = new za.co.whatsyourvibe.business.utils.TimePicker();

        eventTime.show(getSupportFragmentManager(),"EVENT_TIME");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

       // Calendar c = Calendar.getInstance();

       // c.set(Calendar.HOUR,hourOfDay);

      //  c.set(Calendar.MINUTE,minute);

        //String time =  DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        mEventTime.getEditText().setText(hourOfDay + " : " + minute);

    }

    public void selectTime(View view) {

        setEventTime();
    }

    public void selectDate(View view) {

        setEventDate();

    }
}
