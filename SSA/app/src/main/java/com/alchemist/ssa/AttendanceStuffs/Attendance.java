package com.alchemist.ssa.AttendanceStuffs;

import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Attendance extends AppCompatActivity implements AttendanceInterface {
    private final String TAG="Attendance Class:";
    private ConstraintLayout attendanceParent;
    private FloatingActionButton attendancePresent,attendanceAbsent,postAttendance;
    private AttendanceGridAdapter attendanceGridAdapter;
    private Button currentRoll;
    private RecyclerView totalStudentGrid;
    private final int ANIMATION_TIME=4000;
    private Animation shrink_expand;
    private ProgressBar progressBar;
    private boolean scrolled=true;
    private TextView studentName;
    private TextView student_performance,student_total,student_absent,student_remarks;
    private int count=0,offset=0;
    private static final String attendance_url= StringResource.getUrl()+"/showStudent";
    private static final String add_attendance_url=StringResource.getUrl()+"/addAttendance";
    private Spinner cSpinner,sSppiner,subSpinner;
    private boolean flag;
    private List<AttendanceGridModel> attendanceGridModels=new ArrayList<>();
    private List<AttendanceCheck> attendanceChecks=new ArrayList<>();
    private String classes[]={"1st year","2nd year","3rd year","4th year"};
    //private String classes[]={"class 1","class 2","class 3","class 4","class 5","class 6","class 7","class 8","class 9","class 10"};
    private String sections[]={"section 1","section 2","section 3"};
    private String subject[]={"Mth101","CMP555","Eng333","MTH121"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        attendanceParent=findViewById(R.id.attendanceParent);
        cSpinner=findViewById(R.id.classSpinner);
        sSppiner=findViewById(R.id.sectionSpinner);
        subSpinner=findViewById(R.id.subjectSpinner);
        progressBar=findViewById(R.id.progressCircle);

        postAttendance=findViewById(R.id.postAttendance);

        final ArrayAdapter<String> classAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,classes);
        classAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        cSpinner.setAdapter(classAdapter);

        final ArrayAdapter<String> sectionAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,sections);
        sectionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sSppiner.setAdapter(sectionAdapter);

        final ArrayAdapter<String> subjectAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,subject);
        subjectAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        subSpinner.setAdapter(subjectAdapter);

        studentName=findViewById(R.id.studentName);

        shrink_expand=AnimationUtils.loadAnimation(this,R.anim.attendance_shrink_expand);

        attendanceAbsent=findViewById(R.id.attendanceAbsent);//The Present Button
        attendancePresent=findViewById(R.id.attendancePresent);//The Absent Button

        currentRoll=findViewById(R.id.currentRoll);//The big Circular Button in center

        totalStudentGrid=findViewById(R.id.totalStudentGrid);//The Horizontal Recycler View for displaying roll numbers

        final AttendanceLayoutManager attendanceLayoutManager=new AttendanceLayoutManager(this,1,GridLayoutManager.HORIZONTAL,false);//Custom Layout Manager for Recycler view


        totalStudentGrid.setLayoutManager(attendanceLayoutManager);

        attendanceGridAdapter=new AttendanceGridAdapter(this,attendanceGridModels);//Grid Layout adapter
        attendanceGridAdapter.addAttendanceChangeListener(this);

        LayoutAnimationController layoutAnimationController=AnimationUtils.loadLayoutAnimation(this,R.anim.student_grid_layout);
        totalStudentGrid.setLayoutAnimation(layoutAnimationController);

        totalStudentGrid.setAdapter(attendanceGridAdapter);
        student_performance=findViewById(R.id.student_performance);
        student_total=findViewById(R.id.student_total_attendance);
        student_absent=findViewById(R.id.student_normal_attendance);
        student_remarks=findViewById(R.id.student_remarks);

        cSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                attendanceGridModels.clear();
                String class_id= cSpinner.getSelectedItem().toString();

                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE);
                loadStudents(class_id);


//                notify();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       //Load Dummy Data
        //my first commit



