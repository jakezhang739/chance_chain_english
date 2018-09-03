package com.example.jake.chance_chain;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.core.SignInStateChangeListener;

public class settingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Button logOut = (Button) findViewById(R.id.logoutBtn);
        RelativeLayout geren = (RelativeLayout) findViewById(R.id.rel1);
        geren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settingActivity.this,InformationActivity.class);
                startActivity(intent);
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IdentityManager.getDefaultIdentityManager().signOut();
                Intent intent = new Intent(settingActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });
    }


}
