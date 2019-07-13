package za.co.whatsyourvibe.business.activities.event;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import za.co.whatsyourvibe.business.R;
import za.co.whatsyourvibe.business.models.MyEvent;

public class EventCategory extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivNightClub, ivRestaurants, ivFashionShows, ivPicnics, ivTouristSites;
    private ImageView ivConcerts,ivConferences, ivGalas, ivAwardsShows;

    private LinearLayout llNightClub, llRestaurants, llFashionShows, llPicnics, llTouristSites;
    private LinearLayout llConcerts,llConferences, llGalas, llAwardsShows;

    private String strEventType;

    public static MyEvent myEvent;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_category);

        Toolbar toolbar = findViewById(R.id.event_category_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Create Event");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        setOnClickListeners();

        myEvent = new MyEvent();

        Button btnNext = findViewById(R.id.event_category_btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventCategory.this, EventDetailsActivity.class);
                startActivity(i);

                myEvent.setCategory(strEventType);
            }
        });

    }

    private void initViews() {
        ivNightClub = findViewById(R.id.event_category_ivNightClubs);
        ivRestaurants = findViewById(R.id.event_category_ivRestaurants);
        ivFashionShows = findViewById(R.id.event_category_ivFashionShows);
        ivPicnics = findViewById(R.id.event_category_ivPicnics);
        ivTouristSites = findViewById(R.id.event_category_ivTouristSites);
        ivConcerts = findViewById(R.id.event_category_ivConcerts);
        ivConferences = findViewById(R.id.event_category_ivConferences);
        ivGalas = findViewById(R.id.event_category_ivGala);
        ivAwardsShows = findViewById(R.id.event_category_ivAwardsShows);

        llNightClub = findViewById(R.id.event_category_llNightClubs);
        llRestaurants = findViewById(R.id.event_category_llRestaurants);
        llFashionShows = findViewById(R.id.event_category_llFashionShows);
        llPicnics = findViewById(R.id.event_category_llPicnics);
        llTouristSites = findViewById(R.id.event_category_llTouristSites);
        llConcerts = findViewById(R.id.event_category_llConcerts);
        llConferences = findViewById(R.id.event_category_llConferences);
        llGalas = findViewById(R.id.event_category_llGala);
        llAwardsShows = findViewById(R.id.event_category_llAwardsShows);
    }

    private void setOnClickListeners(){

        llNightClub.setOnClickListener(this);
        llRestaurants.setOnClickListener(this);
        llFashionShows.setOnClickListener(this);
        llPicnics.setOnClickListener(this);
        llTouristSites.setOnClickListener(this);
        llConcerts.setOnClickListener(this);
        llConferences.setOnClickListener(this);
        llGalas.setOnClickListener(this);
        llAwardsShows.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.event_category_llAwardsShows:
                ivAwardsShows.setVisibility(View.VISIBLE);
                ivNightClub.setVisibility(View.GONE);
                ivRestaurants.setVisibility(View.GONE);
                ivFashionShows.setVisibility(View.GONE);
                ivPicnics.setVisibility(View.GONE);
                ivTouristSites.setVisibility(View.GONE);
                ivConcerts.setVisibility(View.GONE);
                ivConferences.setVisibility(View.GONE);
                ivGalas.setVisibility(View.GONE);

                strEventType = "AWARDS SHOWS";
                break;

            case R.id.event_category_llNightClubs:
                ivAwardsShows.setVisibility(View.GONE);
                ivNightClub.setVisibility(View.VISIBLE);
                ivRestaurants.setVisibility(View.GONE);
                ivFashionShows.setVisibility(View.GONE);
                ivPicnics.setVisibility(View.GONE);
                ivTouristSites.setVisibility(View.GONE);
                ivConcerts.setVisibility(View.GONE);
                ivConferences.setVisibility(View.GONE);
                ivGalas.setVisibility(View.GONE);

                strEventType = "NIGHT CLUBS";
                break;

            case R.id.event_category_llRestaurants:
                ivAwardsShows.setVisibility(View.GONE);
                ivNightClub.setVisibility(View.GONE);
                ivRestaurants.setVisibility(View.VISIBLE);
                ivFashionShows.setVisibility(View.GONE);
                ivPicnics.setVisibility(View.GONE);
                ivTouristSites.setVisibility(View.GONE);
                ivConcerts.setVisibility(View.GONE);
                ivConferences.setVisibility(View.GONE);
                ivGalas.setVisibility(View.GONE);

                strEventType = "RESTAURANTS";
                break;

            case R.id.event_category_llFashionShows:
                ivAwardsShows.setVisibility(View.GONE);
                ivNightClub.setVisibility(View.GONE);
                ivRestaurants.setVisibility(View.GONE);
                ivFashionShows.setVisibility(View.VISIBLE);
                ivPicnics.setVisibility(View.GONE);
                ivTouristSites.setVisibility(View.GONE);
                ivConcerts.setVisibility(View.GONE);
                ivConferences.setVisibility(View.GONE);
                ivGalas.setVisibility(View.GONE);

                strEventType = "FASHION SHOWS";
                break;

            case R.id.event_category_llPicnics:
                ivAwardsShows.setVisibility(View.GONE);
                ivNightClub.setVisibility(View.GONE);
                ivRestaurants.setVisibility(View.GONE);
                ivFashionShows.setVisibility(View.GONE);
                ivPicnics.setVisibility(View.VISIBLE);
                ivTouristSites.setVisibility(View.GONE);
                ivConcerts.setVisibility(View.GONE);
                ivConferences.setVisibility(View.GONE);
                ivGalas.setVisibility(View.GONE);

                strEventType = "PICNICS";
                break;

            case R.id.event_category_llTouristSites:
                ivAwardsShows.setVisibility(View.GONE);
                ivNightClub.setVisibility(View.GONE);
                ivRestaurants.setVisibility(View.GONE);
                ivFashionShows.setVisibility(View.GONE);
                ivPicnics.setVisibility(View.GONE);
                ivTouristSites.setVisibility(View.VISIBLE);
                ivConcerts.setVisibility(View.GONE);
                ivConferences.setVisibility(View.GONE);
                ivGalas.setVisibility(View.GONE);

                strEventType = "TOURIST SITES";
                break;

            case R.id.event_category_llConcerts:
                ivAwardsShows.setVisibility(View.GONE);
                ivNightClub.setVisibility(View.GONE);
                ivRestaurants.setVisibility(View.GONE);
                ivFashionShows.setVisibility(View.GONE);
                ivPicnics.setVisibility(View.GONE);
                ivTouristSites.setVisibility(View.GONE);
                ivConcerts.setVisibility(View.VISIBLE);
                ivConferences.setVisibility(View.GONE);
                ivGalas.setVisibility(View.GONE);

                strEventType = "CONCERTS";
                break;

            case R.id.event_category_llConferences:
                ivAwardsShows.setVisibility(View.GONE);
                ivNightClub.setVisibility(View.GONE);
                ivRestaurants.setVisibility(View.GONE);
                ivFashionShows.setVisibility(View.GONE);
                ivPicnics.setVisibility(View.GONE);
                ivTouristSites.setVisibility(View.GONE);
                ivConcerts.setVisibility(View.GONE);
                ivConferences.setVisibility(View.VISIBLE);
                ivGalas.setVisibility(View.GONE);

                strEventType = "CONFERENCES";
                break;

            case R.id.event_category_llGala:
                ivAwardsShows.setVisibility(View.GONE);
                ivNightClub.setVisibility(View.GONE);
                ivRestaurants.setVisibility(View.GONE);
                ivFashionShows.setVisibility(View.GONE);
                ivPicnics.setVisibility(View.GONE);
                ivTouristSites.setVisibility(View.GONE);
                ivConcerts.setVisibility(View.GONE);
                ivConferences.setVisibility(View.GONE);
                ivGalas.setVisibility(View.VISIBLE);

                strEventType = "GALAS";
                break;


        }
    }
}
