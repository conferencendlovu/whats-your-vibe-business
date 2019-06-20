package za.co.whatsyourvibe.business.activities.profile;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import za.co.whatsyourvibe.business.R;
import za.co.whatsyourvibe.business.models.Business;

import static android.graphics.Typeface.createFromAsset;

public class MembershipActivity extends AppCompatActivity {

    private TextInputLayout mTilBusinessName, mTilEmailAddress, mTilMembership;
    private Button mBtnChangeMembership;
    private TextView mTvHeading, mTvDescription;
    private FirebaseAuth auth;
    private String displayName, businessId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);

        auth = FirebaseAuth.getInstance();

       if (auth !=null) {
           displayName = auth.getCurrentUser().getDisplayName();
           businessId = auth.getUid();
       }


        Toolbar toolbar = findViewById(R.id.membership_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(displayName + " - Membership");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        initViews();

    }

    private void initViews() {

        Typeface heading = createFromAsset(this.getAssets(), "fonts/fancy.ttf");

        mTvHeading = findViewById(R.id.membership_tvHeading);

        mTvHeading.setTypeface(heading);

        mTilBusinessName = findViewById(R.id.membership_tilBusinessName);

        mTilEmailAddress = findViewById(R.id.membership_tilEmail);

        mTilMembership = findViewById(R.id.membership_tilMembership);

        mBtnChangeMembership = findViewById(R.id.membership_btnChangeMembership);

        mTvDescription = findViewById(R.id.membership_tvDescription);


        if (businessId!=null){
            loadMembershipInfo(businessId);
        }
    }

    private void loadMembershipInfo(final String businessId) {
        FirebaseFirestore profileRef = FirebaseFirestore.getInstance();
        final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Please wait...");

        progressDialog.setCanceledOnTouchOutside(false);

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

                                mTvDescription.setText(business.getBenefits());

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
}
