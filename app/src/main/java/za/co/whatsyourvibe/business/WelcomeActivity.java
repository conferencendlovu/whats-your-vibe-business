package za.co.whatsyourvibe.business;

import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import za.co.whatsyourvibe.business.activities.authentication.SignInActivity;
import za.co.whatsyourvibe.business.activities.authentication.SignUpActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        TextView tvWelcome = findViewById(R.id.welcome_tvWelcome);
        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/fancy.ttf");
        Typeface buttonsFont = Typeface.createFromAsset(this.getAssets(), "fonts/regular.ttf");

        tvWelcome.setTypeface(typeface);

        Button btnSignIn = findViewById(R.id.welcome_btnSignIn);
        Button btnSignUp = findViewById(R.id.welcome_btnSignUp);

        btnSignIn.setTypeface(buttonsFont);
        btnSignUp.setTypeface(buttonsFont);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeActivity.this, SignInActivity.class);
                startActivity(i);
            }
        });
    }
}
