package za.co.whatsyourvibe.business.activities.event;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import za.co.whatsyourvibe.business.R;
import za.co.whatsyourvibe.business.models.MyEvent;

public class EventOverviewActivity extends AppCompatActivity {

    private TextView tvEventDate, tvEventShare, tvEventGoing;
    private TextView tvEventName;
    private TextInputLayout etEventDescription;
    private ImageView btnEdit, btnCancel, btnSave;
    private FirebaseFirestore eventRef;
    private MyEvent myEvent;
    private ProgressDialog progressDialog;

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
    }

    private void loadEventDetails() {

        myEvent = (MyEvent) getIntent().getSerializableExtra("EVENT");

        if (myEvent !=null){

            tvEventDate.setText(myEvent.getDate());

            tvEventGoing.setText(myEvent.getGoing() + " going");

            tvEventShare.setText(myEvent.getShares() + " shares");

            etEventDescription.getEditText().setText(myEvent.getDescription());

            tvEventName.setText(myEvent.getName());

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

}
