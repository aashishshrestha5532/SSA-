package com.alchemist.ssa.AttendanceStuffs;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alchemist.ssa.R;

import java.util.List;


public class AttendanceGridAdapter extends RecyclerView.Adapter<AttendanceGridAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button attendanceStudent;

        private String TAG="AttendanceGridAdapter";

        public ViewHolder(final View itemView) {
            super(itemView);
            attendanceStudent=itemView.findViewById(R.id.attendanceStudent);

            attendanceStudent.setOnClickListener(new View.OnClickListener() {//Listen for the button click
                @Override
                public void onClick(View view) {
                    if(itemPosition==RecyclerView.NO_POSITION)
                        return;
                    notifyItemChanged(itemPosition);//Notify the previously selected button
                    itemPosition=getAdapterPosition();//Gets the current selected button as it is linked with the ViewHolder Instance
                    Log.d(TAG, "onClick: "+itemPosition);
                    notifyItemChanged(itemPosition);//Notify the newly selected button
                    AttendanceGridModel model=attendanceGridModels.get(itemPosition);
                    attendanceInterface.displayOnBigButton(model);//Call the activity to change the button layout in the center
                    //attendanceInterface.setDetails(model);
                }
            });
        }

        /*
        * This method is called when the Present button is pressed
        * */

        public void present() {
            if(itemPosition==RecyclerView.NO_POSITION)
                return;
            attendanceGridModels.get(itemPosition).setStudentStatus(1);
            Log.d(TAG, "present: Student is Present:itemPosition:"+itemPosition);
            manualOnClick();
        }

        /*
        * This method simulates the manual click of the button and shifts the buttons if it is invisible
        * */

        private void manualOnClick() {
            notifyItemChanged(itemPosition);
            itemPosition++;
            if(itemPosition<attendanceGridModels.size()) {//check for Array Out of Bound Condition
                notifyItemChanged(itemPosition);
                Log.d(TAG, "manualOnClick: "+itemPosition);
                AttendanceGridModel model=attendanceGridModels.get(itemPosition);
                attendanceInterface.displayOnBigButton(model);
               // attendanceInterface.setDetails(model);
            } else {//The user is at last,so no scroll ,only change the center button
                attendanceInterface.displayOnBigButton(attendanceGridModels.get(itemPosition-1));
                //attendanceInterface.setDetails(attendanceGridModels.get(itemPosition-1));
            }

        }

        public void absent() {
            if(itemPosition==RecyclerView.NO_POSITION)
                return;
            attendanceGridModels.get(itemPosition).setStudentStatus(0);
            manualOnClick();
        }
    }

    private Context context;
    private List<AttendanceGridModel> attendanceGridModels;
    private AttendanceInterface attendanceInterface;
    private int itemPosition=0;


    public AttendanceGridAdapter(Context context, List<AttendanceGridModel> attendanceGridModels) {
        this.context=context;
        this.attendanceGridModels=attendanceGridModels;
    }



    public void addAttendanceChangeListener(AttendanceInterface attendanceInterface) {
        this.attendanceInterface=attendanceInterface;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.attendance_grid_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       holder.attendanceStudent.setText(attendanceGridModels.get(position).getRollNo());

        if(itemPosition==position)
        holder.itemView.setBackgroundResource(R.drawable.circleback);

        else
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        //holder.itemView.setBackgroundColor(itemPosition==position? Color.GREEN:Color.TRANSPARENT);//Fish code
        if(attendanceGridModels.get(position).getStudentStatus()==0)//Change the layout as per the status of the student
            holder.attendanceStudent.setBackground(ContextCompat.getDrawable(context,R.drawable.absent_student_circle));
        else if(attendanceGridModels.get(position).getStudentStatus()==1)
            holder.attendanceStudent.setBackground(ContextCompat.getDrawable(context,R.drawable.present_student_circle));
        else
            holder.attendanceStudent.setBackground(ContextCompat.getDrawable(context,R.drawable.circle));
    }

    @Override
    public int getItemCount() {
        return attendanceGridModels.size();
    }

    public int getSelectedItem() {
        return itemPosition;
    }
}
