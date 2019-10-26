package za.co.whatsyourvibe.business.activities.event;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import za.co.whatsyourvibe.business.R;

public class EventLocationActivity extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "EventLocationActivity";

    private int AUTOCOMPLETE_REQUEST_CODE = 1;

    private TextView mEventLocation, mEventTime, mEventDate, mEventTimeLabel, mEventDateLabel,
            mEventLocationLabel;
    private double dblLatitude, dblLongitude;

    private boolean isDateSet, isLocationSet, isTimeSet;

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_location);

        Toolbar toolbar = findViewById(R.id.event_location_toolbar);

        isDateSet = false;

        isLocationSet = false;

        isTimeSet = false;

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Event Location");

        String apiKey = getString(R.string.api_key);

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEventLocation = findViewById(R.id.activity_event_location_tvLocation);

        mEventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAutoCompleteActivity();

            }
        });

        mEventDate = findViewById(R.id.activity_event_location_tvDate);

        mEventDateLabel = findViewById(R.id.activity_event_location_tvDateLabel);

        mEventTimeLabel = findViewById(R.id.activity_event_location_tvTimeLabel);

        mEventTime = findViewById(R.id.activity_event_location_tvTime);

        mEventTimeLabel = findViewById(R.id.activity_event_location_tvTimeLabel);

        mEventLocationLabel = findViewById(R.id.activity_event_location_tvLocationLabel);


        Button btnNext = findViewById(R.id.event_location_btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkDateTimeLocation();
            }
        });

    }

    private void checkDateTimeLocation() {

        if (!isLocationSet) {

            Toast.makeText(this, "Please set location", Toast.LENGTH_SHORT).show();

           // return;
        }

        if (!isTimeSet) {

            Toast.makeText(this, "Please set time", Toast.LENGTH_SHORT).show();

            return;
        }


        if (!isDateSet) {

            Toast.makeText(this, "Please set date", Toast.LENGTH_SHORT).show();

            return;
        }

        EventCategory.myEvent.setLocation(mEventLocation.getText().toString());

        EventCategory.myEvent.setTime(mEventTime.getText().toString());

        EventCategory.myEvent.setDate(mEventDate.getText().toString());

        Intent intent = new Intent(EventLocationActivity.this,EventTicketsActivity.class);

        startActivity(intent);

    }

    private void showAutoCompleteActivity() {

        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);

        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                                .setCountry("ZA")
                                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                Place place = Autocomplete.getPlaceFromIntent(data);

                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());

                mEventLocation.setText(place.getAddress());

                mEventLocation.setVisibility(View.VISIBLE);

                mEventLocationLabel.setVisibility(View.VISIBLE);

                isLocationSet = true;

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {

                // TODO: Handle the error.

                Status status = Autocomplete.getStatusFromIntent(data);

                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {

                // The user canceled the operation.
            }
        }
    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR,year);

        c.set(Calendar.MONTH,month);

        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        String date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        mEventDate.setText(date);

        mEventDate.setVisibility(View.VISIBLE);

        mEventDateLabel.setVisibility(View.VISIBLE);

        isDateSet = true;

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

        mEventTime.setText(hourOfDay + " : " + minute);

        mEventTimeLabel.setVisibility(View.VISIBLE);

        mEventTime.setVisibility(View.VISIBLE);

        isTimeSet = true;

    }

    public void selectTime(View view) {

        setEventTime();

    }


    public void selectDate(View view) {

        setEventDate();

    }

    public void selectLocation(View view) {

        showAutoCompleteActivity();

    }
}
