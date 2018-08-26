package com.alchemist.ssa.ResultStuffs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.alchemist.ssa.R;

public class CheckResultBoard extends AppCompatActivity {

    private Spinner class_id,section_id,terminal;
    Button checkResult;
    private String classes[]={"1st year","2nd year","3rd year","4th year"};
    //private String classes[]={"class 1","class 2","class 3","class 4","class 5","class 6","class 7","class 8","class 9","class 10"};
    private String sections[]={"section 1","section 2","section 3"};
    private String terminals[]={"1st","2nd","3rd","4th"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_result_board);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setTitle("Result");
            toolbar.setTitleTextColor(Color.WHITE);
        }

        class_id=findViewById(R.id.class_id);
        section_id=findViewById(R.id.section_id);
        terminal=findViewById(R.id.terminal);
        final ArrayAdapter<String> classAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,classes);
        classAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        class_id.setAdapter(classAdapter);

        final ArrayAdapter<String> sectionAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,sections);
        sectionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        section_id.setAdapter(sectionAdapter);

        final ArrayAdapter<String> terminalAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,terminals);
        sectionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        terminal.setAdapter(terminalAdapter);

        checkResult=findViewById(R.id.checkResult);
        checkResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),StudentResult.class);
                intent.putExtra("class_id",class_id.getSelectedItem().toString().substring(0,1));
                intent.putExtra("section_id",section_id.getSelectedItem().toString().substring(7));
                intent.putExtra("terminal",terminal.getSelectedItem().toString().substring(0,1));

                startActivity(intent);
            }
        });
    }
}
