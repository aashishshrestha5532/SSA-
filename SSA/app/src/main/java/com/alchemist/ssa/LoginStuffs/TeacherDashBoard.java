package com.alchemist.ssa.LoginStuffs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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
import android.widget.TextView;

import com.alchemist.ssa.AttendanceStuffs.Attendance;
import com.alchemist.ssa.EventStuffs.Event;
import com.alchemist.ssa.NetworkStuffs.StringResource;
import com.alchemist.ssa.OtherStuffs.StaffDetail;
import com.alchemist.ssa.R;
import com.alchemist.ssa.ResultStuffs.CheckResultBoard;
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

public class TeacherDashBoard extends AppCompatActivity {

    private TextView mTextMessage;
    private DrawerLayout eventDrawerLayout;
    private NavigationView navigationView;
    private TextView username,email;
    private ConstraintLayout constraintLayout;
    private ActionBarDrawerToggle drawerToggle;
    private CardView attendanceCard,eventCard,resultCard;
    private static String delete_url= StringResource.getUrl()+"/deleteToken";
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dash_board);

        eventDrawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.navview);
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Teacher Dashboard");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        constraintLayout=findViewById(R.id.constrainLayout);

        attendanceCard=findViewById(R.id.attenanceCard);
        eventCard=findViewById(R.id.noticeCard);
        resultCard=findViewById(R.id.resultCard);
        username=navigationView.getHeaderView(0).findViewById(R.id.profile);
        email=navigationView.getHeaderView(0).findViewById(R.id.email);

        loadUserProfile();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("logging out...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        attendanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Attendance.class));
            }
        });
        eventCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Event.class));
            }
        });
        resultCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CheckResultBoard.class));
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id=item.getItemId();

                if(id==R.id.nav_logout){

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(TeacherDashBoard.this);
                    builder1.setTitle("Alert")
                            .setMessage("Do you sure want to logout?")
                            .setCancelable(false)
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.session_key),MODE_PRIVATE);
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

    }

    private void loadUserProfile() {

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.teacher_session_key),MODE_PRIVATE);

        username.setText(sharedPreferences.getString(getString(R.string.teacher_name),""));
        email.setText(sharedPreferences.getString(getString(R.string.teacher_email),""));
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
                        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.teacher_session_key),MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putBoolean(getString(R.string.teacherFirstLogin),true);
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

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.teacher_session_key),MODE_PRIVATE);
                params.put("id",sharedPreferences.getInt(getString(R.string.teacher_session), 1)+"");
                params.put("code","t");
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }


}
