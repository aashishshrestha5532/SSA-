package com.alchemist.ssa.LoginStuffs;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alchemist.ssa.AdminStuffs.AdminHome;
import com.alchemist.ssa.AdminStuffs.EventList;
import com.alchemist.ssa.NetworkStuffs.StringResource;
import com.alchemist.ssa.R;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddEvent extends AppCompatActivity {

    private TextInputEditText eventname,eventDesc,eventDate,eventTime;
    private TextInputLayout eventLayout,descLayout,dateLayout,timeLayout;
    private int  mYear, mMonth, mDay;
    private Button addEvent,updateEvent;
    private ProgressDialog progressDialog;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private ConstraintLayout constraintLayout;
    private String event_type;
    private Bundle bundle;
    private static String addEvent_url= StringResource.getUrl()+"/addEvent";
    private static String updateEvent_url=StringResource.getUrl()+"/updateEvent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash_board);

        bundle=getIntent().getExtras();
        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Inserting...");
        eventname=findViewById(R.id.event_name);
        eventDesc=findViewById(R.id.event_description);
        eventDate=findViewById(R.id.date);
        eventTime=findViewById(R.id.time);
        eventLayout=findViewById(R.id.eventInputLayout);
        descLayout=findViewById(R.id.descInputLayout);
        dateLayout=findViewById(R.id.dateInputLayout);
        timeLayout=findViewById(R.id.timeInputLayout);
        radioGroup=findViewById(R.id.radio);
        addEvent=findViewById(R.id.addEvent);
        updateEvent=findViewById(R.id.update);
        constraintLayout=findViewById(R.id.constrainLayout);
        checkBundle();//this will check whether bundle is null or not if null then addEvent activitity else update event.
        eventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(eventTime);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.holiday:
                        event_type="holiday";
                        break;

                    case R.id.normal:
                        event_type="normal";
                        break;
                }
            }
        });


        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(eventDate);
            }
        });
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showDialog();
                if(validateInput(eventLayout,eventname,"EventName cannot be empty") && validateInput(descLayout,eventDesc,"Description cannot be empty") && validateInput(dateLayout,eventDate,"Date cannot be empty") && validateInput(timeLayout,eventTime,"time cannot be empty")) {
                    addEvent(eventname.getText().toString().trim(), eventDesc.getText().toString().trim(), eventDate.getText().toString().trim(), event_type, eventTime.getText().toString().trim());
                }
            }
        });
        updateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput(eventLayout,eventname,"EventName cannot be empty") && validateInput(descLayout,eventDesc,"Description cannot be empty") && validateInput(dateLayout,eventDate,"Date cannot be empty") && validateInput(timeLayout,eventTime,"time cannot be empty")) {

                    showDialog();
                    updateEvent(eventname.getText().toString(), eventDate.getText().toString(), event_type, eventDesc.getText().toString(), eventTime.getText().toString(), bundle.getInt("event_id"));
                }
            }
        });
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

    public void showDatePicker(final EditText edit_text) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        edit_text.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        c.add(Calendar.DAY_OF_MONTH, 15);
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePickerDialog.show();

    }
    public void showTimePicker(final EditText editText){
        final Calendar calendar=Calendar.getInstance();
        int mHour=calendar.get(Calendar.HOUR);
        int mMin=calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog=new TimePickerDialog(AddEvent.this, new TimePickerDialog.OnTimeSetListener() {
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

    public void addEvent(final String e_name, final String e_Desc, final String e_Date,final String e_type,final String e_time){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, addEvent_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                   // hideDialog();
                    JSONObject jsonObject=new JSONObject(response);
                    boolean flag=jsonObject.getBoolean("insert");
                    if(flag){
                        Snackbar snackbar=Snackbar.make(constraintLayout,"Event is uploaded",Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        eventname.setText("");
                        eventDate.setText("");
                        eventDesc.setText("");
                        eventTime.setText("");
                    }
                    else {
                        Snackbar snackbar = Snackbar.make(constraintLayout, "Sorry! Couldnot upload event", Snackbar.LENGTH_SHORT);
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
                params.put("event_name",e_name);
                params.put("event_date",e_Date);
                params.put("event_type",e_type);
                params.put("event_desc",e_Desc);
                params.put("event_time",e_time);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void updateEvent(final String e_name,final String e_Date,final String e_type,final String e_Desc,final String e_time,final int e_id){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, updateEvent_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    hideDialog();
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getBoolean("update")){
                        Snackbar.make(constraintLayout,"Updated successfully",Snackbar.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(), EventList.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleVolleyError(error);
                hideDialog();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("event_name",e_name);
                params.put("event_date",e_Date);
                params.put("event_type",e_type);
                params.put("event_desc",e_Desc);
                params.put("event_time",e_time);
                params.put("event_id",e_id+"");
                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void checkBundle(){
        if(bundle!=null){
            addEvent.setVisibility(View.GONE);
            updateEvent.setVisibility(View.VISIBLE);

            eventname.setText(bundle.getString("event_name"));
            eventDesc.setText(bundle.getString("event_desc"));
            eventDate.setText(bundle.getString("event_date"));
            eventTime.setText(bundle.getString("event_time"));

        }
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
        startActivity(new Intent(getApplicationContext(), AdminHome.class));
    }
}
