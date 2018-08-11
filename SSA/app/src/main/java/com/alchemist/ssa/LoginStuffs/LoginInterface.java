package com.alchemist.ssa.LoginStuffs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alchemist.ssa.NetworkStuffs.StringResource;
import com.alchemist.ssa.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginInterface extends AppCompatActivity {
            private Button btnLogin;
            private ProgressDialog progressDialog;
            private static String login_url= StringResource.getUrl()+"/loginUser";
            ConstraintLayout constraintLayout;
            private ImageView teacher,student;

            EditText user,password;
            private String code="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_interface);
        progressDialog=new ProgressDialog(this);

        progressDialog.setCancelable(false);
        progressDialog.setMessage("Logging in..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);



        btnLogin=(Button)findViewById(R.id.btnLogin);
        constraintLayout=findViewById(R.id.constrainLayout);


        user=findViewById(R.id.username);
        password=findViewById(R.id.password);

        teacher=findViewById(R.id.teacher);
        student=findViewById(R.id.student);

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code="t";
                student.setBackgroundColor(Color.TRANSPARENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    teacher.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.circleback));
                    ResizeAnimation resizeAnimation = new ResizeAnimation(teacher,200 );
                    resizeAnimation.setDuration(600);
                    teacher.startAnimation(resizeAnimation);

                }
            }
        });
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code="s";
                teacher.setBackgroundColor(Color.TRANSPARENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    student.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.circleback));
                    ResizeAnimation resizeAnimation = new ResizeAnimation(student,180 );
                    resizeAnimation.setDuration(600);
                    student.startAnimation(resizeAnimation);

                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.fcm_key),MODE_PRIVATE);
                String key=sharedPreferences.getString(getString(R.string.firebase_token),"");
                Log.d("f_key",key);
                //Toast.makeText(getApplicationContext(),key,Toast.LENGTH_SHORT).show();
                validateUser(user.getText().toString(),password.getText().toString(),code,key);

                //Toast.makeText(getApplicationContext(),key,Toast.LENGTH_SHORT).show();


                //check the code for login if 0 then student login else teacher login
//                Intent intent = new Intent(LoginInterface.this,Schedule.class);
//                startActivity(intent);
            }
        });


    }

    public void validateUser(final String u, final String p, final String c, final String key){


        StringRequest stringRequest=new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    //if the user  is validated
                try {
                    hideDialog();
                    Log.d("response",response.toString());
                    JSONObject jsonObject=new JSONObject(response);
                    boolean flag_value=jsonObject.getBoolean("flag");
                    boolean duplicate=jsonObject.getBoolean("duplicate");



                    if(flag_value){

                        String l_code=jsonObject.getString("code");
                        int user_id = jsonObject.getInt("user_id");

                        if(l_code.equals("s")) {


                            if(!duplicate) {

                                int class_id = jsonObject.getInt("class_id");
                                int section_id = jsonObject.getInt("section_id");
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.session_key), MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt(getString(R.string.session_value), user_id);
                                editor.putInt(getString(R.string.section_id), section_id);
                                editor.putInt(getString(R.string.class_id), class_id);
                                editor.putBoolean(getString(R.string.firstTimeLogin), false);
                                editor.apply();

                                Intent intent = new Intent(getApplicationContext(), ParentDashBoard.class);
                                startActivity(intent);

                            }
                            else{
                                Snackbar snackbar=Snackbar.make(constraintLayout,"Duplicate user found",Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }

                        }
                        else if(l_code.equals("t")){
                            SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.teacher_session_key),MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            Log.d("email",jsonObject.getString("email"));
                            editor.putInt(getString(R.string.teacher_session),user_id);
                            editor.putString(getString(R.string.teacher_name),jsonObject.getString("name"));
                            editor.putString(getString(R.string.teacher_email),jsonObject.getString("email"));
                            editor.putBoolean(getString(R.string.teacherFirstLogin),false);
                            editor.apply();
                            Intent intent=new Intent(getApplicationContext(),TeacherDashBoard.class);
                            startActivity(intent);
                        }
                    }
                    else{
                        Snackbar snackbar=Snackbar.make(constraintLayout,"Username and password donot match",Snackbar.LENGTH_LONG);
                        snackbar.show();
                        //Toast.makeText(getApplicationContext(),"Username and password not valid",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params=new HashMap<>();

                params.put("user",u);
                params.put("pass",p);
                params.put("code",c);
                params.put("key",key);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }
    public class ResizeAnimation extends Animation {
        final int startWidth;
        final int targetWidth;
        View view;

        public ResizeAnimation(View view, int targetWidth) {
            this.view = view;
            this.targetWidth = targetWidth;
            startWidth = view.getWidth();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newWidth = (int) (startWidth + (targetWidth - startWidth) * interpolatedTime);
            view.getLayoutParams().width = newWidth;
            view.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
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

}
