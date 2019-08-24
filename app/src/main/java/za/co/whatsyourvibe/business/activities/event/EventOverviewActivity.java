package za.co.whatsyourvibe.business.activities.event;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.viewpagerindicator.CirclePageIndicator;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import za.co.whatsyourvibe.business.R;
import za.co.whatsyourvibe.business.adapters.SlidingImageAdapter;
import za.co.whatsyourvibe.business.models.MyEvent;

public class EventOverviewActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView tvEventDate, tvEventShare, tvEventGoing;
    private TextView tvEventName;
    private TextInputLayout etEventDescription;
    private FloatingActionButton btnEdit, btnCancel;
    private Button btnSave;
    private FirebaseFirestore eventRef;
    private MyEvent myEvent;
    private ProgressDialog progressDialog;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private String[] urls;

//    private String[] urls; = new String[] {"https://firebasestorage.googleapis.com/v0/b/whatsyourvibe.appspot.com/o/event_photos%2FSOWltghvbIhx0N5BYmGNhoBOGgK21565190263552?alt=media&token=ac15e054-2a74-4cb4-aaa1-48b9f4ca7f0b",
//            "https://firebasestorage.googleapis.com/v0/b/whatsyourvibe.appspot.com/o/event_photos%2FSOWltghvbIhx0N5BYmGNhoBOGgK21565190270260?alt=media&token=5abe5b19-e68e-496c-b121-95f6d973cdfe",
//            "https://firebasestorage.googleapis.com/v0/b/whatsyourvibe.appspot.com/o/event_photos%2FSOWltghvbIhx0N5BYmGNhoBOGgK21565190275205?alt=media&token=8842d84c-963c-467f-aee2-ccc9e5f2493e",
//            "https://demonuts.com/Demonuts/SampleImages/W-13.JPG", "https://demonuts.com/Demonuts/SampleImages/W-17.JPG", "https://demonuts.com/Demonuts/SampleImages/W-21.JPG"};

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_overview);

        Toolbar toolbar = findViewById(R.id.event_overview_toolbar);
        setSupportActionBar(toolbar);

        eventRef = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Please wait...");

        if (getSupportActionBar() !=null) {
            getSupportActionBar().setTitle("Event Overview");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initViews();

        loadEventDetails();

        initSlider();
    }

    private void initSlider() {

        mPager = findViewById(R.id.event_overview_viewPager);
        mPager.setAdapter(new SlidingImageAdapter(EventOverviewActivity.this,urls));

        CirclePageIndicator indicator = findViewById(R.id.event_overview_indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = urls.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    private void loadEventDetails() {

        myEvent = (MyEvent) getIntent().getSerializableExtra("EVENT");

        if (myEvent !=null){

            tvEventDate.setText(myEvent.getDate());

            tvEventGoing.setText(myEvent.getGoing() + " going");

            tvEventShare.setText(myEvent.getShares() + " shares");

            etEventDescription.getEditText().setText(myEvent.getDescription());

            tvEventName.setText(myEvent.getName());

            urls = new String[] {myEvent.getImage1(),myEvent.getImage2(),myEvent.getImage3()};

        }
    }

    private void initViews() {

        tvEventName = findViewById(R.id.event_overview_tvEventName);

        tvEventDate = findViewById(R.id.event_overview_tvEventDate);

        tvEventShare = findViewById(R.id.event_overview_tvEventShares);

        tvEventGoing = findViewById(R.id.event_overview_tvEventGoing);

        etEventDescription = findViewById(R.id.event_overview_etEventDescription);

        btnCancel = findViewById(R.id.event_overview_btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent();
            }
        });

        btnSave = findViewById(R.id.event_overview_btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });

        btnEdit = findViewById(R.id.event_overview_btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEvent();
            }
        });

        tvEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setEventDate();
            }
        });
    }

    private void deleteEvent() {

        progressDialog.show();

        eventRef.collection("events")
                .document(myEvent.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        progressDialog.dismiss();

                        Toast.makeText(EventOverviewActivity.this, "Event delete successfully",
                                Toast.LENGTH_SHORT).show();

                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();

                        Toast.makeText(EventOverviewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void saveEvent() {

        progressDialog.show();

        eventRef.collection("events")
                .document(myEvent.getId())
                .update("description",etEventDescription.getEditText().getText().toString())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();

                        Toast.makeText(EventOverviewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        etEventDescription.setEnabled(false);

                        btnEdit.setVisibility(View.VISIBLE);

                        btnCancel.setVisibility(View.VISIBLE);

                        btnSave.setVisibility(View.GONE);

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        progressDialog.dismiss();

                        Toast.makeText(EventOverviewActivity.this, "Event updated successfully",
                                Toast.LENGTH_SHORT).show();
                        etEventDescription.setEnabled(false);

                        btnEdit.setVisibility(View.VISIBLE);

                        btnCancel.setVisibility(View.VISIBLE);

                        btnSave.setVisibility(View.GONE);
                    }
                });
    }

    private void editEvent() {
        etEventDescription.setEnabled(true);

        etEventDescription.getEditText().requestFocus();

        btnEdit.setVisibility(View.GONE);

        btnCancel.setVisibility(View.GONE);

        btnSave.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR,year);

        c.set(Calendar.MONTH,month);

        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        String date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());


        eventRef.collection("events")
                .document(myEvent.getId())
                .update("date",tvEventDate.getText().toString())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                        Toast.makeText(EventOverviewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        tvEventDate.setText(date);
                    }
                });

    }

    private void setEventDate() {

        DialogFragment eventDate = new za.co.whatsyourvibe.business.utils.DatePicker();

        eventDate.show(getSupportFragmentManager(),"EVENT_DATE");
    }
}
