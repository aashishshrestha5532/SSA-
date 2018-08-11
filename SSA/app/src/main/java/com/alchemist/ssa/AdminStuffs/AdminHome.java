package com.alchemist.ssa.AdminStuffs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.alchemist.ssa.LoginStuffs.AddEvent;
import com.alchemist.ssa.R;
import com.alchemist.ssa.ScheduleStuffs.AddSchedule;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AdminHome extends AppCompatActivity {
    private CardView eventCard,scheduleCard;
    private static String url_path="http://192.168.1.107:8000"+"/getAdminData";
    private TextView parent_num,teacher_num;
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
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Retreiving data");
        progressDialog.setCancelable(false);
        showDialog();

        eventCard=findViewById(R.id.event_card);
        scheduleCard=findViewById(R.id.scheduleCard);
        loadNum();

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

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
}
