package za.co.whatsyourvibe.business.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import za.co.whatsyourvibe.business.R;
import za.co.whatsyourvibe.business.activities.profile.MembershipActivity;
import za.co.whatsyourvibe.business.models.Image;

public class ImagesAdapter  extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder> {
    private List<Image> imageList;
    private Context context;


    public ImagesAdapter(List<Image> imageList, Context context) {
        this.context = context;
        this.imageList = imageList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item_image, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        Glide
                .with(context)
                .load(imageList.get(position).getDownloadLink())
                .centerCrop()
                .placeholder(R.drawable.spinner)
                .into(myViewHolder.link);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView link, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            link = itemView.findViewById(R.id.row_item_image_ivImage);


        }
    }
}