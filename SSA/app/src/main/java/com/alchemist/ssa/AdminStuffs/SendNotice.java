package com.alchemist.ssa.AdminStuffs;

import android.app.ProgressDialog;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendNotice extends AppCompatActivity {

    TextInputLayout messageTitleLayout, userInputLayout;
    TextInputEditText messageTitle, fromuser;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button sendBtn;
    private static String send_url = StringResource.getUrl() + "/sendNotice";
    private String code;
    private LinearLayout linearLayout;
    private Spinner classSpinner, sectionSpinner;
    private ProgressDialog progressDialog;
    private String classes[]={"1st year","2nd year","3rd year","4th year"};
    //private String classes[] = {"class 1", "class 2", "class 3", "class 4", "class 5", "class 6", "class 7", "class 8", "class 9", "class 10"};
    private String sections[] = {"section 1", "section 2", "section 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notice);
        classSpinner = findViewById(R.id.classSpinner);
        sectionSpinner = findViewById(R.id.sectionSpinner);
        sendBtn = findViewById(R.id.addS);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sending..");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Notice board");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        final ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, classes);
        classAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        classSpinner.setAdapter(classAdapter);

        final ArrayAdapter<String> sectionAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, sections);
        sectionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(sectionAdapter);

        linearLayout = findViewById(R.id.parentStuffs);
        radioGroup = findViewById(R.id.radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.teacher_g:
                        code = "t";
                        linearLayout.setVisibility(View.GONE);
                        break;

                    case R.id.student_g:
                        code = "s";
                        linearLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.both_g:
                        code = "ts";
                        linearLayout.setVisibility(View.GONE);
                        break;
                }
            }
        });

        messageTitleLayout = findViewById(R.id.textmessageInputLayout);
        userInputLayout = findViewById(R.id.textUserInputLayout);

        messageTitle = findViewById(R.id.messageTitle);
        fromuser = findViewById(R.id.fromUser);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput(messageTitleLayout, messageTitle, "Title cannot be empty") && validateInput(userInputLayout, fromuser, "Sender cannot be empty")) {
                    //send the data

                    showDialog();
                    sendToServer(messageTitle.getText().toString().trim(), fromuser.getText().toString().trim(), code);

                }

            }
        });

    }

    private boolean validateInput(TextInputLayout layout, TextInputEditText editText, String msg) {
        if (editText.getText().toString().trim().isEmpty()) {
            layout.setError(msg);
            editText.requestFocus();
            return false;
        } else {
            layout.setErrorEnabled(false);
            return true;
        }
    }

    public void sendToServer(final String msg, final String fromUser, final String code) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, send_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    hideDialog();
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("response", response.toString());
                    if (jsonObject.getBoolean("send")) {
                        messageTitle.setText("");
                        fromuser.setText("");
                        Snackbar snackbar = Snackbar.make(linearLayout, "Successfully sent", Snackbar.LENGTH_LONG);
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
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("msg", msg);
                params.put("from", fromUser);
                params.put("code", code);
                params.put("c_id", classSpinner.getSelectedItem().toString().substring(0,1));
                params.put("s_id", sectionSpinner.getSelectedItem().toString().substring(7));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void showDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }


    }

    private void hideDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    public void handleVolleyError(VolleyError error){
        hideDialog();
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
