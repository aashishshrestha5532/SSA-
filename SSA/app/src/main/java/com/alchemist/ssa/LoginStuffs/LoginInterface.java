package com.alchemist.ssa.LoginStuffs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

            private static String login_url= StringResource.getUrl()+"/loginUser";

            EditText user,password;
            private int code=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_interface);

        btnLogin=(Button)findViewById(R.id.btnLogin);


        user=findViewById(R.id.username);
        password=findViewById(R.id.password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.fcm_key),MODE_PRIVATE);
                String key=sharedPreferences.getString(getString(R.string.firebase_token),"");
                Log.d("f_key",key);
                //Toast.makeText(getApplicationContext(),key,Toast.LENGTH_SHORT).show();
                validateUser(user.getText().toString(),password.getText().toString(),"s",key);

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
                    Log.d("response",response.toString());
                    JSONObject jsonObject=new JSONObject(response);
                    boolean flag_value=jsonObject.getBoolean("flag");
                    int user_id=jsonObject.getInt("user_id");
                    int class_id=jsonObject.getInt("class_id");
                    int section_id=jsonObject.getInt("section_id");

                    if(flag_value){
                        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.session_key),MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putInt(getString(R.string.session_value),user_id);
                        editor.putInt(getString(R.string.section_id),section_id);
                        editor.putInt(getString(R.string.class_id),class_id);
                        editor.putBoolean(getString(R.string.firstTimeLogin),false);
                        editor.apply();

                       Intent intent=new Intent(getApplicationContext(), ParentDashBoard.class);
                       startActivity(intent);
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
}
