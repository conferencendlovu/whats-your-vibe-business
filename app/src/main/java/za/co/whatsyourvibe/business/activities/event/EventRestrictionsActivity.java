package za.co.whatsyourvibe.business.activities.event;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;


import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import za.co.whatsyourvibe.business.R;


public class EventRestrictionsActivity extends AppCompatActivity {

    private static final String TAG = "EventRestrictionsActivi";

    private Switch smoking, age,alcohol;

    private TextView tvMinAgeLabel;

    private EditText etMinAge;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_restrictions);

        Toolbar toolbar = findViewById(R.id.event_restrictions_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Event Restrictions");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        smoking = findViewById(R.id.event_restrictions_swSmoking);

        age = findViewById(R.id.event_restrictions_swAge);


        alcohol = findViewById(R.id.event_restrictions_swAlcohol);

        tvMinAgeLabel = findViewById(R.id.event_restrictions_tvMinAgeLabel);

        etMinAge = findViewById(R.id.event_restrictions_etMinAge);

        Button btnNext = findViewById(R.id.event_restrictions_btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextScreen();
            }
        });

        age.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    tvMinAgeLabel.setVisibility(View.VISIBLE);

                    etMinAge.setVisibility(View.VISIBLE);
                }else {

                    tvMinAgeLabel.setVisibility(View.GONE);

                    etMinAge.setVisibility(View.GONE);

                    etMinAge.setText("");

                }

            }
        });
    }

    private void nextScreen() {

        if (smoking.isChecked()) {
            EventCategory.myEvent.setSmoking("Smoking Allowed");
        }else{
            EventCategory.myEvent.setSmoking("Smoking Not Allowed");
        }

        if (alcohol.isChecked()) {
            EventCategory.myEvent.setAlcohol("Alcohol Allowed");
        }else{
            EventCategory.myEvent.setAlcohol("Alcohol Not Allowed");
        }

        if (age.isChecked()) {
            EventCategory.myEvent.setMinAge(etMinAge.getText()+"");
        }else{
            EventCategory.myEvent.setMinAge("No age restrictions");
        }


        Intent i = new Intent(EventRestrictionsActivity.this,EventOptionsActivity.class);
        startActivity(i);
    }
}
