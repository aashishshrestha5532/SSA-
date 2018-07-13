package com.alchemist.ssa.EventStuffs;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alchemist.ssa.R;

import java.util.List;

/**
 * Created by Ashish Alton on 7/5/2018.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<EventModel> eventModels;
    private Context context;
    public EventAdapter(Context context,List<EventModel> eventModels){
        this.context=context;
        this.eventModels=eventModels;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.singlecardevent,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
         holder.event_title.setText(eventModels.get(position).getEvent_title());
         holder.event_description.setText(eventModels.get(position).getEvent_description());
         holder.event_date.setText(eventModels.get(position).getEvent_date());
         holder.event_image.setImageBitmap(eventModels.get(position).getEvent_image());
    }

    @Override
    public int getItemCount() {
        return eventModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         private TextView event_date,event_title,event_description;
         private ImageView event_image;
        public ViewHolder(View itemView) {
            super(itemView);
            event_date=itemView.findViewById(R.id.event_date);
            event_title=itemView.findViewById(R.id.event_title);
            event_description=itemView.findViewById(R.id.event_description);
            event_image=itemView.findViewById(R.id.event_image);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,EventDetail.class);
                    intent.putExtra("event_title",eventModels.get(getAdapterPosition()).getEvent_title());
                    context.startActivity(intent);

                }
            });


        }
    }
}
