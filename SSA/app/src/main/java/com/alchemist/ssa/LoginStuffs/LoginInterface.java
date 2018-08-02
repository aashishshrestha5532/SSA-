package com.alchemist.ssa.LoginStuffs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alchemist.ssa.EventStuffs.Event;
import com.alchemist.ssa.MainActivity;
import com.alchemist.ssa.R;
import com.alchemist.ssa.ScheduleStuffs.Schedule;

public class LoginInterface extends AppCompatActivity {
            private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_interface);

        btnLogin=(Button)findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginInterface.this,Schedule.class);
                startActivity(intent);
            }
        });
    }
}
