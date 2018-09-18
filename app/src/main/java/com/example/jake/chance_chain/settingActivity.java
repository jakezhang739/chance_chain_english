package com.example.jake.chance_chain;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.core.SignInStateChangeListener;

public class settingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        EditText iptext = (EditText) findViewById(R.id.editText3);
        Button ipiBtn = (Button) findViewById(R.id.button7);
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
        ipiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences ipAdr = getSharedPreferences("ipaddress", 0);
                SharedPreferences.Editor editor = ipAdr.edit();
                editor.putString("address",iptext.getText().toString());
                editor.commit();
                Toast.makeText(getApplicationContext(),R.string.ipad+iptext.getText().toString(),Toast.LENGTH_LONG).show();

            }
        });
    }


}
