package com.example.jake.chance_chain;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.chatbar);
        context = getApplicationContext().getApplicationContext();
        int unread =getIntent().getIntExtra("unread",0);
        RelativeLayout sixin = (RelativeLayout) findViewById(R.id.merel3);
        ImageView back = (ImageView) actionBar.getCustomView().findViewById(R.id.back);
        TextView title = (TextView) actionBar.getCustomView().findViewById(R.id.title);
        title.setText("My Message");
        TextView unreadTag = (TextView) findViewById(R.id.sixinRead);
        if(unread>0){
            unreadTag.setVisibility(View.VISIBLE);
            unreadTag.setText(String.valueOf(unread));
        }
        sixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity.this,liuyan.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }


}
