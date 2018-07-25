package com.alchemist.ssa.ScheduleStuffs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemist.ssa.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Schedule extends AppCompatActivity implements ScheduleInterface {

    private RecyclerView scheduleRecyclerView;
    private static String url_path="";
    private static String day[]={"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
    //private static String response;
    private static String response="{\n" +
            "\"response\":[\t\n" +
            "{\n" +
            "  \"0\":[\n" +
            "\t{\n" +
            "\t\"period\":1,\n" +
            "\t\"subject\":\"math1\",\n" +
            "\t\"time\":\"9 am\",\n" +
            "\t\"teacher\":\"SP\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":2,\n" +
            "\t\"subject\":\"science\",\n" +
            "\t\"time\":\"10 am\",\n" +
            "\t\"teacher\":\"TKB\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":3,\n" +
            "\t\"subject\":\"english\",\n" +
            "\t\"time\":\"11 am\",\n" +
            "\t\"teacher\":\"SK\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":4,\n" +
            "\t\"subject\":\"science\",\n" +
            "\t\"time\":\"12 pm\",\n" +
            "\t\"teacher\":\"TBT\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":5,\n" +
            "\t\"subject\":\"social\",\n" +
            "\t\"time\":\"2 pm\",\n" +
            "\t\"teacher\":\"DA\"\n" +
            "\t}\n" +
            "\t]\n" +
            "},\n" +
            "{\n" +
            " \"1\":[\n" +
            "\t{\n" +
            "\t\"period\":1,\n" +
            "\t\"subject\":\"nepali\",\n" +
            "\t\"time\":\"9 am\",\n" +
            "\t\"teacher\":\"RPM\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":2,\n" +
            "\t\"subject\":\"math2\",\n" +
            "\t\"time\":\"10 am\",\n" +
            "\t\"teacher\":\"YB\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":3,\n" +
            "\t\"subject\":\"english\",\n" +
            "\t\"time\":\"11 am\",\n" +
            "\t\"teacher\":\"ET\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":4,\n" +
            "\t\"subject\":\"science\",\n" +
            "\t\"time\":\"12 pm\",\n" +
            "\t\"teacher\":\"TBK\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":5,\n" +
            "\t\"subject\":\"social\",\n" +
            "\t\"time\":\"2 pm\",\n" +
            "\t\"teacher\":\"DA\"\n" +
            "\t}\n" +
            "\t]\n" +
            "},\n" +
            "{\n" +
            "\"2\":[\n" +
            "\t\t{\n" +
            "\t\"period\":1,\n" +
            "\t\"subject\":\"science\",\n" +
            "\t\"time\":\"9 am\",\n" +
            "\t\"teacher\":\"GT\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":2,\n" +
            "\t\"subject\":\"math\",\n" +
            "\t\"time\":\"10 am\",\n" +
            "\t\"teacher\":\"YB\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":3,\n" +
            "\t\"subject\":\"english\",\n" +
            "\t\"time\":\"11 am\",\n" +
            "\t\"teacher\":\"SK\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":4,\n" +
            "\t\"subject\":\"science\",\n" +
            "\t\"time\":\"12 pm\",\n" +
            "\t\"teacher\":\"ET\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":5,\n" +
            "\t\"subject\":\"social\",\n" +
            "\t\"time\":\"2 pm\",\n" +
            "\t\"teacher\":\"DA\"\n" +
            "\t}\n" +
            "\t]\n" +
            "},\n" +
            "{\n" +
            "\"3\":[\n" +
            "\t\t{\n" +
            "\t\"period\":1,\n" +
            "\t\"subject\":\"opt\",\n" +
            "\t\"time\":\"9 am\",\n" +
            "\t\"teacher\":\"SB\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":2,\n" +
            "\t\"subject\":\"math\",\n" +
            "\t\"time\":\"10 am\",\n" +
            "\t\"teacher\":\"YB\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":3,\n" +
            "\t\"subject\":\"english\",\n" +
            "\t\"time\":\"11 am\",\n" +
            "\t\"teacher\":\"SK\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":4,\n" +
            "\t\"subject\":\"science\",\n" +
            "\t\"time\":\"12 pm\",\n" +
            "\t\"teacher\":\"ADA\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":5,\n" +
            "\t\"subject\":\"social\",\n" +
            "\t\"time\":\"2 pm\",\n" +
            "\t\"teacher\":\"asa\"\n" +
            "\t}\n" +
            "\t]\n" +
            "},\n" +
            "{\n" +
            "\"4\":[\n" +
            "\t\t{\n" +
            "\t\"period\":1,\n" +
            "\t\"subject\":\"opt\",\n" +
            "\t\"time\":\"9 am\",\n" +
            "\t\"teacher\":\"SB\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":2,\n" +
            "\t\"subject\":\"math2\",\n" +
            "\t\"time\":\"10 am\",\n" +
            "\t\"teacher\":\"asa\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":3,\n" +
            "\t\"subject\":\"english\",\n" +
            "\t\"time\":\"11 am\",\n" +
            "\t\"teacher\":\"pas\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":4,\n" +
            "\t\"subject\":\"science\",\n" +
            "\t\"time\":\"12 pm\",\n" +
            "\t\"teacher\":\"asa\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":5,\n" +
            "\t\"subject\":\"social\",\n" +
            "\t\"time\":\"2 pm\",\n" +
            "\t\"teacher\":\"asa\"\n" +
            "\t}\n" +
            "\t]\n" +
            "},\n" +
            "{\n" +
            "\"5\":[\n" +
            "\t\t{\n" +
            "\t\"period\":1,\n" +
            "\t\"subject\":\"math1\",\n" +
            "\t\"time\":\"9 am\",\n" +
            "\t\"teacher\":\"rpm\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":2,\n" +
            "\t\"subject\":\"math2\",\n" +
            "\t\"time\":\"10 am\",\n" +
            "\t\"teacher\":\"asa\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":3,\n" +
            "\t\"subject\":\"english\",\n" +
            "\t\"time\":\"11 am\",\n" +
            "\t\"teacher\":\"pas\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":4,\n" +
            "\t\"subject\":\"science\",\n" +
            "\t\"time\":\"12 pm\",\n" +
            "\t\"teacher\":\"asa\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\"period\":5,\n" +
            "\t\"subject\":\"social\",\n" +
            "\t\"time\":\"2 pm\",\n" +
            "\t\"teacher\":\"asa\"\n" +
            "\t}\n" +
            "\t]\n" +
            "}\n" +
            "    \n" +
            "]\n" +
            "}";
    private ArrayList<ScheduleGridModel> scheduleGridModels = new ArrayList<>();
    private List<ScheduleGridModel2> scheduleGridModel2List =new ArrayList<>();

    private ScheduleDetailsGridAdapter scheduleDetailsGridAdapter;

    private  ScheduleGridAdapter scheduleGridAdapter;
    List<MyTypeModel> lists=new ArrayList<>();
    private TextView textView;
    private CardView scheduleCardView;
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

        JSONObject jsonObject= null;
        try {
            int i=0;
            jsonObject = new JSONObject(response);
            Log.d("response",response);
            JSONArray jsonArray=jsonObject.getJSONArray("response");
            while(i<jsonArray.length()) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                List<ScheduleGridModel2> scheduleGridModels=new ArrayList<>();
                JSONArray jsonArray1 = jsonObject1.getJSONArray(i+"");
                int j=0;
                while(j<jsonArray1.length()){
                    JSONObject jsonObject2=jsonArray1.getJSONObject(j);
                    int period=jsonObject2.getInt("period");
                    String subject="Subject : "+jsonObject2.getString("subject");
                    String time="time - "+jsonObject2.getString("time");
                    String teacher="By- "+jsonObject2.getString("teacher");
                    scheduleGridModels.add(new ScheduleGridModel2(period, subject,time, teacher));
                    scheduleGridAdapter.notifyDataSetChanged();
                    j++;

                }
                lists.add(new MyTypeModel(scheduleGridModels,day[i]));
                //scheduleGridModels.clear();
                i++;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



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
