package com.alchemist.ssa.OtherStuffs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alchemist.ssa.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashish Alton on 8/6/2018.
 */

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder> {

    List<StaffModel> staffModelList;
    private Context context;
    private SearchInterface searchInterface;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.staffdetail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.teacherName.setText(staffModelList.get(position).getTeacherName());
        holder.contactNo.setText(staffModelList.get(position).getPhone() + "");
        holder.position.setText(staffModelList.get(position).getLevel());

    }

    @Override
    public int getItemCount() {
        return staffModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView teacherName,contactNo,position;
        public ViewHolder(View itemView) {
            super(itemView);

            teacherName=itemView.findViewById(R.id.teacherName);
            contactNo=itemView.findViewById(R.id.contactNo);
            position=itemView.findViewById(R.id.teacherPos);



        }
    }

    public StaffAdapter(Context context,List<StaffModel> staffModelList){
        this.context=context;
        this.staffModelList=staffModelList;
    }
    public void setFilter(ArrayList<StaffModel> linkModels){

        staffModelList=new ArrayList<>();
        staffModelList.addAll(linkModels);
        notifyDataSetChanged();

    }



}

