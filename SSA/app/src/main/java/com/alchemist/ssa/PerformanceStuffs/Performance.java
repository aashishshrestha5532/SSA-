package com.alchemist.ssa.PerformanceStuffs;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alchemist.ssa.NetworkStuffs.StringResource;
import com.alchemist.ssa.R;

public class Performance extends AppCompatActivity {
   // GraphView graph;
    ProgressBar progressBar;
    String grade;
    double percent,data;
    double performance;
    double extraP;
    TextView ratingText,adminName,adminRoll,attendanceFrac,resultNum,ratingText2,ratingText3;
    ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
        progressBar=findViewById(R.id.ratingprogress);
        ratingText=findViewById(R.id.ratingText);
        adminName=findViewById(R.id.adminName);
        adminRoll=findViewById(R.id.admininstrator);
        attendanceFrac=findViewById(R.id.number);
        resultNum=findViewById(R.id.number2);
        ratingText2=findViewById(R.id.ratingText2);
        ratingText3=findViewById(R.id.ratingText3);
        constraintLayout=findViewById(R.id.pconstraint);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.session_key), MODE_PRIVATE);

        adminName.setText(sharedPreferences.getString(getString(R.string.student_name),"")+"");
        adminRoll.setText("Roll :"+sharedPreferences.getInt(getString(R.string.section_id),1)+"");


        if(StringResource.getTotal_marks()==null){
            constraintLayout.setVisibility(View.GONE);
            new AlertDialog.Builder(Performance.this)
                    .setTitle("Alert")
                    .setMessage("First check your result to see the performance.")
                    .setCancelable(false)
                    .setPositiveButton("got it", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Whatever...
                            //startActivity(new Intent(getApplicationContext(), StudentResult.class));
                            dialog.cancel();
                        }
                    }).show();
        }
        else {
            constraintLayout.setVisibility(View.VISIBLE);

            if(StringResource.getTotal_marks().equals("Abs"))
                percent=0;
            else
                percent=Double.parseDouble(StringResource.getTotal_marks());


            ratingText.setText(loadPerformance());
            ratingText2.setText(loadRemarks(percent));
            ratingText3.setText(loadRemarks(extraP));
            resultNum.setText((StringResource.getTotal_marks() == null) ? "X" : loadResult(percent));
            attendanceFrac.setText(StringResource.getPresent() + "/" + StringResource.getTotal());

            Animation absentAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.absent_days_animation);

            ObjectAnimator objectAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, (int) percent);//starting from zero to 25 with max value 30
            objectAnimator.setStartDelay(500);
            objectAnimator.setDuration(500);//this is the duration of rotation of progress bar in milliseconds
            objectAnimator.setInterpolator(new DecelerateInterpolator());
            objectAnimator.start();

            absentAnimation.setStartOffset(500);
            absentAnimation.setDuration(500);
            ratingText.startAnimation(absentAnimation);
            ratingText2.startAnimation(absentAnimation);
            ratingText3.startAnimation(absentAnimation);

        }

//        graph = (GraphView) findViewById(R.id.graph);
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
//        graph.addSeries(series);
    }
    public String loadResult(double percent){
        if(percent<20)
            grade="E";
        else if(percent<30)
            grade="D";
        else if(percent<40)
            grade="D+";
        else if(percent<50)
            grade="C";
        else if(percent<60)
            grade="C+";
        else if(percent<70)
            grade="B";
        else if(percent<80)
            grade="B+";
        else if(percent<90)
            grade="A";
        else
            grade="A+";

        return grade;



    }
    public String loadPerformance(){
        //we take 60 % weight of marks 30% of the attendance and remaining percent of the assignments
        /* Total_p= 60% of marks + 30 % of attendance +10 % of assignments  */
        double present= (Integer.parseInt(StringResource.getPresent())*100)/(Integer.parseInt(StringResource.getTotal()));
        performance=(0.6*percent+0.3*present+0.1*100);
        extraP=(0.3*present+0.1*100)*100/40;
        Log.d("present",present+"");
        Log.d("performance",performance+"");
        Log.d("percent",percent+"");
        Log.d("extraP",extraP+"");
        loadResult(performance);

        return loadResult(performance);

    }

    public String loadRemarks(double percent){
        String feedback;

        if(percent<20)
            feedback="Very Insufficient";
        else if (percent<30)
            feedback="Insufficient";
        else if(percent<40)
            feedback="Partially acceptable";
        else if(percent<50)
            feedback="Acceptable";
        else if(percent<60)
            feedback="Satisfactory";
        else if(percent<70)
            feedback="Good";
        else if(percent<80)
            feedback="Very good";
        else if(percent<90)
            feedback="Excellent";
        else
            feedback="Outstanding";
        return feedback;
    }

}
