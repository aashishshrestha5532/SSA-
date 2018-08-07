package com.alchemist.ssa.LoginStuffs;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
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
import com.alchemist.ssa.R;
import com.alchemist.ssa.ResultStuffs.StudentResult;
import com.alchemist.ssa.ScheduleStuffs.Schedule;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
    CardView scheduleCard,noticeCard,resultCard;
    private static String att_path= StringResource.getUrl()+"/provideAttendance";
    private DrawerLayout eventDrawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        progressBar=findViewById(R.id.progress);
        eventDrawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.navview);

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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id=item.getItemId();


                if(id==R.id.nav_logout){
                    SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.session_key),MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putBoolean(getString(R.string.firstTimeLogin),true);
                    editor.commit();
                    startActivity(new Intent(getApplicationContext(),LoginInterface.class));
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
//                status=0;
                Log.d("hca", "onDrawerClosed: Drawer Closed");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //status=2;
                Log.d("hca", "onDrawerOpened: Drawer Opened");
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


}
