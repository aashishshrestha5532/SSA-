package com.alchemist.ssa.ResultStuffs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.alchemist.ssa.R;

import java.util.ArrayList;
import java.util.List;

public class StudentResult extends AppCompatActivity {
    private RecyclerView searchStudentResult;
    private List<StudentResultModel> studentResultModels=new ArrayList<>();
    private StudentResultAdapter studentResultAdapter;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_result);

        searchStudentResult=findViewById(R.id.searchStudentResult);

        searchButton=findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentResultModels.clear();
                loadResult();
            }
        });

        studentResultAdapter=new StudentResultAdapter(this,studentResultModels);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchStudentResult.setLayoutManager(linearLayoutManager);
        searchStudentResult.setAdapter(studentResultAdapter);
    }
    //code if required in future BitmapFactory.decodeResource(getResources(),R.drawable.category4)

    private void loadResult() {
        StudentResultModel model=new StudentResultModel(getSpace("Suson"),1,5, 90.50);
        studentResultModels.add(model);

        model=new StudentResultModel("Talank Baral",1,8,12);
        studentResultModels.add(model);

        model=new StudentResultModel("Bipin Baral",3,5,45.26);
        studentResultModels.add(model);

        model=new StudentResultModel("Suson Kumar Baral",10,15,78.15);
        studentResultModels.add(model);

        model=new StudentResultModel("Zhitiz Baral",2,4,12);
        studentResultModels.add(model);

        model=new StudentResultModel("Ashish Thapa Magar",9,5,49.65);
        studentResultModels.add(model);

        model=new StudentResultModel("Ashish Shrestha",11,12,48.6);
        studentResultModels.add(model);

        model=new StudentResultModel("Suraj Baral",11,15,9);
        studentResultModels.add(model);





        studentResultModels.add(model);
        studentResultAdapter.notifyDataSetChanged();
    }


    public String getSpace(String data){
        int length=data.length();
        //10 space character
        for(int i=0;i<(10-length);i++){
            data=data+"x";
        }


        return data;

    }

}
