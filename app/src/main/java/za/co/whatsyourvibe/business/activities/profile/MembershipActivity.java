package za.co.whatsyourvibe.business.activities.profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import za.co.whatsyourvibe.business.R;
import za.co.whatsyourvibe.business.models.Business;

public class MembershipActivity extends AppCompatActivity {

    private static final String TAG = "MembershipActivity";

    private static final int RC_IMAGE_PICKER = 200;

    private static final int RC_PERMISSION = 200;

    private TextInputLayout mTilBusinessName, mTilBusinessLocation, mTilEmailAddress, mTilMembership;

    private Button mBtnChangeMembership;

    private FloatingActionButton fabEdit, fabSave;

    private FirebaseAuth auth;

    private String displayName, businessId;

    private ProgressDialog progressDialog;

    private FirebaseFirestore profileRef;

    private CircleImageView ivBusinessLogo;

    private StorageReference mStorageReference;

    private String currentUserId, imageLink;

    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_membership);

        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Please wait...");

        profileRef = FirebaseFirestore.getInstance();

        progressDialog.setCanceledOnTouchOutside(false);

       if (auth !=null) {

           displayName = auth.getCurrentUser().getDisplayName();

           businessId = auth.getUid();
       }

        mStorageReference = FirebaseStorage.getInstance().getReference("profile_photos");

        Toolbar toolbar = findViewById(R.id.membership_toolbar);

       setSupportActionBar(toolbar);

       getSupportActionBar().setTitle(displayName + " - Membership");

       getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        initViews();

    }

    private void initViews() {

        ivBusinessLogo = findViewById(R.id.membership_ivLogo);

        mTilBusinessName = findViewById(R.id.membership_tilBusinessName);

        mTilEmailAddress = findViewById(R.id.membership_tilEmail);

        mTilMembership = findViewById(R.id.membership_tilMembership);

        mBtnChangeMembership = findViewById(R.id.membership_btnChangeMembership);

        mTilBusinessLocation = findViewById(R.id.membership_tilBusinessLocation);

        if (businessId!=null){

            loadMembershipInfo(businessId);
        }

        fabEdit = findViewById(R.id.membership_fabEdit);

        fabSave = findViewById(R.id.membership_fabSave);

        setListeners();
    }

    private void setListeners(){

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTilBusinessName.setEnabled(true);

                mTilBusinessLocation.setEnabled(true);

                mTilBusinessName.requestFocus();

                fabEdit.hide();

                fabSave.show();

            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveProfileChanges();

            }
        });

        ivBusinessLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkPermission();

            }
        });

    }

    private void saveProfileChanges() {

        if (TextUtils.isEmpty(mTilBusinessName.getEditText().getText())) {

            mTilBusinessName.setError("Business Name is required!");

            mTilBusinessName.requestFocus();

            return;

        }

        if (TextUtils.isEmpty(mTilBusinessLocation.getEditText().getText())) {

            mTilBusinessLocation.setError("Business Address Is Required!");

            mTilBusinessLocation.requestFocus();

            return;

        }
        Map<String,Object> profileUpdates = new HashMap<>();

        profileUpdates.put("businessName",mTilBusinessName.getEditText().getText().toString() );
        profileUpdates.put("businessAddress",mTilBusinessLocation.getEditText().getText().toString() );

        progressDialog.show();

        profileRef.collection("businessUsers")
                .document(businessId)
                .update(profileUpdates)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();

                        Toast.makeText(MembershipActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();

                        Toast.makeText(MembershipActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();

                        mTilBusinessLocation.setEnabled(false);

                        mTilBusinessName.setEnabled(false);

                        fabEdit.show();

                        fabSave.hide();
                    }
                });
    }

    private void loadMembershipInfo(final String businessId) {

        progressDialog.show();

        profileRef.collection("businessUsers")
                .document(businessId)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();

                        Toast.makeText(MembershipActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        finish();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {



                        if (documentSnapshot.exists()) {

                            Business business = documentSnapshot.toObject(Business.class);

                            if (business!=null) {
                                mTilBusinessName.getEditText().setText(business.getBusinessName());

                                mTilEmailAddress.getEditText().setText(business.getBusinessEmail());

                                mTilMembership.getEditText().setText(business.getMembershipType());

                                if (business.getBusinessLogo() !=null) {

                                    Picasso.get()
                                            .load(business.getBusinessLogo())
                                            .resize(150, 150)
                                            .centerCrop()
                                            .into(ivBusinessLogo);
                                }

                                progressDialog.dismiss();
                            }

                        }else {

                            Toast.makeText(MembershipActivity.this, "Error occurred while loading" +
                                                                            " profile. Please try" +
                                                                            " again",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }


    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {

                // permission not granted. ask for it
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

                // show pop up
                requestPermissions(permissions,RC_PERMISSION);

            }else {

                // permission already granted
                pickImageFromGallery();
            }

        }else{
            // device less then mashmallow

            pickImageFromGallery();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case  RC_PERMISSION :

                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    pickImageFromGallery();

                }else{

                    // permission denied
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void pickImageFromGallery() {

        // intent to pick image
        Intent intent  =   new Intent(Intent.ACTION_PICK);

        intent.setType("image/*");

        startActivityForResult(intent, RC_IMAGE_PICKER);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == RC_IMAGE_PICKER) {

            ivBusinessLogo.setImageURI(data.getData());

            mImageUri = data.getData();

            progressDialog.show();

            uploadImage();

        }
    }

    private void uploadImage() {

        final StorageReference fileReference =
                mStorageReference.child(businessId);

        UploadTask uploadTask = fileReference.putFile(mImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(MembershipActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        imageLink = uri.toString();

                        progressDialog.dismiss();

                        updateBusinessLogo();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();

                        Toast.makeText(MembershipActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

    }

    private void updateBusinessLogo() {

        progressDialog.show();

        profileRef.collection("businessUsers")
                .document(businessId)
                .update("businessLogo",imageLink)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();

                        Toast.makeText(MembershipActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();

                        Toast.makeText(MembershipActivity.this, "Logo updated successfully", Toast.LENGTH_SHORT).show();

                    }
                });
    }

}
