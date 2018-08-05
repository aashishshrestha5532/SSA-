package com.alchemist.ssa.ScheduleStuffs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Schedule extends AppCompatActivity implements ScheduleInterface {

    private RecyclerView scheduleRecyclerView;
    private static String schedule_url_path= StringResource.getUrl()+"/viewClassSchedule";
    private static String day[]={"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};

    private static String periods[]={"1st","2nd","3rd","4th","5th","6th","7th","8th"};
    private ArrayList<ScheduleGridModel> scheduleGridModels = new ArrayList<>();
    private List<ScheduleGridModel2> scheduleGridModel2List =new ArrayList<>();

    private ScheduleDetailsGridAdapter scheduleDetailsGridAdapter;

    private  ScheduleGridAdapter scheduleGridAdapter;
    List<MyTypeModel> lists=new ArrayList<>();
    private TextView textView;
    private RecyclerView detailsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        //response=getString(R.string.json);

        scheduleRecyclerView = findViewById(R.id.scheduleRecyclerView);

        scheduleGridAdapter=new ScheduleGridAdapter(getApplicationContext(),lists);


        scheduleGridAdapter.setScheduleInterface(this);

        loadSchedule();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        scheduleRecyclerView.setLayoutManager(linearLayoutManager);
        scheduleRecyclerView.setAdapter(scheduleGridAdapter);
    }
    private  void loadSchedule(){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, schedule_url_path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    int i=0;
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("response",response);
                    JSONArray jsonArray=jsonObject.getJSONArray("response");
                    while(i<jsonArray.length()) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        List<ScheduleGridModel2> scheduleGridModels=new ArrayList<>();
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("schedule");
                        int j=0;
                        while(j<jsonArray1.length()){
                            JSONObject jsonObject2=jsonArray1.getJSONObject(j);
                            int period=jsonObject2.getInt("period");
                            String subject="Subject : "+jsonObject2.getString("subject");
                            String time="time - "+jsonObject2.getString("start_time");
                            String teacher="By- "+jsonObject2.getString("teacher");
                            scheduleGridModels.add(new ScheduleGridModel2(periods[period-1], subject,time, teacher));
                            scheduleGridAdapter.notifyDataSetChanged();
                            j++;

                        }
                        lists.add(new MyTypeModel(scheduleGridModels,jsonObject1.getString("day")));
                        //scheduleGridModels.clear();
                        i++;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //error msg
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();

                params.put("section_id","1");
                params.put("class_id","1");
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);



    }

    @Override
    public void setDetails(MyTypeModel data) {
        detailsRecyclerView=findViewById(R.id.scheduleDetailsRecyclerView);
        scheduleDetailsGridAdapter=new ScheduleDetailsGridAdapter(getApplicationContext(),data.getLists());
       // loadDetails();



        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
           detailsRecyclerView.setLayoutManager(linearLayoutManager);
           detailsRecyclerView.setAdapter(scheduleDetailsGridAdapter);


    }







}
