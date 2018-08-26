package com.alchemist.ssa.ScheduleStuffs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.alchemist.ssa.NetworkStuffs.StringResource;
import com.alchemist.ssa.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddSchedule extends AppCompatActivity {

    private Spinner cSpinner,subSpinner,teacherSpinner,daySpinner,classSpinner;
    private TextInputLayout start,end,periodLayout,subjectCodeLayout,subjectNameLayout;
    private TextInputEditText startDate,endDate,period,sub_name,sub_code;
    private RadioGroup radioGroup;
    private Button addSchedule,addSubject;
    private boolean isAdded=true;
    private List<String> subject_list=new ArrayList<>();
    private int status;
    private ConstraintLayout constraintLayout,lowConstrain;
    ArrayAdapter<String> subAdapter;
    private RadioButton radioButton;
    private ProgressDialog progressDialog;
    private String classes[]={"1st year","2nd year","3rd year","4th year"};
    //private String classes[]={"class 1","class 2","class 3","class 4","class 5","class 6","class 7","class 8","class 9","class 10"};
    private String teachers[]={"teacher 1","teacher 2","teacher 3"};
    private String subject[]={"Mth101","CMP555","Eng333","MTH121"};
    private String subject2[]={"MTH505"};
    private String subject3[]={"OPT111","UP349"};
    private String days[]={"Sun","Mon","Tue","Wed","Thu","Fri"};
    private String sec;
    private FloatingActionButton floatingActionButton;
    private static String schedule_url= StringResource.getUrl()+"/addSchedule";
    private static String subject_url=StringResource.getUrl()+"/addSubject";
    private static String showSubject_url=StringResource.getUrl()+"/showSubject";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        constraintLayout=findViewById(R.id.constrainLayout);
        lowConstrain=findViewById(R.id.lowerConstrain);
        floatingActionButton=findViewById(R.id.addSubject);

        start=findViewById(R.id.initialTime);
        end=findViewById(R.id.finalTime);
        periodLayout=findViewById(R.id.periodLayout);
        subjectCodeLayout=findViewById(R.id.subjectcodeInputLayout);
        subjectNameLayout=findViewById(R.id.subjectNameInputLayout);

        cSpinner=findViewById(R.id.classSpinner);
        subSpinner=findViewById(R.id.subjectSpinner);
        teacherSpinner=findViewById(R.id.teacherSpinner);
        daySpinner=findViewById(R.id.daySpinner);
        classSpinner=findViewById(R.id.clspinner);
        cSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subject_list.clear();
                showSubject(cSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addSchedule=findViewById(R.id.addSchedule);
        addSubject=findViewById(R.id.addS);
        sub_name=findViewById(R.id.subject_name);
        sub_code=findViewById(R.id.subject_code);

        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Uploading....");
        progressDialog.setCancelable(false);
        radioGroup=findViewById(R.id.radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.section1:
                        sec="1";
                        break;

                    case R.id.section2:
                        sec="2";
                        break;
                    case R.id.section3:
                        sec="3";
                        break;
                }
            }
        });

        startDate=findViewById(R.id.itime);
        endDate=findViewById(R.id.ftime);
        period=findViewById(R.id.period);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(startDate);
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(endDate);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealAnimation(constraintLayout);
                if(isAdded) {
                    Animation rotateClock= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
                    floatingActionButton.startAnimation(rotateClock);
                    isAdded=false;
                    status=1;
                    lowConstrain.setVisibility(View.GONE);
                } else {
                    Animation rotateAnti=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anti);
                    floatingActionButton.startAnimation(rotateAnti);
                    isAdded=true;
                    status=0;
                    lowConstrain.setVisibility(View.VISIBLE);
                }

            }
        });

        final ArrayAdapter<String> classAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,classes);
        classAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        cSpinner.setAdapter(classAdapter);

        subAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,subject);
        subAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        subSpinner.setAdapter(subAdapter);

        final ArrayAdapter<String> teacherAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,teachers);
        teacherAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        teacherSpinner.setAdapter(teacherAdapter);

        final ArrayAdapter<String> dayAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,days);
        dayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        classSpinner.setAdapter(classAdapter);




        addSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateInput(start,startDate,"StartDate cannot be empty") && validateInput(end,endDate,"EndDate cannot be empty")&&validateInput(periodLayout,period,"Period cannot be empty"))
                {
                    showDialog();
                    addScheduleData(startDate.getText().toString().trim(), endDate.getText().toString().trim(), subSpinner.getSelectedItem().toString().trim(), period.getText().toString().trim(), cSpinner.getSelectedItem().toString().trim(), sec, teacherSpinner.getSelectedItem().toString().trim(), daySpinner.getSelectedItem().toString().trim());

                }
            }
        });
        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateInput(subjectCodeLayout,sub_code,"Subject code cannot be empty") && validateInput(subjectNameLayout,sub_name,"SubjectName cannot be empty")) {
                    showDialog();
                    addSubjectData(sub_code.getText().toString().trim(), sub_name.getText().toString().trim(), classSpinner.getSelectedItem().toString());
                }
            }
        });





    }

    public void showTimePicker(final EditText editText){
        final Calendar calendar=Calendar.getInstance();
        int mHour=calendar.get(Calendar.HOUR);
        int mMin=calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog=new TimePickerDialog(AddSchedule.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(hourOfDay<12) {
                    if(hourOfDay==0)
                        hourOfDay=12;
                    editText.setText(hourOfDay + ":" + minute + ":AM");
                }
                else {
                    if(hourOfDay!=12)
                        hourOfDay-=12;
                    editText.setText(hourOfDay + ":" + minute + ":PM");
                }
            }
        },mHour,mMin,false);
        timePickerDialog.show();
    }

    public void addScheduleData(final String startDate,final String endDate,final String sub_id,final String period,final String class_id,final String section_id,final String teacher,final String day){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, schedule_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    hideDialog();
                    JSONObject jsonObject=new JSONObject(response);
                    boolean insert=jsonObject.getBoolean("insert");
                    if(insert){
                        Snackbar snackbar=Snackbar.make(constraintLayout,"Data are inserted successfully",Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    else{
                        Snackbar snackbar=Snackbar.make(constraintLayout,"Sorry! Datas couldnt be inserted",Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("start_time",startDate);
                params.put("end_time",endDate);
                params.put("sub_id",sub_id);
                params.put("period",period);
                params.put("class_id",class_id.substring(5));
                params.put("section_id",section_id);
                params.put("teacher_id",teacher.substring(7));
                params.put("day",day);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    private void revealAnimation(View view) {
        final LinearLayout revealLayout=(LinearLayout) findViewById(R.id.anotherlayout);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            int cx=view.getWidth();
            int cy=view.getHeight();
            float radius=(float)(Math.hypot(cx,cy)*1.2);
            if(revealLayout.getVisibility()==View.GONE) {
                revealLayout.setVisibility(View.VISIBLE);
                ViewAnimationUtils.createCircularReveal(revealLayout,cx,cy,0,radius).start();
            } else {
                Animator animator= ViewAnimationUtils.createCircularReveal(revealLayout,cx,cy,radius,0);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        revealLayout.setVisibility(View.GONE);
                        revealLayout.clearAnimation();
                    }
                });
                animator.start();
            }

        } else {
            backWardRevealAnimation(revealLayout);
        }

    }
    private void backWardRevealAnimation(final View view) {
        Animation translateAnimation;
        if(view.getVisibility()==View.GONE) {
            translateAnimation=new TranslateAnimation(0,0,-constraintLayout.getHeight(),0);
            translateAnimation.setDuration(500);
            translateAnimation.setFillAfter(true);
            view.setVisibility(View.VISIBLE);
        } else {
            translateAnimation=new TranslateAnimation(0,0,0,-constraintLayout.getHeight());
            translateAnimation.setDuration(500);
            translateAnimation.setFillAfter(true);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.GONE);
                    view.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
        view.startAnimation(translateAnimation);
    }
//    @Override
//    public void onBackPressed() {
//        switch (status) {
//            case 0:
//                ActivityCompat.finishAffinity(this);
//                break;
//            case 1:
//                revealAnimation(constraintLayout);
//                Animation rotateAnti=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anti);
//                addSchedule.startAnimation(rotateAnti);
//                isAdded=true;
//                status=0;
//                break;
//            case 2:
//                constraintLayout.setVisibility(View.GONE);
//                status=0;
//                break;
//        }
//
//
//
//
//    }
    public void addSubjectData(final String subject_code, final String subject_name, final String class_id){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, subject_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    hideDialog();
                    Log.d("response",response.toString());
                    JSONObject jsonObject=new JSONObject(response);
                    boolean flag=jsonObject.getBoolean("insert");
                    if(flag){
                        Snackbar snackbar=Snackbar.make(constraintLayout,"Subject Datas are inserted successfully",Snackbar.LENGTH_LONG);
                        snackbar.show();
                        sub_name.setText("");
                        sub_code.setText("");
                    }
                    else{
                        Snackbar snackbar=Snackbar.make(constraintLayout,"Subject Datas couldnt be inserted",Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("subject_code",subject_code);
                params.put("subject_name",subject_name);
                params.put("class_id",class_id.substring(5));
                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void showDialog(){
        if(!progressDialog.isShowing()){
            progressDialog.show();
        }


    }
    private void hideDialog(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }

    }
    private boolean validateInput(TextInputLayout layout, TextInputEditText editText, String msg) {
        if(editText.getText().toString().trim().isEmpty()) {
            layout.setError(msg);
            editText.requestFocus();
            return false;
        } else {
            layout.setErrorEnabled(false);
            return true;
        }
    }

    public void showSubject(final String class_id){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, showSubject_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    int i=0;
                    Log.d("response",response);
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("response");
                    while(i<jsonArray.length()) {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        subject_list.add(jsonObject1.getString("sub_name"));
                        i++;

                    }
                    subAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("class_id",class_id.substring(5));
                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
