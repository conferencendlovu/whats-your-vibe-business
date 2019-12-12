package za.co.whatsyourvibe.business.activities.vibe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import za.co.whatsyourvibe.business.R;
import za.co.whatsyourvibe.business.adapters.ImagesAdapter;
import za.co.whatsyourvibe.business.models.Image;
import za.co.whatsyourvibe.business.models.Vibe;

public class VibeDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private static final String TAG = "VibeDetailsActivity";

    private static final int REQUEST_PICK_VIDEO = 122;

    private int AUTOCOMPLETE_REQUEST_CODE = 1;

    private static final int RC_PERMISSION = 200;

    private static final int COVER_PERMISSION = 201;

    private TextView mDescription, mCategory, mLocation, mDate, mTime, mContacts, mSocialMedia;

    private TextView mTicket;

    private TextView mRestrictions;

    private TextView mPhotos, mVideos;

    private String mVibeLocationId;

    private ProgressDialog progressDialog;

    private String vibeType = "Free Event";

    private String standard = "";

    private String earlyBird = "";

    private String group = "";

    private String vip = "";

    private String vibeId;

    private boolean isCoverImage = false;

    private ImageView ivCoverPhoto;

    private String videoPath;

    private Uri mImageUri, videoUri;

    private String uploadedImages;

    private StorageReference mStorageReference;

    private Button btnPublish, btnUnPublish;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        vibeId = getIntent().getStringExtra("VIBE_ID");

        String vibeTitle = getIntent().getStringExtra("VIBE_TITLE");

        setContentView(R.layout.activity_vibe_details);

        Toolbar toolbar = findViewById(R.id.vibes_details_toolbar);


        mStorageReference = FirebaseStorage.getInstance().getReference("event_photos");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Toast.makeText(VibeDetailsActivity.this, "Test", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        String apiKey = getString(R.string.api_key);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        if (vibeId !=null) {

            getVibeDetails(vibeId);

            getSupportActionBar().setTitle(vibeTitle);

            mVibeLocationId = vibeId;

            initViews(vibeId);

        }else {

            finish();

        }

        Button btnDeleteVibe = findViewById(R.id.vibe_details_btnDeleteEvent);

        btnDeleteVibe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteVibe(vibeId);
            }
        });

    }

    private void deleteVibe(String id) {

        FirebaseFirestore vibeRef = FirebaseFirestore.getInstance();

        vibeRef.collection("vibes")
                .document(id)
                .update("deleted", "yes")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(VibeDetailsActivity.this, "Event deleted successfully",
                                Toast.LENGTH_SHORT).show();


                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(VibeDetailsActivity.this, e.getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                Place place = Autocomplete.getPlaceFromIntent(data);



                mLocation.setText(place.getAddress());

                FirebaseFirestore locationRef = FirebaseFirestore.getInstance();

                HashMap<String, Object> eventLocation = new HashMap<>();

                eventLocation.put("lat", place.getLatLng().latitude);

                eventLocation.put("lng", place.getLatLng().longitude);

                eventLocation.put("location", place.getAddress());

                locationRef.collection("vibes")
                        .document(mVibeLocationId)
                        .update(eventLocation);


            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {

                // TODO: Handle the error.

                Status status = Autocomplete.getStatusFromIntent(data);

                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {

                // The user canceled the operation.
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                // check variable isCOVERHERE

                mImageUri = result.getUri();

                uploadImage();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }

    }

    private void initViews(String id) {

        btnPublish = findViewById(R.id.vibe_details_btnVibeStatusUpdate);

        btnUnPublish = findViewById(R.id.vibe_details_btnVibeStatusUpdateUnpublish);

        ivCoverPhoto = findViewById(R.id.vibe_details_ivPoster);

        mDescription = findViewById(R.id.vibe_details_tvDescription);

        mCategory = findViewById(R.id.vibe_details_tvCategory);

        mLocation = findViewById(R.id.vibe_details_tvLocation);

        mDate = findViewById(R.id.vibe_details_tvDate);

        mTime = findViewById(R.id.vibe_details_tvTime);

        mTicket = findViewById(R.id.vibe_details_tvTickets);

        mRestrictions = findViewById(R.id.vibe_details_tvRestrictions);

        mPhotos = findViewById(R.id.vibe_details_tvPhotos);

        mVideos = findViewById(R.id.vibe_details_tvVideos);

        mContacts = findViewById(R.id.vibe_details_tvContactInfo);

        mSocialMedia = findViewById(R.id.vibe_details_tvSocialMedia);

        // edit buttons

        setViewListeners(id);

        // get vibe photo count

        FirebaseFirestore photosRef = FirebaseFirestore.getInstance();

        photosRef.collection("photos")
                .document(id)
                .collection("vibePhotos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        uploadedImages = task.getResult().size() + " photos uploaded";

                        mPhotos.setText(uploadedImages);

                    }
                });

    }

    private void updateEventStatus(String id, String status) {

        FirebaseFirestore updateEventRef = FirebaseFirestore.getInstance();

        updateEventRef.collection("vibes")
                .document(id)
                .update("status", status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        if (TextUtils.equals("Active",status)) {

                            btnPublish.setVisibility(View.GONE);

                            btnUnPublish.setVisibility(View.VISIBLE);

                            Toast.makeText(VibeDetailsActivity.this, "Event published successfully",
                                    Toast.LENGTH_SHORT).show();


                        }else {

                            btnUnPublish.setVisibility(View.GONE);

                            btnPublish.setVisibility(View.VISIBLE);

                            Toast.makeText(VibeDetailsActivity.this, "Event un-published " +
                                                                             "successfully",
                                    Toast.LENGTH_SHORT).show();
                        }

                        getVibeDetails(id);



                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(VibeDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void setViewListeners(String id) {

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateEventStatus(id, "Active");
            }
        });


        btnUnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateEventStatus(id, "Not Active");
            }
        });


        ivCoverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isCoverImage= true;

                setCoverPhoto(id);
            }
        });

        ImageView editDescription = findViewById(R.id.vibe_details_btnEditDescription);

        editDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editDescription(id);
            }
        });

        ImageView editCategory = findViewById(R.id.vibe_details_btnEditCategory);

        editCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editCategoryDialog(id);

            }
        });

        ImageView editLocation = findViewById(R.id.vibe_details_btnEditLocation);

        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editLocation(id);

            }
        });

        ImageView editDate = findViewById(R.id.vibe_details_btnEditDate);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setEventDate();

            }
        });


        ImageView editTime = findViewById(R.id.vibe_details_btnEditTime);

        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setEventTime();

            }
        });

        ImageView editTickets = findViewById(R.id.vibe_details_btnEditTickets);

        editTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editTicketsDialog(id);

            }
        });

        ImageView uploadImage = findViewById(R.id.vibe_details_btnUploadImage);

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isCoverImage =false;

                checkPermission();
            }
        });

        ImageView viewImages = findViewById(R.id.vibe_details_btnViewImage);

        viewImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showVibeImages(id);
            }
        });

        ImageView uploadVideo = findViewById(R.id.vibe_details_btnUploadVideo);

        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showEditVibeVideoUrl(id);
            }
        });


        ImageView viewVideo = findViewById(R.id.vibe_details_btnViewVideo);

        viewVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showVibeVideo(id);
            }
        });

        ImageView manageRestrictions = findViewById(R.id.vibe_details_btnManageRestrictions);

        manageRestrictions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setEventRestrictionsDialog(id);

            }
        });


        ImageView manangeContacts = findViewById(R.id.vibe_details_btnContactInfo);

        manangeContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showContactsDialog(id);

            }
        });


        ImageView manageSocialMedia = findViewById(R.id.vibe_details_btnSocialMedia);

        manageSocialMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSocialMediaDialog(id);

            }
        });
    }

    private void setCoverPhoto(String id) {

        isCoverImage = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED) {

                // permission not granted. ask for it
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

                // show pop up
                requestPermissions(permissions, COVER_PERMISSION);

            } else {

                // permission already granted
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(4,3)
                        .setAutoZoomEnabled(true)
                        .setMaxZoom(4)
                        .setMinCropResultSize(450,200)
                        .start(this);
            }

        } else {
            // device less then mashmallow

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(4,3)
                    .setAutoZoomEnabled(true)
                    .setMinCropResultSize(450,200)
                    .start(this);

        }

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

    private void pickImageFromGallery() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(4,3)
                .setMinCropResultSize(450,200)
                .start(this);

    }

    private void editDescription(String id) {

        //show edit dialog box
        editDescriptionDialog(id);

        getVibeDetails(id);
    }

    private void editCategoryDialog(String id) {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_update_vibe_category, viewGroup, false);


        FirebaseFirestore categoriesRef = FirebaseFirestore.getInstance();

        List<String> categoriesList = new ArrayList<>();

        final AutoCompleteTextView actCategories =
                dialogView.findViewById(R.id.dialog_update_vibe_category_actCategory);

        actCategories.setHint("Loading categories....");

        categoriesRef.collection("categories")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {

                            for (QueryDocumentSnapshot doc: queryDocumentSnapshots){

                                categoriesList.add(doc.get("title").toString());


                            }

                            ArrayAdapter<String> categoriesAdapter =
                                    new ArrayAdapter<>(VibeDetailsActivity.this,
                                            android.R.layout.simple_list_item_1,
                                            categoriesList);

                            actCategories.setAdapter(categoriesAdapter);
                            actCategories.setHint("Type category name");

                        }else{
                            actCategories.setHint("No categories found");
                        }

                        getVibeDetails(id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        actCategories.setError(e.getMessage());
                    }
                });


        final Button btnUpdate = dialogView.findViewById(R.id.dialog_update_vibe_category_btnUpdate);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnUpdate.setEnabled(false);

                btnUpdate.setText("Updating category...");


                FirebaseFirestore vibesRef = FirebaseFirestore.getInstance();

                vibesRef.collection("vibes")
                        .document(id)
                        .update("category", actCategories.getText().toString())
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                alertDialog.dismiss();

                                Toast.makeText(VibeDetailsActivity.this, e.getMessage(),
                                        Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                alertDialog.dismiss();

                                // redirect to Vibe Detail Screen

                                Toast.makeText(VibeDetailsActivity.this, "Category updated",
                                        Toast.LENGTH_SHORT).show();

                                getVibeDetails(id);


                            }
                        });

            }

        });

        alertDialog.show();

    }

    private void editDescriptionDialog(String id) {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_update_vibe_description, viewGroup, false);


        final TextInputLayout description =
                dialogView.findViewById(R.id.dialog_edit_vibe_description);

        description.getEditText().setText(mDescription.getText().toString());

        final Button btnUpdate = dialogView.findViewById(R.id.dialog_edit_vibe_btnCreate);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                btnUpdate.setEnabled(false);

                btnUpdate.setText("Updating description...");


                FirebaseFirestore vibesRef = FirebaseFirestore.getInstance();

                vibesRef.collection("vibes")
                        .document(id)
                        .update("description", description.getEditText().getText().toString())
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                alertDialog.dismiss();

                                Toast.makeText(VibeDetailsActivity.this, e.getMessage(),
                                        Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                alertDialog.dismiss();

                                // redirect to Vibe Detail Screen

                                Toast.makeText(VibeDetailsActivity.this, "Description updated",
                                        Toast.LENGTH_SHORT).show();

                                getVibeDetails(id);

                            }
                        });

            }
        });
    }

    private void editLocation(String id) {

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.ADDRESS, Place.Field.LAT_LNG);

        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                                .setCountry("ZA")
                                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

        getVibeDetails(id);

    }

    private void editTicketsDialog(String id) {


        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_update_vibe_tickets, viewGroup, false);


        final Switch type =
                dialogView.findViewById(R.id.dialog_update_vibe_tickets_swTicket);



        CheckBox cbStandard = dialogView.findViewById(R.id.dialog_update_vibe_tickets_cbStandard);

        CheckBox cbEarlyBird = dialogView.findViewById(R.id.dialog_update_vibe_tickets_cbEarlyBird);

        CheckBox cbVIP = dialogView.findViewById(R.id.dialog_update_vibe_tickets_cbVip);

        CheckBox cbGroup = dialogView.findViewById(R.id.dialog_update_vibe_tickets_cbGroup);

        EditText etStandard = dialogView.findViewById(R.id.dialog_update_vibe_tickets_etStandard);

        EditText etEarlyBird = dialogView.findViewById(R.id.dialog_update_vibe_tickets_etEarlyBird);

        EditText etGroup = dialogView.findViewById(R.id.dialog_update_vibe_tickets_etGroup);

        EditText etVIP = dialogView.findViewById(R.id.dialog_update_vibe_tickets_etVip);

        final Button btnUpdate = dialogView.findViewById(R.id.dialog_update_vibe_tickets_btnUpdate);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type.isChecked()) {

                    btnUpdate.setEnabled(false);

                    btnUpdate.setText("Updating tickets...");


                    FirebaseFirestore vibesRef = FirebaseFirestore.getInstance();

                    HashMap<String, Object> ticketsRef = new HashMap<>();

                    ticketsRef.put("type", vibeType);

                    ticketsRef.put("standard", etStandard.getText().toString());

                    ticketsRef.put("earlyBird", etEarlyBird.getText().toString());

                    ticketsRef.put("group", etGroup.getText().toString());

                    ticketsRef.put("vip", etVIP.getText().toString());

                    vibesRef.collection("vibes")
                            .document(id)
                            .update(ticketsRef)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    getVibeDetails(id);

                                    alertDialog.dismiss();

                                    mTicket.setText("Early Bird: R" + etEarlyBird.getText().toString() +
                                                            "\nStandard: R" + etStandard.getText().toString() +
                                                            "\nVIP: R" + etVIP.getText().toString()+ "\nGroup: R" + etGroup.getText().toString());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(VibeDetailsActivity.this, e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                }else {

                    mTicket.setText("Free Event");
                }

            }
        });

        type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    cbStandard.setVisibility(View.VISIBLE);

                    cbEarlyBird.setVisibility(View.VISIBLE);

                    cbVIP.setVisibility(View.VISIBLE);

                    cbGroup.setVisibility(View.VISIBLE);

                    vibeType = "Paid Event";

                }else {

                    cbStandard.setVisibility(View.GONE);

                    cbEarlyBird.setVisibility(View.GONE);

                    cbVIP.setVisibility(View.GONE);

                    cbGroup.setVisibility(View.GONE);

                    vibeType = "Free Event";

                }
            }
        });

        cbStandard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    etStandard.setVisibility(View.VISIBLE);

                    standard = etStandard.getText().toString();


                }else {

                    etStandard.setVisibility(View.GONE);

                    etStandard.setText("");

                    standard = "";

                }

            }
        });

        cbEarlyBird.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    etEarlyBird.setVisibility(View.VISIBLE);

                    earlyBird = etEarlyBird.getText().toString();

                }else {

                    etEarlyBird.setVisibility(View.GONE);

                    etEarlyBird.setText("");

                    earlyBird = "";

                }

            }
        });

        cbVIP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    etVIP.setVisibility(View.VISIBLE);

                    vip = etVIP.getText().toString();

                }else {

                    etVIP.setVisibility(View.GONE);

                    etVIP.setText("");

                    vip = "";


                }

            }
        });

        cbGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    etGroup.setVisibility(View.VISIBLE);


                    group = etGroup.getText().toString();


                }else {

                    etGroup.setVisibility(View.GONE);

                    etGroup.setText("");

                    group = "";

                }

            }
        });

        alertDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR,year);

        c.set(Calendar.MONTH,month);

        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        String date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        mDate.setText(date);

        FirebaseFirestore dateRef = FirebaseFirestore.getInstance();

        dateRef.collection("vibes")
                .document(mVibeLocationId)
                .update("date",date);


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

        mTime.setText(hourOfDay + " : " + minute);

        FirebaseFirestore timeRef = FirebaseFirestore.getInstance();

        timeRef.collection("vibes")
                .document(mVibeLocationId)
                .update("time",hourOfDay + " : " + minute);

    }

    // gt vibe description
    private void getVibeDetails(String id) {

        FirebaseFirestore vibeRef = FirebaseFirestore.getInstance();

        vibeRef.collection("vibes")
                .document(id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {

                            Vibe vibe = documentSnapshot.toObject(Vibe.class);

                            if (vibe !=null) {

                                if (TextUtils.equals(vibe.getStatus(),"Active")) {

                                    btnPublish.setVisibility(View.GONE);

                                    btnUnPublish.setVisibility(View.VISIBLE);
                                }else{

                                    btnPublish.setVisibility(View.VISIBLE);

                                    btnUnPublish.setVisibility(View.GONE);

                                }


                                Glide
                                        .with(VibeDetailsActivity.this)
                                        .load(vibe.getCoverPhotoUrl())
                                        .centerCrop()
                                        .into(ivCoverPhoto);

                                mDescription.setText(vibe.getDescription());

                                mCategory.setText(vibe.getCategory());

                                mLocation.setText(vibe.getLocation());

                                mDate.setText(vibe.getDate());

                                mTime.setText(vibe.getTime());

                                mContacts.setText("Tel: " + vibe.getTelephone() +
                                                         "\nCell: " + vibe.getCellphone() +
                                                          "\nEmail: " + vibe.getEmail());

                                mSocialMedia.setText("Facebook: " + vibe.getFacebook() +
                                                             "\nInstagram: " + vibe.getInstagram() +
                                                             "\nTwitter: " + vibe.getTwitter());

                                if (TextUtils.isEmpty(vibe.getAdmission())) {

                                    mTicket.setText("No tickets set for this event");

                                }else {

                                    mTicket.setText("Early Bird: R" + vibe.getEarlyBird() +
                                                            "\nStandard: R" + vibe.getStandard() +
                                                            "\nVIP: R" +  vibe.getVip()+ "\nGroup" +
                                                            ": R" + vibe.getGroup());

                                }

                                if (TextUtils.isEmpty(vibe.getType())) {

                                    mRestrictions.setText("No Restrictions set for this event");

                                }else {

                                    mRestrictions.setText(vibe.getType() + "\n" +
                                                                 vibe.getAge() + "\n" +
                                                                 vibe.getAlcohol() + "\n" +
                                                                 vibe.getSmoking());

                                }

                                if (vibe.getVideoUrl() !=null) {

                                    mVideos.setText("1 video uploaded");

                                    videoPath = vibe.getVideoUrl();

                                }else{

                                    mVideos.setText("No video uploaded");

                                }

                            }

                        }else{

                            Toast.makeText(VibeDetailsActivity.this, "Couldn't load vibe details " +
                                                                             "at this time",
                                    Toast.LENGTH_LONG).show();

                            finish();
                        }


                    }
                });

    }

    private void uploadImage() {

        ProgressDialog dialog = new ProgressDialog(this);

        dialog.setMessage("Uploading Image...");

        dialog.show();

        final StorageReference fileReference =
                mStorageReference.child(mVibeLocationId + Calendar.getInstance().getTimeInMillis());


        UploadTask uploadTask = fileReference.putFile(mImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                dialog.dismiss();

                Toast.makeText(VibeDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                Log.e(TAG, "onFailure: " +  e.getMessage());

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        updateImageLink(uri);

                        dialog.dismiss();

                        Toast.makeText(VibeDetailsActivity.this, "Image uploaded successfully",
                                Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        dialog.dismiss();

                        Toast.makeText(VibeDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

    }

    private void updateImageLink(Uri uri) {

        FirebaseFirestore imagesRef = FirebaseFirestore.getInstance();

        if (isCoverImage) {

            ivCoverPhoto.setImageURI(mImageUri);

            HashMap<String, Object> image = new HashMap<>();

            image.put("coverPhotoUrl", uri.toString());

            imagesRef.collection("vibes")
                    .document(vibeId)
                    .update(image);


        }else {

            HashMap<String, Object> image = new HashMap<>();

            image.put("downloadLink", uri.toString());

            imagesRef.collection("photos")
                    .document(vibeId)
                    .collection("vibePhotos")
                    .add(image);

        }


    }

    private void showVibeImages(String id) {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_vibe_images, viewGroup, false);


        final RecyclerView recyclerView =
                dialogView.findViewById(R.id.dialog_vibe_images_recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ProgressBar progressBar = dialogView.findViewById(R.id.dialog_vibe_images_progressBar);

        TextView textView = dialogView.findViewById(R.id.dialog_vibe_images_textView);


        final ImageView btnClose = dialogView.findViewById(R.id.dialog_vibe_images_btnClose);

        FirebaseFirestore imagesRef = FirebaseFirestore.getInstance();

        imagesRef.collection("photos")
                .document(mVibeLocationId)
                .collection("vibePhotos")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (queryDocumentSnapshots !=null && !queryDocumentSnapshots.isEmpty()) {

                            List<Image> images = new ArrayList<>();

                            for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {

                                Image image = doc.toObject(Image.class);

                                images.add(image);
                            }

                            ImagesAdapter adapter = new ImagesAdapter(images,
                                    getApplicationContext());

                            recyclerView.setAdapter(adapter);

                            adapter.notifyDataSetChanged();

                            progressBar.setVisibility(View.GONE);

                            recyclerView.setVisibility(View.VISIBLE);

                            textView.setVisibility(View.GONE);


                        }else {

                            progressBar.setVisibility(View.GONE);

                            recyclerView.setVisibility(View.GONE);

                            textView.setText("No images uploaded");

                            textView.setVisibility(View.VISIBLE);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressBar.setVisibility(View.GONE);

                        recyclerView.setVisibility(View.GONE);

                        textView.setText(e.getMessage());

                        textView.setVisibility(View.VISIBLE);
                    }
                });


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        alertDialog.show();

    }

    private void showVibeVideo(String id) {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_vibe_video, viewGroup, false);

        WebView webView = dialogView.findViewById(R.id.dialog_vibe_video_wvVideo);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

        webView.setWebChromeClient(new WebChromeClient());

        webView.loadUrl(videoPath);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();


        alertDialog.show();


    }

    private void showEditVibeVideoUrl(String id) {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_video_link, viewGroup, false);

        TextInputLayout etLink = dialogView.findViewById(R.id.dialog_edit_video_link);

        // check button

        Button btnUpdateLink = dialogView.findViewById(R.id.dialog_edit_vibe_btnUpdate);

        // perform on click listener

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();

        btnUpdateLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(etLink.getEditText().getText())) {

                    etLink.getEditText().setError("Link is required!");

                    return;

                }

                FirebaseFirestore updateLinkRef = FirebaseFirestore.getInstance();

                updateLinkRef.collection("vibes")
                        .document(id)
                        .update("videoUrl", etLink.getEditText().getText().toString().trim())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                alertDialog.dismiss();

                                Toast.makeText(VibeDetailsActivity.this, "Link updated",
                                        Toast.LENGTH_SHORT).show();


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                alertDialog.dismiss();

                                Toast.makeText(VibeDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        alertDialog.show();


    }

    private void setEventRestrictionsDialog(String id) {

        FirebaseFirestore restrictionsRef = FirebaseFirestore.getInstance();


        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_vibe_restrictions, viewGroup, false);


        SwitchCompat swRestriction =
                dialogView.findViewById(R.id.dialog_vibe_restrictions_swRestricted);

        CheckBox type= dialogView.findViewById(R.id.dialog_vibe_restrictions_cbType);

        CheckBox age = dialogView.findViewById(R.id.dialog_vibe_restrictions_cbAge);

        CheckBox alcohol = dialogView.findViewById(R.id.dialog_vibe_restrictions_cbAlcohol);

        CheckBox smoking = dialogView.findViewById(R.id.dialog_vibe_restrictions_cbSmoking);

        EditText minAge = dialogView.findViewById(R.id.dialog_vibe_restrictions_etAge);

        swRestriction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    type.setVisibility(View.VISIBLE);

                    age.setVisibility(View.VISIBLE);

                    alcohol.setVisibility(View.VISIBLE);

                    smoking.setVisibility(View.VISIBLE);

                } else {

                    type.setVisibility(View.GONE);

                    age.setVisibility(View.GONE);

                    alcohol.setVisibility(View.GONE);

                    smoking.setVisibility(View.GONE);
                }

            }
        });


        age.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                   minAge.setVisibility(View.VISIBLE);
                } else {
                    // hide age box

                    minAge.setVisibility(View.GONE);
                }
            }
        });

        minAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                restrictionsRef.collection("vibes")
                        .document(id)
                        .update("age","No under " + s + " allowed");

            }
        });

        alcohol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    restrictionsRef.collection("vibes")
                            .document(id)
                            .update("alcohol","Alcohol Allowed");
                } else {
                    restrictionsRef.collection("vibes")
                            .document(id)
                            .update("alcohol","Alcohol Not Allowed");
                }
            }
        });


        smoking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    restrictionsRef.collection("vibes")
                            .document(id)
                            .update("smoking","Smoking Allowed");
                } else {
                    restrictionsRef.collection("vibes")
                            .document(id)
                            .update("smoking","Smoking Not Allowed");
                }
            }
        });


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();

        alertDialog.show();

    }

    private void showSocialMediaDialog(String id) {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_social_media, viewGroup, false);

        EditText facebook = dialogView.findViewById(R.id.dialog_social_media_facebook);

        EditText instagram = dialogView.findViewById(R.id.dialog_social_media_instagram);

        EditText twitter = dialogView.findViewById(R.id.dialog_social_media_twitter);

        // check button

        Button btnUpdateLink = dialogView.findViewById(R.id.dialog_social_media_btnUpdate);

        // perform on click listener

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();

        btnUpdateLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> socialLinks = new HashMap<>();

                socialLinks.put("facebook", facebook.getText().toString().trim());

                socialLinks.put("instagram", instagram.getText().toString().trim());

                socialLinks.put("twitter", twitter.getText().toString().trim());

                FirebaseFirestore socialMediaUpdate = FirebaseFirestore.getInstance();

                socialMediaUpdate.collection("vibes")
                        .document(id)
                        .update(socialLinks)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                alertDialog.dismiss();

                                Toast.makeText(VibeDetailsActivity.this, "Links updated",
                                        Toast.LENGTH_SHORT).show();


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                alertDialog.dismiss();

                                Toast.makeText(VibeDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        alertDialog.show();


    }

    private void showContactsDialog(String id) {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_manage_contacts, viewGroup, false);

        EditText tel = dialogView.findViewById(R.id.dialog_manage_contacts_tel);

        EditText cel = dialogView.findViewById(R.id.dialog_manage_contacts_cell);

        EditText email = dialogView.findViewById(R.id.dialog_manage_contacts_email);

        // check button

        Button btnUpdateLink = dialogView.findViewById(R.id.dialog_social_media_btnUpdate);

        // perform on click listener

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();

        btnUpdateLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> eventsContacts = new HashMap<>();

                eventsContacts.put("telephone", tel.getText().toString().trim());

                eventsContacts.put("cellphone", cel.getText().toString().trim());

                eventsContacts.put("email", email.getText().toString().trim());

                FirebaseFirestore contactsUpdateRef = FirebaseFirestore.getInstance();

                contactsUpdateRef.collection("vibes")
                        .document(id)
                        .update(eventsContacts)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                alertDialog.dismiss();

                                Toast.makeText(VibeDetailsActivity.this, "Contact information " +
                                                                                 "updated",
                                        Toast.LENGTH_SHORT).show();

                                getVibeDetails(id);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                alertDialog.dismiss();

                                Toast.makeText(VibeDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        alertDialog.show();

    }
}
