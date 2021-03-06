package com.alchemist.ssa.StartingStuffs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alchemist.ssa.AdminStuffs.AdminHome;
import com.alchemist.ssa.LoginStuffs.ParentDashBoard;
import com.alchemist.ssa.LoginStuffs.LoginInterface;
import com.alchemist.ssa.LoginStuffs.TeacherDashBoard;
import com.alchemist.ssa.NetworkStuffs.NetworkCallBackInterface;
import com.alchemist.ssa.NetworkStuffs.NetworkStateChangeDetector;
import com.alchemist.ssa.NetworkStuffs.StringResource;
import com.alchemist.ssa.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PostSplash extends AppCompatActivity implements NetworkCallBackInterface {
    private static final String TAG="Splash Activity";
    private ProgressBar progressCircle;
    private FrameLayout retryButton;
    private TextView errorTextView,splashTextView;
    private NetworkStateChangeDetector stateChangeDetector;
    private boolean retryStatus=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postsplash_layout);
        StringResource.setUrl(getString(R.string.ipaddress));
        retryButton=findViewById(R.id.retryButton);
        progressCircle=findViewById(R.id.progressCircle);
        errorTextView=findViewById(R.id.errorTextView);
        splashTextView=findViewById(R.id.splashTextView);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StringResource.isIsNetworkAvailable()) {
                    checkServer();
                } else {
                    if(!retryStatus) {
                        customLoginAnimation(retryButton.getMeasuredWidth(),getPixel(R.dimen.retryButton),false);
                        retryStatus=true;
                        noInternetConnection();
                    }
                }
            }
        });
        stateChangeDetector=new NetworkStateChangeDetector(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter=new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(stateChangeDetector,filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(stateChangeDetector);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //unregisterReceiver(stateChangeDetector);
    }

    @Override
    public void networkSateChanged(boolean state) {
        if(state) {
            Log.d("network","true");
            StringResource.setIsNetworkAvailable(true);
            checkServer();
        } else {
            Log.d("network","false");
            StringResource.setIsNetworkAvailable(false);
            if(!retryStatus) {
                noInternetConnection();
                customLoginAnimation(retryButton.getMeasuredWidth(),getPixel(R.dimen.retryButton),false);
                retryStatus=true;
            }

        }
    }

    public void checkServer(){
        errorTextView.setVisibility(View.GONE);

        if(retryStatus){
            customLoginAnimation(retryButton.getMeasuredWidth(),getPixel(R.dimen.loginButton),true);
            retryStatus = false;
            //the network is not avaiable
        }

        StringRequest stringRequest=new StringRequest(Request.Method.GET, StringResource.getUrl()+"/connect", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Network is ready to connect");
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.d("response",response.toString());
                    boolean flag=jsonObject.getBoolean("flag");
                    if(flag){
                        startUp();
                    }
                    else {
                        customLoginAnimation(retryButton.getMeasuredWidth(),getPixel(R.dimen.retryButton),false);
                        retryStatus=true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                couldNotConnectToServer();

                if(!retryStatus){
                    customLoginAnimation(retryButton.getMeasuredWidth(),getPixel(R.dimen.retryButton),false);
                    retryStatus=true;
                }
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }
    public void noInternetConnection(){
        errorTextView.setText("No Internet Connection");
        errorTextView.animate().alpha(1.0f).setDuration(250).start();
        errorTextView.setVisibility(View.VISIBLE);

    }
    private void customLoginAnimation(int initialValue,int finalValue,boolean show) {
        ValueAnimator animator=ValueAnimator.ofInt(initialValue,finalValue);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value=(Integer)valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams=retryButton.getLayoutParams();
                layoutParams.width=value;
                retryButton.requestLayout();
            }
        });
        if(show) {
            splashTextView.animate().alpha(0f).setDuration(250).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    progressCircle.setVisibility(View.VISIBLE);
                }
            }).start();
            splashTextView.setVisibility(View.GONE);
        } else {
            splashTextView.animate().alpha(1f).setDuration(250).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    progressCircle.setVisibility(View.GONE);
                }
            }).start();
            splashTextView.setVisibility(View.VISIBLE);
        }
        animator.setDuration(250);
        animator.start();

    }
    private void couldNotConnectToServer() {
        errorTextView.setText("Couldn't Connect To The Server");
        errorTextView.animate().alpha(1.0f).setDuration(250).start();
        errorTextView.setVisibility(View.VISIBLE);
    }
    private int getPixel(int dp) {
        return getResources().getDimensionPixelSize(dp);
    }

    public void startUp(){

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.session_key),MODE_PRIVATE);
        SharedPreferences sharedPreferences2=getApplicationContext().getSharedPreferences(getString(R.string.teacher_session_key),MODE_PRIVATE);
        SharedPreferences sharedPreferences3=getApplicationContext().getSharedPreferences(getString(R.string.admin_session),MODE_PRIVATE);

        Log.d("firstTime",sharedPreferences.getBoolean(getString(R.string.firstTimeLogin),true)+"");
        Log.d("adminFirstTime",sharedPreferences3.getBoolean(getString(R.string.adminFirstTimeLogin),true)+"");
        if(sharedPreferences.getBoolean(getString(R.string.firstTimeLogin),true)&& sharedPreferences2.getBoolean(getString(R.string.teacherFirstLogin),true)&& sharedPreferences3.getBoolean(getString(R.string.adminFirstTimeLogin),true)){
            startActivity(new Intent(getApplicationContext(), LoginInterface.class));
        }
        else {
            if(!sharedPreferences.getBoolean(getString(R.string.firstTimeLogin),false)){
                startActivity(new Intent(getApplicationContext(),ParentDashBoard.class));
            }
            else if(!sharedPreferences2.getBoolean(getString(R.string.teacherFirstLogin),false))
            startActivity(new Intent(getApplicationContext(), TeacherDashBoard.class));

            else if(!sharedPreferences3.getBoolean(getString(R.string.adminFirstTimeLogin),false)){
                startActivity(new Intent(getApplicationContext(), AdminHome.class));
            }

        }
//        if(sharedPreferences2.getBoolean(getString(R.string.teacherFirstLogin),true)){
//            startActivity(new Intent(getApplicationContext(),LoginInterface.class));
//        }
//        else
//            startActivity(new Intent(getApplicationContext(), TeacherDashBoard.class));

        finish();



    }


}
