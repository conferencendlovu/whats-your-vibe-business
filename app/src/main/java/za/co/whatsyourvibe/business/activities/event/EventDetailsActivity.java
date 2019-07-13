package za.co.whatsyourvibe.business.activities.event;

import android.content.Context;
import android.content.Intent;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import za.co.whatsyourvibe.business.R;

public class EventDetailsActivity extends AppCompatActivity {

    private TextInputLayout tilEventName, tilDescription;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Toolbar toolbar = findViewById(R.id.event_details_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Event Details");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tilEventName = findViewById(R.id.event_details_tilEventName);
        tilDescription = findViewById(R.id.event_details_tilEventDescription);

        Button btnNext = findViewById(R.id.event_details_btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInputs();
            }
        });

    }

    private void checkInputs(){

        if (TextUtils.isEmpty(tilEventName.getEditText().getText())) {
            tilEventName.setError("Event name is required!");
            tilEventName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(tilDescription.getEditText().getText())) {
            tilDescription.setError("Event description is required!");
            tilDescription.requestFocus();
            return;
        }

        if (tilEventName.getEditText().getText().length() < 6) {
            tilEventName.setError("Event name is too short");
            tilEventName.requestFocus();
            return;
        }

        if (tilDescription.getEditText().getText().length() < 20) {
            tilDescription.setError("Event description is too short");
            tilDescription.requestFocus();
            return;
        }

        String eventName = tilEventName.getEditText().getText().toString().trim().toUpperCase();
        String eventDescription = tilDescription.getEditText().getText().toString().trim();


        EventCategory.myEvent.setName(eventName);
        EventCategory.myEvent.setDescription(eventDescription);
        EventCategory.myEvent.setPoster("default_poster.png");

        Intent i = new Intent(EventDetailsActivity.this, EventLocationActivity.class);
        startActivity(i);
    }
}
