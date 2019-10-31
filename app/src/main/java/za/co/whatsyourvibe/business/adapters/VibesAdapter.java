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

import za.co.whatsyourvibe.business.R;
import za.co.whatsyourvibe.business.activities.vibe.VibeDetailsActivity;
import za.co.whatsyourvibe.business.models.Image;
import za.co.whatsyourvibe.business.models.Vibe;

public class VibesAdapter extends RecyclerView.Adapter<VibesAdapter.MyViewHolder>{
    private List<Vibe> vibeList;
    private Context context;

    public VibesAdapter(List<Vibe> vibeList, Context context){
        this.context = context;
        this.vibeList = vibeList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item_vibe,viewGroup,
                false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        Glide
                .with(context)
                .load(vibeList.get(position).getCoverPhotoUrl())
                .centerCrop()
                .into(myViewHolder.cover);

        myViewHolder.title.setText(vibeList.get(position).getTitle());

        myViewHolder.description.setText(vibeList.get(position).getDescription());

        myViewHolder.category.setText(vibeList.get(position).getCategory());

        myViewHolder.status.setText(vibeList.get(position).getStatus());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, VibeDetailsActivity.class);


                intent.putExtra("VIBE_ID", vibeList.get(position).getId());

                intent.putExtra("VIBE_TITLE", vibeList.get(position).getTitle());

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return vibeList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, description, category, status;

        ImageView cover;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.row_item_vibe_tvTitle);

            description = itemView.findViewById(R.id.row_item_vibe_tvDescription);

            category = itemView.findViewById(R.id.row_item_vibe_tvCategory);

            status = itemView.findViewById(R.id.row_item_vibe_tvStatus);

            cover = itemView.findViewById(R.id.row_item_vibe_ivImage);

        }
    }
}

