package za.co.whatsyourvibe.business.activities.authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import za.co.whatsyourvibe.business.MainActivity;
import za.co.whatsyourvibe.business.R;

public class SignInActivity extends AppCompatActivity {

    private TextView instruction;
    private FirebaseAuth auth;
    private LinearLayout resetPasswordLayout, loginLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        auth = FirebaseAuth.getInstance();

        initViews();

        ImageView btnClose = findViewById(R.id.sign_in_ivClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initViews() {

        resetPasswordLayout = findViewById(R.id.sign_in_layoutResetPassword);
        loginLayout = findViewById(R.id.sign_in_llLoginLayout);

        final TextInputLayout mTilEmailAddress = findViewById(R.id.sign_in_tilEmail);

        final TextInputLayout mTilPassword = findViewById(R.id.sign_in_tilPassword);

        instruction = findViewById(R.id.sign_in_tvInstruction);

        TextView tvCreateAccount = findViewById(R.id.sign_in_tvCreateAccount);
        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);

                startActivity(i);

                finish();
            }
        });

        TextView tvForgotPassword = findViewById(R.id.sign_in_tvForgotPassword);

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });

        Button mBtnLogin = findViewById(R.id.sign_in_btnLogin);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(mTilEmailAddress.getEditText().getText())){
                    mTilEmailAddress.setError("Email address is required!");
                    mTilEmailAddress.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(mTilPassword.getEditText().getText())){
                    mTilPassword.setError("Password is required!");
                    mTilPassword.requestFocus();
                    return;
                }
                String email = mTilEmailAddress.getEditText().getText().toString().trim();
                String password = mTilPassword.getEditText().getText().toString().trim();

                signInUser(email,password);

            }
        });
    }

    private void forgotPassword() {

        resetPasswordLayout.setVisibility(View.VISIBLE);
        loginLayout.setVisibility(View.GONE);

        final EditText emailAddress = findViewById(R.id.sign_in_etEmailAddress);

        Button btnReset = findViewById(R.id.sign_in_btnReset);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(emailAddress.getText())){
                    emailAddress.setError("Field cannot be empty!");
                    emailAddress.requestFocus();

                }else{
                    emailResetLink(emailAddress.getText().toString().trim());
                }

            }
        });



    }

    private void emailResetLink(String emailAddress) {

        final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Please wait...");

        progressDialog.setCanceledOnTouchOutside(false);

        progressDialog.show();

        final TextView message = findViewById(R.id.sign_in_tvMessage);
        message.setTextColor(Color.RED);

        auth.sendPasswordResetEmail(emailAddress)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();

                        message.setText(e.getMessage());
                    }
                })

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        progressDialog.dismiss();

                        Toast.makeText(SignInActivity.this, "Reset email address sent",
                                Toast.LENGTH_SHORT).show();

                        finish();
                    }
                });

    }

    private void signInUser(String email, String password) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Authenticating, please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    SharedPreferences sharedPreferences = getSharedPreferences("WYV", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("uid", task.getResult().getUser().getUid());
                    editor.putString("displayName", task.getResult().getUser().getDisplayName());

                    editor.apply();

                    Intent i = new Intent(SignInActivity.this, MainActivity.class);
                    progressDialog.dismiss();
                    startActivity(i);
                    finish();
                }else{
                    progressDialog.dismiss();
                    instruction.setTextColor(Color.RED);
                    instruction.setText(task.getException().getMessage());
                }

            }
        });


    }
}
