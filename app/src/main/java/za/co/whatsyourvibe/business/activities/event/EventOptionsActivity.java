package za.co.whatsyourvibe.business.activities.event;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import dmax.dialog.SpotsDialog;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import za.co.whatsyourvibe.business.R;

public class EventOptionsActivity extends AppCompatActivity {

    private TextInputLayout tilEventFee;
    private RadioGroup rbgEventPrivacy;
    private double dblEventFee;
    private boolean isEventPaid;
    private String mEventPrivacy;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_options);

        Toolbar toolbar = findViewById(R.id.event_options_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Event Options");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Switch swEventPaid = findViewById(R.id.event_options_swEventFee);

        tilEventFee = findViewById(R.id.event_options_tilEventFee);
        rbgEventPrivacy = findViewById(R.id.event_options_rbgPrivacy);

        isEventPaid = false;

        Button btnSubmit = findViewById(R.id.event_options_btnCreateEvent);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEventPaid){
                    if (TextUtils.isEmpty(tilEventFee.getEditText().getText())){
                        tilEventFee.setError("Please enter event entry fee!");
                        tilEventFee.requestFocus();
                        return;
                    }

                    dblEventFee =
                            Double.parseDouble(tilEventFee.getEditText().getText().toString().trim());
                    createEvent();

                }else{
                    dblEventFee = 0.00;
                    createEvent();
                }

                if (rbgEventPrivacy.getCheckedRadioButtonId() == -1){
                    Toast.makeText(EventOptionsActivity.this, "Please select event privacy",
                            Toast.LENGTH_SHORT).show();
                    return;
                }



            }
        });

        swEventPaid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    tilEventFee.setVisibility(View.VISIBLE);
                    isEventPaid = true;
                }else{
                    tilEventFee.setVisibility(View.GONE);
                    dblEventFee = 0.00;
                    isEventPaid = false;
                }
            }
        });
    }

    private void createEvent() {

        final AlertDialog dialog = new SpotsDialog.Builder().setContext(EventOptionsActivity.this).build();

        dialog.setMessage("Please wait, creating your event...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        int id = rbgEventPrivacy.getCheckedRadioButtonId();
        String privacy = "";

        if(id == R.id.event_options_rbPublic){
            privacy = "PUBLIC";
        } else if(id == R.id.event_options_rbPrivate){
           privacy = "PRIVATE";
        }

        SharedPreferences sharedPreferences = getSharedPreferences("WYV", MODE_PRIVATE);

        String creatorUid = sharedPreferences.getString("uid","12345");
        String displayName = sharedPreferences.getString("displayName","Guest");

        EventCategory.myEvent.setEventEntryFee(dblEventFee);
        EventCategory.myEvent.setEventPrivacy(privacy);
        EventCategory.myEvent.setCreatorDisplayName(displayName);
        EventCategory.myEvent.setCreatorUid(creatorUid);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("events")
                .add(EventCategory.myEvent)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        dialog.dismiss();
                        Intent i = new Intent(EventOptionsActivity.this,
                                EventCreationSuccessActivity.class);

                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(EventOptionsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
