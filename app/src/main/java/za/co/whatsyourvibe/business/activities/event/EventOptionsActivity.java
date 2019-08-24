package za.co.whatsyourvibe.business.activities.event;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

import dmax.dialog.SpotsDialog;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import za.co.whatsyourvibe.business.R;

public class EventOptionsActivity extends AppCompatActivity {

    private TextInputLayout tilEventFee;

    private RadioGroup rbgEventPrivacy;

    private double dblEventFee;

    private boolean isEventPaid;

    private String mEventPrivacy;

    private String businessId, displayName;

    private StorageReference mStorageReference;

    private Uri mImageUri1, mImageUri2,mImageUri3;

    private AlertDialog dialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_options);

        Toolbar toolbar = findViewById(R.id.event_options_toolbar);

        dialog = new SpotsDialog.Builder().setContext(EventOptionsActivity.this).build();

        dialog.setMessage("Please wait, creating your event...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        mStorageReference = FirebaseStorage.getInstance().getReference("event_photos");

        businessId = auth.getUid();

        displayName = auth.getCurrentUser().getDisplayName();

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
                    uploadImage();

                }else{
                    dblEventFee = 0.00;
                    uploadImage();
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

    private void uploadImage() {

        dialog.setMessage("Uploading Image 1");

        dialog.show();

        final StorageReference fileReference =
                mStorageReference.child(businessId + Calendar.getInstance().getTimeInMillis());

        mImageUri1 = Uri.parse(EventCategory.myEvent.getImage1());

        UploadTask uploadTask = fileReference.putFile(mImageUri1);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(EventOptionsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        mImageUri1 = uri;

                        uploadImage2();

//                        dialog.setMessage("Creating your event...");
//
//                        createEvent();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        dialog.dismiss();

                        Toast.makeText(EventOptionsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

    }

    private void uploadImage2() {

        dialog.setMessage("Uploading Image 2");

        dialog.show();

        final StorageReference fileReference =
                mStorageReference.child(businessId + Calendar.getInstance().getTimeInMillis());

        mImageUri2 = Uri.parse(EventCategory.myEvent.getImage2());

        UploadTask uploadTask = fileReference.putFile(mImageUri2);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(EventOptionsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        mImageUri2 = uri;

                        uploadImage3();

//                        dialog.setMessage("Creating your event...");
//
//                        createEvent();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        dialog.dismiss();

                        Toast.makeText(EventOptionsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

    }

    private void uploadImage3() {

        dialog.setMessage("Uploading Image 3");

        dialog.show();

        final StorageReference fileReference =
                mStorageReference.child(businessId + Calendar.getInstance().getTimeInMillis());

        mImageUri3 = Uri.parse(EventCategory.myEvent.getImage3());

        UploadTask uploadTask = fileReference.putFile(mImageUri3);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(EventOptionsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        mImageUri3 = uri;

                       // uploadImage2();

                        dialog.setMessage("Creating your event...");

                        createEvent();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        dialog.dismiss();

                        Toast.makeText(EventOptionsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }

    private void createEvent() {

        int id = rbgEventPrivacy.getCheckedRadioButtonId();

        if(id == R.id.event_options_rbPublic){
            mEventPrivacy = "PUBLIC";
        } else if(id == R.id.event_options_rbPrivate){
            mEventPrivacy = "PRIVATE";
        }

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference doc = firebaseFirestore.collection("events").document();

        String eventId = doc.getId();

        EventCategory.myEvent.setId(eventId);

        EventCategory.myEvent.setEventEntryFee(dblEventFee);
        EventCategory.myEvent.setEventPrivacy(mEventPrivacy);
        EventCategory.myEvent.setCreatorDisplayName(displayName);
        EventCategory.myEvent.setCreatorUid(businessId);
        EventCategory.myEvent.setImage1(mImageUri1.toString());
        EventCategory.myEvent.setImage2(mImageUri2.toString());
        EventCategory.myEvent.setImage3(mImageUri3.toString());

        firebaseFirestore.collection("events")
                .document(eventId)
                .set(EventCategory.myEvent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        Intent i = new Intent(EventOptionsActivity.this,
                                EventCreationSuccessActivity.class);

                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(EventOptionsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
