package com.alchemist.ssa.ResultStuffs;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.alchemist.ssa.NetworkStuffs.StringResource;
import com.alchemist.ssa.OtherStuffs.SearchInterface;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentResult extends AppCompatActivity {
    private RecyclerView searchStudentResult;
    private List<StudentResultModel> studentResultModels=new ArrayList<>();
    private StudentResultAdapter studentResultAdapter;
    private Button searchButton;
    private SearchInterface searchInterface;
    Bundle bundle;
    ProgressDialog progressDialog;
    private static String class_url= StringResource.getUrl()+"/viewClassResult";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_result);
        bundle=getIntent().getExtras();
        studentResultAdapter=new StudentResultAdapter(this,studentResultModels);
        searchStudentResult=findViewById(R.id.searchStudentResult);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showDialog();
        loadResult();

        searchButton=findViewById(R.id.searchButton);
        Toolbar toolbar= findViewById(R.id.resultToolBar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setTitle("Student Results");
            toolbar.setTitleTextColor(Color.WHITE);
        }

        searchInterface=new SearchInterface() {
            @Override
            public void onSearch(String newText) {
               String smallText=newText.toLowerCase();
                //Toast.makeText(getApplicationContext(),newText,Toast.LENGTH_SHORT).show();
                ArrayList<StudentResultModel> newlinkModels=new ArrayList<>();

                for(StudentResultModel linkModel: studentResultModels){
                    //searching by name as well as category
                    String st_name=linkModel.getName();
                    //String level=linkModel.getLevel();
                    if(st_name.contains(newText) || st_name.toLowerCase().contains(smallText)){
                        newlinkModels.add(linkModel);
                    }


                }

                studentResultAdapter.setFilter(newlinkModels);
               // studentResultAdapter.notifyDataSetChanged();

            }
        };
        setSupportActionBar(toolbar);




        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchStudentResult.setLayoutManager(linearLayoutManager);
        searchStudentResult.setAdapter(studentResultAdapter);
//        studentResultModels.clear();
//        studentResultAdapter.notifyDataSetChanged();


    }
    //code if required in future BitmapFactory.decodeResource(getResources(),R.drawable.category4)

    private void loadResult() {


        StringRequest stringRequest=new StringRequest(Request.Method.POST, class_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                JSONObject jsonObject= null;
                try {
                    int i=0;
                    String marks="";
                    Log.d("response",response.toString());
                    jsonObject = new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("student_list");
                    while(i<jsonArray.length()){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        int id=jsonObject1.getInt("id");
                        String st_name=jsonObject1.getString("student_name");
                        int roll_num=jsonObject1.getInt("roll_num");
                        String percentage=jsonObject1.getString("Percentage");
                        boolean present=jsonObject1.getBoolean("Present");

                        //if the student is present then only its marks is evealuated else do % dash empty
                        if(present){
                            marks=percentage;

                        }
                        else
                            marks="Abs";


                        studentResultModels.add(new StudentResultModel(id,st_name,i+1,roll_num,marks));


                        i++;
                    }
                    studentResultAdapter.notifyDataSetChanged();
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

                Map<String, String> params = new HashMap<>();
                if (bundle != null) {
                    params.put("section_id",bundle.getString("section_id"));
                    params.put("class_id",bundle.getString("class_id"));
                    params.put("terminal",bundle.getString("terminal"));
                } else {
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.session_key), MODE_PRIVATE);

                    params.put("class_id", sharedPreferences.getInt(getString(R.string.class_id), 1) + "");
                    params.put("section_id", sharedPreferences.getInt(getString(R.string.section_id), 1) + "");
                    params.put("terminal", "1");


                }
                return params;
            }
        };

        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }


    public String getSpace(String data){
        int length=data.length();
        //10 space character
        for(int i=0;i<(10-length);i++){
            data=data+"x";
        }


        return data;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.search_bar,menu);
        MenuItem menuItem=menu.findItem(R.id.search);



        SearchManager searchManager=(SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setQueryHint("Enter Student name");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(getApplicationContext(),"sssss",Toast.LENGTH_SHORT).show();
                //staffAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchInterface.onSearch(newText);
                return true;
            }
        });
        return true;
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






}
