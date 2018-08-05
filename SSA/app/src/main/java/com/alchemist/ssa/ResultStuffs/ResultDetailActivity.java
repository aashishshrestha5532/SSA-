package com.alchemist.ssa.ResultStuffs;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.alchemist.ssa.R;

public class ResultDetailActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private TableLayout.LayoutParams tableParams;
    private TableRow.LayoutParams rowParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_detail);

        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("name")+"'s Result");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tableLayout=findViewById(R.id.resultTable);


        addHeaders("Subjects","Pass Marks","Full Marks","Obtained Marks");
        addRows("Science","32","80","74");
        addRows("Math","32","80","40");
        addRows("Health","32","80","85");
        addRows("Popn","32","80","60");
        addRows("Opt math","32","80","64");
        addRows("Social","32","80","70");
        addRows("English","32","80","74");
        addRows("Grammar","32","80","85");
        addRows("Total","-","-","86");





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

}
