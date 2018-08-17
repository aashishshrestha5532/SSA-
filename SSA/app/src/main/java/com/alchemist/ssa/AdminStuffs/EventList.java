package com.alchemist.ssa.AdminStuffs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

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

public class EventList extends AppCompatActivity {
    RecyclerView recyclerView;
    EventListAdapter eventListAdapter;
    private ConstraintLayout constraintLayout;
    List<EventModelData> list=new ArrayList<>();
    private ProgressDialog progressDialog;
    private static final String event_url= StringResource.getUrl()+"/events";
    private static final String delete_url=StringResource.getUrl()+"/deleteEvent";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        constraintLayout=findViewById(R.id.eventConstrain);
        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        showDialog();
        Toolbar toolbar= findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setTitle("Events");
            toolbar.setTitleTextColor(Color.WHITE);
        }

        recyclerView=findViewById(R.id.eventRecycleview);
        recyclerView.setNestedScrollingEnabled(false);
        eventListAdapter=new EventListAdapter(list,getApplicationContext());

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        loadEventList();
        recyclerView.setAdapter(eventListAdapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(getApplicationContext(), "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast.makeText(getApplicationContext(), "on Swiped ", Toast.LENGTH_SHORT).show();
                //Remove swiped item from list and notify the RecyclerView
                AlertDialog.Builder builder1 = new AlertDialog.Builder(EventList.this);
                builder1.setTitle("Alert")
                        .setMessage("Do you sure want to delete it?")
                        .setCancelable(false)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getApplicationContext(),list.get(viewHolder.getAdapterPosition()).getEvent_id()+"",Toast.LENGTH_SHORT).show();
                                deleteEvent(list.get(viewHolder.getAdapterPosition()).getEvent_id());
                                int position = viewHolder.getAdapterPosition();
                                list.remove(position);
                                eventListAdapter.notifyDataSetChanged();

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        eventListAdapter.notifyDataSetChanged();
                    }
                }).show();


            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    public void loadEventList(){
        StringRequest stringRequest=new StringRequest(Request.Method.GET, event_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    hideDialog();
                    int count=0;
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("event_list");

                    while(count<jsonArray.length()){
                        JSONObject jsonObject1=jsonArray.getJSONObject(count);
                        int id=jsonObject1.getInt("event_id");
                        String event_title=jsonObject1.getString("name");
                        String event_type=jsonObject1.getString("type");
                        String event_date=jsonObject1.getString("date");
                        String event_desc=jsonObject1.getString("description");
                        String event_time=jsonObject1.getString("time");
                            list.add(new EventModelData(id,event_title,event_type,event_desc,event_date,event_time, BitmapFactory.decodeResource(getResources(),R.drawable.logoholiday)));
                        count++;

                    }
                   eventListAdapter.notifyDataSetChanged();
                    //change

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
    public void deleteEvent(final int id){
            final StringRequest stringRequest=new StringRequest(Request.Method.POST, delete_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        boolean delete=jsonObject.getBoolean("delete");
                        if(delete){
                            Snackbar snackbar=Snackbar.make(constraintLayout,"Successfully deleted",Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                        else{
                            Snackbar snackbar=Snackbar.make(constraintLayout,"Couldnt delete",Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                        //handle volley error
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<>();
                    params.put("event_id",String.valueOf(id));
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

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(),AdminHome.class));
    }
}
