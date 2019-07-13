package za.co.whatsyourvibe.business.activities.authentication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import za.co.whatsyourvibe.business.MainActivity;
import za.co.whatsyourvibe.business.R;

public class AccountCreationSuccessActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation_success);

        TextView tvHeading = findViewById(R.id.account_creation_success_tvCongratulations);
        Typeface congratulations = Typeface.createFromAsset(this.getAssets(), "fonts/fancy.ttf");
        tvHeading.setTypeface(congratulations);

        TextView tvInstruction = findViewById(R.id.account_creation_success_tvInstruction);

        Button btnSignIn = findViewById(R.id.account_creation_success_btnLogin);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AccountCreationSuccessActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
