package za.co.whatsyourvibe.business.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import za.co.whatsyourvibe.business.R;
import za.co.whatsyourvibe.business.activities.event.EventOverviewActivity;
import za.co.whatsyourvibe.business.models.Event;
import za.co.whatsyourvibe.business.models.MyEvent;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder>{
    private List<MyEvent> eventsList;
    private Context context;

    public EventsAdapter(List<MyEvent> eventsList, Context context){
        this.context = context;
        this.eventsList = eventsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_event_row,viewGroup,
                false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        myViewHolder.event.setText(eventsList.get(position).getName());
        myViewHolder.date.setText(eventsList.get(position).getDate());
        myViewHolder.shares.setText(eventsList.get(position).getShares() + " shares");
        myViewHolder.going.setText(eventsList.get(position).getGoing() + " going");
        myViewHolder.description.setText(eventsList.get(position).getDescription());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventOverviewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("EVENT", eventsList.get(position));

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView event, date, shares ,going , description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            event = itemView.findViewById(R.id.item_event_tvEventName);
            date = itemView.findViewById(R.id.item_event_tvEventDate);
            shares = itemView.findViewById(R.id.item_event_tvEventShares);
            going = itemView.findViewById(R.id.item_event_tvEventGoing);
            description = itemView.findViewById(R.id.item_event_tvEventDescription);

        }
    }
}