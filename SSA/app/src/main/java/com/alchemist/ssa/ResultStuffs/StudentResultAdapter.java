package com.alchemist.ssa.ResultStuffs;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.alchemist.ssa.OtherStuffs.SearchInterface;
import com.alchemist.ssa.R;

import java.util.ArrayList;
import java.util.List;

public class StudentResultAdapter extends RecyclerView.Adapter<StudentResultAdapter.ViewHolder> {
    private SearchInterface searchInterface;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView studentProfile;
        public TextView studentName,studentPos,studentMarks,studentRollNo;
        private Context context;


        public ViewHolder(final Context context, View itemView) {
            super(itemView);
            this.context=context;
            studentProfile=itemView.findViewById(R.id.studentProfile);
            studentName=itemView.findViewById(R.id.studentName);
            studentPos=itemView.findViewById(R.id.studentPos);
            studentMarks=itemView.findViewById(R.id.studentMarks);
            studentRollNo=itemView.findViewById(R.id.studentRollNo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(context,ResultDetailActivity.class);
                    intent.putExtra("name",studentName.getText().toString());
                    context.startActivity(intent);

//                    Pair<View,String> pair= Pair.create((View)studentProfile,"studentProfile");
//                    Intent intent=new Intent(context,ResultDetailActivity.class);
//                    intent.putExtra("name",studentName.getText().toString());
//                    ActivityOptionsCompat compat= ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)(context),pair);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        context.startActivity(intent,compat.toBundle());
//                    }
                }
            });
        }
    }

    private Context context;
    private List<StudentResultModel> studentResultModels;
    private int lastPosition=-1;

    public StudentResultAdapter(Context context, List<StudentResultModel> studentResultModels) {
        this.context = context;
        this.studentResultModels = studentResultModels;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.student_list_result,parent,false);
        return new ViewHolder(context,view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.studentRollNo.setText(studentResultModels.get(position).getRollNo()+"");
        holder.studentPos.setText(studentResultModels.get(position).getPosition()+"");
        holder.studentMarks.setText(studentResultModels.get(position).getMarks()+"");
        holder.studentName.setText(studentResultModels.get(position).getName());

        if(position>lastPosition) {
            Animation animation= AnimationUtils.loadAnimation(context, R.anim.student_grid_anim);
            holder.itemView.startAnimation(animation);
            lastPosition=holder.getAdapterPosition();
        }


    }

    public void setFilter(List<StudentResultModel> models){
        studentResultModels=new ArrayList<>();
        studentResultModels.addAll(models);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return studentResultModels.size();
    }

    public void addSearch(SearchInterface searchInterface){
        this.searchInterface=searchInterface;
    }

}
