package za.co.whatsyourvibe.business.activities.event;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.google.android.libraries.places.internal.en;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import za.co.whatsyourvibe.business.R;

public class EventTicketsActivity extends AppCompatActivity {

    private CheckBox cbStandard, cbEarlyBird, cbVIP, cbGroup;

    private Switch swTicket;

    private EditText etStandard, etEarlyBird, etVIP, etGroup;

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_tickets);

        Toolbar toolbar = findViewById(R.id.event_tickets_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Event Tickets");

        initViews();

        setListeners();

        Button btnNext = findViewById(R.id.event_ticket_btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (swTicket.isChecked()) {
                    EventCategory.myEvent.setGroupTicket(Double.parseDouble(etGroup.getText().toString()));

                    EventCategory.myEvent.setVipTicket(Double.parseDouble(etVIP.getText().toString()));

                    EventCategory.myEvent.setStandardTicket(Double.parseDouble(etStandard.getText().toString()));

                    EventCategory.myEvent.setEarlyBirdTicket(Double.parseDouble(etEarlyBird.getText().toString()));

                }

                Intent intent = new Intent(EventTicketsActivity.this,
                        EventRestrictionsActivity.class);

                startActivity(intent);

            }
        });

    }

    private void setListeners() {

        swTicket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    cbStandard.setVisibility(View.VISIBLE);

                    cbEarlyBird.setVisibility(View.VISIBLE);

                    cbVIP.setVisibility(View.VISIBLE);

                    cbGroup.setVisibility(View.VISIBLE);

                }else {

                    cbStandard.setVisibility(View.GONE);

                    cbEarlyBird.setVisibility(View.GONE);

                    cbVIP.setVisibility(View.GONE);

                    cbGroup.setVisibility(View.GONE);

                }
            }
        });

        cbStandard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    etStandard.setVisibility(View.VISIBLE);


                }else {

                    etStandard.setVisibility(View.GONE);

                    etStandard.setText("");

                    EventCategory.myEvent.setStandardTicket(0.00);
                }

            }
        });

        cbEarlyBird.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    etEarlyBird.setVisibility(View.VISIBLE);

                }else {

                    etEarlyBird.setVisibility(View.GONE);

                    etEarlyBird.setText("");

                    EventCategory.myEvent.setEarlyBirdTicket(0.00);
                }

            }
        });

        cbVIP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    etVIP.setVisibility(View.VISIBLE);

                }else {

                    etVIP.setVisibility(View.GONE);

                    etVIP.setText("");

                    EventCategory.myEvent.setVipTicket(0.00);
                }

            }
        });

        cbGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    etGroup.setVisibility(View.VISIBLE);

                }else {

                    etGroup.setVisibility(View.GONE);

                    etGroup.setText("");

                    EventCategory.myEvent.setGroupTicket(0.00);
                }

            }
        });
    }

    private void initViews() {

        swTicket = findViewById(R.id.event_ticket_swTicket);

        cbStandard = findViewById(R.id.event_ticket_cbStandardTicket);

        cbEarlyBird = findViewById(R.id.event_ticket_cbEarlyBird);

        cbVIP = findViewById(R.id.event_ticket_cbVipTicket);

        cbGroup = findViewById(R.id.event_ticket_cbGroupTicket);

        etStandard = findViewById(R.id.event_ticket_etStandardTicket);

        etEarlyBird = findViewById(R.id.event_ticket_etEarlyBird);

        etGroup = findViewById(R.id.event_ticket_etGroupTicket);

        etVIP = findViewById(R.id.event_ticket_etVipTicket);

    }
}
