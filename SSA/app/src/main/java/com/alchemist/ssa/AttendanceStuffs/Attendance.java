package com.alchemist.ssa.AttendanceStuffs;

import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Spinner;
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

public class Attendance extends AppCompatActivity implements AttendanceInterface {
    private final String TAG="Attendance Class:";
    private ConstraintLayout attendanceParent;
    private FloatingActionButton attendancePresent,attendanceAbsent,postAttendance;
    private AttendanceGridAdapter attendanceGridAdapter;
    private Button currentRoll;
    private RecyclerView totalStudentGrid;
    private final int ANIMATION_TIME=4000;
    private Animation shrink_expand;
    private boolean scrolled=true;
    private TextView studentName;
    private TextView student_performance,student_total,student_absent,student_remarks;
    private int count=0,offset=0;
    private static final String attendance_url= StringResource.getUrl()+"/showStudent";
    private Spinner cSpinner,sSppiner;
    private boolean flag;
    private List<AttendanceGridModel> attendanceGridModels=new ArrayList<>();
    private List<AttendanceCheck> attendanceChecks=new ArrayList<>();
    private String classes[]={"class 1","class 2","class 3","class 4","class 5","class 6","class 7","class 8","class 9","class 10"};
    private String sections[]={"section A","section B","section C"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        attendanceParent=findViewById(R.id.attendanceParent);
        cSpinner=findViewById(R.id.classSpinner);
        sSppiner=findViewById(R.id.sectionSpinner);

        postAttendance=findViewById(R.id.postAttendance);

        final ArrayAdapter<String> classAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,classes);
        classAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        cSpinner.setAdapter(classAdapter);

        final ArrayAdapter<String> sectionAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,sections);
        sectionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sSppiner.setAdapter(sectionAdapter);

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


                int i=0;
                while(i<attendanceChecks.size()){
                    Log.d("datas",attendanceChecks.get(i).getRoll()  +  "  status - "+attendanceChecks.get(i).isFlag());
                    i++;
                }
                Log.d("size",attendanceChecks.size()+"");
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
                params.put("class_id",c_id.substring(5));
                params.put("section_id","1");

                return params;
            }
        };



        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
//        AttendanceGridModel model=new AttendanceGridModel("Susan Thapa","1",30,22,"good","good");
//        attendanceGridModels.add(model);
//
//        model=new AttendanceGridModel("Rajesh Thapa","2",30,23,"good","good");
//        attendanceGridModels.add(model);
//
//        model=new AttendanceGridModel("Talank Baral","3",30,23,"good","good");
//
//        attendanceGridModels.add(model);
//
//        model=new AttendanceGridModel("Rajeshsa Thapa","4",30,21,"good","good");
//
//        attendanceGridModels.add(model);
//
//        model=new AttendanceGridModel("Elton Thapa","5",30,23,"good","good");
//
//        attendanceGridModels.add(model);
//
//        model=new AttendanceGridModel("Hari Thapa","6",30,19,"average","average");
//
//        attendanceGridModels.add(model);
//        model=new AttendanceGridModel("Hari Thapa","7",30,19,"average","average");
//
//        attendanceGridModels.add(model);
//
//
//        attendanceGridAdapter.notifyDataSetChanged();

    }

    private int getPixel(int dp) {
        return getResources().getDimensionPixelSize(dp);
    }




    @Override
    public void onResume() {
        super.onResume();
    }
}
