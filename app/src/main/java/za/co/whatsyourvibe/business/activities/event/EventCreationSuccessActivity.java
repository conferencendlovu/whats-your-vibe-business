package za.co.whatsyourvibe.business.activities.event;

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

public class EventCreationSuccessActivity extends AppCompatActivity {


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation_success);

        TextView tvHeading = findViewById(R.id.event_creation_success_tvCongratulations);
        Typeface congratulations = Typeface.createFromAsset(this.getAssets(), "fonts/fancy.ttf");
        tvHeading.setTypeface(congratulations);

        TextView tvInstruction = findViewById(R.id.event_creation_success_tvInstruction);

        Button btnMyEvents = findViewById(R.id.event_creation_success_btnMyEvents);
        Button btnNewEvent = findViewById(R.id.event_creation_success_btnCreateAnotherEvent);
        Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/regular.ttf");
        btnMyEvents.setTypeface(font);
        btnNewEvent.setTypeface(font);
        tvInstruction.setTypeface(font);

        btnMyEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventCreationSuccessActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

//        btnNewEvent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(EventCreationSuccessActivity.this, CreateEventActivity.class);
//                startActivity(i);
//                finish();
//            }
//        });
    }
}
