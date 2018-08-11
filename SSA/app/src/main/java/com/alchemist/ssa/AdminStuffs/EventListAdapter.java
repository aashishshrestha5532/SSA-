package com.alchemist.ssa.AdminStuffs;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alchemist.ssa.LoginStuffs.AddEvent;
import com.alchemist.ssa.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashish Alton on 8/11/2018.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    List<EventModelData> eventModelDataList=new ArrayList<>();
    private Context context;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.admineventcard,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.event_title.setText(eventModelDataList.get(position).getEventName());
        holder.event_description.setText(eventModelDataList.get(position).getEventDesc());
        holder.event_date.setText(eventModelDataList.get(position).getEventDate());
        holder.event_image.setImageBitmap(eventModelDataList.get(position).getEvent_image());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,AddEvent.class);
                intent.putExtra("event_id",eventModelDataList.get(position).getEvent_id());
                intent.putExtra("event_name",eventModelDataList.get(position).getEventName());
                intent.putExtra("event_date",eventModelDataList.get(position).getEventDate());
                intent.putExtra("event_desc",eventModelDataList.get(position).getEventDesc());
                intent.putExtra("event_type",eventModelDataList.get(position).getEventType());
                intent.putExtra("event_time",eventModelDataList.get(position).getEventTime());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventModelDataList.size();
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

        }

    }
    public EventListAdapter(List<EventModelData> eventModelDataList,Context context){
        this.context=context;
        this.eventModelDataList=eventModelDataList;
    }



}
