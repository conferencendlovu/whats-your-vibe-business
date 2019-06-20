package za.co.whatsyourvibe.business.activities.event;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import za.co.whatsyourvibe.business.R;

public class EventLocationActivity extends AppCompatActivity {

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
                Log.e("WYV", "An error occurred: " + status.getStatusMessage());
            }
        });

    }
}
