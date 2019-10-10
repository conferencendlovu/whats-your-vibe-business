package za.co.whatsyourvibe.business.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import za.co.whatsyourvibe.business.R;
import za.co.whatsyourvibe.business.activities.event.EventCategory;
import za.co.whatsyourvibe.business.activities.event.EventDetailsActivity;
import za.co.whatsyourvibe.business.models.Category;
import za.co.whatsyourvibe.business.models.MyEvent;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {
    private List<Category> categories;
    private Context context;


    public CategoriesAdapter(List<Category> categories, Context context) {
        this.context = context;
        this.categories = categories;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category, viewGroup,
                false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        myViewHolder.title.setText(categories.get(position).getTitle().toUpperCase());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, EventDetailsActivity.class);
                context.startActivity(i);

                EventCategory.myEvent.setCategory(categories.get(position).getTitle().toUpperCase().trim());

            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.item_category_category_title);


        }
    }
}