package za.co.whatsyourvibe.business.activities.event;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import za.co.whatsyourvibe.business.R;
import za.co.whatsyourvibe.business.adapters.CategoriesAdapter;
import za.co.whatsyourvibe.business.models.Category;
import za.co.whatsyourvibe.business.models.MyEvent;

public class EventCategory extends AppCompatActivity {

    public static MyEvent myEvent;

    private RecyclerView recyclerView;

    private CategoriesAdapter adapter;

    private TextView textView;

    private ProgressBar progressBar;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_category);

        Toolbar toolbar = findViewById(R.id.event_category_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Create an Event");

        recyclerView = findViewById(R.id.event_category_recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        textView = findViewById(R.id.event_category_textView);

        progressBar = findViewById(R.id.event_category_progressBar);

        recyclerView.setHasFixedSize(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getCategories();


        myEvent = new MyEvent();

    }

    private void getCategories() {

        FirebaseFirestore categoriesRef = FirebaseFirestore.getInstance();

        categoriesRef.collection("categories")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (queryDocumentSnapshots !=null && queryDocumentSnapshots.size() > 0) {

                            List<Category> categoryList = new ArrayList<>();

                            for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {

                                Category category = doc.toObject(Category.class);

                                categoryList.add(category);
                            }

                            adapter = new CategoriesAdapter(categoryList,getApplicationContext());

                            recyclerView.setAdapter(adapter);

                            adapter.notifyDataSetChanged();

                            progressBar.setVisibility(View.GONE);

                            textView.setVisibility(View.GONE);

                            recyclerView.setVisibility(View.VISIBLE);

                        }else {

                            progressBar.setVisibility(View.GONE);

                            textView.setVisibility(View.VISIBLE);

                            textView.setText("No Categories Available");

                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);

                        textView.setVisibility(View.VISIBLE);

                        textView.setText(e.getMessage());

                        recyclerView.setVisibility(View.GONE);
                    }
                });
    }

}
