package com.alchemist.ssa.LoginStuffs;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alchemist.ssa.EventStuffs.Event;
import com.alchemist.ssa.NetworkStuffs.StringResource;
import com.alchemist.ssa.OtherStuffs.StaffDetail;
import com.alchemist.ssa.PerformanceStuffs.Performance;
import com.alchemist.ssa.R;
import com.alchemist.ssa.ResultStuffs.StudentResult;
import com.alchemist.ssa.ScheduleStuffs.Schedule;
import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ParentDashBoard extends AppCompatActivity {

    ProgressBar progressBar;

    ProgressDialog progressDialog;
    TextView absentDays;
    CardView scheduleCard,noticeCard,resultCard,performanceCard;
    private static String att_path= StringResource.getUrl()+"/provideAttendance";
    private static String delete_url=StringResource.getUrl()+"/deleteToken";
    private DrawerLayout eventDrawerLayout;
    private TextView username,email;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        progressBar=findViewById(R.id.progress);
        eventDrawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.navview);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("logging out...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        constraintLayout=findViewById(R.id.constrainLayout);
        username=navigationView.getHeaderView(0).findViewById(R.id.profile);
        email=navigationView.getHeaderView(0).findViewById(R.id.email);
        loadUserProfile();

        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        collapsingToolbarLayout=findViewById(R.id.collabstoolbar);
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));

        absentDays=findViewById(R.id.presentDay);

        scheduleCard=findViewById(R.id.scheduleCardView);
        noticeCard=findViewById(R.id.noticeCard);
        resultCard=findViewById(R.id.resultCard);
        performanceCard=findViewById(R.id.performanceCard);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id=item.getItemId();

                if(id==R.id.nav_logout){

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ParentDashBoard.this);
                    builder1.setTitle("Alert")
                            .setMessage("Do you sure want to logout?")
                    .setCancelable(false)
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.session_key),MODE_PRIVATE);
                            showDialog();
                           deleteToken();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();



                }

                else if(id==R.id.staffList){
                    startActivity(new Intent(getApplicationContext(),StaffDetail.class));
                }
                return true;
            }
        });
        drawerToggle=new ActionBarDrawerToggle(this,eventDrawerLayout,toolbar,R.string.open,R.string.close) {
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

        };
        eventDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();


        setAttendance();

            //showDialog();

        scheduleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Schedule.class));
            }
        });
        noticeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Event.class));
            }
        });
        resultCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StudentResult.class));
            }
        });

        performanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Performance.class));
            }
        });

        //loadAttendance(class_id,section_id,user_id);


    }

    public void setAttendance(){


        StringRequest stringRequest=new StringRequest(Request.Method.POST, att_path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.d("response",response.toString());
                    JSONObject jsonObject=new JSONObject(response);
                    String presentDays=jsonObject.getString("present");
                    String totalDays=jsonObject.getString("total");
                    float present=(Integer.parseInt(presentDays)*100) / (Integer.parseInt(totalDays));
                    Log.d("present",present+"");
                    absentDays.setText((int)(present)+" %");
                    StringResource.setPresent(presentDays);
                    StringResource.setTotal(totalDays);

                    Animation absentAnimation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.absent_days_animation);

                    ObjectAnimator objectAnimator=ObjectAnimator.ofInt(progressBar,"progress",0, (int)present);//starting from zero to 25 with max value 30
                    objectAnimator.setStartDelay(500);
                    objectAnimator.setDuration(500);//this is the duration of rotation of progress bar in milliseconds
                    objectAnimator.setInterpolator(new DecelerateInterpolator());
                    objectAnimator.start();

                    absentAnimation.setStartOffset(500);
                    absentAnimation.setDuration(500);
                    absentDays.startAnimation(absentAnimation);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    //handle the volley error
                    // i will do later
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.session_key),MODE_PRIVATE);
                Map<String,String> params=new HashMap<>();
                params.put("class_id",sharedPreferences.getInt(getString(R.string.class_id),1)+"");
                params.put("section_id",sharedPreferences.getInt(getString(R.string.section_id),1)+"");
                params.put("user_id",sharedPreferences.getInt(getString(R.string.session_value),1)+"");

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
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
    public void deleteToken(){

        //Toast.makeText(getApplicationContext(),sharedPreferences.getInt(getString(R.string.session_value), 1)+"",Toast.LENGTH_SHORT);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, delete_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("delete",response.toString());
                hideDialog();

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    boolean flag=jsonObject.getBoolean("delete");

                    if(flag){
                        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.session_key),MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putBoolean(getString(R.string.firstTimeLogin),true);
                        editor.commit();
                        startActivity(new Intent(getApplicationContext(),LoginInterface.class));
                    }
                    else{
                        Snackbar snackbar=Snackbar.make(constraintLayout,"Couldnot empty token",Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleVolleyError(error);
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.session_key),MODE_PRIVATE);

                params.put("id",sharedPreferences.getInt(getString(R.string.session_value), 1)+"");
                params.put("code","s");
                return params;
            }
        };

        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void loadUserProfile() {

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.session_key),MODE_PRIVATE);

        username.setText(sharedPreferences.getString(getString(R.string.student_name),""));
        email.setText(sharedPreferences.getString(getString(R.string.parent_email),""));
    }

    public void handleVolleyError(VolleyError error){
        hideDialog();
        if(error instanceof TimeoutError){
            showListError("Server TimeOut");

        }
        else if(error instanceof ServiceConnection){
            showListError("Server Connection Lost");
        }
        else if(error instanceof NoConnectionError){
            showListError("No Internet Connection");
        }
    }

    public void showListError(String error){
        Snackbar snackbar=Snackbar.make(constraintLayout,error,Snackbar.LENGTH_LONG);
        snackbar.show();
    }



}
