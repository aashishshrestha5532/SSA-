package com.alchemist.ssa.ScheduleStuffs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alchemist.ssa.R;

import java.util.List;

public class ScheduleGridAdapter  extends RecyclerView.Adapter<ScheduleGridAdapter.ViewHolder>{
    private Context context;
    List<MyTypeModel> list;

    private ScheduleInterface scheduleInterface;

    public ScheduleGridAdapter(Context context,List<MyTypeModel> list){
        this.context=context;
        this.list=list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button dailySchedule;

        public ViewHolder(View itemView) {
            super(itemView);

            dailySchedule = itemView.findViewById(R.id.dailySchedule);
            dailySchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //ScheduleGridModel model= list.get(getAdapterPosition());
                    scheduleInterface.setDetails(list.get(getAdapterPosition()));


                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.schedule_grid_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScheduleGridAdapter.ViewHolder holder, int position) {
        holder.dailySchedule.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();

    }
    public void setScheduleInterface(ScheduleInterface scheduleInterface){
        this.scheduleInterface = scheduleInterface;
    }


}