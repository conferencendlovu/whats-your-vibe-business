package za.co.whatsyourvibe.business.activities.event;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import za.co.whatsyourvibe.business.R;

public class EventDetailsActivity extends AppCompatActivity {

    private static final String TAG = "EventDetails";

    private static final int RC_IMAGE_PICKER = 200;

    private static final int RC_PERMISSION = 200;

    private TextInputLayout tilEventName, tilDescription;

    private ImageView ivEventPoster1, ivEventPoster2, ivEventPoster3;

    private String eventPosterUrl1, eventPosterUrl2, eventPosterUrl3;

    private int selectedImage = 0;

    private boolean isImageOneSelected = false;


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

        ivEventPoster1 = findViewById(R.id.event_details_ivEventImage1);

        ivEventPoster1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedImage = 1;

                checkPermission();

            }
        });

        ivEventPoster2 = findViewById(R.id.event_details_ivEventImage2);

        ivEventPoster2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedImage = 2;

                checkPermission();

            }
        });

        ivEventPoster3 = findViewById(R.id.event_details_ivEventImage3);

        ivEventPoster3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedImage = 3;

                checkPermission();

            }
        });

        eventPosterUrl1 = "";
        eventPosterUrl2 = "";
        eventPosterUrl3 = "";

        Button btnNext = findViewById(R.id.event_details_btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInputs();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putString("EVENT_NAME", tilEventName.getEditText().getText().toString());

        outState.putString("EVENT_DESCRIPTION", tilDescription.getEditText().getText().toString());

        outState.putString("IMAGE_1", eventPosterUrl1);

        outState.putString("IMAGE_2", eventPosterUrl2);

        outState.putString("IMAGE_3", eventPosterUrl3);




    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

      //  super.onRestoreInstanceState(savedInstanceState);

        tilEventName.getEditText().setText(savedInstanceState.getString("EVENT_NAME"));

        tilDescription.getEditText().setText(savedInstanceState.getString("EVENT_DESCRIPTION"));

        ivEventPoster1.setImageURI( (Uri)(savedInstanceState.get("IMAGE_1")));

        ivEventPoster2.setImageURI( (Uri)(savedInstanceState.get("IMAGE_2")));

        ivEventPoster3.setImageURI((Uri) (savedInstanceState.get("IMAGE_3")));
    }

    private void checkInputs() {

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

        if (!isImageOneSelected) {

            Toast.makeText(this, "Please upload at least one image", Toast.LENGTH_SHORT).show();

            return;
        }

        String eventName = tilEventName.getEditText().getText().toString().trim().toUpperCase();
        String eventDescription = tilDescription.getEditText().getText().toString().trim();


        EventCategory.myEvent.setName(eventName);
        EventCategory.myEvent.setDescription(eventDescription);
        EventCategory.myEvent.setImage1(eventPosterUrl1);
        EventCategory.myEvent.setImage2(eventPosterUrl2);
        EventCategory.myEvent.setImage3(eventPosterUrl3);

        Intent i = new Intent(EventDetailsActivity.this, EventLocationActivity.class);
        startActivity(i);
    }

    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {

                // permission not granted. ask for it
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

                // show pop up
                requestPermissions(permissions, RC_PERMISSION);

            } else {

                // permission already granted
                pickImageFromGallery();
            }

        } else {
            // device less then mashmallow

            pickImageFromGallery();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case RC_PERMISSION:

                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    pickImageFromGallery();

                } else {

                    // permission denied
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void pickImageFromGallery() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(4,3)
                .setMinCropResultSize(450,200)
                .start(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                if (selectedImage == 1) {

                    ivEventPoster1.setImageURI(result.getUri());

                    eventPosterUrl1 =result.getUri().toString();

                    isImageOneSelected = true;

                }

                if (selectedImage == 2) {

                    ivEventPoster2.setImageURI(result.getUri());

                    eventPosterUrl2 =result.getUri().toString();

                }

                if (selectedImage == 3) {

                    ivEventPoster3.setImageURI(result.getUri());

                    eventPosterUrl3 =result.getUri().toString();

                }



                Uri resultUri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK && requestCode == RC_IMAGE_PICKER) {
//
//            if (selectedImage == 1) {
//
//                ivEventPoster1.setImageURI(data.getData());
//
//                eventPosterUrl1 = data.getDataString();
//
//            }
//
//            if (selectedImage == 2) {
//
//                ivEventPoster2.setImageURI(data.getData());
//
//                eventPosterUrl2 = data.getDataString();
//
//            }
//
//            if (selectedImage == 3) {
//
//                ivEventPoster3.setImageURI(data.getData());
//
//                eventPosterUrl3 = data.getDataString();
//
//            }

//            ivEventPoster1.setImageURI(data.getData());
//
//            eventPosterUrl = data.getDataString();

        // mImageUri = data.getData();

        //  progressDialog.show();

        //  uploadImage();

    }
}
