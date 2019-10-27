package za.co.whatsyourvibe.business.activities.vibe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import za.co.whatsyourvibe.business.R;
import za.co.whatsyourvibe.business.models.Vibe;

public class VibeDetailsActivity extends AppCompatActivity {

    private static final String TAG = "VibeDetailsActivity";

    private TextView mDescription, mCategory, mLocation, mDate, mTime;

    private TextView mTicket;

    private TextView mRestrictions;

    private TextView mPhotos, mVideos;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        String vibeId = getIntent().getStringExtra("VIBE_ID");

        String vibeTitle = getIntent().getStringExtra("VIBE_TITLE");

        setContentView(R.layout.activity_vibe_details);

        Toolbar toolbar = findViewById(R.id.vibes_details_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button addButton = findViewById(R.id.toolbar_createVibe);

        addButton.setVisibility(View.GONE);

        initViews();

        if (vibeId !=null) {

            getVibeDetails(vibeId);

            getSupportActionBar().setTitle(vibeTitle);

        }else {

            Toast.makeText(VibeDetailsActivity.this, "Couldn't load vibe details " +
                                                             "at this time",
                    Toast.LENGTH_LONG).show();

            finish();

        }
    }

    private void initViews() {

        mDescription = findViewById(R.id.vibe_details_tvDescription);

        mCategory = findViewById(R.id.vibe_details_tvCategory);

        mLocation = findViewById(R.id.vibe_details_tvLocation);

        mDate = findViewById(R.id.vibe_details_tvDate);

        mTime = findViewById(R.id.vibe_details_tvTime);

        mTicket = findViewById(R.id.vibe_details_tvTickets);

        mRestrictions = findViewById(R.id.vibe_details_tvRestrictions);

        mPhotos = findViewById(R.id.vibe_details_tvPhotos);

        mVideos = findViewById(R.id.vibe_details_tvVideos);

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

                                mDescription.setText(vibe.getDescription());

                                mCategory.setText(vibe.getCategory());

                                mLocation.setText(vibe.getLocation());

                                mDate.setText(vibe.getDate());

                                mTime.setText(vibe.getTime());

                                mTicket.setText("No tickets available for this event");

                                mRestrictions.setText("Unrestricted vibe");

                                mPhotos.setText("No photos uploaded");

                                mVideos.setText("No video uploaded");

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
}
