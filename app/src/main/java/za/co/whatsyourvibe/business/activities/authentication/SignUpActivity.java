package za.co.whatsyourvibe.business.activities.authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import za.co.whatsyourvibe.business.MainActivity;
import za.co.whatsyourvibe.business.R;
import za.co.whatsyourvibe.business.activities.vibe.VibesActivity;
import za.co.whatsyourvibe.business.models.Business;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout mTilBusinessName, mTilBusinessEmail, mTilPassword, mTilConfirmPassword;
    private ProgressDialog mProgressDialog;
    private String uid;

    private Business mNewBusiness;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        uid = mAuth.getUid();

        ImageView btnClose = findViewById(R.id.sign_up_ivClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnCreateAccount = findViewById(R.id.sign_up_btnCreateAccount);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInputFields();
            }
        });


        mNewBusiness = new Business();

        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait while we create your account...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);


        TextView tvHeading = findViewById(R.id.sign_up_tvGetOnBoardHeading);
        Typeface heading = Typeface.createFromAsset(this.getAssets(), "fonts/fancy.ttf");
        tvHeading.setTypeface(heading);

        initViews();


    }

    private void checkInputFields() {

        if (TextUtils.isEmpty(mTilBusinessName.getEditText().getText())) {

            mTilBusinessName.getEditText().setError("Business name is required!");

            mTilBusinessName.getEditText().requestFocus();

            return;
        }

        if (mTilBusinessName.getEditText().getText().length() < 5) {

            mTilBusinessName.getEditText().setError("Business name is too short!");

            mTilBusinessName.getEditText().requestFocus();

            return;
        }

        if (TextUtils.isEmpty(mTilBusinessEmail.getEditText().getText())) {

            mTilBusinessEmail.getEditText().setError("Business Email Address is required!");

            mTilBusinessEmail.getEditText().requestFocus();

            return;
        }

        if (!Patterns.EMAIL_ADDRESS
                    .matcher(mTilBusinessEmail.getEditText().getText().toString().trim())
                    .matches()) {
            mTilBusinessEmail.getEditText().setError("Please enter valid email address!");

            mTilBusinessEmail.getEditText().requestFocus();
        }


        if (mTilBusinessEmail.getEditText().getText().length() < 5) {

            mTilBusinessEmail.getEditText().setError("email address is too short!");

            mTilBusinessEmail.getEditText().requestFocus();

            return;
        }

        if (!TextUtils.equals(mTilPassword.getEditText().getText().toString().trim(),
                mTilConfirmPassword.getEditText().getText().toString().trim())) {

            mTilPassword.getEditText().setError("Password do no match!");

            mTilPassword.getEditText().requestFocus();

            return;
        }

        createAccount();
    }

    private void createAccount() {

        mProgressDialog.show();

        mAuth.createUserWithEmailAndPassword(mTilBusinessEmail.getEditText().getText().toString().trim(),
                mTilPassword.getEditText().getText().toString().trim())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        if (authResult !=null){
                            updateFirebaseProfile();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        mProgressDialog.dismiss();

                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void updateFirebaseProfile() {

        FirebaseUser user = mAuth.getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                          .setDisplayName(mTilBusinessName.getEditText().getText().toString())
                                                          .build();

        user.updateProfile(profileUpdates);
        mProgressDialog.dismiss();

        FirebaseFirestore businessProfileRef = FirebaseFirestore.getInstance();

        mNewBusiness.setBenefits("Post picture about event, specials and 100 character " +
                                         "description, valid for 24 hours with 50KM radius " +
                                         "exposure");

        mNewBusiness.setBusinessEmail(mTilBusinessEmail.getEditText().getText().toString());

        mNewBusiness.setBusinessName(mTilBusinessName.getEditText().getText().toString());

        mNewBusiness.setBusinessId(user.getUid());

        mNewBusiness.setMembershipType("Bronze Vibe");


        businessProfileRef.collection("businessUsers")
                .document(user.getUid())
                .set(mNewBusiness)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SignUpActivity.this, "Account created successfully",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SignUpActivity.this, VibesActivity.class);

                        startActivity(intent);

                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        finish();
                    }
                });



    }

    private void initViews() {

        mTilBusinessName = findViewById(R.id.sign_up_tilBusinessName) ;

        mTilBusinessEmail = findViewById(R.id.sign_up_tilBusinessEmail);

        mTilPassword = findViewById(R.id.sign_up_tilPassword);

        mTilConfirmPassword = findViewById(R.id.sign_up_tilConfirmPassword);


    }


}