//    Log.d("first",attendanceGridModels.get(0).getName()+"");



        attendancePresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int currentPosition=attendanceGridAdapter.getSelectedItem();//Get the current selected Roll Number
                Log.d("currentPos",currentPosition+"");
                flag=true;
              //  Toast.makeText(getApplicationContext(),flag+ " "+Integer.parseInt(attendanceGridModels.get(currentPosition).getRollNo())+"",Toast.LENGTH_SHORT).show();
               /*
                * Check if the current position is less than the size of the model we are using
                * Also check if there is any pending call back from layout manager,if so ,ignore this button press
                * */
                if(currentPosition<attendanceGridModels.size()&&scrolled && attendanceChecks.size()<attendanceGridModels.size()) {
                    scrolled=false;

                    Log.d("current pos",currentPosition+"");
                    Log.d("roll ",attendanceGridModels.get(currentPosition).getRollNo());

                    attendanceChecks.add(new AttendanceCheck(flag, Integer.parseInt(attendanceGridModels.get(currentPosition).getRollNo())));

                    attendanceLayoutManager.setPresent(true);//For sending data from the call back method
                    currentRoll.startAnimation(shrink_expand);
                    totalStudentGrid.smoothScrollToPosition(currentPosition+1);//Scroll the position if it is currently invisible
                }

                    for (AttendanceCheck check : attendanceChecks) {
                        if (check.getRoll() == Integer.parseInt(attendanceGridModels.get(currentPosition).getRollNo())) {
                            Log.d("match", "mathcing " + currentPosition);

                            flag=!flag;
                            //attendanceLayoutManager.setPresent(false);
                            //color changing of the button
                            attendanceGridModels.get(currentPosition).setStudentStatus(0);
                            attendanceChecks.set(currentPosition, new AttendanceCheck(flag, attendanceChecks.get(currentPosition).getRoll()));
                            attendanceGridAdapter.notifyDataSetChanged();
                        }

                    }
                }


        });

        attendanceAbsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=false;

                int currentPosition=attendanceGridAdapter.getSelectedItem();

              //  Toast.makeText(getApplicationContext(),flag+ " "+Integer.parseInt(attendanceGridModels.get(currentPosition).getRollNo())+"",Toast.LENGTH_SHORT).show();

                if(currentPosition<attendanceGridModels.size()&&scrolled && attendanceChecks.size()<attendanceGridModels.size()) {
                    scrolled=false;
                    Log.d("current pos",currentPosition+"");
                    Log.d("roll ",attendanceGridModels.get(currentPosition).getRollNo());


                    attendanceChecks.add(new AttendanceCheck(flag, Integer.parseInt(attendanceGridModels.get(attendanceGridAdapter.getSelectedItem()).getRollNo())));


                    attendanceLayoutManager.setPresent(false);
                    currentRoll.startAnimation(shrink_expand);
                    totalStudentGrid.smoothScrollToPosition(currentPosition+1);

                }

                for(AttendanceCheck check:attendanceChecks) {
                    if (check.getRoll() == Integer.parseInt(attendanceGridModels.get(currentPosition).getRollNo())) {
                        Log.d("match", "mathcing " + currentPosition);
                        int a[]={1,0};
                        //attendanceLayoutManager.setPresent(true);
                        flag=!flag;
                        attendanceGridModels.get(currentPosition).setStudentStatus(1);
                        attendanceChecks.set(currentPosition, new AttendanceCheck(flag, attendanceChecks.get(currentPosition).getRoll()));
                        attendanceGridAdapter.notifyDataSetChanged();
                    }


                }
            }

        });
            postAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stringBuilder=new StringBuilder();
                int i=0,j=0;
                JSONObject jsonData=new JSONObject();
                while(i<attendanceChecks.size()){
                    try {
                        stringBuilder.append(String.valueOf(attendanceChecks.get(i).getRoll()).length());
                        stringBuilder.append(attendanceChecks.get(i).getRoll());
                        if(!attendanceChecks.get(i).isFlag()){
                            stringBuilder.append(1);
                        }
                        else
                            stringBuilder.append(0);

                        //stringBuilder.append(attendanceChecks.get(i).isFlag());
                        jsonData.put("count"+String.valueOf(i+1),attendanceChecks.get(i).getRoll());
                        Log.d("datas",attendanceChecks.get(i).getRoll()  +  "  status - "+attendanceChecks.get(i).isFlag());
                        i++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //we have to clear all the color after the attendance



                }
                Log.d("jsonSize",jsonData.toString());

                Log.d("size",attendanceChecks.size()+"");
               // sendJson(stringBuilder.toString());

                Log.d("StringBuilder",stringBuilder.toString());
                sendJson(stringBuilder.toString(),attendanceChecks.size());
                attendanceChecks.clear();
                //checking whether the attendance is complete or not
//                if()
            }
        });

        final ViewTreeObserver observer=attendanceParent.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    attendanceParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                attendanceLayoutManager.addOnScrollCompleteListener(Attendance.this);
            }
        });





    }
    /*
    * Call back from the layout manager when the scrolling is complete
    * If this call back is not used ,then we might get NPE
    * */

    @Override
    public void scrollComplete(boolean present) {//The parameter is the value sent in onclick of floating button
        int currentPosition=attendanceGridAdapter.getSelectedItem();
        if(present)
            ((AttendanceGridAdapter.ViewHolder)totalStudentGrid.findViewHolderForAdapterPosition(currentPosition)).present();//Make the student Present
        else
            ((AttendanceGridAdapter.ViewHolder)totalStudentGrid.findViewHolderForAdapterPosition(currentPosition)).absent();//Make the student Absent
        scrolled=true;
    }


    /*
    * Call back from the grid adapter when the button is clicked and the layout moves to the next button
    * Actually this gets called from scrollComplete
    *
    * */

    @Override
    public void displayOnBigButton(AttendanceGridModel model) {
        currentRoll.setText(model.getRollNo());
        studentName.setText(model.getName());
        student_performance.setText("Performance - "+model.getPerformance());
        student_remarks.setText("Remarks - "+model.getRemarks());
        student_total.setText("Total Attendance - "+model.getTotalAttendance());
        student_absent.setText("Present -"+(model.getPresentAttendance())+ " days");

        if(model.getStudentStatus()==0)
            currentRoll.setBackground(ContextCompat.getDrawable(this,R.drawable.absent_student_circle));//Change the drawable
        else if(model.getStudentStatus()==1)
            currentRoll.setBackground(ContextCompat.getDrawable(this,R.drawable.present_student_circle));
        else
            currentRoll.setBackground(ContextCompat.getDrawable(this,R.drawable.circle));
    }

    private void loadStudents(final String c_id) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, attendance_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setIndeterminate(false);
                progressBar.setVisibility(View.GONE);

                try {
                    int i=0;
                    Log.d("response",response.toString());
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("response");
                    while(i<jsonArray.length()) {
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        AttendanceGridModel model = new AttendanceGridModel(jsonObject1.getString("student_name"),jsonObject1.getString("id"),jsonObject1.getInt("total"),jsonObject1.getInt("present"),jsonObject1.getString("performance"),jsonObject1.getString("performance"));
                       attendanceGridModels.add(model);

                        i++;
                    }
                    attendanceGridAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error","Error is majar");
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

               String sec_id=sSppiner.getSelectedItem().toString();
                Map<String,String> params=new HashMap<>();
                params.put("class_id",c_id.substring(0,1));
                params.put("section_id",sec_id.substring(7));
                params.put("sub_id",subSpinner.getSelectedItem().toString());
                return params;
            }
        };



        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private int getPixel(int dp) {
        return getResources().getDimensionPixelSize(dp);
    }


    public void sendJson(final String dataa, final int asize){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, add_attendance_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("response",response.toString());
                    JSONObject jsonObject=new JSONObject(response);
                    boolean insert=jsonObject.getBoolean("insert");
                    if(insert){
                        Snackbar snackbar=Snackbar.make(attendanceParent,"Attendance is posted successfully",Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    else {
                        Snackbar snackbar=Snackbar.make(attendanceParent,"Sorry! Attendance is interuptted",Snackbar.LENGTH_LONG);
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
                progressBar.setVisibility(View.GONE);
                progressBar.setIndeterminate(false);
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("datas",dataa);
                params.put("size",String.valueOf(asize));
                params.put("class_id",cSpinner.getSelectedItem().toString().substring(0,1));
                params.put("section_id",sSppiner.getSelectedItem().toString().substring(7));
                params.put("sub_id",subSpinner.getSelectedItem().toString());
                return params;

            }

        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
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
        Snackbar snackbar=Snackbar.make(attendanceParent,error,Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}
