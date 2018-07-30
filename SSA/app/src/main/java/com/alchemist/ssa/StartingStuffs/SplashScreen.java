package com.alchemist.ssa.StartingStuffs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences=getPreferences(MODE_PRIVATE);

        if(sharedPreferences.getBoolean("firstSetup",true)){
            SharedPreferences.Editor editor=sharedPreferences.edit();

            editor.putBoolean("firstSetup",false);
            editor.apply();

            startActivity(new Intent(getApplicationContext(),WelcomeActivity.class));
        }

        else{
            startActivity(new Intent(getApplicationContext(),PostSplash.class));
        }
        finish();





    }
}
