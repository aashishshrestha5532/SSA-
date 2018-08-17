package com.alchemist.ssa.AdminStuffs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemist.ssa.LoginStuffs.AddEvent;
import com.alchemist.ssa.LoginStuffs.LoginInterface;
import com.alchemist.ssa.NetworkStuffs.StringResource;
import com.alchemist.ssa.R;
import com.alchemist.ssa.ScheduleStuffs.AddSchedule;
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

public class AdminHome extends AppCompatActivity {
    private CardView eventCard,scheduleCard;
    private static String url_path= StringResource.getUrl()+"/getAdminData";
    private TextView parent_num,teacher_num,event_num,schedule_num,attendance_num,adminName;
    private ProgressDialog progressDialog;
    com.github.clans.fab.FloatingActionButton fevent,fschedule,fnotice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);


        fevent=findViewById(R.id.addEvent);
        fschedule=findViewById(R.id.addSchedule);
        fnotice=findViewById(R.id.addNotice);
        parent_num=findViewById(R.id.number);
        teacher_num=findViewById(R.id.number2);
        event_num=findViewById(R.id.number4);
        schedule_num=findViewById(R.id.number3);
        attendance_num=findViewById(R.id.number5);
        adminName=findViewById(R.id.adminName);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Retreiving data");
        progressDialog.setCancelable(false);
        showDialog();

        eventCard=findViewById(R.id.event_card);
        scheduleCard=findViewById(R.id.scheduleCard);
        loadNum();

        adminName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(AdminHome.this);
                builder1.setTitle("Alert")
                        .setMessage("Do you sure want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences3=getApplicationContext().getSharedPreferences(getString(R.string.admin_session),MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPreferences3.edit();
                                editor.putBoolean(getString(R.string.adminFirstTimeLogin),true);
                                editor.commit();
                               Intent intent=new Intent(getApplicationContext(), LoginInterface.class);
                               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                               startActivity(intent);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
            }
        });

        eventCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),EventList.class));
            }
        });

        fevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddEvent.class));
            }
        });
        fschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddSchedule.class));
            }
        });
        fnotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SendNotice.class));
            }
        });
    }
    public void loadNum(){
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url_path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    hideDialog();
                    JSONObject jsonObject=new JSONObject(response);
                    parent_num.setText(jsonObject.getString("student"));
                    teacher_num.setText(jsonObject.getString("teacher"));
                    event_num.setText(jsonObject.getString("events"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleVolleyError(error);
            }
        });
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
        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
      //  finish();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
