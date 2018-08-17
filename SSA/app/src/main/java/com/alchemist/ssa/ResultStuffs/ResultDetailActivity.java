package com.alchemist.ssa.ResultStuffs;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResultDetailActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private TableLayout.LayoutParams tableParams;
    private TableRow.LayoutParams rowParams;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private static String marks_url= StringResource.getUrl()+"/viewStudentMarks";
    private TextView totalPercent;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    //private List<StudentMarkModel> studentMarkModels=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_detail);

        totalPercent=findViewById(R.id.totalPercent);
        progressBar=findViewById(R.id.progressResult);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

       SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.session_key), MODE_PRIVATE);
       SharedPreferences sharedPreferences1=getApplicationContext().getSharedPreferences(getString(R.string.teacher_session_key),MODE_PRIVATE);




        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("name"));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        loadPercent(getIntent().getStringExtra("percentage"));

        collapsingToolbarLayout=findViewById(R.id.collapseToolbar);
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));

        tableLayout=findViewById(R.id.resultTable);


        addHeaders("Subjects","Pass Marks","Full Marks","Obtained Marks");
        if(String.valueOf(sharedPreferences.getInt(getString(R.string.session_value),1)).equals(getIntent().getStringExtra("student_roll"))|| !sharedPreferences1.getBoolean(getString(R.string.teacherFirstLogin),true)) {
            showDialog();
            loadMarks(getIntent().getStringExtra("student_roll"));
            StringResource.setTotal_marks(getIntent().getStringExtra("percentage"));
        }
        else {
            tableLayout.setVisibility(View.GONE);
            new AlertDialog.Builder(ResultDetailActivity.this)
                    .setTitle("School policies")
                    .setMessage("Sorry We cant display other marks.")
                    .setCancelable(false)
                    .setPositiveButton("got it", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Whatever...
                            dialog.cancel();
                        }
                    }).show();
            //Toast.makeText(getApplicationContext(), "Sorry Its against our school policies", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private TextView getTextView(String text) {
        TextView textView=new TextView(this);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        textView.setPadding(5,5,5,5);
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    private TextView getHeaderTextView(String text) {
        TextView textView=new TextView(this);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
        textView.setPadding(5,5,5,5);
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    private void addHeaders(String subjects,String passMarks,String fullMarks,String obtainedMarks) {
        TableRow row1=new TableRow(this);
        row1.setGravity(Gravity.CENTER);
        row1.setOrientation(LinearLayout.HORIZONTAL);
        row1.addView(getHeaderTextView(subjects));
        row1.addView(getHeaderTextView(passMarks));
        row1.addView(getHeaderTextView(fullMarks));
        row1.addView(getHeaderTextView(obtainedMarks));
        row1.setDividerDrawable(ContextCompat.getDrawable(this,R.drawable.table_divider));
        row1.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

        tableLayout.addView(row1);
    }


    private void addRows(String subjects,String passMarks,String fullMarks,String obtainedMarks) {
        TableRow row1=new TableRow(this);
        row1.setGravity(Gravity.CENTER);
        row1.addView(getTextView(subjects));
        row1.addView(getTextView(passMarks));
        row1.addView(getTextView(fullMarks));
        row1.addView(getTextView(obtainedMarks));
        row1.setDividerDrawable(ContextCompat.getDrawable(this,R.drawable.table_divider));
        row1.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        tableLayout.addView(row1);


    }

    public void loadMarks(final String roll){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, marks_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    int i=0,total=0;
                    hideDialog();
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("marks_list");
                    Log.d("response",response.toString());

                    while(i<jsonArray.length()){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        total+=jsonObject1.getInt("marks");
                        addRows(jsonObject1.getString("subject"),"32","80",jsonObject1.getInt("marks")+"");
                        //check in which subject the marks is less than average.
                        //studentMarkModels.add(new StudentMarkModel(jsonObject1.getString()))

                        i++;
                    }
                    addRows("Total","","",total+"");

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
                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.session_key),MODE_PRIVATE);
                Map<String,String> params=new HashMap<>();
                params.put("class_id",sharedPreferences.getInt(getString(R.string.class_id),1)+"");
                params.put("section_id",sharedPreferences.getInt(getString(R.string.section_id),1)+"");
                params.put("terminal","1");//terminal exam default
                params.put("student_id",roll);

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void loadPercent(String percent){
        double data;
        String rtext;

        data=(!percent.equals("Abs"))?Double.parseDouble(percent):0;
        rtext=(data==0)?"abs":data+" %";
        totalPercent.setText(rtext);




        Animation absentAnimation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.absent_days_animation);

        ObjectAnimator objectAnimator=ObjectAnimator.ofInt(progressBar,"progress",0, (int)data);//starting from zero to 25 with max value 30
        objectAnimator.setStartDelay(500);
        objectAnimator.setDuration(500);//this is the duration of rotation of progress bar in milliseconds
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();

        absentAnimation.setStartOffset(500);
        absentAnimation.setDuration(500);
        totalPercent.startAnimation(absentAnimation);

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
