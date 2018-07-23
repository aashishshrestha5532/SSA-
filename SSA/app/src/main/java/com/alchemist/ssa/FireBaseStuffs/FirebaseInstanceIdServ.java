package com.alchemist.ssa.FireBaseStuffs;

import android.content.SharedPreferences;
import android.util.Log;

import com.alchemist.ssa.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
/**
 * Created by Ashish Alton on 7/20/2018.
 */

public class FirebaseInstanceIdServ extends FirebaseInstanceIdService{

    SharedPreferences sharedPreferences;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
      String token;
      token= FirebaseInstanceId.getInstance().getToken();
      Log.d("f_token",token);


      sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.fcm_key),MODE_PRIVATE);
      SharedPreferences.Editor editor=sharedPreferences.edit();
      editor.putString(getString(R.string.firebase_token),token);
      editor.commit();

    }
}
