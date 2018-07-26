package com.alchemist.ssa.ScheduleStuffs;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
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
    private int itemPosition=0;

    private ScheduleInterface scheduleInterface;

    public ScheduleGridAdapter(Context context,List<MyTypeModel> list){
        this.context=context;
        this.list=list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button dailySchedule;

        public ViewHolder(final View itemView) {
            super(itemView);


            dailySchedule = itemView.findViewById(R.id.dailySchedule);
            dailySchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //ScheduleGridModel model= list.get(getAdapterPosition());
                    scheduleInterface.setDetails(list.get(getAdapterPosition()));

                    if(getAdapterPosition()==RecyclerView.NO_POSITION)
                        return;

                    notifyItemChanged(itemPosition);
                    itemPosition=getAdapterPosition();
                    notifyItemChanged(itemPosition);



                   // dailySchedule.setBackgroundColor(Color.TRANSPARENT);


                }
            });
           // dailySchedule.setBackgroundColor(Color.RED);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.schedule_grid_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.dailySchedule.setText(list.get(position).getName());
        if(itemPosition==position){
            holder.dailySchedule.setBackgroundColor(Color.TRANSPARENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.dailySchedule.setBackground(ContextCompat.getDrawable(context,R.drawable.ovalback));
            }
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.dailySchedule.setBackground(ContextCompat.getDrawable(context,R.drawable.rounded_rectangle));
            }
        }
        //holder.dailySchedule.setBackgroundColor((itemPosition==position)? Color.TRANSPARENT:R.drawable.rounded_rectangle);
               //itemPosition++;

    }

    @Override
    public int getItemCount() {
        return list.size();

    }
    public int getSelectedItemPosition(){
        return itemPosition;
    }
    public void setScheduleInterface(ScheduleInterface scheduleInterface){
        this.scheduleInterface = scheduleInterface;
    }

    public int getselected(int x){
        itemPosition=x;

        return itemPosition;
    }
}
