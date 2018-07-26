package com.alchemist.ssa.ScheduleStuffs;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alchemist.ssa.R;

import java.util.List;

 class ScheduleDetailsGridAdapter extends RecyclerView.Adapter<ScheduleDetailsGridAdapter.ViewHolder>{
    private Context context;
    private List<ScheduleGridModel2> scheduleList;

    public ScheduleDetailsGridAdapter(Context context,List<ScheduleGridModel2> scheduleList){
        this.context=context;
        this.scheduleList = scheduleList;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView scheduleCardView;
        private TextView subjectName,teacherName,subjectTime,className;
        public ViewHolder(View itemView) {
            super(itemView);

            scheduleCardView = itemView.findViewById(R.id.scheduleCardView);

            subjectName=itemView.findViewById(R.id.txtSubjectName);
            teacherName=itemView.findViewById(R.id.txtTeacherName);
            subjectTime=itemView.findViewById(R.id.txtSubjectTime);
            className=itemView.findViewById(R.id.txtClassName);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view= LayoutInflater.from(context).inflate(R.layout.schedule_details_gridlayout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        fetching the position of the schedule

        holder.className.setText(scheduleList.get(position).getClassName()+"");
            holder.subjectName.setText(scheduleList.get(position).getSubject()+"");

        holder.subjectTime.setText(scheduleList.get(position).getTime()+"");

        holder.teacherName.setText(scheduleList.get(position).getTeacherName()+"");


    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }


}
